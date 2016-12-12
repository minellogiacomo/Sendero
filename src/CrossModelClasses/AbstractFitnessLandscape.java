package CrossModelClasses;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import gnu.trove.TObjectFloatHashMap;
/**
 * 
 * The interface by which both NK and NKC Fitness Landscapes have to abide
 * 
 * @author Administrator
 *
 */
public abstract class AbstractFitnessLandscape
{
	
	protected double[] weightings;
	protected int[][] array_K;
	protected int N_size_of;
	protected int K_size_of;
	protected int[] array_A;
	protected int fitness_range_dp;
	
	protected TObjectFloatHashMap[] fitnessCache;
	protected Uniform uniform;
	protected Normal normal;
	
	protected int K_identical_or_random;
	protected int K_neighbours_or_random;
	protected int fitness_method_averaging_weightings;
	protected int fitness_method;
	
	//the method that calculates the fitness of a location
	public abstract double getFitness(String key, String[] locations);
	
	
	/**
	 * Creating an array of how many K each characteristic depends upon
	 * 
	 * @return
	 */
	protected int[][] createKList()
	{
		int[] K = new int[N_size_of];
		if(K_identical_or_random == ParameterOptions.IDENTICAL)
		{//if all K should be identical
			for(int n = 0; n < K.length; n++)
			{//put K in every cell of the array
				K[n] = K_size_of;
			}
		}
		else if(K_identical_or_random == ParameterOptions.RANDOM)
		{//if K is meant to be random
			for(int n = 0; n < K.length; n++)
			{//put a random number between one and K in each cell of the array
				if(K_size_of*2 >= N_size_of)
				{
					int difference = N_size_of - K_size_of - 1; 
					K[n] = uniform.nextIntFromTo(K_size_of-difference, K_size_of+difference);
				}
				else
				{ 
					K[n] = uniform.nextIntFromTo(0, K_size_of*2);
				}
			}
		}
		else if(K_identical_or_random == ParameterOptions.GAUSSIAN)
		{//if K is meant to be rnaodm in a gaussian pattern
			for(int n = 0; n < K.length; n++)
			{//put a random gaussian number * K into every cell of the array
				int difference = N_size_of - K_size_of - 1;
				int diff_2 = K_size_of;
				if(difference > diff_2)
				{
					difference = diff_2;
				}
				
				double next_gaussian = 0.0;
				while(next_gaussian == 0.0)
				{
					//create new gaussian and change so it is between -0.5 and 0.5
					next_gaussian = (normal.nextDouble() / 7);
					if((next_gaussian > 0.5)||(next_gaussian < -0.5))
					{
						next_gaussian = 0.0;
					}
				}
				//multiple it by the difference*2 then it is between -diff and diff
				next_gaussian = next_gaussian * (difference * 2);
				//move this so it is in the range K-difference to K+difference
				next_gaussian = next_gaussian + K_size_of;
				//round it so that it is a whole number
				K[n] = (int)Math.round(next_gaussian);
			}
		}
		
		//create a new K array
		int[][] array_K = new int[N_size_of][];
		
		for (int n = 0; n < array_K.length; n++)
		{
			//for every element in the K array create a new array 
			//of size K[n] as defined above
			array_K[n] = new int[K[n]];
		}
		
		if(K_neighbours_or_random == ParameterOptions.NEIGHBOURS)
		{//if K are to be neighbours of N then
			for(int n = 0; n < array_K.length; n++)
			{//for every characteristic N in the orgnaiztion
				int characteristic = n;
				for(int k = 0; k < array_K[n].length; k++)
				{//for every K dependant characteristic choose a characteristic
					if(characteristic + 1 < N_size_of)
					{//if there is a neighbouring characteristic choose that one
						characteristic++;
					}
					else
					{//if there isn't a neighbouring one wrapp round to the begining
						characteristic = 0;
					}
					
					array_K[n][k] = characteristic;
				}
			}
		}
		else if(K_neighbours_or_random == ParameterOptions.RANDOM)
		{//if K are to be chosen randomly from among N
			for(int n = 0; n < array_K.length; n++)
			{//for every characteristic N in the orgnaiztion
				for(int k = 0; k < array_K[n].length; k++)
				{//for every dependancy this characteristic should have
					boolean done = false;
					while(!done)
					{//whilst we have not found a characteristic to be the dependancy
						//choose a new characterisitc from N at random
						array_K[n][k] = uniform.nextIntFromTo(0, N_size_of-1);
						done = true;
						if(array_K[n][k] == n)
						{//if we chose the current characteristic
							//set done to false such that we can try again
							done = false;
						}
						else
						{//if we dont choose the current characteristic
							for(int i = 0; i < k; i++)
							{//check we havne't chosen a characteristic already used
								if(array_K[n][k] == array_K[n][i])
								{
									done = false;
								}
							}
						}//end else
					}//end whils ! done
				}//end for every K
			}//end for every N
		}
		
		return array_K;
	}
	
