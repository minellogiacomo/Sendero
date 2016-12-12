package NKModel;

import java.util.ArrayList;
import java.util.Date;

import CrossModelClasses.ParameterOptions;
import CrossModelClasses.AbstractOrganization;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import uchicago.src.sim.network.Edge;
import uchicago.src.sim.network.Node;



/**
 * The organization class is an agent that moves from location to location
 * across the landscape (moving only when the next location found is fitter)
 * 
 * @author 	Amy Marshall
 * @version	1
 */

public class NKOrganization extends AbstractOrganization implements Node
{

	//local variables passed in from the Model
	private int jump_J;
	private int jump_successful_limit;
	private int jump_search_time_limit;
	private boolean communications;
	
	//monitors how many successful jumps have been taken
	private int successfulJumps;
	private int unsuccessfulJumps;
	
	private boolean still_walking;
	//private boolean still_jumping;
	
	private ArrayList<Edge> inEdges;
	private ArrayList<Edge> outEdges;
	private String nodeLabel;
	
	public NKOrganization()
	{
		//set to true as the orngaiztion has only just been created
		//and thus will still be moving
		still_walking = true;
		
		//creating the random genorators
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
		
		inEdges = new ArrayList<Edge>();
		outEdges = new ArrayList<Edge>();
		nodeLabel = "";
	}
	
	/**
	 * Set up the orgnaiztion, called when the organiztion is created
	 * to give it all the detail needed to walka over the landscape
	 * 
	 * @param landscape the fitness landscape belonging to the orgnaiztion
	 * @param location_key the current location of the orgnaiztion
	 * @param fitness_threshold under which the orgnaiztion wont move
	 * @param jump_J whether or not the orngaiztion will jump
	 * @param jump_successful_limit number of successful jumps allowed
	 * @param jump_search_time_limit number of failed jump attempts allowed
	 * @param communications whether or not the organization can communicate with others
	 * @param organizational_walk_type the way the orgnaiztion walks over the landscape
	 */
	public void setUpOrganization(NKFitnessLandscape landscape, String location_key, 
			double fitness_threshold, int jump_J, int jump_successful_limit, 
			int jump_search_time_limit, boolean communications, 
			int organizational_walk_type, int next_neighbour_method)
	{
		//setting initial location on landscape
		this.landscape = landscape;
		this.location_key = location_key;
		location_fitness = landscape.getFitness(location_key, null);
		
		//setting the parameters passed through from the model
		this.fitness_threshold = fitness_threshold;
		this.jump_J = jump_J;
		this.jump_successful_limit = jump_successful_limit;
		this.jump_search_time_limit = jump_search_time_limit;
		this.communications = communications;
		this.organizational_walk_type = organizational_walk_type;
		this.next_neighbour_method = next_neighbour_method;
		//setting up local variables
		ticksToFindFitterVariant = 0;
		ticksSinceLastMove = 0;
		
		// need to calculate fitter neighbour count
		// in future will be done when the org moves
		// by moveTo method.
		fitterNeighboursCount = -1;
		
		nearestNeighbours = landscape.getAllNeighbours(location_key);
		
		for (String thisNeighbour: nearestNeighbours) {
			if (thisNeighbour != null) 
                        {
				if (super.fitterNeighboursCount == -1)
                                {
                                    super.fitterNeighboursCount=0;
                                }
				Double neighbourFitness = landscape.getFitness(thisNeighbour, null);
				if (neighbourFitness > super.location_fitness) 
                                {
                                    super.fitterNeighboursCount++;
                                }
                        }
		}
		
	}
	
	/**
	 * Records whether or not the orgnaiztaion is still walking (this is used
	 * by the data collection class)
	 * 
	 * @return true is the orgnaiztaion is still walkling, flase otherwise
	 */
	public boolean getStillWalking()
	{
		return still_walking;
	}
	
	
	/**
	 * Returns whether or not the organisation has finished jumping
	 * 
	 * @return true is the number of jumps taken is equal to the limit 
	 */
	public boolean reachedJumpLimit()
	{
		if((successfulJumps == jump_successful_limit)&&(jump_successful_limit != 0))
		{
                    return true;
		}
                if((unsuccessfulJumps == jump_search_time_limit) && (jump_search_time_limit!=0))
                {
                    return true;
                }
                
                return false;
		
	}
	
	/**
	 * Move the organization to this location
	 * 
	 * @param location_key	location to move to
	 */
	public void moveTo(String location_key, String[] C_locations)
	{
		super.moveTo(location_key, C_locations);
		unsuccessfulJumps = 0;
		still_walking = true;
		
		nearestNeighbours = landscape.getAllNeighbours(location_key);
		
		for (String thisNeighbour: nearestNeighbours) {
			if (thisNeighbour != null) {
				if (super.fitterNeighboursCount == -1) 
                                {
                                    super.fitterNeighboursCount = 0;
                                }
				Double neighbourFitness = landscape.getFitness(thisNeighbour, null);
				if (neighbourFitness > super.location_fitness) 
                                {
                                    super.fitterNeighboursCount++;
                                }
				}
                        }
                }
	
