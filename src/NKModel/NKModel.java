package NKModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import CrossModelClasses.ParameterOptions;
import CrossModelClasses.ArrayStringUtils;
import DataCollection.NKDataCollector;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModel;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.network.Edge;
import uchicago.src.sim.network.NetworkFactory;
import uchicago.src.sim.network.Node;


/**
 * 
 * Model extends the SimpleModel class of repast in order to set up
 * and run the simulation.  This class continas a method to build 
 * the mdoel and three to run the model preStep(), step() and 
 * postStep() which occur of every tick of the simulation.
 * 
 * @author 	Amy Marshall
 * @version	1
 */
public abstract class NKModel extends SimModelImpl implements SimModel
{
        private boolean inBatchMode; // false if in GUI mode - set by main class
	private NKDataCollector collector;
	private NKFitnessLandscape landscape;
	private int tick_count = 0;
	private Uniform uniform;
	private Normal normal;
	private int[] array_A;
	private double global_maximum_fitness = 0;
	protected String name;
	private Schedule schedule;
	private ArrayList<NKOrganization> agentList;

        
        // set in NKModelBatch according to command line params
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
	
	//the number of orgnaiztions that re thrown onto the landscape 
	//at the start of the simulation 
	public int organizations_no_of;
	public void setOrganizations_no_of(int organizations_no_of)
	{
		this.organizations_no_of = organizations_no_of;
	}
	public int getOrganizations_no_of()
	{
		return organizations_no_of;
	}
	
	//the number of decimal places the fitness range goes to
	public int fitness_range_dp;
	public void setFitness_range_dp(int fitness_range_dp)
	{
		this.fitness_range_dp = fitness_range_dp;
	}
	public int getFitness_range_dp()
	{
		return fitness_range_dp;
	}
	
	//whether all K are the same or whether K depends on N
	public int K_identical_or_random; // identical (0) or random (1) or gaussian (2)
	public void setK_identical_or_random(int K_identical_or_random)
	{
		this.K_identical_or_random = K_identical_or_random;
	}
	public int getK_identical_or_random()
	{
		return K_identical_or_random;
	}
	
	//whether K are neighbours of N or whether K are chosen randomly from N
	public int K_neighbours_or_random; // neighbours(0) or random(1)
	public void setK_neighbours_or_random(int K_neighbours_or_random)
	{
		this.K_neighbours_or_random = K_neighbours_or_random;
	}
	public int getK_neighbours_or_random()
	{
		return K_neighbours_or_random;
	}
	
	//whether all A are the same or whether they are dependant on N 
	public int A_identical_or_random; // identical(0) or random(1)  or gaussian (2)
	public void setA_identical_or_random(int A_identical_or_random)
	{
		this.A_identical_or_random = A_identical_or_random;
	}
	public int getA_identical_or_random()
	{
		return A_identical_or_random;
	}
	
	//whether the orgnaiztion can jump over the andscape or not
	public int jump_J; // walk(0), local jump(1) or long jump(2)
	public void setJump_J(int jump_J)
	{
		this.jump_J = jump_J;
	}
	public int getJump_J()
	{
		return jump_J;
	}
	
	//the threshold underwhich the orgnaiztion will not move to a fitter neighbour
	public double fitness_threshold;
	public void setFitness_threshold(double fitness_threshold)
	{
		this.fitness_threshold = fitness_threshold;
	}
	public double getFitness_threshold()
	{
		return fitness_threshold;
	}
	
	//the method by which an organization finds the next neighbour it will look at
	public int next_neighbour_method; // ordered(0) or random with mem(1) or random no mem(2)
	public void setNext_neighbour_method(int next_neighbour_method)
	{
		this.next_neighbour_method = next_neighbour_method;
	}
	public int getNext_neighbour_method()
	{
		return next_neighbour_method;
	}
	
	//the method by which fitness is calculated
	public int fitness_method; // average(0) or weakest(1)
	public void setFitness_method(int fitness_method)
	{
		this.fitness_method = fitness_method;
	}
	public int getFitness_method()
	{
		return fitness_method;
	}
	
	//the method by which average fitness is calculated
	public int fitness_method_averaging_weightings; // identical(0) or random(1)  or gaussian (2)
	public void setFitness_method_averaging_weightings(int fitness_method_averaging_weightings)
	{
		this.fitness_method_averaging_weightings = fitness_method_averaging_weightings;
	}
	public int getFitness_method_averaging_weightings()
	{
		return fitness_method_averaging_weightings;
	}
	
