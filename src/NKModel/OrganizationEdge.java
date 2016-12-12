package NKModel;

import uchicago.src.sim.network.Edge;
import uchicago.src.sim.network.Node;


/**
 * 
 * Oegnaiztaion in the network leave messages for each other in the 
 * OrgnaiztionEdges as a method of communications
 * 
 * Messages are left in an orgnaiztions to edges and placed in an 
 * orgnaiztions from edges
 * 
 * @author Administrator
 *
 */

public class OrganizationEdge implements Edge
{
	//setting up the variables
	Node fromNode;
	Node toNode;
	String type;
	String label;
	double strength;
	FitnessMessage msg;
	
	/**
	 * Leaving a message for another organization involves saving the
	 * message in the msg variable
	 * 
	 * @param fitness
	 * @param location
	 */
	public void leaveFitnessMessage(double fitness, String location)
	{
		msg = new FitnessMessage(fitness,location);
	}
	
	/**
	 * Another orgnaiztion collects the messages left previously
	 * 
	 * @return
	 */
	public FitnessMessage readFitnessMessage()
	{
		return msg;
	}
	
	/**
	 * If the fitness is asked for but not the whoe mesage return the
	 * fittness stored in the message
	 * 
	 * @return
	 */
	public double readFitness()
	{
		return msg.getFitness();
	}
	
	/**
	 * If the loctaion is asked for but not the whole message return
	 * the loction stored in the message
	 * 
	 * @return
	 */
	public String readLocation()
	{
		return msg.getLocation();
	}

	/**
	 * return the node this edge is from
	 */
	public Node getFrom() {
		return fromNode;
	}

	/**
	 * return the lable of this edge
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * return the strength of this edge
	 */
	public double getStrength() {
		return strength;
	}

	/**
	 * return the node this edge is to
	 */
	public Node getTo() {
		return toNode;
	}

	/**
	 * return the type of this edge
	 */
	public String getType() {
		return type;
	}

	/**
	 * set the node this edge is from
	 */
	public void setFrom(Node fromNode) {
		this.fromNode = fromNode;
	}

	/**
	 * set the label of this edge
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * set the strenght of this edge
	 */
	public void setStrength(double strength) {
		this.strength = strength;
	}

	/**
	 * set the node this edge is to
	 */
	public void setTo(Node toNode) {
		this.toNode = toNode;
	}

	/**
	 * set the type of this edge
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}