	/**
	 * Returns the current fitness of the organization
	 * 
	 * @return Double
	 */
	public Double getFitness(String[] C_locations)
	{
		return location_fitness;
	}
	
	/**
	 * Returns the current number of fitter neighbours for the organization
	 * 
	 * @return int
	 */
	public int getFitterNeighboursCount()
	{
		return fitterNeighboursCount;
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
		//The organization tries to communicate with others in its network
		if(communications)
		{//if communications are activated
			if(searchCommunications())
			{//if there is a fitter option within the network
				//dont continue
				return true;
			}
		}
		
		//if there isn't a fitter alternative in the network or communication 
		//are not activated use nearest neighbours - possibly already populated in super.moveTo
		
		if(nearestNeighbours == null)
		{//find the nearest neighbours of the current location
			nearestNeighbours = landscape.getAllNeighbours(location_key);
		}
		
		if(step())
		{//if a step across the landscape is taken return and do not attempt to jump
			return true;
		}
		
                //if we have looked at all the fitter neighbours consider jumping

                //before we consider jumping - have we reached the successful 
                //jumps threshold if so, need to stop walking / jumping
        
              
		if((((successfulJumps < jump_successful_limit)||(jump_successful_limit == 0))&&(jump_J == 1))
			&&((jump_search_time_limit == 0)||(unsuccessfulJumps < jump_search_time_limit)))
		{//if the organization has made fewer successful jumps than the maximum
			//assk the current location for a long jump
			String jump = landscape.getLongJump(jump_search_time_limit);
    		if(jump != null)
    		{//if a jump is returned
    			if(landscape.getFitness(jump, null) - getFitness(null) > fitness_threshold)
    			{//if the new location is fitter than the current location then move
    				moveTo(jump, null);
    				still_walking = true;
    				successfulJumps++;

    				return true;
    			}
    			else
    			{//if the new location is less fit that the current location
                               
    				unsuccessfulJumps++;

    			}
    		}
                       
		}
		
		//if network, step and jump have failed, then we won't move
		return false;	
	}	
	