	//the number of successfull jumps allowed
	public int jump_successful_limit;
	public void setJump_successful_limit(int successful_jump_limit)
	{
		this.jump_successful_limit = successful_jump_limit;
	}
	public int getJump_successful_limit()
	{
		return jump_successful_limit;
	}
	
	//the umber of unsuccessful jumps allowed
	public int jump_search_time_limit;
	public void setJump_search_time_limit(int jump_search_time_limit)
	{
		this.jump_search_time_limit = jump_search_time_limit;
	}
	public int getJump_search_time_limit()
	{
		return jump_search_time_limit;
	}
	
	//the type of walk the orgnaiztion takes over the landscape
	public int organization_walk_type; //one_mutant_neighbour(0), fitter_dynamics(1), gready_dynamics(2)
	public void setOrganization_walk_type(int organization_walk_type)
	{
		this.organization_walk_type = organization_walk_type;
	}
	public int getOrganization_walk_type()
	{
		return organization_walk_type;
	}
	
	//whether or not the orgnaiztion use a communictaions network to look
	//at each others fitnesses and loctaions
	public boolean comms_network;
	public void setComms_network(boolean comms_network)
	{
		this.comms_network = comms_network;
	}
	public boolean getComms_network()
	{
		return comms_network;
	}
	
	//whether or not the communictaions network changes over time
	public boolean comms_network_change;
	public void setComms_network_change(boolean comms_network_change)
	{
		this.comms_network_change = comms_network_change;
	}
	public boolean getComms_network_change()
	{
		return comms_network_change;
	}
	
	//the chance of the network changing over time
	public int comms_network_change_chance;
	public void setComms_network_change_chance(int comms_network_change_chance)
	{
		this.comms_network_change_chance = comms_network_change_chance;
	}
	public int getComms_network_change_chance()
	{
		return comms_network_change_chance;
	}
	
	//the frequency at which the network changes, every ? ticks
	public int comms_network_change_frequency;
	public void setComms_network_change_frequency(int comms_network_change_frequency)
	{
		this.comms_network_change_frequency = comms_network_change_frequency;
	}
	public int getComms_network_change_frequency()
	{
		return comms_network_change_frequency;
	}
	
	//the type of network used
	public int comms_network_type;
	public void setComms_network_type(int comms_network_type)
	{
		this.comms_network_type = comms_network_type;
	}
	public int getComms_network_type()
	{
		return comms_network_type;
	}
	
	//if the network used is a small world network what is the connect radius
	public int comms_network_small_world_connect_radius;
	public void setComms_network_small_world_connect_radius(int comms_network_small_world_connect_radius)
	{
		this.comms_network_small_world_connect_radius = comms_network_small_world_connect_radius;
	}
	public int getComms_network_small_world_connect_radius()
	{
		return comms_network_small_world_connect_radius;
	}
	
	//if the network used is a random network what is the connection probability
	//as a percentage
	public int comms_network_connection_probability_percentage;
	public void setComms_network_connection_probability_percentage(int comms_network_connection_probability_percentage)
	{
		this.comms_network_connection_probability_percentage = comms_network_connection_probability_percentage;
	}
	public int getComms_network_connection_probability_percentage()
	{
		return comms_network_connection_probability_percentage;
	}
	
	//if the network is a small world network what is the rewire probability
	//as a percentage
	public int comms_network_rewire_probability_percentage;
	public void setComms_network_rewire_probability_percentage(int comms_network_rewire_probability_percentage)
	{
		this.comms_network_rewire_probability_percentage = comms_network_rewire_probability_percentage;
	}
	public int getComms_network_rewire_probability_percentage()
	{
		return comms_network_rewire_probability_percentage;
	}
	
	//is life and death of orgnaiztions modelled
	public boolean life_and_death;
	public void setLife_and_death(boolean life_and_death)
	{
		this.life_and_death = life_and_death;
	}
	public boolean getLife_and_death()
	{
		return life_and_death;
	}
	
