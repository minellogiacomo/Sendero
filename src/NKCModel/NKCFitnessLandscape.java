package NKCModel;

import java.util.Date;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import CrossModelClasses.AbstractFitnessLandscape;
import CrossModelClasses.ParameterOptions;
import CrossModelClasses.ArrayStringUtils;

/**
 * This is the NKCFitnessLandscape and it stores the relative fitnesses 
 * of locations in the landscape such that orgnaizations on this landscape
 * can calculate their fitness.
 * 
 * This uses the NKFitnesslandscape and delegates the functionaility of 
 * some methods to it.
 * 
 * @author Amy Marshall
 *
 */
public class NKCFitnessLandscape extends AbstractFitnessLandscape
{
	//inputted parameters
	private int C_size_of;
	private int S_size_of;
	private int X_size_of;
	private int X_neighbours_or_random;
	private int X_identical_or_random;
	private int C_neighbours_or_random;	
	private int C_identical_or_random;
	private int[] species_array_N;
	private int your_place;
	
	//arrays to be created
	private int X_used;
	private int[] array_X;
	private int[][][] array_C;

	/**
	 * Constructor creates the fitness landscape assigning user parameters to 
	 * variables of the ladnscape
	 * 
	 * @param fitness_range_dp the decimal places the fitness range goes to
	 * @param N_size_of the number of characteristics in an orgnaiztaion
	 * @param K_size_of the number of characteristics each N depends on
	 * @param A_size_of the number of states of each characteristics
	 * @param C_size_of the number of characterisitcs of other species each depends on
	 * @param S_size_of the number of species in the system
	 * @param X_size_of the number of species each other species is linked to
	 * @param K_identical_or_random if every N has the same K or K is assigned randomly to N
	 * @param K_neighbours_or_random wheteher K are neighbours to N or assigned randomly
	 * @param X_neighbours_or_random wheteher X are neighbours to S or assigned randomly
	 * @param X_identical_or_random if every S has the same X or X is assigned randomly to N
	 * @param C_identical_or_random if every N has the same C or C is assigned randomly to N
	 * @param species_array_N an array containing reference to the size of the other species N
	 * @param your_place this species place in the species array
	 * @param array_A the number of states each N hass to choose form
	 * @param fitness_method_averaging_weightings whether averaging should be IDENTICAL or WEIGHTED 
	 * @param fitness_method whether fitness should be calculated using the AVERAGE or WEAKEST
	 */
	public NKCFitnessLandscape(int fitness_range_dp, int N_size_of, 
			int K_size_of, int A_size_of, int C_size_of, int S_size_of, 
			int X_size_of, int K_identical_or_random, int K_neighbours_or_random, 
			int X_identical_or_random, int X_neighbours_or_random, int C_identical_or_random, 
			int C_neighbours_or_random, int[] species_array_N, int your_place, int[] array_A,
			int fitness_method_averaging_weightings, int fitness_method)
	{
		//asigning user parameters
		this.array_A = array_A;
		this.N_size_of = N_size_of;
		this.K_size_of = K_size_of;
		this.C_size_of = C_size_of;
		this.S_size_of = S_size_of;
		this.X_size_of = X_size_of;
		this.X_identical_or_random = X_identical_or_random;
		this.X_neighbours_or_random = X_neighbours_or_random;
		this.C_identical_or_random = C_identical_or_random;
		this.C_neighbours_or_random = C_neighbours_or_random;
		this.fitness_method_averaging_weightings = fitness_method_averaging_weightings;
		this.fitness_method = fitness_method;
		this.species_array_N = species_array_N;
		this.your_place = your_place;
		this.fitness_range_dp = fitness_range_dp;
		
		//initializing the random generators
		MersenneTwister generator1;
		MersenneTwister generator2;
		if(ParameterOptions.SEED == ParameterOptions.DATE)
		{
			Date date1 = new Date();
			generator1 = new MersenneTwister(date1);
			Date date2 = new Date();
		    generator2 = new MersenneTwister(date2);
		}
		else
		{
			generator1 = new MersenneTwister(123);
		    generator2 = new MersenneTwister(123);
		}

	    normal = new Normal(1.0, 1.0, generator1);
	    uniform = new Uniform(generator2);
		
	    //creating the arrays for distribution of K, X and C
	    array_K = createKList();
	    weightings = createWeightingsList();
	    
	    createXList();
	    createCList();
	}
	
