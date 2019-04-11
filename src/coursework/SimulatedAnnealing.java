package coursework;

import model.Fitness;
import model.Individual;
import model.NeuralNetwork;

public class SimulatedAnnealing extends NeuralNetwork{
	
	public static void main(String[] args) {		
		NeuralNetwork simulatedAnnealing= new SimulatedAnnealing();
		simulatedAnnealing.run();
	}
	
	@Override
	public void run() {		
        // Set initial temp and cooling rate
//        double temp = 100000;
//        double coolingRate = 0.001;
        double temp = 100000;
        double coolingRate = 0.001;
        
		// initialise a single individual
		Individual currentIndividual = new Individual();
		currentIndividual.fitness = Fitness.evaluate(currentIndividual, this);
		
		// set as best
		best = currentIndividual.copy();
		System.out.println("Initial solution fitness: " + best);
        
		for(int gen = 0; gen < Parameters.maxEvaluations; gen++) {
			Individual newIndividual = currentIndividual.copy();
			
			// Get random gene locations in the chromosome 
            int chromeGenePos1 = Parameters.random.nextInt(newIndividual.chromosome.length);
            int chromeGenePos2 = Parameters.random.nextInt(newIndividual.chromosome.length);

            // Get the values from the selected positions in the chromosome
            double geneSwap1 = newIndividual.chromosome[chromeGenePos1];
            double geneSwap2 = newIndividual.chromosome[chromeGenePos2];

            // Swap genes
            newIndividual.chromosome[chromeGenePos1] = geneSwap2;
            newIndividual.chromosome[chromeGenePos2] = geneSwap1;
            
            newIndividual.fitness = Fitness.evaluate(newIndividual, this);
            			
			// Decide if we should accept the neighbour
            double acceptanceProbability = acceptanceProbability(
            		currentIndividual.fitness, 
            		newIndividual.fitness, 
            		temp);
            if (acceptanceProbability > Parameters.random.nextDouble()) {
                currentIndividual = newIndividual.copy();
            }

            // Replace best solution found
            if (currentIndividual.fitness < best.fitness) {
                best = currentIndividual.copy();
            }
            
            // Cool system down
            temp *= 1 - coolingRate;
            System.out.println(gen + "\t" + best);
		}
		
//        System.out.println("Final solution fitness: " + best.fitness);
//        System.out.println("Final: " + best);
		
//		outputStats();
//		saveNeuralNetwork();
	}


    // Calculate the acceptance probability
    public static double acceptanceProbability(double currFitness, double newFitness, double temp) {
        // If the new solution is better, simply accept it
        if (newFitness < currFitness) {
            return 1.0;
        }
        // If the new solution is worse, return an acceptance probability
        return Math.exp((currFitness - newFitness) / temp);
    }

    
	@Override
	public double activationFunction(double x) {
		switch(Parameters.activationType) {
		case ELU:
		default:
			if (x > 0) return x;
			return 0.1 * (Math.pow(Math.E, x) - 1);
		case HARD_ELISH:
			if (x < 0) return Math.max(0, Math.min(1, (x + 1) / 2)) * (Math.pow(Math.E, x) - 1);
			return x * Math.max(0, Math.min(1, (x + 1) / 2));
		case LEAKY_R:
			if (x > 0) return x;
			return 0.01 * x;
		case RELU:
			if (x > 0) return x;
			return -1;
		case SELU:
			if (x > 0) return x * 1.0507009;
			return 1.0507009 * (1.673263 * Math.pow(Math.E, x)) - 1.673263;
		case STEP:
			if (x <= 0) return -1.00;
			return 1.0;
		case SWISH:
			return x * (1 / (1 + Math.pow(Math.E, -x)));
		case TANH:
			if (x < -20.0) {
				return -1.0;
			} else if (x > 20.0) {
				return 1.0;
			}
			return Math.tanh(x);
		}
	}
}