	//under what threshold (as a difference between the fittest orgniaztion 
	//and the orgnaiztion in quetsion) should that orgaiztions die
	public double life_and_death_threashold;
	public void setLife_and_death_threashold(double life_and_death_threashold)
	{
		this.life_and_death_threashold = life_and_death_threashold;
	}
	public double getLife_and_death_threashold()
	{
		return life_and_death_threashold;
	}
	
	//what method is used for creating new organizations
	public int life_and_death_new_org_method; // random_new_org (0), copy_old_org (1), both (2)
	public void setLife_and_death_new_org_method(int life_and_death_new_org_method)
	{
		this.life_and_death_new_org_method = life_and_death_new_org_method;
	}
	public int getLife_and_death_new_org_method()
	{
		return life_and_death_new_org_method;
	}
	
	//how is data collected
	public int collect_data; //to screen(0), to file(1) or both(2)
	public void setCollect_data(int collect_data)
	{
		this.collect_data = collect_data;
	}
	
	public int getCollect_data()
	{
		return collect_data;
	}
	
	//what is the name of the file data is collected to
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
	
	
	
	public void begin()
	{
            buildModel();
	    buildSchedule();
	}
	
	/**
	 * Tears down simulation in preparation for next run, sets 
	 * back vars to reasonable default
	 *
	 */
	public void setup() 
	{
		//setting everything back to initial values
                if (collector!=null)
                    collector.destroyGraphs();
		collector = null;
		landscape = null;
		tick_count = 0;
		uniform = null;
		normal = null;
		array_A = null;
		global_maximum_fitness = 0.0;
		name = "Sendero NK model";
		agentList = null;
		
		//setting up a new schedule
		schedule = new Schedule(1);
		agentList = new ArrayList<NKOrganization>();
		
		//creating the random generators
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
	}

	/**
	 * Creates objects that the simulation uses, agents are created here
	 * and added to master list of agents
	 *
	 */
	public void buildModel() 
	{
		//create the states array
		createA_array();
		
		//initialize tick count
		tick_count = 0;
		
		//create the landscape
                landscape = new NKFitnessLandscape(N_size_of, array_A, K_size_of, fitness_range_dp, K_identical_or_random, 
				K_neighbours_or_random, fitness_method_averaging_weightings, fitness_method);   
	    
		//create all organizations
		createOrganizations(createNetwork());
		
		//set up the data collector - if we are in batch mode, then alter
                // the collect_data value to output to file only.              
                if(inBatchMode)
                    collect_data = ParameterOptions.TO_FILE;
                
		collector = new NKDataCollector(this, N_size_of, A_size_of, collect_data);
		collector.setUpGraphs();
		collector.setUpDataCollectionToFile(agentList, landscape,data_collection_file_name);
		collector.setUpDataCollection(agentList, landscape);
	}
	
	
	/**
	 * Create the array of possible states that each characterisitc in 
	 * the orgnaiztaion can have
	 *
	 */
	public void createA_array()
	{
		//assign states to each characteristic
		array_A = new int[N_size_of];
		
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
				array_A[n] = uniform.nextIntFromTo(1, A_size_of*2);
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
	}
	
	
	private void buildSchedule() {
	    
		class NKOrgRun extends BasicAction {
			public void execute() {
				preStep();
				step();
				postStep();
			}
		}

		NKOrgRun run = new NKOrgRun();
                schedule.scheduleActionBeginning(1, run);

                schedule.scheduleActionAt(simulation_halt, this, "stop", Schedule.LAST);
		  
		//check that K is smaller than N
		if(K_size_of >= N_size_of)
		{
			schedule.scheduleActionAt(0, this, "stop", Schedule.LAST);
		}
		//check the radius of the small worl netowrk is smaller than the number of organizations
		if(comms_network_small_world_connect_radius >= organizations_no_of)
		{
			//System.err.println("Please ensure the comms_network_small_world_connect_radius " +
								//"is less than the number of organizations specified");
			schedule.scheduleActionAt(0, this, "stop", Schedule.LAST);
		}	
	}

	
	/**
	 * This method is run before every step takes place, this method will only do
	 * anyhting in the case that the user has selected to use life and death within 
	 * the simnulation
	 * 
	 */
	public void preStep()
	{	
		if(life_and_death)
		{//if the user has selected to use life and death with in the simulation
			while(agentList.size() < organizations_no_of)
			{//while there are less ExtendedNKOrganizations than there are meant to be create a new organziation
				NKOrganization org = new NKOrganization();
				createNewOrganizations(org);
				//add the org to the network if there is one
				if(comms_network)
				{
					addToNetwork(org);
				}
				//add the newly created organzition to the agent list
				agentList.add(org);
			}
		}
		
		if(comms_network)
		{//if we are communicating with other agents
			for(int i = 0; i < agentList.size(); i++)
			{//each agent must send a communictaion out telling it's fittness
				NKOrganization org = (NKOrganization) agentList.get(i);
				org.sendCommunications();
			}
		}
	}
	
