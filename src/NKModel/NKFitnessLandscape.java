package NKModel;

import java.util.Date;

import CrossModelClasses.AbstractFitnessLandscape;
import CrossModelClasses.ParameterOptions;
import CrossModelClasses.ArrayStringUtils;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

/**
 * This is the NKFitnessLandscape and it stores the relative fitnesses 
 * of locations in the landscape such that orgnaizations on this landscape
 * can calculate their fitness.
 * 
 * @author Amy Marshall
 *
 */

public class NKFitnessLandscape extends AbstractFitnessLandscape
{	
	/**
	 * Constructor creates the fitness landscape assigning user parameters to 
	 * variables of the ladnscape
	 * 
	 * @param N_size_of the number of characteristics in an orgnaiztaion
	 * @param array_A the number of states each N hass to choose form
	 * @param K_size_of the number of characteristics each N depends on
	 * @param fitness_range_dp the decimal places the fitness range goes to
	 * @param K_identical_or_random if every N has the same K or K is assigned randomly to N
	 * @param K_neighbours_or_random wheteher K are neighbours to N or assigned randomly
	 * @param fitness_method_averaging_weightings whether averaging should be IDENTICAL or WEIGHTED 
	 * @param fitness_method whether fitness should be calculated using the AVERAGE or WEAKEST
	 */
	public NKFitnessLandscape(int N_size_of, int[] array_A, int K_size_of, int fitness_range_dp, 
			int K_identical_or_random, int K_neighbours_or_random, int fitness_method_averaging_weightings, 
			int fitness_method)
	{
		//asigning user parameters
		this.N_size_of = N_size_of;
		this.array_A = array_A;
		this.K_size_of = K_size_of;
		this.fitness_range_dp = fitness_range_dp;
		this.K_identical_or_random = K_identical_or_random;
		this.K_neighbours_or_random = K_neighbours_or_random;
		this.fitness_method_averaging_weightings = fitness_method_averaging_weightings;
		this.fitness_method = fitness_method;
		
		//setting up the random generators
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
		
	    //creating the array for distribution of K
		array_K = createKList();
		//creating the weighting calculations
		weightings = createWeightingsList();
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
		{//for every characteristic in the location
			int[] K = new int[array_K[n].length+1];
			//create a state array of the states of characteristics that effect them
			K[0] = state_array[n];
			for(int k = 0; k < array_K[n].length; k++)
			{//for each characteristic that effects N
				//add it to N's state array
				K[k+1] = state_array[array_K[n][k]];
			}
			double new_fitness = characteristicFitness(n,K);
			
			//save the average fitness
			average_fitness = average_fitness + new_fitness;
			
			//save the weakest fitness
			if(new_fitness < weakest_fitness)
			{
				//save this as the weaker fitness
				weakest_fitness = new_fitness;
			}
		}
		if(fitness_method == ParameterOptions.AVERAGE)
		{
			//divide through by N to calc average fitness
			if(fitness_method_averaging_weightings == ParameterOptions.IDENTICAL)
			{//if we are using no weightings return the average of the fitness
				return average_fitness / N_size_of;
			}
			else //if(fitness_method_averaging_weightings == ParameterOptions.RANDOM)
			{
				//if we are using weightings they have already been weighted 
				//to be a fraction of the overall fitness
				return average_fitness;
			}
		}
		else //if (fitness_method == ParameterOptions.WEAKEST)
		{
			return weakest_fitness;
		}
	}
	
}