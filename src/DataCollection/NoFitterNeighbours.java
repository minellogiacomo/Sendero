package DataCollection;

import java.util.ArrayList;
import java.util.Iterator;

import uchicago.src.sim.analysis.NumericDataSource;
import uchicago.src.sim.analysis.Sequence;
import CrossModelClasses.AbstractFitnessLandscape;
import CrossModelClasses.ParameterOptions;
import NKCModel.CoevolutionarySet;
import NKModel.NKOrganization;

/**
 * 
 * This class, given an agent list calculates the number of fitter
 * niehgbours each orgnaiztion has 
 * 
 * @author Amy Marshall
 *
 */
public class NoFitterNeighbours implements NumericDataSource, Sequence
{
	//class variables
	private int method;
	private ArrayList agentList;
	private AbstractFitnessLandscape landscape;
	private int N_size_of;
	private int A_size_of;
	private int species;
	private String[] other_locations;
	
	/**
	 * Sets up the no of fitter neighbours class setting the class variables
	 * 
	 * @param agentList the list of all agents in the simulation
	 * @param landscape the landscape the agents traverse
	 * @param method the method max, min or average
	 * @param N_size_of the no of charactersitics in the orgnaiztion
	 * @param A_size_of the number of states of each characteristic
	 * @param species the spsceies of orgnaiztion we are deaing with (if NKC)
	 */
	public NoFitterNeighbours(ArrayList agentList, AbstractFitnessLandscape landscape, int method, int N_size_of, int A_size_of, int species)
	{
		this.agentList = agentList;
		this.landscape = landscape;
		this.method = method;
		this.N_size_of = N_size_of;
		this.A_size_of = A_size_of;
		this.species = species;
		other_locations = null;
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
            //first get the count of fitter neighbours from each agent and store
            ArrayList<Integer> fitterNeighbourValues = new ArrayList<Integer>(agentList.size());
            
            for (int j=0; j < agentList.size(); j++) { 
                Object agent = agentList.get(j);
                if(agent instanceof NKOrganization) {
                    NKOrganization nkAgent = (NKOrganization)agent;
                    int thisagentvalue = nkAgent.getFitterNeighboursCount();
                    if (thisagentvalue!=-1)
		    {
			    fitterNeighbourValues.add(thisagentvalue);
		    }
                }
                if(agent instanceof CoevolutionarySet) {
                    CoevolutionarySet nkcAgent = (CoevolutionarySet)agent;
		    int thisagentvalue = nkcAgent.getSpeciesFitterNeighbours(species);
                    if (thisagentvalue!= -1)
		    {
			    fitterNeighbourValues.add(thisagentvalue);
		    }		
                }                
            }

            switch (method) {
            
            case ParameterOptions.GRAPH_AVERAGE:
                double total = 0;
                
                Iterator<Integer> itr = fitterNeighbourValues.iterator();
                while(itr.hasNext()) {
                    total += itr.next();
                }

               double average = (total / fitterNeighbourValues.size());
	       return average;
            
            case ParameterOptions.GRAPH_MINIMUM:
                
		double min = (A_size_of - 1) * N_size_of;
		
                Iterator<Integer> itr2 = fitterNeighbourValues.iterator();
                while(itr2.hasNext()) {	
			double test = (double)itr2.next();

			if (test < min) {
				min = test;
			}
                }
	       return min;
		                  
            case ParameterOptions.GRAPH_MAXIMUM:
                double max = 0;
		
                Iterator<Integer> itr3 = fitterNeighbourValues.iterator();
                while(itr3.hasNext()) {
			
			double test = (double)itr3.next();
			if (test > max) {
				max = test;
			}
                }
		return max;
       
            default:
                return -1;
            }
        }
}