	/**
	 * Creating an array of how many X species each characteristic depends upon
	 * 
	 */
	private void createXList()
	{	
		if(X_identical_or_random == ParameterOptions.IDENTICAL)
		{//if all X should be identical
			X_used = X_size_of;
		}
		else if(X_identical_or_random == ParameterOptions.RANDOM)
		{//if X is meant to be random
			//X_used = uniform.nextIntFromTo(0, X_size_of);
			if(X_size_of*2 >= S_size_of)
			{
				int difference = S_size_of - X_size_of - 1; 
				X_used = uniform.nextIntFromTo(X_size_of-difference, X_size_of+difference);
			}
			else
			{ 
				X_used = uniform.nextIntFromTo(0, X_size_of*2);
			}
		}
		else if(X_identical_or_random == ParameterOptions.GAUSSIAN)
		{//if K is meant to be random in a gaussian pattern
			int difference = S_size_of - X_size_of - 1;
			int diff_2 = X_size_of;
			
			//check that the differewnce isn't bigger than the origional number
			if(difference > diff_2)
			{//if it is then use the original number instead
				difference = diff_2;
			}
			
			double next_gaussian = 0.0;
			while(next_gaussian == 0.0)
			{
				//create new gaussian and change so it is between -0.5 and 0.5
				next_gaussian = (normal.nextDouble() / 7);
				if((next_gaussian > 0.5)||(next_gaussian < -0.5))
				{
					//if the new gaussian isn't in range set to 0 and try again
					next_gaussian = 0.0;
				}
			}
			//multiple it by the difference*2 then it is between 0 and difference*2
			next_gaussian = next_gaussian * (difference * 2);
			//move this so it is in the range K-difference to K+difference
			next_gaussian = next_gaussian + X_size_of;
			//round it so that it is a whole number
			X_used = (int)Math.round(next_gaussian);
		}
		
		//initializing the X_array
		array_X = new int[X_used];
		
		if(X_neighbours_or_random == ParameterOptions.NEIGHBOURS)
		{//if the neighbours of S are to be used used
			//starting at the current position in the species array
			int species = your_place;
			for(int s = 0; s < array_X.length; s++)
			{//for every S species in the X array
				if(species + 1 < S_size_of)
				{
					//if the next species in the species arrary is smaller 
					//than the total number of species 
					species++;
				}
				else
				{
					//if is is not then go back to the begining of the list
					//of species
					species = 0;
				}
				//assign this species to the next space in the X array
				array_X[s] = species;
			}
		}
		else if(X_neighbours_or_random == ParameterOptions.RANDOM)
		{//if random S are to be used
			for(int s = 0; s < array_X.length; s++)
			{//for every species S in the X array
				boolean done = false;
				while(!done)
				{//whilst we haven't found the next X continue looking
					//randomly assign a next X and set done to be true
					array_X[s] = uniform.nextIntFromTo(0, S_size_of-1);
					done = true;
					
					if(array_X[s] == your_place)
					{//if the species choses itself to link to
						//then set done as false in order to try again
						done = false;
					}
					else
					{//otherwise, check species is not already in the X array
						for(int i = 0; i < s; i++)
						{
							if(array_X[s] == array_X[i])
							{//is species is already in X array
								//set done as flase in order to try again
								done = false;
							}
						}//end for each member of X array
					}//end else
				}//done while !done
			}//end for each S
		}//end if X_neighbours_or_random == RANDOM
		
	}
	