	/**
	 * Iterate through all agents in the simulation and call the method that 
	 * executes there behaviour
	 * 
	 */
	public void step()
	{	
            int done_walking = 0;
            int done_jumping = 0;
            int size = agentList.size();
            for (int i = 0; i < size; i++) 
            {//for every agent in the sinulation
                NKOrganization o = (NKOrganization) agentList.get(i);
                //take an adaptive walk step
                o.adaptiveWalk(null);
                if(!o.getStillWalking())
                {
                        done_walking++;
                }
                if(o.reachedJumpLimit())
                {
                        done_jumping++;
                }
            }
           
	    if(((done_walking >= size) &&((jump_J == 0))||(done_jumping >= size)))
	    {
                schedule.scheduleActionAt(schedule.getCurrentTime()+1, this, "stop", Schedule.LAST);
	    }
	    tick_count++;
	    
	    //update the graphs
            collector.updateGraphs();
	}
	
	/**
	 * This method is run after every step has taken place, this method will only do
	 * anyhting in the case that the user has selected to use life and death within 
	 * the simnulation
	 * 
	 * At the end of each tick some agents will die, this method calculates 
	 * which agents will be effected by this
	 * 
	 */
	public void postStep() 
	{	
            if(life_and_death)
            {//if the user has selected to use life and death
                    //find the maximum fitness
                    global_maximum_fitness = 0;
                    for(int i = 0; i < agentList.size(); i++)
            {//for every organization in the agent list
                    NKOrganization org = (NKOrganization) agentList.get(i); 
                    if(landscape.getFitness(org.getLocation(), null) > global_maximum_fitness)
                    {//if it is the fittest organziation seen so far record its fitness
                            global_maximum_fitness = landscape.getFitness(org.getLocation(), null);
                    }
            }

                    for(int i = 0; i < agentList.size(); i++)
                    {//for every organziation in the agent list
                            NKOrganization org1 = (NKOrganization) agentList.get(i);
                            if (landscape.getFitness(org1.getLocation(), null) <= global_maximum_fitness - life_and_death_threashold)
                            {//see if it's fitness is within the user specified range of the maximum fitness 

                                    if(comms_network)
                                    {//if there are communictaions networks as well as life and death
                                            removeLinksInNetwork(org1);
                                    }
                                    //if its not then remove the rognaization from the agent list
                                    agentList.remove(i);
                            }
                    }
            }

            if(((comms_network)&&(comms_network_change))&&(tick_count == comms_network_change_frequency))
            {//if we are caing the network and we are at the change frequence
                    //reset the counter
                    tick_count = 0;
                    for(int o = 0; o < agentList.size(); o++)
                    { //for every orgnaiztions
                            //randomly decide if we change this organization links at al
                            int random = uniform.nextIntFromTo(0, 100);
                            if(random < comms_network_change_chance)
                            {//if changing the orngaiztions links then
                                    //run the change netowrk method
                                    changeNetwork(o);
                            }
			}
		}
	}
	
	/**
	 * Create all organizations, take in a network of organizations,
	 * and pass them all necessary parameters including an initial 
	 * location
	 * 
	 */
	public void createOrganizations(List network)
	{
		for(int o = 0; o < network.size(); o++)
		{
			//create an initial location for the organization
			int[] start_loc_int = new int[N_size_of];
			for (int n = 0; n < N_size_of; n++)
			{
				start_loc_int[n] = uniform.nextIntFromTo(0, array_A[n]-1);
			}
			
			String s = ArrayStringUtils.arrayToString(start_loc_int);
			
			//create the new organization giving it an initial location
			NKOrganization org = (NKOrganization) network.get(o);
			org.setUpOrganization(landscape, s, fitness_threshold, jump_J, 
					jump_successful_limit, jump_search_time_limit, comms_network, 
					organization_walk_type, next_neighbour_method);
			//add the organization to the location list
			agentList.add(org);
		}
	}
	
