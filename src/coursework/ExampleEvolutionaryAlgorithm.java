package coursework;

import java.util.ArrayList;
import java.util.Collections;
import model.Fitness;
import model.Individual;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	// The Main Evolutionary Loop
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		// main EA processing loop
		while (evaluations < Parameters.maxEvaluations) {
			// Select 2 Individuals from the current population
			Individual parent1 = tournamentSelect(); 
			Individual parent2 = tournamentSelect();
//			Individual parent1 = rouletteSelect(); 
//			Individual parent2 = rouletteSelect();
//			Individual parent1 = rankSelect(); 
//			Individual parent2 = rankSelect();
			
			
			// Generate a child by crossover
//			ArrayList<Individual> children = uniformCrossover(parent1, parent2);
//			ArrayList<Individual> children = onePointCrossover(parent1, parent2);	
			ArrayList<Individual> children = twoPointCrossover(parent1, parent2);
//			ArrayList<Individual> children = arithmeticCrossover(parent1, parent2);
			
			
			//mutate the offspring
			mutate(children);
			
			// Evaluate the children
			evaluateIndividuals(children);			

			// Replace children in population
//			replace(children);
			tournamentReplace(children);
//			regeneratePopulation();

			// check to see if the best has improved
			best = getBest();
			
			// Implemented in NN class. 
			outputStats();
			
			//Increment number of completed generations			
		}

		//save the trained network to disk
		saveNeuralNetwork();
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 */
	private Individual getBest() {
		best = null;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	
	
	
	/**
	 * SELECTION
	 */
	private Individual select() {
		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		return parent.copy();
	}
	private Individual tournamentSelect() {
		/**
		 * Elitism - copy the best chromosome (or a few best chromosomes) to new population
		 * 1 - Pick t solutions completely at random from the population
		 * 2 - Select the best of the t solutions to be a parent
		 */
		final int TOURNAMET_SIZE = 20;
		
		Collections.shuffle(population);
		Individual parent = population
				.stream()
				.limit(TOURNAMET_SIZE)
				.sorted((c1, c2) -> c1.compareTo(c2))
				.findFirst()
				.orElse(null);
		return parent;
	}
	// Fitness proportionate selection - roulette wheel selection
	private Individual rouletteSelect() {
		// calculate the total weight
		double weightSum = population
				.stream()
				.mapToDouble(c -> 1 - c.fitness)
				.sum();
		
		// Generate a random number between 0 and weightSum
		double rand = weightSum * Parameters.random.nextDouble();
		// Find random value based on weights
		for(Individual indiv : population) {		
			rand -= (1 - indiv.fitness);		
			if(rand < 0) 
				return indiv;
		}
		// When rounding errors occur, return the last item 
		return population.get(-1);
	}

	// Ranked Fitness proportionate
	private Individual rankSelect() {
		population.sort((c1, c2) -> c2.compareTo(c1));
		
		int rand = Parameters.random.nextInt(Parameters.popSize);
		for (int i = 0; i < Parameters.popSize; i++) {
			rand--;
			if (rand < i) {
				return population.get(i);
			}
		}
		return population.get(-1);
	}
	
	
	

	/**
	 * CROSSOVER 
	 */
	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		children.add(parent1.copy());
		children.add(parent2.copy());		
		return children;
	}
	private ArrayList<Individual> uniformCrossover(Individual parent1, Individual parent2){
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		for (int i = 0; i < parent1.chromosome.length; i++){
			if(Parameters.random.nextBoolean()){
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			} else {
		    child1.chromosome[i] = parent2.chromosome[i];
		    child2.chromosome[i] = parent1.chromosome[i];
			}
		}
		
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child1);
		children.add(child2);	
		return children;
	}
	private ArrayList<Individual> onePointCrossover(Individual parent1, Individual parent2){
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		int cutPoint = Parameters.random.nextInt(parent1.chromosome.length);
		
		for (int i = 0; i < parent1.chromosome.length; i++){
			if(i < cutPoint){
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			} else {
		    child1.chromosome[i] = parent2.chromosome[i];
		    child2.chromosome[i] = parent1.chromosome[i];
			}
		}
		
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child1);
		children.add(child2);	
		return children;
	}
	private ArrayList<Individual> twoPointCrossover(Individual parent1, Individual parent2){
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		int chromLen = parent1.chromosome.length;
		int cutPoint1 = Parameters.random.nextInt(chromLen);
		int cutPoint2 = Parameters.random.nextInt((chromLen - cutPoint1) + 1) + cutPoint1;
		
		for (int i = 0; i < chromLen; i++){
			if(i < cutPoint1 || i >= cutPoint2){
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			} else {
		    child1.chromosome[i] = parent2.chromosome[i];
		    child2.chromosome[i] = parent1.chromosome[i];
			}
		}
		
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child1);
		children.add(child2);	
		return children;
	}
	private ArrayList<Individual> arithmeticCrossover(Individual parent1, Individual parent2){
		Individual child = new Individual();
		for (int i = 0; i < parent1.chromosome.length; i++){
			double avgChrom = (parent1.chromosome[i] + parent2.chromosome[i]) / 2;
			child.chromosome[i] = avgChrom;
		}
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child);
		return children;
	}
	
	
	
	
	
	
	
	
	/**
	 * MUTATION
	 */
	private void mutate(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}

	
	
	
	
	
	/**
	 * REPLACEMENT
	 */
	private void replace(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}		
	}
	// Replace using tournament - same as selection but with worst
	private void tournamentReplace(ArrayList<Individual> individuals) {
		final int TOURNAMET_SIZE = 20;
		
		for (Individual individual : individuals) {
			Collections.shuffle(population);
			Individual worstChrom = population
				.stream()
				.limit(TOURNAMET_SIZE)
				.sorted((c1, c2) -> c2.compareTo(c1))
				.findFirst()
				.orElse(null);

			population.remove(worstChrom);
			population.add(individual);
		}
	}
	
	public void regeneratePopulation() {
		// NOT USED. Replace the worst individuals with randomly generated ones
		population.sort((c1, c2) -> c2.compareTo(c1));
		for (int i = 0; i < 15; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.remove(0);
			population.add(individual);
		}
		evaluateIndividuals(population);
	}

	// Returns the index of the worst member of the population
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}
	

	
	
	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
