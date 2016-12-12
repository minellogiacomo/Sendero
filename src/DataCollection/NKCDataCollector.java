package DataCollection;

import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.analysis.DataRecorder;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.SimModel;
import CrossModelClasses.ParameterOptions;
import NKCModel.Species;

/**
 *
 * The NKCDataCollection class orcestrates the collection of data for
 * the NKC Model
 *
 * @author Amy Marshall
 *
 */
public class NKCDataCollector
{
	//varaibles
	private OpenSequenceGraph[] graph;
	private DataRecorder recorder;

	//inputted parameters
	private SimModel simModel;
	private Species[] organization_species;
	private int collect_data;
	private int N_size_of;
	private int K_size_of;

	/**
	 * Collects data for the simModel inputted
	 *
	 * @param simModel the current model to collect data for
	 * @param organization_species the array of orgnaiztional species to collect data for
	 * @param  N_size_of    test
	 * @param  K_size_of    test
   * @param collect_data whether to collect to file, to screen or both
	 */
	public NKCDataCollector(SimModel simModel, int N_size_of, int K_size_of, Species[] organization_species, int collect_data)
	{
		this.N_size_of = N_size_of;
		this.K_size_of = K_size_of;
		this.simModel = simModel;
		this.organization_species = organization_species;
		this.collect_data = collect_data;

		//create a new array of ten sequence graphs
		graph = new OpenSequenceGraph[10];
	}

	/**
	 * Setting up the grahs in order to collect data to screen
	 *
	 */
	public void setUpGraphs()
	{
		//set up the ten sequence graphs
		OpenSequenceGraph graph0 = graph[0];
		OpenSequenceGraph graph1 = graph[1];
		OpenSequenceGraph graph2 = graph[2];
		OpenSequenceGraph graph3 = graph[3];
		OpenSequenceGraph graph4 = graph[4];
		OpenSequenceGraph graph5 = graph[5];
		OpenSequenceGraph graph6 = graph[6];
		OpenSequenceGraph graph7 = graph[7];
		OpenSequenceGraph graph8 = graph[8];
		OpenSequenceGraph graph9 = graph[9];

		if((collect_data == ParameterOptions.ON_SCREEN)||(collect_data == ParameterOptions.BOTH))
		{//if collecting data to screen then initilize the grpahs
			if (graph0 != null){
				graph0.dispose();
			}
			graph0 = null;
			graph0 = new OpenSequenceGraph("Maximum fitness over time", simModel);
			graph0.setAxisTitles("time", "Fitness");
			graph[0] = graph0;

			if (graph1 != null){
				graph1.dispose();
			}
			graph1 = null;
			graph1 = new OpenSequenceGraph("Average fitness over time", simModel);
			graph1.setAxisTitles("time", "Fitness");
			graph[1] = graph1;

			if (graph2 != null){
				graph2.dispose();
			}
			graph2 = null;
			graph2 = new OpenSequenceGraph("Minimum fitness over time", simModel);
			graph2.setAxisTitles("time", "Fitness");
			graph[2] = graph2;

			if (graph3 != null){
				graph3.dispose();
			}
			graph3 = null;
			graph3 = new OpenSequenceGraph("Maximum waiting Time for Movement", simModel);
			graph3.setAxisTitles("time", "Waiting time");
			graph[3] = graph3;

			if (graph4 != null){
				graph4.dispose();
			}
			graph4 = null;
			graph4 = new OpenSequenceGraph("Average waiting Time for Movement", simModel);
			graph4.setAxisTitles("time", "Waiting time");
			graph[4] = graph4;

			if (graph5 != null){
				graph5.dispose();
			}
			graph5 = null;
			graph5 = new OpenSequenceGraph("Minimum waiting Time for Movement", simModel);
			graph5.setAxisTitles("time", "Waiting time");
			graph[5] = graph5;

			if (graph6 != null){
				graph6.dispose();
			}
			graph6 = null;
			graph6 = new OpenSequenceGraph("Maximum Number of Fitter Neighbours", simModel);
			graph6.setAxisTitles("time", "Fitter neighbours");
			graph[6] = graph6;

			if (graph7 != null){
				graph7.dispose();
			}
			graph7 = null;
			graph7 = new OpenSequenceGraph("Average Number of Fitter Neighbours", simModel);
			graph7.setAxisTitles("time", "Fitter neighbours");
			graph[7] = graph7;

			if (graph8 != null){
				graph8.dispose();
			}
			graph8 = null;
			graph8 = new OpenSequenceGraph("Minimum Number of Fitter Neighbours", simModel);
			graph8.setAxisTitles("time", "Fitter neighbours");
			graph[8] = graph8;

			if (graph9 != null){
				graph9.dispose();
			}
			graph9 = null;
			graph9 = new OpenSequenceGraph("Fraction Still Walking", simModel);
			graph9.setAxisTitles("time", "Still Walking");
			graph[9] = graph9;
		}
	}


