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
		
        // Set initial temp, cooling rate, and improvement count
        double temp = 10000;
        double coolingRate = 0.003;
        int custEvolutionCount = 100;

		// main EA processing loop
		while (evaluations < Parameters.maxEvaluations) {
			
			// Select 2 Individuals from the current population
			Individual parent1; 
			Individual parent2;
			
			switch(Parameters.selectionType) {
				case TOURNAMENT:
				default:
					parent1 = tournamentSelect(); 
					parent2 = tournamentSelect();
					break;
				case ROULETTE:
					parent1 = rouletteSelect(); 
					parent2 = rouletteSelect();
					break;
				case RANK:
					parent1 = rankSelect(); 
					parent2 = rankSelect();
					break;
				case BEST:
					parent1 = population
						.stream()
						.sorted((c1, c2) -> c1.compareTo(c2))
						.findFirst()
						.orElse(null);
					parent2 = population
						.stream()
						.sorted((c1, c2) -> c1.compareTo(c2))
						.skip(1)
						.findFirst()
						.orElse(null);
					break;
				case RANDOM:
					parent1 = randSelect(); 
					parent2 = randSelect();
			}


			// Generate a child by crossover
			ArrayList<Individual> children;
			
			switch(Parameters.crossoverType) {
				case ARITHM:
					children = arithmeticCrossover(parent1, parent2);
					break;
				case ONE_POINT:
					children = onePointCrossover(parent1, parent2);
					break;
				case TWO_POINTS:
				default:
					children = twoPointCrossover(parent1, parent2);
					break;
				case UNIFORM:
					children = uniformCrossover(parent1, parent2);
					break;
			}
						
			//mutate the offspring
			mutate(children);
			
			evaluateIndividuals(children);
			// Mutate with annealing function
//				children.set(0, mutateAnnealation(children.get(0), temp));
//				children.set(1, mutateAnnealation(children.get(1), temp));
//				temp *= 1 - coolingRate;
			
			
			
			// Re-initialise
			custEvolutionCount += 7;
			if (custEvolutionCount >= 1000 && getBest().fitness > 0.15) {
				population = initialise();
				custEvolutionCount = 0;
			} 

			// Escape local optima
//				if (custEvolutionCount >= 4500 && getBest().fitness < 0.15) {
////				if (noImprovement >= 500) {
//					mutateFromBestN(10, 0.05, 1);
//					keepBestN(70);
//					custEvolutionCount = 0;
////					mutate(population);
////					evaluateIndividuals(population);
////					
////					mutateApartFirstN(10, 0.5);
////					mutateFromBestN(10, 0.05, 1.2);
//				}
			
			// Evaluate the children
			evaluateIndividuals(children);			

			// Replace children in population
			switch(Parameters.replaceType) {
				case REP_TOURNAMENT:
				default:
					tournamentReplace(children);
					break;
				case REP_WORST:
					replaceWorst(children);
					break;
			}

//				regeneratePopulation();

			// check to see if the best has improved
//				if (getBest().fitness == best.fitness) {
//					Parameters.setMutationRate(Parameters.mutateRate + 0.1);
//					Parameters.setMutationChange(Parameters.random.nextDouble() * 2.0);
//				} else {
//					Parameters.setMutationRate(0.05);
//					Parameters.setMutationChange(1);
//				}
			
			injectIndividual();  // Inject a new individual
			
			best = getBest();
			
			
			outputStats();
		}

