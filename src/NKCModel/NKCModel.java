package NKCModel;

import java.util.ArrayList;
import java.util.Date;

import CrossModelClasses.ArrayStringUtils;
import CrossModelClasses.ParameterOptions;
import DataCollection.NKCDataCollector;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModel;
import uchicago.src.sim.engine.SimModelImpl;

/**
 *
 * The model class, this is where the mdoel is set up, wheere the
 * simulation begins and from where every step of the model is called
 *
 * This class sets up the fitness landscapes of all orgnaizational
 * sepcies and creates orgnaizations on them.  It links these
 * orgnaiztaions to orgnaizxtaion of other species and sets them
 * walking over the landscape
 *
 * @author Amy Marshall
 *
 */
public abstract class NKCModel extends SimModelImpl implements SimModel
{
	//the local variable of the class
	private int[][] array_A;
	private NKCDataCollector collector;
	private int tick_count = 0;
	private Uniform uniform;
	private Normal normal;
	protected String name;
	private Schedule schedule;
	private ArrayList agentList;

        private boolean inBatchMode;

        // set in NKCModelBatch according to command line params
        // used in data collectors to force data collection
        // to file (and suppress graphs) when in batch mode
        public boolean isInBatchMode() {
            return inBatchMode;
        }

        public void setInBatchMode(boolean inBatchMode) {
            this.inBatchMode = inBatchMode;
        }



	/**************************************/
	/************THE PARAMETERS************/
	/**************************************/
 //TRY
 //the numbeer of characteristics an organization has
 public int N_size_of;
 public void setN_size_of(int N_size_of)
 {
	 this.N_size_of = N_size_of;
 }
 public int getN_size_of()
 {
	 return N_size_of;
 }

 //the number of characterisitcs each characterisit depends upon
 public int K_size_of;
 public void setK_size_of(int K_size_of)
 {
	 this.K_size_of = K_size_of;
 }
 public int getK_size_of()
 {
	 return K_size_of;
 }

 //the number of states each characteristic can have
 public int A_size_of;
 public void setA_size_of(int A_size_of)
 {
	 this.A_size_of = A_size_of;
 }
 public int getA_size_of()
 {
	 return A_size_of;
 }

 //END TRY


	//the number of coevolutionary sets, tuples of organizations, thrown
	//onto the landscape at the begining of the simulation
	public int organizations_no_of;
	public void setOrganizations_no_of(int organizations_no_of)
	{
		this.organizations_no_of = organizations_no_of;
	}
	public int getOrganizations_no_of()
	{
		return organizations_no_of;
	}

	//the number of decimal points the fitness range should go to
	public int fitness_range_dp;
	public void setFitness_range_dp(int fitness_range_dp)
	{
		this.fitness_range_dp = fitness_range_dp;
	}
	public int getFitness_range_dp()
	{
		return fitness_range_dp;
	}

	//the number of species of orgnaiztaion
	public int S_species;
	public void setS_species(int S_species)
	{
		this.S_species = S_species;
	}
	public int getS_species()
	{
		return S_species;
	}

	//the number of other species of orgnaiztaion each species
	//is linked to
	public int X_species;
	public void setX_species(int X_species)
	{
		this.X_species = X_species;
	}
	public int getX_species()
	{
		return X_species;
	}

	//the number of traits in species A2, A3,..., An each trait from
	//species A1 is linked to
	public int C_links_between_species;
	public void setC_links_between_species(int C_links_between_species)
	{
		this.C_links_between_species = C_links_between_species;
	}
	public int getC_links_between_species()
	{
		return C_links_between_species;
	}

	//the threshold under which an organization will not move a fitter neighbour
	public double fitness_threshold;
	public void setFitness_threshold(double fitness_threshold)
	{
		this.fitness_threshold = fitness_threshold;
	}
	public double getFitness_threshold()
	{
		return fitness_threshold;
	}

