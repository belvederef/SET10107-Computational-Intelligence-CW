package coursework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jws.soap.SOAPBinding.ParameterStyle;

import coursework.Parameters.ActivationType;
import coursework.Parameters.CrossoverType;
import coursework.Parameters.InitialisationType;
import coursework.Parameters.MutationType;
import coursework.Parameters.ReplaceType;
import coursework.Parameters.SelectionType;
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
//			findBestMutChange();
//			findBestHiddenNodes();
//			findBestSelection();
//			testTournamentSize();
//			findBestCrossover();
//			findBestInitialisation();
//			findBestReplace();
//			findBestActivation();
//			findImmigrationEfficacy();
//			findBestMutation();
			
			findMinMaxGenes();
			findBestSACoolRate();
//			findBestActivationSA();
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
		for (int i=10; i <= 110; i = i + 10) {
			if (i == 60) continue;
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				Parameters.popSize = i;
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
				Parameters.mutateRate = i;
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
	
	private static void findBestMutChange() throws IOException {		
		for (double i=1.15; i < 1.75; i = i + 0.10) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				Parameters.mutateChange = i;
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
					  ""+Parameters.mutateChange, 
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
		for(int i=3; i<15; i++) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
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
	
	private static void findBestInitialisation() throws IOException {		
		for(InitialisationType initialisationType : Parameters.InitialisationType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.initialisationType = initialisationType;
				
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
					  ""+Parameters.initialisationType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findBestSelection() throws IOException {		
		for(SelectionType selectionType : Parameters.SelectionType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.selectionType = selectionType;
				
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
					  ""+Parameters.selectionType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();
		}
	}
	
	private static void findBestCrossover() throws IOException {		
		for(CrossoverType crossoverType : Parameters.CrossoverType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.crossoverType = crossoverType; 
				
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
					  ""+Parameters.crossoverType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findBestReplace() throws IOException {		
		for(ReplaceType replaceType : Parameters.ReplaceType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.replaceType = replaceType; 
				
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
					  ""+Parameters.replaceType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void testTournamentSize() throws IOException {
		int[] sizesToTest = { 5, 10, 20, 30, 50, 70, 90 };
		for(int i : sizesToTest) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 20;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.selectionType = Parameters.SelectionType.TOURNAMENT;
				Parameters.tournamentSize = i;
				
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
					  ""+Parameters.tournamentSize, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();
		}
	}
	
	private static void findMinMaxGenes() throws IOException {		
		for(double i=0.1; i<1; i = i + 0.1) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.minGene = -i;
				Parameters.maxGene = i;
				
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
					  ""+Parameters.minGene, 
					  ""+Parameters.maxGene, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findBestActivation() throws IOException {		
		for(ActivationType activationType : Parameters.ActivationType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.activationType = activationType; 
				
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
					  ""+Parameters.activationType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findBestSACoolRate() throws IOException {		
		double bestValue = 100;
		for(double i=0.0007; i<0.0013; i = i + 0.0001) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.SAcoolingRate = i;
				
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
					  ""+Parameters.SAcoolingRate, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();
			if ((avgTest/runs) > bestValue) bestValue = (avgTest/runs); 
		}
		findBestActivationSA(bestValue);
	}
	
	private static void findBestActivationSA(double bestCoolRate) throws IOException {
		Parameters.SAcoolingRate = bestCoolRate;
		for(ActivationType activationType : Parameters.ActivationType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.activationType = activationType; 
				
				//Create a new Neural Network Trainer Using the above parameters 
				NeuralNetwork nn = new SimulatedAnnealing();		
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
					  ""+Parameters.activationType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findBestMutation() throws IOException {		
		for(MutationType mutationType : Parameters.MutationType.values()) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				Parameters.mutationType = mutationType; 
				
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
					  ""+Parameters.mutationType, 
					  String.format("%.5f", avgTrain/runs), 
					  String.format("%.5f", avgTest/runs) 
				};
		
			String line = convertToCSV(dataLines);
			
			FileWriter csvOutputFile = new FileWriter("results/results.csv", true);
			csvOutputFile.write(line + "\n");//appends the string to the file
			csvOutputFile.close();

		}
	}
	
	private static void findImmigrationEfficacy() throws IOException {		
		for(int i=0; i<2; i++) {
			// For each pop size
			double avgTrain = 0;
			double avgTest = 0;
			int runs = 10;
			
			for (int r=0; r < runs; r++) {
				//Set the data set for training 
				Parameters.setDataSet(DataSet.Training);
				if (i == 0) {
					Parameters.immigration = false;
				} else {
					Parameters.immigration = true;
				}
				
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
					  ""+Parameters.immigration, 
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
