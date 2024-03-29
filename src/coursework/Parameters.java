package coursework;

import java.lang.reflect.Field;
import java.util.Random;
import model.LunarParameters;
import model.NeuralNetwork;
import model.LunarParameters.DataSet;

public class Parameters {
	// Possible options
	public enum InitialisationType { RANDOM, AUGMENTED, POS_NEG } // augmented is best
	public enum SelectionType { RANDOM, TOURNAMENT, ROULETTE, RANK, BEST }
	public enum CrossoverType { UNIFORM, ONE_POINT, TWO_POINTS, ARITHM }
	public enum MutationType { STANDARD, CONSTRAINED, ANNEALING }
	public enum ReplaceType { WORST, TOURNAMENT }
	public enum ActivationType { TANH, STEP, RELU, LEAKY_R, ELU, SELU, 
		SWISH, HARD_ELISH }
	
	// The ones chosen
	public static InitialisationType initialisationType = InitialisationType.POS_NEG;  
	public static SelectionType selectionType = SelectionType.TOURNAMENT;
	public static CrossoverType crossoverType = CrossoverType.TWO_POINTS;
	public static MutationType mutationType = MutationType.STANDARD;
	public static ReplaceType replaceType = ReplaceType.TOURNAMENT;
	public static ActivationType activationType = ActivationType.SELU;
	
	private static int numHidden = 13;	// final 12
	private static int numGenes = calculateNumGenes();
	public static double minGene = -1; // specifies minimum and maximum weight values 
	public static double maxGene = +1;
		
	public static int tournamentSize = 10; // final - 10. (select and replace)
	public static int popSize = 50; // default: 40
	public static int maxEvaluations = 20000; // max 20000
	public static boolean immigration = false; // final - false
	
	// Parameters for mutation 
	// Rate = probability of changing a gene
	// Change = the maximum +/- adjustment to the gene value
	public static double mutateRate = 0.45; // final 0.45 good 0.45 - def 0.01. Mutation rate for mutation operator
	public static double mutateChange = 0.95; // final 0.95 good 1.00 - def 0.05. Delta change for mutation operator
	
	public static double SAcoolingRate = 0.0011;
	
	//Random number generator used throughout the application
	public static long seed = System.currentTimeMillis();
	public static Random random = new Random(seed);

	//set the NeuralNetwork class here to use your code from the GUI
	public static Class neuralNetworkClass = ExampleEvolutionaryAlgorithm.class;
	

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
