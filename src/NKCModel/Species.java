package NKCModel;

/**
 * 
 * The species class stores the details of each species of 
 * orgnaiztion and stores the fitness landscape of the orgnaiztion
 * 
 * @author Amy Marshall
 *
 */
public class Species
{
	private int N;
	private int A;
	private int K;
	private NKCFitnessLandscape landscape;
	
	/**
	 * Each species is set up be entering the following detail
	 * 
	 * @param N the number characteristics
	 * @param K the number of dependancies between charactersitics
	 * @param A the nube rof states of each characteristic
	 */
	public Species(int N, int K, int A)
	{
		this.N = N;
		this.K = K;
		this.A = A;
	}
	
	/**
	 * getter and setting methods for N
	 */
	public int getN(){
		return N;
	}
	public void setN(int N){
		this.N = N;
	}
	
	/**
	 * getter and setting methods for K
	 */
	public int getK(){
		return K;
	}
	public void setK(int K){
		this.K = K;
	}
	
	/**
	 * getter and setting methods for A
	 */
	public int getA(){
		return A;
	}
	public void setA(int A){
		this.A = A;
	}
	
	/**
	 * getter and setting methods for the fitness landscape
	 */
	public NKCFitnessLandscape getLandscape(){
		return landscape;
	}
	public void setLandscape(NKCFitnessLandscape landscape){
		this.landscape = landscape;
	}
}