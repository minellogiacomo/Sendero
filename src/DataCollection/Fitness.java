package DataCollection;

import java.util.ArrayList;

import uchicago.src.sim.analysis.NumericDataSource;
import uchicago.src.sim.analysis.Sequence;
import CrossModelClasses.AbstractFitnessLandscape;
import CrossModelClasses.ParameterOptions;
import NKCModel.CoevolutionarySet;
import NKCModel.NKCOrganization;
import NKModel.NKOrganization;

/**
 * 
 * This class, given an agent list calculates the fitness of each 
 * orgnaiztion 
 * 
 * @author Amy Marshall
 *
 */
public class Fitness implements NumericDataSource, Sequence
{
	//classs variables
	private int method;
	private ArrayList agentList;
	private AbstractFitnessLandscape landscape;
	private int species;
	
	
	/**
	 * This method sets up the fitness data colletcion class
	 * 
	 * @param agentList the list of agents in the simulation
	 * @param landscape the landscape the orgnaiztion is traversing
	 * @param method max, min or average fitness
	 * @param species of organization (if NKC)
	 */
	public Fitness(ArrayList agentList, AbstractFitnessLandscape landscape, int method, int species)
	{
		this.agentList = agentList;
		this.landscape = landscape;
		this.method = method;
		this.species = species;
		
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

        private double calculate()
        {   
            // fitness values to add, min or max
            ArrayList <Double> fitness_values = new ArrayList<Double> (agentList.size());

            // for each agent in agent list
            for (Object agent : agentList)
            {                          
                // is it NK or NKC organization?
                // need to get fitness value as appropriate
                if (agent instanceof NKOrganization)
                {
                    NKOrganization thisNKAgent = (NKOrganization) agent;
                    fitness_values.add(thisNKAgent.getFitness(null));
                }
                else if (agent instanceof CoevolutionarySet)
                {
                    CoevolutionarySet thisNKCAgent = (CoevolutionarySet) agent;
                    fitness_values.add(thisNKCAgent.getSpeciesFitness(species));
                }
            }

            switch (method) {
            // get minimum maximum or average from fitness values as required
            case ParameterOptions.GRAPH_AVERAGE:
                double total = 0;
                
                for (double value : fitness_values)
                {
                    total += value;
                }

               double average = (total / fitness_values.size());
	       return average;
            
            case ParameterOptions.GRAPH_MINIMUM:
		double min = 1;
                
                for (double value : fitness_values) 
                {
                    if (value < min) {
                            min = value;
                    }
                }
	       return min;
		
                        
            case ParameterOptions.GRAPH_MAXIMUM:
                double max = 0;
                
                for (double value : fitness_values) 
                {			
                    if (value > max) 
                    {
                        max = value;
                    }
                }
	        return max;            
       
            default:
                return -1;
            }
        }	
}