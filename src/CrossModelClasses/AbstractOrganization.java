package CrossModelClasses;

import java.util.ArrayList;
import cern.jet.random.Uniform;


/**
 * 
 * The interface by which both NK and NKC Organisms have to abide
 * 
 * @author Administrator
 *
 */
public abstract class AbstractOrganization
{
//	The location that the organism is currently at
	protected String location_key;
	protected Double location_fitness;
	protected AbstractFitnessLandscape landscape;
	protected Uniform uniform;
	
	//local variables passed in from the Model
	protected double fitness_threshold;
	protected int organizational_walk_type;

	//local variabls
	//monitors how many neighbours have been looked at so far
	protected int nextNeighbour;
	//monitors how many ticks it took to make the last move
	protected int ticksToFindFitterVariant;
	//monitors how many ticks have passed since the last move
	protected int ticksSinceLastMove;
	//contains all neighbours of the current location
	protected String[] nearestNeighbours;
	//the method the next neighbour is chosen
	protected int next_neighbour_method;
	//the neighbours already looked at (used only with random_with_memory)
	protected ArrayList<Integer> looked_at;
	
	//counter for number of fitter neighbours
	protected int fitterNeighboursCount;
	
	
	/**
	 * Move the organism to this location
	 * 
	 * @param location_key	location to move to
	 */
	protected void moveTo(String location_key, String[] C_locations)
	{
		//change the location
		this.location_key = location_key;
		this.location_fitness = landscape.getFitness(location_key, C_locations);
		//set counters back to initial state
		nextNeighbour = 0;
		nearestNeighbours = null;
		fitterNeighboursCount = -1;
		//set the number of ticks since the last move 
		ticksToFindFitterVariant = ticksSinceLastMove;
		ticksSinceLastMove = 0;
		looked_at = new ArrayList<Integer>();
		
	}
	
	/**
	 * Returns the current location of the organism
	 * SHOULD ONLY BE USED BY THE MODEL CLASS
	 * 
	 * @return Location
	 */
	public String getLocation()
	{
		return location_key;
	}
	
	//method to ge the current fitness
	protected abstract Double getFitness(String[] C_locations);
	
	/**
	 * How many ticks it took to find a fitter variant last time
	 * SHOULD ONLY BE USED BY THE MODEL CLASS
	 * 
	 * @return
	 */
	public int getTicksToFindFitterVariant()
	{
		return ticksToFindFitterVariant;
	}
	
	//method that orcestrate the movement across the landscape
	protected abstract boolean adaptiveWalk(String[] C_locations);
	
	/**
	 * get the next neighbour to that organism
	 * 
	 * @return Location of next neighbour
	 */
	public String getNextNeighbour(int next_neighbour_method)
	{
		if(looked_at == null)
		{//if the ArrayList is null create it
			looked_at = new ArrayList<Integer>();
		}
		
		String next_location = null;
		if(next_neighbour_method == ParameterOptions.SYSTEMATIC)
		{
			while((next_location == null)&&(nextNeighbour < nearestNeighbours.length))
			{//whilst there are more neighbours, and whilst the next thing in the array is null
				//find the next thing i the array
				next_location = nearestNeighbours[nextNeighbour];
				nextNeighbour++;
			}
			return next_location;
		}
		else if(next_neighbour_method == ParameterOptions.RANDOM_WITH_MEMORY)
		{//if we chose the next neighbour from the set of all neighbours randomly
			while((next_location == null)&&(nextNeighbour < nearestNeighbours.length))
			{//while we still have more to look at and we haven't chosen one yet
				//choose one
				int nextNeighbour = uniform.nextIntFromTo(0, (nearestNeighbours.length-1));
				for(int a = 0; a < looked_at.size(); a++)
				{//look through all the ones previously chosen
					if(looked_at.get(a).intValue() == nextNeighbour)
					{//if the one chosen has previously been used
						//set the nextNieghbour to invalid
						nextNeighbour = -1;
					}
				}
				if(nextNeighbour != -1)
				{//if netNeighbour is not invalid
					next_location = nearestNeighbours[nextNeighbour];
					this.nextNeighbour++;
					Integer integer = new Integer(nextNeighbour);
					looked_at.add(integer);
				}//if nextNeighbour is invalid the loop will be run again
			}
			return next_location;
		}
		else // if next_neighbour_method == ParameterOptions.RANDOM_NO_MEMORY
		{
			//continue searching the array untill a new location is found
			while(next_location == null)
			{
				//choose the next location randomly
				int nextNeighbour = uniform.nextIntFromTo(0, nearestNeighbours.length-1);
				next_location = nearestNeighbours[nextNeighbour];
			}
			return next_location;
		}
	}
}
