package DataCollection;

import java.util.ArrayList;
import NKCModel.CoevolutionarySet;
import NKModel.NKOrganization;
import uchicago.src.sim.analysis.NumericDataSource;
import uchicago.src.sim.analysis.Sequence;

/**
 * 
 * This class, given an agent list calculates the nume rof agent still 
 * walking
 * 
 * @author Amy Marshall
 *
 */
public class FractionStillWalking implements NumericDataSource, Sequence
{
	//class varriables
	private ArrayList agentList;
	
	/**
	 * setting up the fraction still walkaing class
	 * 
	 * @param agentList the list of agents in the simulation
	 * @param landscape the andscape the agents are traversing
	 * @param species the species of agent if this is for the NKC Model
	 */
	public FractionStillWalking(ArrayList agentList)
	{
		this.agentList = agentList;
	}

	/**
	 * method impliemnted from NumericDataSource
	 */
	public double execute()
	{
		return calculate();
	}
	
	/**
	 * method impliemnted from Sequence
	 */
	public double getSValue()
	{
		return calculate();
	}
	
	/**
	 * calculate the fraction of agents still walking
	 * 
	 * @return double
	 */
	private double calculate()
	{
		double still_walking = 0.0;
		for(int i = 0; i < agentList.size(); i++)
		{//for every agent in the agent list
			if(agentList.get(i) instanceof NKOrganization)
			{//if it is an NKOrgnaiztions
				NKOrganization org = (NKOrganization) agentList.get(i);
				if(org.getStillWalking())
				{//if it is still walkaing
					//increment the figure of orgnaiztions still walkling
					still_walking++;
				}
			}
			else if (agentList.get(i) instanceof CoevolutionarySet)
			{//if it is an agent in the NKC Model
				CoevolutionarySet set = (CoevolutionarySet) agentList.get(i);
				if(set.getStillWalking())
				{//if the given species is still walkaing
					//increment the figure of orgnaiztions still walkling
					still_walking++;
				}
			}
		}
		return (still_walking/agentList.size())*100.0;
	}	

}