	/**
         * 
	 * Attempts to take a step across the landscape, return true if a step is 
	 * made and false otherwise
	 * 
	 * @param C_locations lists current locations of other species of orgnaization
	 * @return true if a step acros the landscape is taken and flase otherwise
	 */
	private boolean step()
	{
		if(organizational_walk_type == ParameterOptions.ONE_MUTANT_NEIGHBOUR)
		{//on each tick look at one neighbour see if it is fitter and move to it if it is
			if(nextNeighbour < nearestNeighbours.length)
	    	{//if there is another nearest neighbour available
				String neighbour = null;
				neighbour = getNextNeighbour(next_neighbour_method);
				if(neighbour != null)
				{//neighbour will return null if there are no more spaces to check for fitter neighbours
					if(landscape.getFitness(neighbour, null) - getFitness(null) > fitness_threshold)
					{//if the next neighbour returned is fitter the that current position
						moveTo(neighbour, null);
						return true;
					}
				}
				else
				{//if there are no more spaces left to check then
					nextNeighbour = nearestNeighbours.length;
					still_walking = false;
				}
	    	}
			else
			{//otherwise stop walking
				still_walking = false;
			}
		}
		else if (organizational_walk_type == ParameterOptions.FITTER_DYNAMICS)
		{//on each tick look at all neighbours and move to one of the fittest ones
			double fittest_location_value = location_fitness;
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
					double next_location_value = landscape.getFitness(next_location, null);
					if(next_location_value > fittest_location_value)
					{//if the fitness is higher than the current fitteset
						//clear the array list
						same_fitness.clear();
						//add the new location
						same_fitness.add(next_location);
						//update the fittest value
						fittest_location_value = next_location_value;
					}
					else if (next_location_value == fittest_location_value)
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
                                // provided chosen new location is fitter by more than threshold
                                if (fittest_location_value - location_fitness > fitness_threshold) 
                                {
                                    moveTo(same_fitness.get(step), null);
                                    return true;
                                }
				
			}
			else
			{//otherwise stop walkling
				still_walking = false;
			}
		}
		else if (organizational_walk_type == ParameterOptions.GREEDY_DYNAMICS)
		{//on each tick look at neighbours in turn until one is found that is fitter and move to that one
			for (int i = 0; i < nearestNeighbours.length; i++)
			{//for every neighbour of this location
				String next_neighbour = nearestNeighbours[i];
				if(next_neighbour != null)
				{// the neighbour is not empty
					//find its fitness
					Double next_neighbour_fitness = landscape.getFitness(next_neighbour, null);
					
                                        if(next_neighbour_fitness - location_fitness > fitness_threshold)
					{//if its fitness is higher (by threshold) then move
						moveTo(next_neighbour, null);
						return true;
					}
				}
			}
			//if no higher fitness is found stop walkling
			still_walking = false;
		}
		//if no the method arrives here no move has been made so return false
		return false;
	}
	
	
	/**
	 *  Sending a communication out through the edges of the netwokr
	 *  
	 */
	public void sendCommunications()
	{
		for (int i = 0; i < outEdges.size(); i++)
		{//for every edge going out to an organziation
			OrganizationEdge e = (OrganizationEdge) outEdges.get(i);
			//leave a message for that organzitaion to pick up
			e.leaveFitnessMessage(location_fitness,location_key);
		}
	}
	
	/**
	 * Looking at all other agents communications through the edges of the network
	 * 
	 * @return true if the organization moved to another location using one of these 
	 * communications false otherwise
	 */
	private boolean searchCommunications()
	{
		String best_location = location_key;
		double best_fitness = location_fitness;
		for (int i = 0; i < inEdges.size(); i++)
		{//for every edge going into an organization
			OrganizationEdge e = (OrganizationEdge) inEdges.get(i);
			//collect the message that organziation left
			FitnessMessage msg = e.readFitnessMessage();
			double edge_fitness = msg.getFitness();
			String edge_location = msg.getLocation();
			
			//if it has a better fitness than the current orgnaiztaion or any 
			//other linked orgnaization whoes message has been read so far
			if(edge_fitness > best_fitness)
			{//then save the details of the loction this orgnaiztaion is at
				best_location = edge_location;
				best_fitness = edge_fitness;
			}
		}
		if(!best_location.equals(location_key))
		{//if there is a location that is better than the current one move to it
			moveTo(best_location, null);
			//return true so that the will not be another move this time step
			return true;
		}
		//return flase so that an adaptive walk will be attempted this time step
		return false;
	}
	
	/**
	 * Part of Node interface, add an edge going into this node
	 * this edge is used to collect information
	 * 
	 */
	public void addInEdge(Edge edge) {
		inEdges.add(edge);
	}

	/**
	 * Part of Node interface, add an out edge coming form this node,
	 * this edge is used to send information
	 */
	public void addOutEdge(Edge edge) {
		outEdges.add(edge);
	}

	/**
	 * Part of Node interface, remove all in edges
	 */
	public void clearInEdges() {
		inEdges.clear();
	}

	/**
	 * Part of Node interface, remove all out edges
	 */
	public void clearOutEdges() {
		outEdges.clear();
	}

	/**
	 * NOT USED, IMPLIMENT LATER IF NEEDED
	 */
	public Object getId() {
		return null;
	}

	/**
	 * Part of Node interface, return an array list of all in edges
	 */
	public ArrayList<Edge> getInEdges() {
		return inEdges;
	}

	/**
	 * Part of Node interface, return the node label
	 */
	public String getNodeLabel() {
		return nodeLabel;
	}

	/**
	 * Part of Node interface, return an array list of all out edges
	 */
	public ArrayList<Edge> getOutEdges() {
		return outEdges;
	}

	/**
	 * Part of Node interface, return true if there is an in edge from this
	 * node to the inputted node
	 * 
	 * @param Node node
	 */
	public boolean hasEdgeFrom(Node node) {
		for(int i = 0; i < inEdges.size();i++)
		{
			Edge e = (Edge) inEdges.get(i);
			Node n = e.getFrom();
			if(n == node)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Part of Node interface, return true if there is an out edge from this
	 * node to the inputted node
	 * 
	 * @param Node node
	 */
	public boolean hasEdgeTo(Node node) {
		for(int i = 0; i < outEdges.size();i++)
		{
			Edge e = (Edge) outEdges.get(i);
			Node n = e.getTo();
			if(n == node)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Part of Node interface, remove the specified edge from the in edge 
	 * array list
	 * 
	 * @param Edge edge
	 */
	public void removeInEdge(Edge edge) {
		inEdges.remove(edge);
	}

	/**
	 * Part of Node interface, remove the specified edge from the out edge 
	 * array list
	 * 
	 * @param Edge edge
	 */
	public void removeOutEdge(Edge edge) {
		outEdges.remove(edge);
	}

	/**
	 * Part of Node interface, set the node label to the give string
	 * 
	 * @param String label
	 */
	public void setNodeLabel(String label) {
		nodeLabel = label;
	}

}