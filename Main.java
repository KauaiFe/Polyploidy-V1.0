package speciesmodel;

import java.io.*;
import java.util.*;

//This is the standard implementation of the software used in the manuscript. 
//Number os species are printed every 500 generations.

public class Main {

	public static void main(String[] args) throws IOException {
		
		int numGenerations = 10001;
		int popSize = 2500;
		int genomeSize = 200;
		int matingRadius = 3;
		int dispersalRadius = 2;
		double similarityThreshold = 0.95;
		double meiosisCoeff = 0.05;
		double probPolyploid = 0.05;
		double mutationRate = 0.00005;
		double probDispersal  = 0.01;
		double probMate = 0.3;
		double reducedFertility = 0.12;
		
		//Create text file in which data will be printed
		File file = new File("NumberOfSpecies.txt");
		FileWriter fw = new FileWriter(file);
		PrintWriter speciesFile = new PrintWriter(fw);
		speciesFile.println("diploids;polyploids;generation");
		
		Population pop = new Population(popSize, genomeSize);
		EvoMethods solve = new EvoMethods(meiosisCoeff, probPolyploid, 
												mutationRate, matingRadius, reducedFertility);
		SpTracker sp = new SpTracker(similarityThreshold);
					
		HashMap<Integer, int[][]> population = pop.getPopGenomes();
		HashMap<Integer, int[]> positions = pop.getPopPositions();

		for(int g = 0; g < numGenerations; g++) {
			
			//Empty hashmaps are initialized to store next generation iteratively
			HashMap<Integer, int[][]> newPopulation = new HashMap<>();
			HashMap<Integer, int[]> newPositions = new HashMap<>();
					
			int offspringCount = 0;
					
			while(newPopulation.size() < population.size()) {
						
				int parent01 = offspringCount;

				int[][] parent01Chromosome = population.get(parent01);

				ArrayList<Integer> mates = solve.findMates(positions, parent01);
						
				double pMate = Math.random();
				if(pMate < probMate) {
					int newMate = (int) (Math.random()*mates.size());
					parent01Chromosome = population.get(mates.get(newMate));
				}
					
				for(int i = 0; i < mates.size(); i++) {
						
					//A new gamete for parent01 is generated for every mating trial
					int[][] gameteP1 = solve.meiosis(parent01Chromosome);
							
					//Select a random mating inside the mating radius
					int mate = (int) (Math.random()*mates.size());

					int[][] mate02Chromosome = population.get(mates.get(mate));
					int[][] gameteP2 = solve.meiosis(mate02Chromosome);
					
					//If gametes from parent 01 or 02 are null (reduced fertility)
					//then throw NullPointerException and continue execution
					try {
								
						double similarity = solve.computeSimilarity(gameteP1, gameteP2);
								
						if(gameteP1.length == gameteP2.length && similarity >= similarityThreshold
								&& parent01Chromosome.length == mate02Chromosome.length) {
									
							int[][] offspring = joinGametes(gameteP1, gameteP2);
							int[] offspringPosition = positions.get(parent01);
								
							double t = Math.random();
							if(t < probDispersal) {
								offspringPosition = newPosition(positions, parent01, dispersalRadius);
							}
								
							newPopulation.put(offspringCount, offspring);
							newPositions.put(offspringCount, offspringPosition);
							offspringCount++;
							break;
									
						}
							
					}catch(NullPointerException e) {
							
					}
						
				}
			}
				
			population = newPopulation;
			positions = newPositions;
			
			//Print current generation on the screen
			System.out.println("Generation: "+g);

			if(g % 500 == 0){
					
				double[][] adjMatrix = solve.buildAdjMatrix(population);
				int[][] species = sp.countSp(adjMatrix);
					
				int diploidNumbers = countDiploids(species, population);
				int polyploidNumbers = species.length - diploidNumbers;
				
				//Prints the number of diploid and polyploid species in the system at generation g
				speciesFile.println(diploidNumbers+";"+polyploidNumbers+";"+g);
					
			}
			
		}
			speciesFile.close();
	}
			

	// #############################################################################################
	
	//Join gametes from both parents to form offspring, which is stored in newPopulation
	static int[][] joinGametes(int[][] gametes01, int[][] gametes02) {
		
		int ploidy = gametes01.length + gametes02.length;
		int[][] newGenome = new int[ploidy][gametes01[0].length];

		for(int j = 0; j < gametes01.length; j++) {
			for(int k = 0; k < gametes01[j].length; k++) {
				newGenome[j][k] = gametes01[j][k];
			}
		}
		for(int j = gametes01.length; j < ploidy; j++) {
			for(int k = 0; k < gametes02[0].length; k++) {
				newGenome[j][k] = gametes02[j-gametes01.length][k];
			}
		}
		
		return newGenome;
	}
	
	//Find new position of offspring given probDispersal
	static int[] newPosition(HashMap<Integer, int[]> positions, int parent, int dispersalRadius) {
		
		int[] parentPosition = positions.get(parent);
		ArrayList<int[]> newPositions = new ArrayList<>();
		
		for(int i = 0; i < 128; i++) {
			for(int j = 0; j < 128; j++) {
				if(Math.abs(i - parentPosition[0]) <= dispersalRadius 
						&& Math.abs(j - parentPosition[1]) <= dispersalRadius) {
					int[] pos = {i, j};
					newPositions.add(pos);
				}
			}
		}
		
		int index = (int) (Math.random()*newPositions.size());
		int[] position = newPositions.get(index);

		return position;
	}
	
	//Count number of diploid species in population
	static int countDiploids(int[][] species, HashMap<Integer, int[][]> population) {
		
		int numDiploids = 0;
		for(int i = 0; i < species.length; i++) {
			for(int j = 0; j < species[i].length; j++) {
				if(species[i][j] == 1 && population.get(j).length == 2) {
					numDiploids++;
					break;
				}
			}
		}
		return numDiploids;
	}
	
	static HashMap<Integer, int[][]> joinPopulations(HashMap<Integer, int[][]> backtrack, 
													HashMap<Integer, int[][]> population){
		
		HashMap<Integer, int[][]> jointPopulations = new HashMap<>();
		
		for(int i = 0; i < backtrack.size()*2; i++) {
			if(i < backtrack.size()) {
				jointPopulations.put(i, backtrack.get(i));
			}else {
				jointPopulations.put(i, population.get(i-backtrack.size()));
			}
		}
		
		return jointPopulations;
	}

}