	//the method through with the fitness is calculated
	public int fitness_method; // average(0) or weakest(1)
	public void setFitness_method(int fitness_method)
	{
		this.fitness_method = fitness_method;
	}
	public int getFitness_method()
	{
		return fitness_method;
	}

	//the method by which an organization finds the next neighbour it will look at
	public int next_neighbour_method; // average(0) or weakest(1)
	public void setNext_neighbour_method(int next_neighbour_method)
	{
		this.next_neighbour_method = next_neighbour_method;
	}
	public int getNext_neighbour_method()
	{
		return next_neighbour_method;
	}

	//the method through with the method is calculated (if average
	//if chosen as the above fitness_method, it then must be chosen if
	//this is an identical averaging or a weighted averaging)
	public int fitness_method_averaging_weightings;//identical(0) or weighted(1)
	public void setFitness_method_averaging_weightings(int fitness_method_averaging_weightings)
	{
		this.fitness_method_averaging_weightings = fitness_method_averaging_weightings;
	}
	public int getFitness_method_averaging_weightings()
	{
		return fitness_method_averaging_weightings;
	}

	//how the organization walks over the landscape
	public int organization_walk_type; //one_mutant_neighbour(0), fitter_dynamics(1), gready_dynamics(2)
	public void setOrganization_walk_type(int organization_walk_type)
	{
		this.organization_walk_type = organization_walk_type;
	}
	public int getOrganization_walk_type()
	{
		return organization_walk_type;
	}


        // the order in which the species step at each tick
        // 0 = sequential, 1 = random
        public int organization_walk_order;
        public int getOrganization_walk_order() {
            return organization_walk_order;
        }
        public void setOrganization_walk_order(int organization_walk_order) {
            this.organization_walk_order = organization_walk_order;
        }


	//whether all A are the same or A is different dependant on N
	public int A_identical_or_random; // identical(0) or random(1)  or gaussian (2)
	public void setA_identical_or_random(int A_identical_or_random)
	{
		this.A_identical_or_random = A_identical_or_random;
	}
	public int getA_identical_or_random()
	{
		return A_identical_or_random;
	}

	//whether all K are the same or whether K are different dependant on N
	public int K_identical_or_random; // identical (0) or random (1) or gaussian (2)
	public void setK_identical_or_random(int K_identical_or_random)
	{
		this.K_identical_or_random = K_identical_or_random;
	}
	public int getK_identical_or_random()
	{
		return K_identical_or_random;
	}

	//whether K are th neighbours of N or whetheer they are chosen randomly form N
	public int K_neighbours_or_random; // neighbours(0) or random(1)
	public void setK_neighbours_or_random(int K_neighbours_or_random)
	{
		this.K_neighbours_or_random = K_neighbours_or_random;
	}
	public int getK_neighbours_or_random()
	{
		return K_neighbours_or_random;
	}

	//whether X are the neigbours of S or wheteher X are chosen radomly form S
	public int X_neighbours_or_random;
	public void setX_neighbours_or_random(int X_neighbours_or_random)
	{
		this.X_neighbours_or_random = X_neighbours_or_random;
	}
	public int getX_neighbours_or_random()
	{
		return X_neighbours_or_random;
	}

	//whether X is the same form every N or whether X is different dependant on the N
	public int X_identical_or_random;
	public void setX_identical_or_random(int X_identical_or_random)
	{
		this.X_identical_or_random = X_identical_or_random;
	}
	public int getX_identical_or_random()
	{
		return X_identical_or_random;
	}

	//whether C is the same for every N or whether C is different dependant on the N
	public int C_identical_or_random;
	public void setC_identical_or_random(int C_identical_or_random)
	{
		this.C_identical_or_random = C_identical_or_random;
	}
	public int getC_identical_or_random()
	{
		return C_identical_or_random;
	}

	//whether C is the same for every N or whether C is different dependant on the N
	public int C_neighbours_or_random;
	public void setC_neighbours_or_random(int C_neighbours_or_random)
	{
		this.C_neighbours_or_random = C_neighbours_or_random;
	}
	public int getC_neighbours_or_random()
	{
		return C_neighbours_or_random;
	}