	/**
	 * Creates a network of organizations and organization edges, this can then
	 * be used for organizations to communicate
	 * 
	 * @return List all ndoe sin the network
	 */
	public List createNetwork()
	{
		List network = null;
		//EdgeFactory.createEdge(nodeA, nodeB);
		int size = organizations_no_of;
		Class nodeClass = NKOrganization.class;
		Class edgeClass = OrganizationEdge.class;
		if(comms_network)
		{//if we are using communictaions networks
			if(comms_network_type == ParameterOptions.LINEAR_NETWORK)
			{//if a linear network is to be created
				//create an unlicnked network
				network = NetworkFactory.createUnlinkedNetwork(size, nodeClass);
				for(int i = 0; i < network.size(); i++)
				{//for every node in the network list
					if(i+1 < network.size())
					{//if it is not the last node
						//connect it both ways with the next node in the list
						networkLink((NKOrganization)network.get(i), (NKOrganization)network.get(i+1));
					}
					else
					{//if it is the last ndoe in the list
						//connect it both ways with the first ndoe in the list
						networkLink((NKOrganization)network.get(i), (NKOrganization)network.get(0));
					}
				}
				
			}
			else if(comms_network_type == ParameterOptions.FULLY_CONNECTED_NETWORK)
			{//if a fully connected network is to be created
				//create an unlinked network
				network = NetworkFactory.createUnlinkedNetwork(size, nodeClass);
				for(int i = 0; i < network.size(); i++)
				{//for every node in the network list
					for(int j = i; j < network.size(); j++)
					{//connect it both ways with every other node in the list
						networkLink((NKOrganization)network.get(i), (NKOrganization)network.get(j));
					}
				}
			}
			else if(comms_network_type == ParameterOptions.RANDOM_NETWORK)
			{//if a random network is to be created
				double density = comms_network_connection_probability_percentage/100.0; 
				boolean allowLoops = false;
				boolean isSymmetric = true;
				//use the factory method to create this
				network = NetworkFactory.createRandomDensityNetwork(size,density,allowLoops,isSymmetric,nodeClass,edgeClass);
			}
			else if(comms_network_type == ParameterOptions.SMALL_WORLD_NETWORK)
			{//if a small world networkl is to be created
				int connectRadius = comms_network_small_world_connect_radius;
				double rewireProb = comms_network_rewire_probability_percentage/100;
				//use the factory method to create this
				network = NetworkFactory.createWattsStrogatzNetwork(size, connectRadius, rewireProb, nodeClass, edgeClass);
			}
		}
		else //if not using communications
		{
			network = NetworkFactory.createUnlinkedNetwork(size, nodeClass);
		}
		return network;
	}
	
