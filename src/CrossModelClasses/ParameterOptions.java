package CrossModelClasses;

/**
 * 
 * This class contains parameters used by classes of both models
 * 
 * @author Amy Marshall
 *
 */

public class ParameterOptions
{
	//distribution of K and A
	public static final int IDENTICAL = 0;
	public static final int RANDOM = 1;
	public static final int GAUSSIAN = 2;
	public static final int NEIGHBOURS = 0;

	//how an organization moves over the landscape
	public static final int WALK = 0;
	public static final int LONG_JUMP = 1;
                        
        // order in which NKC species move on the landscape
        public static final int SEQUENTIAL = 0;

	//method of calculating fitness
	public static final int AVERAGE = 0;
	public static final int WEAKEST = 1;

	//how the next neighbour is found
	public static final int SYSTEMATIC = 0;
	public static final int RANDOM_WITH_MEMORY = 1;
	public static final int RANDOM_NO_MEMORY = 2;

	//types of network
	public static final int LINEAR_NETWORK = 0;
	public static final int FULLY_CONNECTED_NETWORK = 1;
	public static final int RANDOM_NETWORK = 2;
	public static final int SMALL_WORLD_NETWORK = 3;
	
	//organizational life and death
	public static final int RANDOM_NEW_ORG = 0;
	public static final int COPY_OLD_ORG = 1;
	public static final int BOTH = 2;

	//graphing options
	public static final int GRAPH_AVERAGE = 0;
	public static final int GRAPH_MAXIMUM = 1;
	public static final int GRAPH_MINIMUM = 2;
	
	//Random stuff
	public static final int DATE = 0;
	public static final int STATIC = 1;
	public static int SEED = DATE;
	
	//orgnaizinal walk tyope
	public static final int ONE_MUTANT_NEIGHBOUR = 0;
	public static final int FITTER_DYNAMICS = 1;
	public static final int GREEDY_DYNAMICS = 2;
	
	//data collections
	public static final int ON_SCREEN = 0; 
	public static final int TO_FILE = 1;
	//public static final int BOTH = 2; as specified above

        // whether to stop simulation if all species stop walking
        public static final int RUN_TO_SIMULATION_HALT = 0;
        public static final int STOP_EARLY = 1;
	
}