	//whether data is collected to screen or to file
	public int collect_data;//to screen(0), to file(1) or both(2)
	public void setCollect_data(int collect_data)
	{
		this.collect_data = collect_data;
	}

	public int getCollect_data()
	{
		return collect_data;
	}

	//the location and name of the xml file
	public String xml_file;
	public void setxml_file(String xml_file)
	{
		this.xml_file = xml_file;
	}

	public String getxml_file()
	{
		return xml_file;
	}

	//the location and name of the file that data will be collected to
	public String data_collection_file_name;
	public void setdata_collection_file_name(String data_collection_file_name)
	{
		this.data_collection_file_name = data_collection_file_name;
	}

	public String getdata_collection_file_name()
	{
		return data_collection_file_name;
	}

	//at what number of ticks should the simulation halt?
	public int simulation_halt;
	public void setsimulation_halt(int simulation_halt)
	{
		this.simulation_halt = simulation_halt;
	}

	public int getsimulation_halt()
	{
		return simulation_halt;
	}

        // should simulation be stopped if all species stop walking
        public int stop_simulation_early;
        public int getStop_simulation_early() {
                return stop_simulation_early;
            }

        public void setStop_simulation_early(int stop_simulation_early) {
            this.stop_simulation_early = stop_simulation_early;
        }


	public void begin()
	{
		buildModel();
	    buildSchedule();
	}

