package coursework;

import java.util.ArrayList;

import model.Fitness;
import model.Individual;
import model.NeuralNetwork;

public class SimulatedAnnealing extends NeuralNetwork{

	/**
	 * Sets the fitness of the individuals passed as parameters
	 */
	private void evaluateIndividuals(Individual[] individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}
	
    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }
	
	public static void main(String[] args) {		
		NeuralNetwork simulatedAnnealing= new SimulatedAnnealing();
		simulatedAnnealing.run();
	}
	
	@Override
	public void run() {		
        // Set initial temp
        double temp = 10000;
		
        // Cooling rate
        double coolingRate = 0.003;
        
		// initialise a single individual
		Individual currentIndividual = new Individual();
		currentIndividual.fitness = Fitness.evaluate(currentIndividual, this);
		
		// set as best
		best = currentIndividual.copy();
		
		System.out.println("Initial solution fitness: " + best);
        
//		while (temp > 1) {
		for(int gen = 0; gen < Parameters.maxEvaluations; gen++) {
			Individual newIndividual = currentIndividual.copy();
			
			// Get a random genes in the chromosome (change with next int)
            int chromeGenePos1 = (int) (newIndividual.chromosome.length * Parameters.random.nextDouble());
            int chromeGenePos2 = (int) (newIndividual.chromosome.length * Parameters.random.nextDouble());

            // Get the values at selected positions in the chromosome
            double geneSwap1 = newIndividual.chromosome[chromeGenePos1];
            double geneSwap2 = newIndividual.chromosome[chromeGenePos2];

            // Swap them
            newIndividual.chromosome[chromeGenePos1] = geneSwap2;
            newIndividual.chromosome[chromeGenePos2] = geneSwap1;
            
            newIndividual.fitness = Fitness.evaluate(newIndividual, this);
            
            // Get energy of solutions
            double currentEnergy = currentIndividual.fitness;
            double neighbourEnergy = newIndividual.fitness;
			
			// Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) 
            		> Parameters.random.nextDouble()) {
                currentIndividual = newIndividual.copy();
            }

            // Keep track of the best solution found
            if (currentIndividual.fitness < best.fitness) {
                best = currentIndividual.copy();
            }
            
            // Cool system
            temp *= 1 - coolingRate;
		}
		
        System.out.println("Final solution fitness: " + best.fitness);
        System.out.println("Final: " + best);
		
		//run for max evaluations
//		for(int gen = 0; gen < Parameters.maxEvaluations; gen++) {
//			//mutate the best
//			Individual candidate = mutateBest();
//			
//			//accept if better
//			if(candidate.fitness < best.fitness) {
//				best = candidate;
//			}
//			
//			outputStats();
//		}
//		saveNeuralNetwork();
	}

	
	@Override
	public double activationFunction(double x) {
//		if (x < -20.0) {
//			return -1.0;
//		} else if (x > 20.0) {
//			return 1.0;
//		}
//		return Math.tanh(x);
		// ELU 
		if (x > 0) return x;
		return 0.1 * (Math.pow(Math.E, x) - 1);
	}

	
}