	/**
	 * Setting up the data recorder in order to collect data to file
	 *
	 */
	public void setUpDataCollectionToFile(ArrayList agentList, String file_name)
	{
		if((collect_data == ParameterOptions.TO_FILE)||(collect_data == ParameterOptions.BOTH))
		{//if collecting to file
			recorder = new DataRecorder(file_name, simModel);
			for(int s = 0; s < organization_species.length; s++)
			{//for every species of orgnaiztions collect the following
				recorder.addNumericDataSource("Average Fitness "+s,new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_AVERAGE,s));
				recorder.addNumericDataSource("Maximum Fitness "+s,new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MAXIMUM,s));
				recorder.addNumericDataSource("Minimum Fitness "+s,new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MINIMUM,s));
				recorder.addNumericDataSource("Average Wait Before Move "+s,new WaitTime(agentList, ParameterOptions.GRAPH_AVERAGE,s));
				recorder.addNumericDataSource("Maximum Wait Before Move "+s,new WaitTime(agentList, ParameterOptions.GRAPH_MAXIMUM,s));
				recorder.addNumericDataSource("Minimum Wait Before Move "+s,new WaitTime(agentList, ParameterOptions.GRAPH_MINIMUM,s));
				recorder.addNumericDataSource("Average Number of Fitter Neighbours "+s,new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_AVERAGE, organization_species[s].getN(), organization_species[s].getA(),s));
				recorder.addNumericDataSource("Maximum Number of Fitter Neighbours "+s,new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MAXIMUM, organization_species[s].getN(), organization_species[s].getA(),s));
				recorder.addNumericDataSource("Minimum Number of Fitter Neighbours "+s,new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MINIMUM, organization_species[s].getN(), organization_species[s].getA(),s));
			}
			recorder.addNumericDataSource("Fraction Still Walking",new FractionStillWalking(agentList));
			recorder.writeToFile();
		}
	}

	/**
	 * Set up the data collection to screen
	 *
	 */
	public void setUpDataCollection(ArrayList agentList)
	{
		//setting up an array of colours for each species of orgnaiztion
		Color[] c = new Color[20];
		c[0] = Color.black;
		c[1] = Color.blue;
		c[2] = Color.cyan;
		c[3] = Color.green;
		c[4] = Color.magenta;
		c[5] = Color.orange;
		c[6] = Color.pink;
		c[7] = Color.red;
		c[8] = Color.yellow;
		c[9] = Color.gray;

		if((collect_data == ParameterOptions.ON_SCREEN)||(collect_data == ParameterOptions.BOTH))
		{//set up only if showing on screen
			int colour = 9;
			for(int s = 0; s < organization_species.length; s++)
			{//for every species

				//choose a colour
				if(colour != 9)
				{
					colour++;
				}
				else
				{
					colour = 0;
				}

				//initilize the graph
				for(int i = 0; i < graph.length; i++)
				{
					OpenSequenceGraph g = graph[i];
					g.setXRange(0, 100);
					g.setYRange(0, 1);
					g.display();
				}

				OpenSequenceGraph graph0 = graph[0];
				OpenSequenceGraph graph1 = graph[1];
				OpenSequenceGraph graph2 = graph[2];
				OpenSequenceGraph graph3 = graph[3];
				OpenSequenceGraph graph4 = graph[4];
				OpenSequenceGraph graph5 = graph[5];
				OpenSequenceGraph graph6 = graph[6];
				OpenSequenceGraph graph7 = graph[7];
				OpenSequenceGraph graph8 = graph[8];

				graph1.addSequence("species "+s, new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_AVERAGE, s), c[colour]);
				graph0.addSequence("species "+s, new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MAXIMUM, s), c[colour]);
				graph2.addSequence("species "+s, new Fitness(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MINIMUM, s), c[colour]);

				graph4.addSequence("species "+s, new WaitTime(agentList, ParameterOptions.GRAPH_AVERAGE, s), c[colour]);
				graph3.addSequence("species "+s, new WaitTime(agentList, ParameterOptions.GRAPH_MAXIMUM, s), c[colour]);
				graph5.addSequence("species "+s, new WaitTime(agentList, ParameterOptions.GRAPH_MINIMUM, s), c[colour]);

				graph7.addSequence("species "+s, new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_AVERAGE, organization_species[s].getN(), organization_species[s].getA(), s), c[colour]);
				graph6.addSequence("species "+s, new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MAXIMUM, organization_species[s].getN(), organization_species[s].getA(), s), c[colour]);
				graph8.addSequence("species "+s, new NoFitterNeighbours(agentList, organization_species[s].getLandscape(), ParameterOptions.GRAPH_MINIMUM, organization_species[s].getN(), organization_species[s].getA(), s), c[colour]);


			}
			OpenSequenceGraph graph9 = graph[9];
			graph9.addSequence("faction still walking", new FractionStillWalking(agentList), c[colour]);
		}
	}

	/**
	 * This method is run as every step of the model to update the graphs
	 * and push collected data to the file
	 *
	 */
	public void updateGraphs()
	{
		if((collect_data == ParameterOptions.ON_SCREEN)||(collect_data == ParameterOptions.BOTH))
		{//if showing on screen
			//take a step in every graph
			OpenSequenceGraph graph0 = graph[0];
			OpenSequenceGraph graph1 = graph[1];
			OpenSequenceGraph graph2 = graph[2];
			OpenSequenceGraph graph3 = graph[3];
			OpenSequenceGraph graph4 = graph[4];
			OpenSequenceGraph graph5 = graph[5];
			OpenSequenceGraph graph6 = graph[6];
			OpenSequenceGraph graph7 = graph[7];
			OpenSequenceGraph graph8 = graph[8];
			OpenSequenceGraph graph9 = graph[9];
			graph0.step();
			graph1.step();
			graph2.step();
			graph3.step();
			graph4.step();
			graph5.step();
			graph6.step();
			graph7.step();
			graph8.step();
			graph9.step();
		}
		if((collect_data == ParameterOptions.TO_FILE)||(collect_data == ParameterOptions.BOTH))
		{//if collecting to file
			recorder.record();
			recorder.writeToFile();
		}
	}

        public void destroyGraphs() {
        // destroy any existing graphs. Called by nkmodel.setup()
            for (OpenSequenceGraph thisGraph : graph) {
                if(thisGraph!=null)
                    thisGraph.dispose();
            }
        }

}