	/**
	 * Creating an array of how many and which C each characteristic 
	 * depends upon from each species
	 * 
	 * @return
	 */
	private void createCList()
	{

		int[][] C = new int[N_size_of][X_used];
		if(C_identical_or_random == ParameterOptions.IDENTICAL)
		{//if all C should be identical
			for(int n = 0; n < C.length; n++)
			{//for all N
				for (int x = 0; x < C[n].length; x++)
				{//for all S
					//put C in every cell of the array
					C[n][x] = C_size_of;
				}
			}
		}
		else if(C_identical_or_random == ParameterOptions.RANDOM)
		{//if C is meant to be random
			for(int n = 0; n < C.length; n++)
			{//put a random number between one and K in each cell of the array
				for(int x = 0; x < X_used; x++)
				{//for every species this species is dependant on
					//find the dependant species
					int species = array_X[x];
					
					if(C_size_of*2 > species_array_N[species])
					{//if 2*C is greater than the N of that species
						//find the differnect
						int difference = species_array_N[species] - C_size_of;
						//find a random number for C plus or minus that difference 
			
						C[n][x] = uniform.nextIntFromTo(C_size_of-difference-1, C_size_of+difference-1);
					}
					else
					{//otherwise if 2*C is not greater than the N of that species
						//find a random number between 0 and 2*C
						C[n][x] = uniform.nextIntFromTo(0, C_size_of*2);
					}
				}
			}
		}
		else if(C_identical_or_random == ParameterOptions.GAUSSIAN)
		{//if K is meant to be rnaodm in a gaussian pattern
			for(int n = 0; n < C.length; n++)
			{//put a random gaussian number * K into every cell of the array
				for(int x = 0; x < X_used; x++)
				{
					//find the dependant species
					int species = array_X[x];
					
					int difference = species_array_N[species] - C_size_of;
					int diff_2 = C_size_of;
					
					//check that the difference isn't infact bigger than the origional
					if(difference > diff_2)
					{//if it is use the origional
						difference = diff_2;
					}

					double next_gaussian = 0.0;
					while(next_gaussian == 0.0)
					{
						//create new gaussian and change so it is between -0.5 and 0.5
						next_gaussian = (normal.nextDouble() / 7);
						if((next_gaussian > 0.5)||(next_gaussian < -0.5))
						{//if the gaussian selected isn't in range set to 0 to try again
							next_gaussian = 0.0;
						}
					}
					
					//multiple it by the difference*2 then it is between 0 and difference*2
					next_gaussian = next_gaussian * (difference * 2);
					//move this so it is in the range K-difference to K+difference
					next_gaussian = next_gaussian + C_size_of;
					//round it so that it is a whole number
					C[n][x] = (int)Math.round(next_gaussian) - 1;
				}
			}
		}
		
		if(C_neighbours_or_random == ParameterOptions.NEIGHBOURS)	
		{
			//initialize the C array
			array_C = new int[N_size_of][X_used][];
			for (int n = 0; n < N_size_of; n++)
			{//for every N characteristic
				for (int x = 0; x < X_used; x++)
				{//for every X species that characteristic is linked to
					array_C[n][x] = new int[C[n][x]];
					if(array_C[n][x].length != 6){
						//System.err.println("ERROR: array_C[n][x]!=6");
					}
					for (int c = 0; c < array_C[n][x].length; c++)
					{//for every C link to that species
						array_C[n][x][c] = c;
					}//end for every C link
				}//end for every X species
			}//end for every N species
		}//end if C_neighbours_or_rnadom == NEIGHBOURS
		else //if (C_neighbours_or_random == ParameterOptions.RANDOM)
		{
			//initialize the C array
			array_C = new int[N_size_of][X_used][];
			for (int n = 0; n < N_size_of; n++)
			{//for every N characteristic
				for (int x = 0; x < X_used; x++)
				{//for every X species that characteristic is linked to
					array_C[n][x] = new int[C[n][x]];
					for (int c = 0; c < array_C[n][x].length; c++)
					{//for every C link to that species
						boolean done = false;
						while(!done)
						{//whilst we haven't found a dependant C
							//pick one at random form the C's available
							array_C[n][x][c] = uniform.nextIntFromTo(0, species_array_N[array_X[x]]-1);
							done = true;
							for(int i = 0; i < c; i++)
							{//check that this C hasn't already been used in the C array
								if(array_C[n][x][c] == array_C[n][x][i])
								{//if it have set done to false in order to try again
									done = false;
								}
							}
						}//end while !done
					}//end for every C link
				}//end for every X species
			}//end for every N species
		}//end else C_neighbours_or_random == RANDOM
	}
	
	/**
	 * Use the detail given to work out the fitness of this location
	 * 
	 * @param fitness_method			characteristic dependancies are averaged, weakest taken or weighted 
	 * @param uniform_or_random_K		number of dependancies of is K the average number is K
	 * @param neighbours_or_random_K	is K made up of the closest characteristics to N or is K random
	 */
	public double getFitness(String key, String[] locations)
	{
		int[] state_array = ArrayStringUtils.stringToArray(key);
		double average_fitness = 0.0;
		double weakest_fitness = 1.0;
		for(int n = 0; n < N_size_of; n++)
		{//for every characteristic
			//System.out.println("For characteristic "+n+": ");
			//finds the length of the C part of the array
			int C_part_of_Array = 0;
			for(int x = 0 ; x < array_C[n].length; x++)
			{
				C_part_of_Array = C_part_of_Array + array_C[n][x].length;
			}
		
			
			//create a new array to hold the states of that N, K and C
			int[] K = new int[1 + K_size_of + C_part_of_Array];
			
			//add the state of N to the array
			K[0] = state_array[n];
			//add the states of K to the array
			for(int k = 0; k < array_K[n].length; k++)
			{	
				K[k+1] = state_array[array_K[n][k]];
			}	
			
			//for every species thais species depends upon
			int nxt = array_K[n].length + 1;
			for (int x = 0 ; x < array_X.length; x++)
			{
				//put the location of the species x into a string
				int x_species = array_X[x];
				int[] loc = ArrayStringUtils.stringToArray(locations[x_species]);
				//for every C that the current species depends upon
				for(int c = 0; c < array_C[n][x].length; c++)
				{
					//assigning to that place in the sates array the correct
					//C to bu put there from that characteristic
					K[nxt] = loc[array_C[n][x][c]];
					nxt++;
				}
			}
			
			double new_fitness = characteristicFitness(n,K);
			
			//saving the average fitness
			average_fitness = average_fitness + new_fitness;
			
			//saving the weakest fitness
			if(new_fitness < weakest_fitness)
			{
				//save this as the weaker fitness
				weakest_fitness = new_fitness;
			}
		}
		if(fitness_method == ParameterOptions.AVERAGE)
		{//if we are using no weightings return the average of the fitness
			if(fitness_method_averaging_weightings == ParameterOptions.IDENTICAL)
			{
				return average_fitness / N_size_of;
			}
			else //if(fitness_method_averaging_weightings == ParameterOptions.RANDOM)
			{
				return average_fitness;
			}
		}
		else //if(fitness_method == ParameterOptions.WEAKEST)
		{
			//these selection should not be added together so leave to error
			return weakest_fitness;
		}
	}
	
}