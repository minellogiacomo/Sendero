package NKModel;

/**
 * 
 * The class FitnessMessage is the message that is left by an orgnaiztion, 
 * for another organization, in the OrgnaiztionEdge
 * 
 * The fitness message merely stores the location and the fitness of one 
 * orgnaiztions position on the landscape
 * 
 * @author Administrator
 *
 */
public class FitnessMessage
{
	//variables
	private double fitness;
	private String location;
	
	/**
	 * Create the fitness message
	 * 
	 * @param fitness
	 * @param location
	 */
	public FitnessMessage(double fitness, String location)
	{
		this.fitness = fitness;
		this.location = location;
	}
	
	/**
	 * retrun the location
	 */
	public String getLocation()
	{
		return location;
	}
	
	/**
	 * return the fitness
	 */
	public double getFitness()
	{
		return fitness;
	}
}