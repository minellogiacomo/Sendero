package NKCModel;

import java.util.ArrayList;
import java.util.Date;

import CrossModelClasses.AbstractOrganization;
import CrossModelClasses.ParameterOptions;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;


/**
 * The organization class is an agent that moves from location to location
 * across the landscape (moving only when the next location found is fitter)
 * 
 * In the NKC Model this is an agent within an agent. The coevolutionary set 
 * is the main Repast agent, added to the Repast AgentList.  But every
 * coevolutionary set is made up of a number of NKCOrgnaiztions that move over 
 * their own individual landscapes
 * 
 * @author 	Amy Marshall
 * @version	1
 */

public class NKCOrganization extends AbstractOrganization
{
	
	/**
	 * Creating the orgnaiztion
	 *
	 */
	public NKCOrganization()
	{	
		//create the random genorator
		MersenneTwister generator2;
		if(ParameterOptions.SEED == ParameterOptions.DATE)
		{
			Date date = new Date();
			generator2 = new MersenneTwister(date);
		}
		else
		{
			generator2 = new MersenneTwister(123);
		}
	    uniform = new Uniform(generator2);
	}
	
		
	
	/**
	 * Set up the organization, called when the organization is created
	 * to give it all the detail needed to walka over the landscape
	 * 
	 * @param landscape the fitness landscape belonging to the orgnaiztion
	 * @param location_key the current location of the orgnaiztion
	 * @param fitness_threshold under which the orgnaiztion wont move
	 * @param organizational_walk_type the way the orgnaiztion walks over the landscape
	 */
	public void setUpOrganization(NKCFitnessLandscape landscape, String location_key, 
			double fitness_threshold, int organizational_walk_type, 
                        int next_neighbour_method)
	{
		//setting initial location on landscape
		this.landscape = landscape;
		this.location_key = location_key;
		location_fitness = 0.0;
		
		//setting the parameters passed through from the model
		this.fitness_threshold = fitness_threshold;
		this.organizational_walk_type = organizational_walk_type;
		this.next_neighbour_method = next_neighbour_method;
		
		//setting up local variables
		ticksToFindFitterVariant = 0;
		ticksSinceLastMove = 0;		
	}
		
	/**
	 * Returns the current fitness of the organization
	 * 
	 * @return Double
	 */
	public Double getFitness(String[] C_locations)
	{
		return landscape.getFitness(location_key, C_locations);
	}

        public Double getFitnessThreshold()
        {
            return fitness_threshold;
        }
	
	/**
	 * Every step of the simulation the organization will attempt to take 
	 * a step on its adaptive walk.  
	 * 
	 * If communication is activaed the organization will first attemtp to 
	 * communicate with others in its network to determin if it can move 
	 * to another place on the landscape through the network
	 * 
	 * Otheriwse this will be a step across the landscape to a one mutant 
	 * neighbour if there is a fitter one, and a jump if not (IFF jumps are 
	 * activated)
	 *
	 */
	public boolean adaptiveWalk(String[] C_locations)
	{
		ticksSinceLastMove++;		

		//if there isn't a fitter alternative in the network or communication 
		//are not activated
		
		if(nearestNeighbours == null)
		{//find the nearest neighbours of the current location
			nearestNeighbours = landscape.getAllNeighbours(location_key);
		}
		return step(C_locations);        
	}	
	
	/**
	 * Attempts to take a step across the landscape, return true if a step is 
	 * made and false otherwise
	 * 
	 * @param C_locations lists current locations of other species of orgnaization
	 * @return true if a step acros the landscape is taken and flase otherwise
	 */
	private boolean step(String[] C_locations)
	{
                nearestNeighbours = landscape.getAllNeighbours(location_key);

		if(organizational_walk_type == ParameterOptions.ONE_MUTANT_NEIGHBOUR)
		{//on each tick look at one neighbour see if it is fitter and move to it if it is
			if(nextNeighbour < nearestNeighbours.length)
	    	{//if there is another nearest neighbour available
				String neighbour = null;
				neighbour = getNextNeighbour(next_neighbour_method);
				
				if(neighbour==null)
				{
					nextNeighbour = 0;
					neighbour = getNextNeighbour(next_neighbour_method);
				}
				
				if(neighbour != null)
				{//neighbour will return null if there are no more spaces to check for fitter neighbours
					if(landscape.getFitness(neighbour, C_locations) - getFitness(C_locations) > fitness_threshold)
					{//if the next neighbour returned is fitter the that current position
						moveTo(neighbour, C_locations);
						return true;
					}
				}
                	}
			
		}
		else if (organizational_walk_type == ParameterOptions.FITTER_DYNAMICS)
		{//on each tick look at all neighbours and move to one of the fittest ones
			double fittest_location_value = getFitness(C_locations);
			//create an array list to store all locations found with the max 
			//fittness (if there is more than one location
			ArrayList<String> same_fitness = new ArrayList<String>();
			//add the fitness of the current location to this
			same_fitness.add(location_key);
			
			for (int i = 0; i < nearestNeighbours.length; i++)
			{//for every neighbour to this location
				//look at the next neighbour
				String next_location = nearestNeighbours[i];
				if(next_location != null)
				{//if the next neighbour is not equal to null
					//find its fitness
					double next_location_value = landscape.getFitness(next_location, C_locations);
					if((next_location_value + fitness_threshold) > fittest_location_value)
					{//if the fitness is higher than the current fitteset
						//clear the array list
						same_fitness.clear();
						//add the new loction
						same_fitness.add(next_location);
						//update the fittest value
						fittest_location_value = next_location_value;
					}
					else if ((next_location_value + fitness_threshold) == fittest_location_value)
					{//if the location has the same value as the current fittest 
						same_fitness.add(next_location);
					}
				}
			}
			
			if(!(same_fitness.get(0)).equals(location_key))
			{//if the highest fitness isn't this location
				//randomly choose one of the locations with highest fitness
				int step = uniform.nextIntFromTo(0, same_fitness.size() - 1);
				//move to that location
				moveTo(same_fitness.get(step), C_locations);
				return true;
			}
			
		}
		else if (organizational_walk_type == ParameterOptions.GREEDY_DYNAMICS)
		{//on each tick look at neighbours in turn until one is found that is fitter and move to that one
                        double currentFitness = getFitness(C_locations);
                        for (int i = 0; i < nearestNeighbours.length; i++)
			{//for every neighbour of this location
				String next_neighbour = nearestNeighbours[i];
				if(next_neighbour != null)
				{// the neighbour is not empty
					//find its fitness
					Double next_neighbour_fitness = landscape.getFitness(next_neighbour, C_locations);
					if((next_neighbour_fitness - currentFitness) > fitness_threshold)
					{//if its fitness is higher than the cururent fitness move
						moveTo(next_neighbour, C_locations);
						return true;
					}
				}
			}
		}
		//if no the method arrives here no move has been made so return false
		return false;
	}

}