//		saveNeuralNetwork();  // save the trained network to disk
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
	 * INITIALISATION. Generates a randomly initialised population
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			// chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}
	
	private void partialInitialise() {
		Individual individual;
		population.sort((c1, c2) -> c2.compareTo(c1));
		for (int i = 0; i < 15; ++i) {
			individual = new Individual();
			population.remove(0);
			population.add(individual);
		}
		evaluateIndividuals(population);
	}

	private void keepBestN(int n) {
		population.sort((c1, c2) -> c2.compareTo(c1));
		for (int i = 0; i < Parameters.popSize - n; ++i) {
			Individual individual = new Individual();
			population.remove(0);
			population.add(individual);
		}
		evaluateIndividuals(population);
	}
	
	
	
	/**
	 * SELECTION
	 */
	private Individual randSelect() {
		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		return parent.copy();
	}
	private Individual tournamentSelect() {
		/**
		 * Elitism - copy the best chromosome (or a few best chromosomes) to new population
		 * (happens if tournament size is equal to total pop size)
		 * 1 - Pick t solutions completely at random from the population
		 * 2 - Select the best of the t solutions to be a parent
		 */
		final int TOURNAMET_SIZE = (int) (Parameters.popSize * 0.2);
		
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
	private void mutateApartFirstN(int n, double mutationRate) {	
		population.sort((c1, c2) -> c2.compareTo(c1));
		for(int i=0; i < Parameters.popSize - n; i++) {
			Individual individual = population.get(i);
			for (int j = 0; j < individual.chromosome.length; j++) {
				if (Parameters.random.nextDouble() < mutationRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[j] += (Parameters.mutateChange);
					} else {
						individual.chromosome[j] -= (Parameters.mutateChange);
					}
				}
			}
		}		
		evaluateIndividuals(population);
	}
	private void mutateFromBestN(int n, double mutationRate, double mutationChange) {	
		double before = 0;
		double after = 0;
		for (Individual individual : population) {
			individual.fitness = Fitness.evaluate(individual, this);
			before += individual.fitness;
		}
		before = before / population.size();
		
		
		ArrayList<Individual> bests = new ArrayList<Individual>();
		
		population.sort((c1, c2) -> c1.compareTo(c2));
		for (int i=0; i<n; i++) {	
			bests.add(population.get(i).copy());
		}
		
		population.sort((c1, c2) -> c2.compareTo(c1));
		
		
		for(int i=0; i < n; i++) {
			Individual individual = bests.get(i);
			for (int j = 0; j < individual.chromosome.length; j++) {
				if (Parameters.random.nextDouble() < mutationRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[j] += (mutationChange);
					} else {
						individual.chromosome[j] -= (mutationChange);
					}
				}
			}
			population.set(i, individual);
		}		
//		evaluateIndividuals(population);
		
		for (Individual individual : population) {
			individual.fitness = Fitness.evaluate(individual, this);
			after += individual.fitness;
		}
		after = after / population.size();
		System.out.println("before: " + before + " after: " + after);
	}
	
	
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }
	private Individual mutateAnnealation(Individual individual, double temp) {
		Individual newIndividual = individual.copy();
		
		// Get a random genes in the chromosome (change with next int)
        int chromeGenePos1 = (int) (newIndividual.chromosome.length * Parameters.random.nextDouble());
        int chromeGenePos2 = (int) (newIndividual.chromosome.length * Parameters.random.nextDouble());

        // Get the values at selected positions in the chromosome
        double geneSwap1 = newIndividual.chromosome[chromeGenePos1];
        double geneSwap2 = newIndividual.chromosome[chromeGenePos2];

        // Swap them
        newIndividual.chromosome[chromeGenePos1] = geneSwap2;
        newIndividual.chromosome[chromeGenePos2] = geneSwap1;
        
        // Evaluate fitness
        newIndividual.fitness = Fitness.evaluate(newIndividual, this);
        
        // Get energy of solutions
        double currentEnergy = individual.fitness;
        double neighbourEnergy = newIndividual.fitness;
		
		// Decide if we should accept the neighbour
        if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) 
        		> Parameters.random.nextDouble()) {
        	individual = newIndividual;
        }

        return individual;
        // Keep track of the best solution found
//        if (currentIndividual.fitness < best.fitness) {
//            best = currentIndividual.copy();
//        }
	}
	
	
	
	
	
	/**
	 * REPLACEMENT
	 */
	private void replaceWorst(ArrayList<Individual> individuals) {
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
	

    private void injectIndividual() {
        Individual newIndividual = new Individual();
        newIndividual.fitness = Fitness.evaluate(newIndividual, this);
        population.sort((c1, c2) -> c2.compareTo(c1));
        population.set(2, newIndividual);
    }

	
	@Override
	public double activationFunction(double x) {
		// Sigmoid
//		return 1 / (1 + Math.pow(Math.E, -x));
		
		// Tanh
//		if (x < -20.0) {
//			return -1.0;
//		} else if (x > 20.0) {
//			return 1.0;
//		}
//		return Math.tanh(x);
		
		// Heaviside or step function
//		if (x <= 0) return -1.00;
//		return 1.0;
		
		// ReLU
//		if (x > 0) return x;
//		return -1;
		
		// Leaky ReLU
//		if (x > 0) return x;
//		return 0.01 * x;
		
		// ELU - Top 2 most effective
		if (x > 0) return x;
		return 0.1 * (Math.pow(Math.E, x) - 1);
//		return 1.673263 * (Math.pow(Math.E, x) - 1);

	 	
		// SELU - top 2 most effective
//		if (x > 0) return x * 1.0507009;
//		return 1.0507009 * (1.673263 * Math.pow(Math.E, x)) - 1.673263;
		
		// Swish
//		return x * (1 / (1 + Math.pow(Math.E, -x)));
		
		// HardELiSH - https://arxiv.org/pdf/1808.00783.pdf
//		if (x < 0) return Math.max(0, Math.min(1, (x + 1) / 2)) * (Math.pow(Math.E, x) - 1);
//		return x * Math.max(0, Math.min(1, (x + 1) / 2));
	}
}
