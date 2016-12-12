package NKCModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * As the agent class of the NKCModel the CoevolutionarySet stores 
 * a set of one of every species of organization that will walk over 
 * their respective landscapes together warping the landscape of each 
 * other.
 * 
 * @author Amy Marshall
 *
 */
public class CoevolutionarySet
{
	//the set of species of organization
	private ArrayList<NKCOrganization> coevolutionary_set;
	private ArrayList<NKCFitnessLandscape> landscape_set;
	private boolean still_walking = true;
        private ArrayList<Integer> fitterNeighboursCounts;
        private ArrayList<Double> endTickFitnesses;
        private ArrayList<Integer> walkOrder;
        private int walkRandom;
	
	/**
	 * The constructor for the co-evolutionary set creates a new array list
	 * for the set of species to be stored in.
         * An arrayList walkOrder is populated in constructor with species numbers
         * in order. This is used (and shuffled if required) to determine the
         * order in which species walk during a tick
	 *
	 */
		public CoevolutionarySet(int species_count)
	{
		coevolutionary_set = new ArrayList<NKCOrganization>(species_count);
		landscape_set = new ArrayList<NKCFitnessLandscape>();
                endTickFitnesses = new ArrayList<Double>();
                fitterNeighboursCounts = new ArrayList<Integer>();               
                
                //populate walkOrder with species numbers in order
                // will be shuffled in adaptiveWalk() if required
                // according to value of walkRandom
                walkOrder = new ArrayList<Integer>(species_count);
                for(int i=0; i < species_count; i++)
                    walkOrder.add(i);
                walkRandom = 0;             
	}

	/**
	 * Sets whether the species in the set will step in sequential or
         * random order. The order is stored as integers in walkOrder.
         * This is then shuffled (or not) to give the order in which species
         * are stepped.
	 * 
	 * @param 0 for sequential - 1 for random
	 * @return the value for speciesWalkOrder
	 */
        public void setSpeciesWalkOrder(int order) {
            this.walkRandom = order;
        }
        
        public int getSpeciesWalkOrder() {
            return getSpeciesWalkOrder();
        }

	/**
	 * Returning the spieces at the given index of the ArrayList
	 * 
	 * @param species the integer reference of the species
	 * @return the NKCOrganization
	 */
	public NKCOrganization getSpecies(int species)
	{
		return coevolutionary_set.get(species);
	}
	
	/**
	 * The model can add as many different species as requried to
	 * the co-evolutionary set through this mehtod
	 * 
	 * @param org the species of orgnaiztion being added
	 */
	public void addSpecies(NKCOrganization org, NKCFitnessLandscape l)
	{
		coevolutionary_set.add(org);
		landscape_set.add(l);
	}
	
	/**
	 * Called by the step method of the model this method calls the
	 * adaptive walk method of every NKCOrganization it holds a 
	 * reference to
	 *
	 */
	public boolean adaptiveWalk()
	{
                boolean anySpeciesMoved;
                anySpeciesMoved = false;
                
                // if species are to be stepped in random order
                // shuffle the walkOrder list (contains 0 to species_count-1)
                if (walkRandom==1) {
                    Collections.shuffle(walkOrder);
                }

               if(still_walking) {
                    //get species numbers to step from walk order
                    for(int s : walkOrder)
                    {//for every species in the coevolutionary set
                     //in order according to walkOrder contents

                        //find that species
                        NKCOrganization org = coevolutionary_set.get(s);
                        //create a new string of locations indicating the location of
                        //every other orgnaiztion species on their own landscape
                        String[] C_locations = makeLocationArray();
                        //run the adaptive walkl mehtod passing in the location of 
                        //every other orgnaiztion species on their own landscape
                        anySpeciesMoved|=org.adaptiveWalk(C_locations);
                    }
                }

                // if any of the species moved, or if we have no data
                // calculate species fitnesses and fitter neighbour counts at 
                // the end of the tick
                
                // once done (or not, if unneccessary), use end tick fitter
                // neighbour counts to determine whether the set has reached
                // Nash equilibrium (i.e all species have no fitter neighbours)

                if((anySpeciesMoved) | fitterNeighboursCounts.isEmpty() 
                                      | endTickFitnesses.isEmpty())
                {
                    calculateEndTickFitnesses();
                    calculateFitterNeighboursCount();
                } 

 		if(doneWalking())
		{
                    still_walking = false;
		}
                return anySpeciesMoved;
	}

        public int getSpeciesCount()
        {
            return coevolutionary_set.size();
        }

        public double getSpeciesFitness(int species) 
        {
            return endTickFitnesses.get(species);
        }

        public int getSpeciesFitterNeighbours(int species)
        {
            return fitterNeighboursCounts.get(species);
        }

        public void calculateEndTickFitnesses()
        {
            // clear any previous values, if present
            if (endTickFitnesses!=null)
            {
            endTickFitnesses.clear();
            }
            
            // get locations of all other species in the set
            String[] C_locations = makeLocationArray();

            for (NKCOrganization org : coevolutionary_set)
            // for each species in coevolutionary set
            {                 
                // calculate fitness for the species
                endTickFitnesses.add(org.getFitness(C_locations));
            }        
        }

        public void calculateFitterNeighboursCount()
        {
            double neighbourFitness = 0;
            int fitterNeighbours = 0;
            double baseFitness = 0;
            
            // clear any previous values, if present
            if (!(fitterNeighboursCounts.isEmpty()))
            {
            fitterNeighboursCounts.clear();
            }
            // get locations of all species in the set
            String C_locations[] = makeLocationArray();

             for (int i = 0; i < coevolutionary_set.size(); i++)
            // for each species in coevolutionary set
            // retrieve it and its landscape
            {   
                fitterNeighbours = 0;
                NKCOrganization org = coevolutionary_set.get(i);
                NKCFitnessLandscape thisLandscape = landscape_set.get(i);

                // get fitness of current species for comparison
                baseFitness = org.getFitness(C_locations);
                Double fitness_threshold = org.getFitnessThreshold();
                
                // get current location for this species on its landscape
                String currentLocation = org.getLocation();
                String neighbours[] = thisLandscape.getAllNeighbours(currentLocation);
                
                // for each of its current neighbours
                for (String neighbour : neighbours) {
                    if (neighbour != null) {
                        neighbourFitness = thisLandscape.getFitness(neighbour, C_locations);
                        if ((neighbourFitness - baseFitness) > fitness_threshold)
                        {
                            fitterNeighbours++;
                        }
                    }
                }
                fitterNeighboursCounts.add(fitterNeighbours);
            }     
        }
	
	/**
	 * This method finds the location of every organization species in 
	 * their own landscape and stores this in an array.
	 * 
	 * @return a String[] array of locations
	 */
	public String[] makeLocationArray()
	{
		String[] C_locations = new String[coevolutionary_set.size()];
		for(int i = 0; i < coevolutionary_set.size(); i++)
		{//for every orgnaiztaional species
			//find the orgnaizational species
			NKCOrganization o = coevolutionary_set.get(i);
			//find the loctation of that species and place it in the
			//locations array at the index of that species
			C_locations[i] = o.getLocation();
		}
		return C_locations;
	}

	public boolean doneWalking()
        {
            for (int fitterNeighbours : fitterNeighboursCounts)
            {
            if (fitterNeighbours!=0)
                return false;
            }
            
            return true;
	}

        public int getTotalFitterNeighbours() {
            int total = 0;
            for (int fitterNeighbours : fitterNeighboursCounts)
            {
            total+=fitterNeighbours;
            }
            return total;             
        }

        public boolean getStillWalking()
        {
            return still_walking;
        }
}