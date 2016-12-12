package NKModel;

import java.util.Hashtable;


import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.reflector.RangePropertyDescriptor;

public class NKModelGUI extends NKModel
{
	public NKModelGUI()
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

		// if average is selected, a method of weighting the average can be
		// selected either this can be identical or random
		Hashtable<Integer, String> h6 = new Hashtable<Integer, String>();
		h6.put(0, "IDENTICAL");
		h6.put(1, "RANDOM");
		ListPropertyDescriptor pd6 = new ListPropertyDescriptor(
				"Fitness_method_averaging_weightings", h6);
		descriptors.put("Fitness_method_averaging_weightings", pd6);

		// the type of communication nextwork that is used, only
		// avialble when communication is sleetced as an option
		Hashtable<Integer, String> h8 = new Hashtable<Integer, String>();
		h8.put(0, "LINEAR NETWORK");
		h8.put(1, "FULLY CONNECTED NETWORK");
		h8.put(2, "RANDOM NETWORK");
		h8.put(3, "SMALL WORLD NETWORK");
		ListPropertyDescriptor pd8 = new ListPropertyDescriptor(
				"Comms_network_type", h8);
		descriptors.put("Comms_network_type", pd8);

		// if life and death is selected as an option, a way for
		// organizations to be born can be sleected
		Hashtable<Integer, String> h9 = new Hashtable<Integer, String>();
		h9.put(0, "RANDOM NEW ORG");
		h9.put(1, "COPY OLD ORG");
		h9.put(2, "BOTH");
		ListPropertyDescriptor pd9 = new ListPropertyDescriptor(
				"Life_and_death_new_org_method", h9);
		descriptors.put("Life_and_death_new_org_method", pd9);

		// percentage chance of communications being established
		// over the communications network must be a number bwtween 0 and 100
		RangePropertyDescriptor pd10 = new RangePropertyDescriptor(
				"Comms_network_connection_probability_percentage", 0, 100, 10);
		descriptors.put("Comms_network_connection_probability_percentage", pd10);
		
		//how the organiztaion walks across the landscape
		Hashtable<Integer, String> h11 = new Hashtable<Integer, String>();
		h11.put(0, "ONE MUTANT NEIGHBOUR");
		h11.put(1, "FITTER DYNAMICS");
		h11.put(2, "GREEDY DYNAMICS");
		ListPropertyDescriptor pd11 = new ListPropertyDescriptor(
				"Organization_walk_type", h11);
		descriptors.put("Organization_walk_type", pd11);
		
		//how the orgasnisation chooses the next neighbour to look at
		Hashtable<Integer, String> h12 = new Hashtable<Integer, String>();
		h12.put(0, "SYSTEMATIC");
		h12.put(1, "RANDOM WITH MEMORY");
		h12.put(2, "RANDOM NO MEMORY");
		ListPropertyDescriptor pd12 = new ListPropertyDescriptor(
				"next_neighbour_method", h12);
		descriptors.put("Next_neighbour_method", pd12);
		
		//what is the chance of the communications network changing over time
		RangePropertyDescriptor pd13 = new RangePropertyDescriptor(
				"Comms_network_change_chance", 0, 100, 10);
		descriptors.put("Comms_network_change_chance", pd13);
		
		//how should data be collected?
		Hashtable<Integer, String> h14 = new Hashtable<Integer, String>();
		h14.put(0, "ON SCREEN");
		h14.put(1, "TO FILE");
		h14.put(2, "BOTH");
                h14.put(3, "NONE");
		ListPropertyDescriptor pd14 = new ListPropertyDescriptor(
				"Collect_data", h14);
		descriptors.put("Collect_data", pd14);
		
		//percentage chance of small world communications network being 
		//rewired must be a number bwtween 0 and 100
		RangePropertyDescriptor pd15 = new RangePropertyDescriptor(
				"Comms_network_rewire_probability_percentage", 0, 100, 10);
		descriptors.put("Comms_network_rewire_probability_percentage", pd15);
		
		name = "NK Model";
	}
}