	/**
	 * Tears down simulation in preparation for next run, sets
	 * back varaibles to reasonable default
	 *
	 */
	public void setup()
	{

		//setting everything back to initial values
                // destroy any existing graphs
                if (collector != null)
                    collector.destroyGraphs();
		array_A = null;
		collector = null;
		tick_count = 0;
		uniform = null;
		normal = null;
		name = "Sendero NKC HACK model";

		//setting up a new schedule
		schedule = new Schedule(1);
		agentList = new ArrayList();

		//creation of the random generator
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
			generator2 = new MersenneTwister(321);
		}
		normal = new Normal(1.0, 1.0, generator1);
                uniform = new Uniform(generator2);
	}


	/**
	 * Building the model befor the simulation run, read in the orgnaiztaion
	 * species from the xml file, create the landscapes and add them to the
	 * species definitions
	 *
	 * Create the agents, the coevolutionary sets of sepcies each one containing
	 * one of each species of orgnaiztaion and throw them on the landscape ready
	 * for the start of the simulation
	 *
	 * Set up the data collector ready to start collecting data in this run of the
	 * simulation
	 *
	 */
	public void buildModel()
	{
		//create a new xml reader and read in the data
		XMLReader x = new XMLReader();
		Species[] species_of_organziation = null;

		species_of_organziation = x.readFile(xml_file);

		//some of the most important checks for validity of parameters
		//complete parameter checking is not done as it is assumed the
		//use of the model will put in sensible parameters

		for(int i = 0; i < species_of_organziation.length; i++)
		{
			Species s = species_of_organziation[i];
			if(s.getK() >= s.getN())
			{//checking K is smaller than N for every species
				System.err.println("K must be between zero and N-1");
				this.fireEndSim();
			}

			if(s.getN() < C_links_between_species)
			{//checking N is smaller than C for every species
				System.err.println("C must be smaller than or equal to N");
				this.fireEndSim();
			}
		}

		array_A = new int[S_species][];

		if(species_of_organziation.length != S_species)
		{
			//checking the number of orgnaiztion species specified in the file is
			//the same as the number specified by the parameters
			System.err.println("Please input the same number of species of orgnaiztion into the xml " +
					"file as into the parameter S_species. Number in xml file = "+
					species_of_organziation.length +", number in param file = " + S_species);

			//this command doesn't currently cause the sim to end if it hasn't started, but as far as i
			//am aware a repast bug fix should sort this - therefore this will currently still cause an exception
			this.fireEndSim();
		}

		if(X_species > S_species)
		{//if X is smaller than the number of Species
			System.err.println("X must be smaller than S");

			//this command doesn't currently cause the sim to end if it hasn't started, but as far as i
			//am aware a repast bug fix should sort this - therefore this will currently still cause an exception
			this.fireEndSim();
		}

		//create a newe array to hold the size of N or each species
		int[] species_array_N = new int[S_species];

		for(int s = 0; s < S_species; s++)
		{
			//fill in their size of N in the size of N array
			species_array_N[s] = species_of_organziation[s].getN();
		}

		for(int s = 0; s < S_species; s++)
		{//for each species
			array_A[s] = defineA(species_of_organziation[s].getN(), species_of_organziation[s].getA());
			//create a fitness landscape for that species
			NKCFitnessLandscape landscape = new NKCFitnessLandscape(fitness_range_dp,
					species_of_organziation[s].getN(), species_of_organziation[s].getK(),
					species_of_organziation[s].getA(), C_links_between_species, S_species,
					X_species, K_identical_or_random, K_neighbours_or_random, X_identical_or_random,
					X_neighbours_or_random, C_identical_or_random, C_neighbours_or_random,
					species_array_N, s, array_A[s], fitness_method_averaging_weightings, fitness_method);
			//set the landscape in the species definition
			species_of_organziation[s].setLandscape(landscape);
		}

		//create all organizations
		createOrganizations(species_of_organziation);


                //set up data collection for the organziation
                // if we are running in batch mode, override the param file
                // entry to force data collection to file (and suppress graphs)
                if(inBatchMode)
                    collect_data = ParameterOptions.TO_FILE;

		collector = new NKCDataCollector(N_size_of, A_size_of,this, species_of_organziation, collect_data);
		collector.setUpGraphs();
		collector.setUpDataCollectionToFile(agentList, data_collection_file_name);
		collector.setUpDataCollection(agentList);
	}

	private void buildSchedule() {

		class NKOrgRun extends BasicAction {
			public void execute() {
				step();
			}
		}

		NKOrgRun run = new NKOrgRun();
                schedule.scheduleActionBeginning(1, run);
                schedule.scheduleActionAt(simulation_halt, this, "stop", Schedule.LAST);
	}

	/**
	 * On every step of the model, the adaptive walk method of each
	 * co-evolutionary set is called and the species of orgnaiztaions
	 * walk over their respective landscape
	 *
	 * The graphs are updated such that the progress of the species
	 * of organizations can be seen
	 */
	public void step()
	{
            int agentsStopped = 0;

	    for (int i = 0; i < agentList.size(); i++)
	    {//for every agent in the sinulation
	    	CoevolutionarySet o = (CoevolutionarySet) agentList.get(i);
	    	//take an adaptive walk step
                boolean err = o.adaptiveWalk();
	    }
	    //System.out.println("No species moved in " + noSpeciesMoved + " sets");

	    tick_count++;

	    //update the graphs
            collector.updateGraphs();

            // check how many agents have stopped
            for (int j = 0; j < agentList.size(); j++)
            {
                CoevolutionarySet agent = (CoevolutionarySet)agentList.get(j);
                if (agent.doneWalking())
                {
                    agentsStopped++;
                }
            }

            // if all have stopped, schedule an end to the simulation.
            if (agentsStopped == agentList.size())
            {
                if (stop_simulation_early == ParameterOptions.STOP_EARLY)
                    schedule.scheduleActionAt(schedule.getCurrentTime()+1, this, "stop", Schedule.LAST);
            }
        }

	/**
	 * Coevolutionary sets of orgnaiztaions are created and thrown onto
	 * their respective landscapes
	 *
	 * @param species_of_organziation
	 */
	public void createOrganizations(Species[] species_of_organziation)
	{
		for(int o = 0; o < organizations_no_of; o++)
		{//for every orgnaiztaion that is means to be thrown onto the landscape

			//create a co-evolutionary set
			// to hold S_species number of species
                        // pass parameter for species walk order
			CoevolutionarySet set = new CoevolutionarySet(S_species);
                        set.setSpeciesWalkOrder(organization_walk_order);



			for(int s = 0; s < S_species; s++)
			{//for every species of orgnaiztion in the simultaion
				//create an NKCORgnaziation for it
				NKCOrganization species = new NKCOrganization();
				//set up the orgnaiztaion, giving it all detail needed and
				//placing it on a location in its landscape
				species.setUpOrganization(species_of_organziation[s].getLandscape(),
						makeLocation(species_of_organziation[s].getN(), array_A[s]),
						fitness_threshold, organization_walk_type, next_neighbour_method);
				//add the species of orgnaiztion to the co-evolutionary set
				set.addSpecies(species,species_of_organziation[s].getLandscape());
			}
			//add the coevolutionary set to the agent list
			agentList.add(set);
		}
	}

	/**
	 * Create a random location in the landscape or the given detail
	 *
	 * @param N_size_of the size of N in the given landscape
	 * @param A_size_of the size of A in the given landscape
	 * @return
	 */
	public String makeLocation(int N_size_of, int[] array_A)
	{
		//create an initial location for the organization

                int[] characteristics = new int[N_size_of];
                for (int i=0; i < N_size_of; i++)
                {
                characteristics[i] = uniform.nextIntFromTo(0, array_A[i]-1);
                }
                return ArrayStringUtils.arrayToString(characteristics);
	}

	public int[] defineA(int N_size_of, int A_size_of)
	{
		//assign states to each characteristic
		int[] array_A = new int[N_size_of];

		if(A_identical_or_random == ParameterOptions.IDENTICAL)
		{//if the number of states of each characteristic is going to be identical
			for(int n = 0; n < N_size_of; n++)
			{//in each element in the states array put the same number of states
				array_A[n] = A_size_of;
			}
		}
		else if(A_identical_or_random == ParameterOptions.RANDOM)
		{//if the number of states is going to be random with a mean of A_size_of
			for(int n = 0; n < N_size_of; n++)
			{//for each element in the states array create a new random number in this range
				array_A[n] = uniform.nextIntFromTo(1, A_size_of*2);;
			}
		}
		else if(A_identical_or_random == ParameterOptions.GAUSSIAN)
		{//if the number of states is going to be random with a gaussian shape around A_size_of
			for(int n = 0; n < N_size_of; n++)
			{
				//for each element in the states array create a new gaussian
				//number and map it to the correct range
				double w  = normal.nextDouble();
				//gaussian is from range -3.5 to 3.5, divide by 7 to put in
				//range -0.5 to 0.5
				double x = w / 7;
				//add 0.5 to put in range 0 to 1
				double y = x + 0.5;
				//multiple by A_size_of*2 to put in the range 0 to 2A
				double z = y*A_size_of*2;
				//round it to an integer value
				array_A[n] = (int)Math.round(z);
			}
		}
		return array_A;
	}


	/**
	 * Initializes the parameters, method from implemented class
	 *
	 * (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getInitParam()
	 */
	public String[] getInitParam()
	{
		String[] params = new String[] {
			"N_size_of",
			"K_size_of",
			"A_size_of",
				"S_species",
				"X_species",
				"C_links_between_species",
				"fitness_range_dp",
				"organizations_no_of",
				"fitness_threshold",
				"next_neighbour_method",
				"fitness_method",
				"organization_walk_type",
                                "organization_walk_order",
				"A_identical_or_random",
				"K_identical_or_random",
				"K_neighbours_or_random",
				"X_neighbours_or_random",
				"X_identical_or_random",
				"C_identical_or_random",
				"C_neighbours_or_random",
				"Collect_data",
				"xml_file",
				"data_collection_file_name",
				"fitness_method_averaging_weightings",
				"simulation_halt",
                                "stop_simulation_early",
				"looped_halt"};
		return params;
	}

	/**
	 * Retruns the name of the model, method from implemented class
	 *
	 * (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the schedule, method from implemented class
	 *
	 * (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getSchedule()
	 */
	public Schedule getSchedule()
	{
		return schedule;
	}
}
