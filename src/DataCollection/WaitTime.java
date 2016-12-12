package DataCollection;

import java.util.ArrayList;

import uchicago.src.sim.analysis.NumericDataSource;
import uchicago.src.sim.analysis.Sequence;
import CrossModelClasses.ParameterOptions;
import NKCModel.CoevolutionarySet;
import NKCModel.NKCOrganization;
import NKModel.NKOrganization;

/**
 * 
 * This class, given an agent list (and a species in regard to the
 * NKC Model - can be left blank for the NK Model) calculates the
 * wait time since the last move 
 * 
 * @author Amy Marshall
 *
 */
public class WaitTime implements NumericDataSource, Sequence
{
	//variables
	private int method;
	private ArrayList agentList;
	private int species;
	
	/**
	 * Sets up the wait time class setting the class variables
	 * 
	 * @param agentList the list of lal agents in the simulation
	 * @param method max, min or average
	 * @param species the species this is to be calculated for if for NKC
	 */
	public WaitTime(ArrayList agentList, int method, int species)
	{
		this.agentList = agentList;
		this.method = method;
		this.species = species;
	}
	
	/**
	 * mehtod impliemnted from NumericDataSource
	 */
	public double execute()
	{
		return calculate();
	}
	
	/**
	 * mehtod impliemnted from Sequence
	 */
	public double getSValue()
	{
		return calculate();
	}
	
	/**
	 * Finding the number of ticks form the agent class 
	 * 
	 * @param organization
	 * @return
	 */
	private int getTicksToFindFitter(Object organization)
	{
		int ticks = 0;
		if(organization instanceof NKOrganization)
		{//if the agent is an NK Organiztaions
			NKOrganization org = (NKOrganization) organization;
			ticks = org.getTicksToFindFitterVariant();
		}
		else if (organization instanceof CoevolutionarySet)
		{//if the agent is a coevolutioary set
			CoevolutionarySet set = (CoevolutionarySet) organization;
			NKCOrganization org = set.getSpecies(species);
			ticks = org.getTicksToFindFitterVariant();
		}
		else
		{
			System.err.println("There has been a fatal error");
			System.exit(1);
		}
		
		return ticks;
	}
	
	/**
	 * Returning the wait time for the next move 
	 * over all organizations this tick
	 */
	private double calculate()
	{
		if(method == ParameterOptions.GRAPH_AVERAGE)
		{//if graphing the average
			int wait = 0;
			int count = 0;
			for(int i = 0; i < agentList.size(); i++)
			{//for every organziation in the agent list
				int ticksToFindFitterVariant = getTicksToFindFitter(agentList.get(i));
				//add the number of ticks the organization had to wait last time
				wait = wait + ticksToFindFitterVariant;
				count++;
			}
			//return the average wait time
			if (count != 0)
			{
				return wait / count;
			}
			else
			{
				return 0;
			}
		}
		else if (method == ParameterOptions.GRAPH_MAXIMUM)
		{//if graphing the maximum
			int max_wait = 0;
			for(int i = 0; i < agentList.size(); i++)
			{//for every organization in the agent list
				int ticksToFindFitterVariant = getTicksToFindFitter(agentList.get(i));
				if(ticksToFindFitterVariant > max_wait)
				{
					//if the time this organization has had to wait is 
					//higher than any other organziation save the value
					max_wait = ticksToFindFitterVariant;
				}
			}
			return max_wait;
		}
		else if (method == ParameterOptions.GRAPH_MINIMUM)
		{//if graphing the minimum
			if(agentList.size() > 0)
			{
				int min_wait = getTicksToFindFitter(agentList.get(0));
				for(int i = 1; i < agentList.size(); i++)
				{//for every organization in the agent list
					int ticksToFindFitterVariant = getTicksToFindFitter(agentList.get(i));
					if(ticksToFindFitterVariant < min_wait)
					{
						//if the minimum wait is smaller than that of any 
						//other orgnaiztion then save it
						min_wait = ticksToFindFitterVariant;
					}
				} 
				return min_wait;
			}
		}
		return 0;
	}

}