	/**
	 * When life and Death is effective this method creates new organizations
	 * whilst the model is running dependant on the life_and_death options
	 * selected during start up
	 * 
	 * @param organisation that has just been created
	 */
	public void createNewOrganizations(NKOrganization org)
	{
		if(life_and_death_new_org_method == ParameterOptions.RANDOM_NEW_ORG)
		{//if the new organziations are set to be created randomly
                        
                        //create an initial location for the organization
			int[] start_loc_int = new int[N_size_of];
			for (int n = 0; n < N_size_of; n++)
			{
				start_loc_int[n] = uniform.nextIntFromTo(0, array_A[n]-1);
			}
			
			String s = ArrayStringUtils.arrayToString(start_loc_int);
                        
			org.setUpOrganization(landscape, s,fitness_threshold, jump_J, 
					jump_successful_limit, jump_search_time_limit, comms_network, 
					organization_walk_type, next_neighbour_method);
		}
		else if (life_and_death_new_org_method == ParameterOptions.COPY_OLD_ORG)
		{//if the new organziations are set to be created by copying the location of an old organziation
			int new_org = uniform.nextIntFromTo(0, agentList.size()-1);
			NKOrganization old_org = (NKOrganization) agentList.get(new_org);
			org.setUpOrganization(landscape, old_org.getLocation(), fitness_threshold, 
					jump_J, jump_successful_limit, jump_search_time_limit, comms_network, 
					organization_walk_type, next_neighbour_method);
		}
		else if (life_and_death_new_org_method == ParameterOptions.BOTH)
		{//if both (Levinthal)
			double average_fitness = 0;
    		for(int i = 0; i < agentList.size(); i++)
    		{//for every ExtendedNKOrganization in the agent list
    			NKOrganization o = (NKOrganization) agentList.get(i);  
				average_fitness = average_fitness + landscape.getFitness(o.getLocation(), null);
    		}
    		average_fitness = average_fitness / agentList.size();
			double genetic_load = 1 - average_fitness/global_maximum_fitness;
			double new_org_type = uniform.nextDouble();
			if(new_org_type <= genetic_load)
			{
				int new_org = uniform.nextIntFromTo(0, agentList.size()-1);
				NKOrganization old_org = (NKOrganization) agentList.get(new_org);
				org.setUpOrganization(landscape, old_org.getLocation(),fitness_threshold, 
						jump_J, jump_successful_limit, jump_search_time_limit, comms_network, 
						organization_walk_type, next_neighbour_method);
			}
			else
			{
                                  //create an initial location for the organization
                                int[] start_loc_int = new int[N_size_of];
                                for (int n = 0; n < N_size_of; n++)
                                {
                                        start_loc_int[n] = uniform.nextIntFromTo(0, array_A[n]-1);
                                }

                                String s = ArrayStringUtils.arrayToString(start_loc_int);
				org.setUpOrganization(landscape, s, fitness_threshold, jump_J, 
						jump_successful_limit, jump_search_time_limit, comms_network, 
						organization_walk_type, next_neighbour_method);
			}
		}
	}
	
	/**
	 * Add new links within the network for the new orgnaization
	 * 
	 * @param org
	 */
	private void addToNetwork(NKOrganization org)
	{
		if((comms_network_type == ParameterOptions.LINEAR_NETWORK)
			||(comms_network_type == ParameterOptions.SMALL_WORLD_NETWORK))
		{
			//add two random two way network links
			int random_link_one = uniform.nextIntFromTo(0, agentList.size()-1);
			int random_link_two = uniform.nextIntFromTo(0, agentList.size()-1);
			
			//adding the first line
			networkLink(org, (NKOrganization)agentList.get(random_link_one));
			networkLink(org, (NKOrganization)agentList.get(random_link_two));
		}
		else if(comms_network_type == ParameterOptions.FULLY_CONNECTED_NETWORK)
		{
			//add a link in the network between this organization and every other organization
			for(int o = 0; o < agentList.size(); o++)
			{//for every orgnaization already in the agent list
				//add a two way network link 
				networkLink(org, (NKOrganization)agentList.get(o));
			}
		}
		else if(comms_network_type == ParameterOptions.RANDOM_NETWORK)
		{
			for(int o = 0; o < agentList.size(); o++)
			{//for every orgnaization already in the agent list
				//pick a random number between 0 and 100
				int yes_or_no = uniform.nextIntFromTo(0, 100);
				if(yes_or_no > comms_network_connection_probability_percentage)
				{//if this is within random then create an edges to the next node
					networkLink(org, (NKOrganization)agentList.get(o));
				}
			}
		}
	}
	
	/**
	 * Creates a link in the communictaions network between org1 and org2
	 * 
	 * @param org1
	 * @param org2
	 */
	private void networkLink(NKOrganization org1, NKOrganization org2)
	{
		//creates the nodes and the edge between them
		NKOrganization fromNode1;
		NKOrganization toNode1;
		OrganizationEdge edge1;
		
		//sets up the edge one way
		fromNode1 = org1;
		toNode1 = org2;
		edge1 = new OrganizationEdge();
		edge1.setFrom(fromNode1);
		edge1.setTo(toNode1);
		toNode1.addInEdge(edge1);
		fromNode1.addOutEdge(edge1);
		
		//creates the nodes and the edge between them
		NKOrganization fromNode2;
		NKOrganization toNode2;
		OrganizationEdge edge2;
		
		//sets up the sege the other way
		fromNode2 = org2;
		toNode2 = org1;
		edge2 = new OrganizationEdge();
		edge2.setFrom(fromNode2);
		edge2.setTo(toNode2);
		toNode2.addInEdge(edge2);
		fromNode2.addOutEdge(edge2);
	}
	
