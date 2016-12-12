package NKCModel;

import java.util.Hashtable;

import uchicago.src.reflector.ListPropertyDescriptor;

public class NKCModelGUI extends NKCModel
{

	public NKCModelGUI()
	{		    
		super();
		//should K be identical or should each K be different with the maximum
		// K being the K given?
		Hashtable<Integer, String> h1 = new Hashtable<Integer, String>();
		h1.put(0, "IDENTICAL");
		h1.put(1, "RANDOM");
		h1.put(2, "GAUSSIAN");
		ListPropertyDescriptor pd1 = new ListPropertyDescriptor(
				"K_identical_or_random", h1);
		descriptors.put("K_identical_or_random", pd1);

		// distribution of K, should K be the neighbours of the characteristic,
		// or randomly chosen from among the other characteristics
		Hashtable<Integer, String> h2 = new Hashtable<Integer, String>();
		h2.put(0, "NEIGHBOURS");
		h2.put(1, "RANDOM");
		ListPropertyDescriptor pd2 = new ListPropertyDescriptor(
				"K_neighbours_or_random", h2);
		descriptors.put("K_neighbours_or_random", pd2);

		// should A be identical or should each A be different with the maximum
		// A being the A given?
		Hashtable<Integer, String> h3 = new Hashtable<Integer, String>();
		h3.put(0, "IDENTICAL");
		h3.put(1, "RANDOM");
		h3.put(2, "GAUSSIAN");
		ListPropertyDescriptor pd3 = new ListPropertyDescriptor(
				"A_uniform_or_random", h3);
		descriptors.put("A_identical_or_random", pd3);

		// should the organization only be able to WALK or should it be able
		// to both walk and JUMP across the landscape
		Hashtable<Integer, String> h4 = new Hashtable<Integer, String>();
		h4.put(0, "WALK");
		h4.put(1, "LONG JUMP");
		ListPropertyDescriptor pd4 = new ListPropertyDescriptor("Jump_J", h4);
		descriptors.put("Jump_J", pd4);

		// should the fitness of a characterisitc be calculated as the weakest
		// of the K + 1 characteristics or should it be an average? (Average may 
		// be weighted)
		Hashtable<Integer, String> h5 = new Hashtable<Integer, String>();
		h5.put(0, "AVERAGE");
		h5.put(1, "WEAKEST");
		ListPropertyDescriptor pd5 = new ListPropertyDescriptor(
				"Fitness_method", h5);
		descriptors.put("Fitness_method", pd5);

		//how the orgasnisation chooses the next neighbour to look at
		Hashtable<Integer, String> h6 = new Hashtable<Integer, String>();
		h6.put(0, "SYSTEMATIC");
		h6.put(1, "RANDOM WITH MEMORY");
		h6.put(2, "RANDOM NO MEMORY");
		ListPropertyDescriptor pd6 = new ListPropertyDescriptor(
				"next_neighbour_method", h6);
		descriptors.put("Next_neighbour_method", pd6);
		
		//how the organiztaion walks across the landscape
		Hashtable<Integer, String> h7 = new Hashtable<Integer, String>();
		h7.put(0, "ONE MUTANT NEIGHBOUR");
		h7.put(1, "FITTER DYNAMICS");
		h7.put(2, "GREEDY DYNAMICS");
		ListPropertyDescriptor pd7 = new ListPropertyDescriptor(
				"Organization_walk_type", h7);
		descriptors.put("Organization_walk_type", pd7);
		
		//should every C be identical or should every C be different with the 
		//average at the inputted C?
		Hashtable<Integer, String> h8 = new Hashtable<Integer, String>();
		h8.put(0, "IDENTICAL");
		h8.put(1, "RANDOM");
		h8.put(2, "GAUSSIAN");
		ListPropertyDescriptor pd8 = new ListPropertyDescriptor(
				"C_identical_or_random", h8);
		descriptors.put("C_identical_or_random", pd8);
		
		//should X be taken from the neighbours of S or should X be chosen at 
		//random from among S?
		Hashtable<Integer, String> h9 = new Hashtable<Integer, String>();
		h9.put(0, "NEIGHBOURS");
		h9.put(1, "RANDOM");
		ListPropertyDescriptor pd9 = new ListPropertyDescriptor(
				"X_neighbours_or_random", h9);
		descriptors.put("X_neighbours_or_random", pd9);
		
		//should every X be identical or should every X be different with the 
		//average at the inputted X?
		Hashtable<Integer, String> h10 = new Hashtable<Integer, String>();
		h10.put(0, "IDENTICAL");
		h10.put(1, "RANDOM");
		ListPropertyDescriptor pd10 = new ListPropertyDescriptor(
				"X_identical_or_random", h10);
		descriptors.put("X_identical_or_random", pd10);
		
		//How should data be collected?
		Hashtable<Integer, String> h11 = new Hashtable<Integer, String>();
		h11.put(0, "ON SCREEN");
		h11.put(1, "TO FILE");
		h11.put(2, "BOTH");
		ListPropertyDescriptor pd11 = new ListPropertyDescriptor(
				"Collect_data", h11);
		descriptors.put("Collect_data", pd11);
		
		// if average is selected, a method of weighting the average can be
		// selected either this can be identical or random
		Hashtable<Integer, String> h12 = new Hashtable<Integer, String>();
		h12.put(0, "IDENTICAL");
		h12.put(1, "RANDOM");
		ListPropertyDescriptor pd12 = new ListPropertyDescriptor(
				"Fitness_method_averaging_weightings", h12);
		descriptors.put("Fitness_method_averaging_weightings", pd12);

                // should simulation be stopped if all species stop walking?
		// or should we continue to simulation halt
		Hashtable<Integer, String> h13 = new Hashtable<Integer, String>();
		h13.put(0, "RUN TO SIMULATION_HALT");
		h13.put(1, "STOP SIMULATION EARLY");
		ListPropertyDescriptor pd13 = new ListPropertyDescriptor(
				"Stop_simulation_early", h13);
		descriptors.put("Stop_simulation_early", pd13);
                
                //should C be identical or should each C be different with the maximum
		// C being the C given?
		Hashtable<Integer, String> h14 = new Hashtable<Integer, String>();
		h14.put(0, "IDENTICAL");
		h14.put(1, "RANDOM");
		ListPropertyDescriptor pd14 = new ListPropertyDescriptor(
				"C_neighbours_or_random", h14);
		descriptors.put("C_neighbours_or_random", pd14);

                
                // should simulation be stopped if all species stop walking?
		// or should we continue to simulation halt
		Hashtable<Integer, String> h15 = new Hashtable<Integer, String>();
		h15.put(0, "SEQUENTIAL");
		h15.put(1, "RANDOM");
		ListPropertyDescriptor pd15 = new ListPropertyDescriptor(
				"Organization_walk_order", h15);
		descriptors.put("Organization_walk_order", pd15);
                
                
		name = "NKC Model";
	}
}
