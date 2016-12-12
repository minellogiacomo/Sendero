package DataCollection;

import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.analysis.DataRecorder;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.SimModel;
import CrossModelClasses.AbstractFitnessLandscape;
import CrossModelClasses.ParameterOptions;

/**
 * The NKDataCollection class orcestrates thecollection of data for
 * the NK Model
 *
 *
 * @author     Amy Marshall
 * @created    October 10, 2007
 */
public class NKDataCollector {
	//variables
	private OpenSequenceGraph graph1;
	private OpenSequenceGraph graph2;
	private OpenSequenceGraph graph3;
	private OpenSequenceGraph graph4;
	private DataRecorder recorder;

	//inputed parameters
	private SimModel simModel;
	private int N_size_of;
	private int A_size_of;
	private int collect_data;


	/**
	 * collects data for the simModel inputted
	 *
	 *
	 * @param  simModel      the current model to collect data for
	 * @param  N_size_of     the number of characteristics
	 * @param  A_size_of     the number of states of each characteristic
	 * @param  collect_data  whether to collect to file, to screen or both
	 */
	public NKDataCollector(SimModel simModel, int N_size_of, int A_size_of, int collect_data) {
		this.simModel = simModel;
		this.N_size_of = N_size_of;
		this.A_size_of = A_size_of;
		this.collect_data = collect_data;
	}

    public void destroyGraphs() {
        // destroy any existing graphs. Called by nkmodel.setup()
        if (graph1 != null) {
            graph1.dispose();
	}
        if (graph2 != null) {
            graph2.dispose();
	}
        if (graph3 != null) {
            graph3.dispose();
	}
        if (graph4 != null) {
            graph4.dispose();
        }
    }


	/**
	 * If collecting data to screen set up the graphs
	 */
	public void setUpGraphs() {
		if ((collect_data == ParameterOptions.ON_SCREEN) || (collect_data == ParameterOptions.BOTH)) {
			//if collecting data to screen set up the graphs
			
			graph1 = null;
			graph1 = new OpenSequenceGraph("Fitness over time", simModel);
			graph1.setAxisTitles("time", "Fitness");

			if (graph2 != null) {
				graph2.dispose();
			}
			graph2 = null;
			graph2 = new OpenSequenceGraph("Waiting Time for Movement", simModel);
			graph2.setAxisTitles("time", "Waiting Time");

			if (graph3 != null) {
				graph3.dispose();
			}
			graph3 = null;
			graph3 = new OpenSequenceGraph("Number of Fitter Neighbours", simModel);
			graph3.setAxisTitles("time", "Fitter Neighbours");

			if (graph4 != null) {
				graph4.dispose();
			}
			graph4 = null;
			graph4 = new OpenSequenceGraph("Fraction Still Walking", simModel);
			graph4.setAxisTitles("time", "Still Walking");
		}
	}


	/**
	 * If collecting data to fie set up the data recorder
	 *
	 * @param  agentList  The new upDataCollectionToFile value
	 * @param  landscape  The new upDataCollectionToFile value
	 * @param  file_name  The new upDataCollectionToFile value
	 */
	public void setUpDataCollectionToFile(ArrayList agentList, AbstractFitnessLandscape landscape, String file_name) {
		if ((collect_data == ParameterOptions.TO_FILE) || (collect_data == ParameterOptions.BOTH)) {
			//if colletcing data to file
			recorder = new DataRecorder(file_name, simModel);
			recorder.addNumericDataSource("Average Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_AVERAGE, 0));
			recorder.addNumericDataSource("Maximum Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_MAXIMUM, 0));
			recorder.addNumericDataSource("Minimum Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_MINIMUM, 0));
			recorder.addNumericDataSource("Average Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_AVERAGE, 0));
			recorder.addNumericDataSource("Maximum Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_MAXIMUM, 0));
			recorder.addNumericDataSource("Minimum Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_MINIMUM, 0));
			recorder.addNumericDataSource("Average Number of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_AVERAGE, N_size_of, A_size_of, 0));
			recorder.addNumericDataSource("Maximum Number of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_MAXIMUM, N_size_of, A_size_of, 0));
			recorder.addNumericDataSource("Minimum Number of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_MINIMUM, N_size_of, A_size_of, 0));
			recorder.addNumericDataSource("Fraction Still Walking", new FractionStillWalking(agentList));
			recorder.writeToFile();
		}
	}


	/**
	 * if collecting data to screen set up the data collection
	 *
	 * @param  agentList  The new upDataCollection value
	 * @param  landscape  The new upDataCollection value
	 */
	public void setUpDataCollection(ArrayList agentList, AbstractFitnessLandscape landscape) {

		if ((collect_data == ParameterOptions.ON_SCREEN) || (collect_data == ParameterOptions.BOTH)) {
			//if collecting data to screen
			graph1.setXRange(0, 100);
			graph1.setYRange(0, 1);
			graph1.display();
			graph1.addSequence("Average Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_AVERAGE, 0), Color.blue);
			graph1.addSequence("Maximum Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_MAXIMUM, 0), Color.green);
			graph1.addSequence("Minimum Fitness", new Fitness(agentList, landscape, ParameterOptions.GRAPH_MINIMUM, 0), Color.red);

			graph2.setXRange(0, 100);
			graph2.setYRange(0, 1);
			graph2.display();
			graph2.addSequence("Average Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_AVERAGE, 0), Color.blue);
			graph2.addSequence("Maximum Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_MAXIMUM, 0), Color.green);
			graph2.addSequence("Minimum Wait Before Move", new WaitTime(agentList, ParameterOptions.GRAPH_MINIMUM, 0), Color.red);

			graph3.setXRange(0, 100);
			graph3.setYRange(0, 1);
			graph3.display();
			graph3.addSequence("Average Number Of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_AVERAGE, N_size_of, A_size_of, 0), Color.blue);
			graph3.addSequence("Maximum Number Of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_MAXIMUM, N_size_of, A_size_of, 0), Color.green);
			graph3.addSequence("Minimum Number Of Fitter Neighbours", new NoFitterNeighbours(agentList, landscape, ParameterOptions.GRAPH_MINIMUM, N_size_of, A_size_of, 0), Color.red);

			graph4.setXRange(0, 100);
			graph4.setYRange(0, 1);
			graph4.display();
			graph4.addSequence("Fraction Still Walking", new FractionStillWalking(agentList), Color.blue);
		}
	}


	/**
	 * At each time step of the model update the graphs and the data recorder
	 * dependant on what is set up
	 */
	public void updateGraphs() {
		if ((collect_data == ParameterOptions.ON_SCREEN) || (collect_data == ParameterOptions.BOTH)) {
			//if collecting data to grpah update the graphs
			graph1.step();
			graph2.step();
			graph3.step();
			graph4.step();
		}
		if ((collect_data == ParameterOptions.TO_FILE) || (collect_data == ParameterOptions.BOTH)) {
			//if collecting data to file update the file
			recorder.record();
			recorder.writeToFile();
		}
	}
}