		public String[] getAllNeighbours(String key) {
		
		//System.out.println(key);
		// convert to int array
		int[] key_ints = ArrayStringUtils.stringToArray(key);
		String[] retArray = new String[N_size_of * 2];
		
		// for each characteristic in the int array
		for (int i=0; i < key_ints.length; i++) {
			
			int plusOne = -1;
			int minusOne = -1;

                        if (key_ints[i] + 1 < array_A[i]) {
				plusOne = key_ints[i] + 1;
			}
			
			//if already at max (need to wrap around)
			if (key_ints[i] == array_A[i]-1 && key_ints[i]>0)
				plusOne = 0;
			
			// check if minus one is valid value
			if (key_ints[i] - 1 >= 0) {
				minusOne = key_ints[i] -1;
			}
			// if zero, wrap around
			if (key_ints[i] == 0) {
				minusOne = array_A[i] -1;
			}
			// check that results are different
			// e.g. can't wrap around if array_A[i] = 2
			if (minusOne == plusOne) {
				minusOne = -1;
			}
			// if we have a valid plusOne and / or minusOne
			// make the location array
			if (plusOne != -1) {
				int[] plusArr = key_ints.clone();
				plusArr[i] = plusOne;
				retArray[i * 2] = ArrayStringUtils.arrayToString(plusArr);
			}
			
			if (minusOne!= -1) {
				int[] minusArr = key_ints.clone();
				minusArr[i] = minusOne;
				retArray[(i * 2) + 1] = ArrayStringUtils.arrayToString(minusArr);
			}
		}	
			return retArray;
	}
	
	/**
	 * Creating a table of weightings for each characterisitc
	 * 
	 */
	protected double[] createWeightingsList()
	{ 
		double[] weightings = new double[N_size_of];
		double total = 1.0;
		for(int n = 0; n < weightings.length-1; n++)
		{//for each wewighting appart from the last
			//choose a ranodm number between one and the total so far
			weightings[n] = uniform.nextDoubleFromTo(0, total);
			total = total - weightings[n]; 
		}
		//set the last number as the remainder needed to add to one
		weightings[weightings.length-1] = total;
		return weightings;
	}
        
        /**
	 * Assign an initial fitness to each state of each characteristic
	 *
	 */
	protected Double characteristicFitness(int N, int[] K)
	{
		double fitness = 0.0;
		if(fitnessCache == null)
		{//if the hashtable has not yet been created create it
			fitnessCache = new TObjectFloatHashMap[N_size_of];
		}
		
		//fetch the hastable from the Nth index of the array
		TObjectFloatHashMap table = fitnessCache[N];
		if(table == null)
		{
			//if there is no hashtable there create one
			table = new TObjectFloatHashMap();
			//add it to the array
			fitnessCache[N] = table;
		}
		
		String K_string = "";
		for(int k = 0; k < K.length; k++)
		{//for every characterisitic that effects N
			//add it to a key string to identify it in the hashtable
			K_string = K_string + K[k];
		}
		
		if(table.containsKey(K_string))
		{//if the hash table already continas this string
			//find the fitness
			fitness = (double)table.get(K_string);
		}
		else
		{//if the hastable doesn'ta realy contina the string
			//create it by assigning it a random fitness
			fitness = uniform.nextDouble();
			fitness = fitness * Math.pow(10,fitness_range_dp);
			fitness = Math.round(fitness)/Math.pow(10,fitness_range_dp);
			//add it to the hashtable
			table.put(K_string, (float)fitness);
		}
		
		if(fitness_method_averaging_weightings == ParameterOptions.IDENTICAL)
		{//if we are using no weightings return the given fitness
			return fitness;
		}
		else //if(fitness_method_averaging_weightings == ParameterOptions.RANDOM)
		{//if we are using weightings weight the fitness first
			return fitness*weightings[N];
		}
		
	}
	
	/**
	 * Return a distance that is a long jump from this location
	 * 
	 * @param jump_search_time_limit
	 * @return
	 */
	public String getLongJump(int jump_search_time_limit)
	{       
                //return a random location to consider jumping to
                int[] start_loc_int = new int[N_size_of];
                for (int n = 0; n < N_size_of; n++)
                {
                        start_loc_int[n] = uniform.nextIntFromTo(0, array_A[n]-1);
                }

                return ArrayStringUtils.arrayToString(start_loc_int);
	
	}
}