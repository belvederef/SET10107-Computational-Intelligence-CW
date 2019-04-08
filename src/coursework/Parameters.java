package coursework;

import java.lang.reflect.Field;
import java.util.Random;
import model.LunarParameters;
import model.NeuralNetwork;
import model.LunarParameters.DataSet;

public class Parameters {
	// Possible options
	public enum SelectionType { RANDOM, TOURNAMENT, ROULETTE, RANK, BEST }
	public enum CrossoverType { UNIFORM, ONE_POINT, TWO_POINTS, ARITHM }
	public enum ReplaceType { REP_WORST, REP_TOURNAMENT }
	
	// The ones chosen
	public static SelectionType selectionType = SelectionType.TOURNAMENT;
	public static CrossoverType crossoverType = CrossoverType.TWO_POINTS;
	public static ReplaceType replaceType = ReplaceType.REP_WORST;
	
	private static int numHidden = 10;	
	private static int numGenes = calculateNumGenes();
	public static double minGene = -1; // specifies minimum and maximum weight values 
	public static double maxGene = +1;
		
	public static int popSize = 100; // default: 40
	public static int maxEvaluations = 20000; // max 20000
	
	// Parameters for mutation 
	// Rate = probability of changing a gene
	// Change = the maximum +/- adjustment to the gene value
	public static double mutateRate = 0.05; // good 0.05 - def 0.01. Mutation rate for mutation operator
	public static double mutateChange = 0.9; // good 1.00 - def 0.05. Delta change for mutation operator
	
	//Random number generator used throughout the application
	public static long seed = System.currentTimeMillis();
	public static Random random = new Random(seed);

	//set the NeuralNetwork class here to use your code from the GUI
	public static Class neuralNetworkClass = ExampleEvolutionaryAlgorithm.class;
	
	
	public static void setMutationRate(double mutationRate) {
		mutateRate = mutationRate;
	}
	public static void setMutationChange(double mutationChange) {
		mutateChange = mutationChange;
	}
	/**
	 * Do not change any methods that appear below here.
	 * 
	 */
	
	public static int getNumGenes() {					
		return numGenes;
	}
	
	private static int calculateNumGenes() {
		int num = (NeuralNetwork.numInput * numHidden) + (numHidden * NeuralNetwork.numOutput) + numHidden + NeuralNetwork.numOutput;
		return num;
	}

	public static int getNumHidden() {
		return numHidden;
	}
	
	public static void setHidden(int nHidden) {
		numHidden = nHidden;
		numGenes = calculateNumGenes();		
	}

	public static String printParams() {
		String str = "";
		for(Field field : Parameters.class.getDeclaredFields()) {
			String name = field.getName();
			Object val = null;
			try {
				val = field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str += name + " \t" + val + "\r\n";
			
		}
		return str;
	}
	
	public static void setDataSet(DataSet dataSet) {
		LunarParameters.setDataSet(dataSet);
	}
	
	public static DataSet getDataSet() {
		return LunarParameters.getDataSet();
	}
	
	public static void main(String[] args) {
		printParams();
	}
}