	/**
	 * The given orgnaiztaion is dead, remove all the links the given 
	 * organization has in the network
	 * 
	 * @param org1 
	 */
	private void removeLinksInNetwork(NKOrganization org1)
	{
		for(int i = 0; i < agentList.size(); i++)
		{//for all organiztaion in the simulation
			NKOrganization org2 = (NKOrganization) agentList.get(i);
			if(org2.hasEdgeFrom(org1))
			{//if that orgnaiztaion has an edge from the one we are removing
				ArrayList inEdges = org2.getInEdges();
				for(int edge_index = 0; edge_index < inEdges.size(); edge_index++)
				{//look at all edges
					Edge e = (Edge) inEdges.get(edge_index);
					if(e.getFrom() == org1)
					{//find that edge and remove it
						org2.removeInEdge(e);
					}
				}
			}
			if(org2.hasEdgeTo(org1))
			{//if that organization has an edge to the one we are removing
				ArrayList outEdges = org2.getOutEdges();
				for(int edge_index = 0; edge_index < outEdges.size(); edge_index++)
				{//look at all edges
					Edge e = (Edge) outEdges.get(edge_index);
					if(e.getTo() == org1)
					{//find that edge and remove it
						org2.removeOutEdge(e);
					}
				}
			}
		}
	}
	
	/**
	 * Change the links the given organization has within the network
	 * 
	 * @param organization_index
	 */
	private void changeNetwork(int organization_index)
	{
		//CHANGES MADE WILL DEGRADE SMALLWORLD AND LINEAR NETWORKS SO THEY NO LONGER 
		//TAKE THERE ORIGIONAL FORM
		NKOrganization org = (NKOrganization) agentList.get(organization_index);
		ArrayList<Edge> outEdges = org.getOutEdges();
		if(((comms_network_type == ParameterOptions.RANDOM_NETWORK)
				||(comms_network_type == ParameterOptions.LINEAR_NETWORK))
				||(comms_network_type == ParameterOptions.SMALL_WORLD_NETWORK))
		{//if we are looking at a random network or a linear network
			for(int e = 0; e < outEdges.size(); e++)
			{//for every out edge of this orgnaization
				//decide randomly whether we are going to change it
				int random_edge_change = uniform.nextIntFromTo(0, 100);
				if(random_edge_change < comms_network_change_chance)
				{//if we are going to change it
					//remove that edge
					Edge old_edge = (Edge) outEdges.get(e);
					Node other_org = (Node) old_edge.getTo();
					org.removeOutEdge(old_edge);
					org.removeInEdge(old_edge);
					other_org.removeOutEdge(old_edge);
					other_org.removeInEdge(old_edge);
					
					//decide which orgnaization we are creating a new edge with
					int random_new_edge = uniform.nextIntFromTo(0, agentList.size()-1);
					//add the new edge if the organization is not trying to link to itself 
					if(random_new_edge != e)
					{
						networkLink(org, (NKOrganization)agentList.get(random_new_edge));

					}
				}
			}
		}
		else //if(comms_network_type == ParameterOptions.FULLY_CONNECTED_NETWORK)
		{
			//nothing changes becasue it will always be a fully connected network
		}
	}
	
	/**
	 * Returns the model parameters
	 * 
	 */
	public String[] getInitParam() 
	{
		String[] params = new String[] {"N_size_of", 
				"K_size_of",
				"fitness_range_dp",
				"A_identical_or_random", 
				"jump_J", 
				"A_size_of", 
				"K_identical_or_random", 
				"K_neighbours_or_random",
				"next_neighbour_method",
				"fitness_method",
				"organizations_no_of", 
				"fitness_threshold",
				"jump_successful_limit",
				"jump_search_time_limit",
				"organization_walk_type",
				"fitness_method_averaging_weightings", 
				"comms_network", 
				"comms_network_type",
				"comms_network_change",
				"comms_network_change_chance",
				"comms_network_change_frequency",
				"comms_network_small_world_connect_radius",
				"life_and_death",
				"life_and_death_threashold", 
				"life_and_death_new_org_method", 
				"comms_network_connection_probability_percentage",
				"comms_network_rewire_probability_percentage",
				"collect_data",
				"data_collection_file_name",
				"simulation_halt"};
		return params;
	}
	
	/**
	 * Returns the model name
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Returns the model schedule
	 */
	public Schedule getSchedule() 
	{
		return schedule;
	}
	
}