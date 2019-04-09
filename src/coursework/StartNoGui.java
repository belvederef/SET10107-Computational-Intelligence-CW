package coursework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Fitness;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Example of how to to run the {@link ExampleEvolutionaryAlgorithm} without the need for the GUI
 * This allows you to conduct multiple runs programmatically 
 * The code runs faster when not required to update a user interface
 *
 */
public class StartNoGui {

	public static void main(String[] args) {
		/*** Train the Neural Network using our Evolutionary Algorithm **/
	
		//Set the data set for training 
//		Parameters.setDataSet(DataSet.Training);
//		
//		//Create a new Neural Network Trainer Using the above parameters 
//		NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
//		nn.run();
//		
//		/*test the trained network on the unseen test set */
//		Parameters.setDataSet(DataSet.Test);
//		double fitness = Fitness.evaluate(nn);
//
//		System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness);
	
		
		/**
		 * Or We can reload the NN from the file generated during training and test it on a data set 
		 * We can supply a filename or null to open a file dialog 
		 * Note that files must be in the project root and must be named *-n.txt
		 * where "n" is the number of hidden nodes
		 * ie  1518461386696-5.txt was saved at timestamp 1518461386696 and has 5 hidden nodes
		 * Files are saved automatically at the end of training
		 *  
		 *  Uncomment the following code and replace the name of the saved file to test a previously trained network 
		 */
		
//		NeuralNetwork nn2 = NeuralNetwork.loadNeuralNetwork("1234567890123-5.txt");
//		Parameters.setDataSet(DataSet.Random);
//		double fitness2 = Fitness.evaluate(nn2);
//		System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness2);
		
		
		
		try {
//			findBestPopSize();
//			findBestMutRate();
			findBestHiddenNodes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private static String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .collect(Collectors.joining(","));
	}
	private static void findBestPopSize() throws IOException {		
		for (int i=60; i <= 60; i = i + 10) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				Parameters.setPopSize(i);
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				
				//Create a new Neural Network Trainer Using the above parameters 
				NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
				nn.run();
				
				// train
				double trainFitness = Fitness.evaluate(nn);
				// test
				Parameters.setDataSet(DataSet.Test);
				double testFitness = Fitness.evaluate(nn);
				
				avgTrain += trainFitness;
				avgTest += testFitness;
			}
			
			// once r runs have completed
			String[] dataLines = new String[] { 
					  ""+Parameters.popSize, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();
		
		}
	}
	
	private static void findBestMutRate() throws IOException {		
		for (double i=0.45; i < 1.05; i = i + 0.10) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				Parameters.setMutateRate(i);
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				
				//Create a new Neural Network Trainer Using the above parameters 
				NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
				nn.run();
				
				// train
				double trainFitness = Fitness.evaluate(nn);
				// test
				Parameters.setDataSet(DataSet.Test);
				double testFitness = Fitness.evaluate(nn);
				
				avgTrain += trainFitness;
				avgTest += testFitness;
			}
			
			// once r runs have completed
			String[] dataLines = new String[] { 
					  ""+Parameters.mutateRate, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();
		
		}
	}
	
	private static void findBestHiddenNodes() throws IOException {		
		for(int i=10; i<15; i++) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.setHidden(i);
				
				//Create a new Neural Network Trainer Using the above parameters 
				NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
				nn.run();
				
				// train
				double trainFitness = Fitness.evaluate(nn);
				// test
				Parameters.setDataSet(DataSet.Test);
				double testFitness = Fitness.evaluate(nn);
				
				avgTrain += trainFitness;
				avgTest += testFitness;
			}
			// once r runs have completed
			String[] dataLines = new String[] { 
					  ""+Parameters.getNumHidden(), 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
}
