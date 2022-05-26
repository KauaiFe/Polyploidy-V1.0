package speciesmodel;

import java.util.ArrayList;
import java.util.HashMap;

/* Here the Artificial Genome (AG) model is implemented to initialize the program.
 * Each individual receives two copies of the AGs, thus making them diploids.
 * Each AG consists of a sequence of four nucleotides taken randomly from the set {0, 1, 2, 3}
 * Each chromosome has length chrLength;
 * A population with size 'popSize' is created.
 */

public class Population {

	private int popSize;
	private int chrLength;
	private int[][] chromosome;
	private HashMap<Integer, int[][]> popGenomes;
	private HashMap<Integer, int[]> popPositions;
	private HashMap<Integer, Integer> ploidyLevel;
	
	public HashMap<Integer, int[]> getPopPositions() {
		return popPositions;
	}

	public HashMap<Integer, int[][]> getPopGenomes() {
		return popGenomes;
	}
	
	public HashMap<Integer, Integer> getPloidyLevel() {
		return ploidyLevel;
	}
	
	public Population(int popSize, int chrLength) { // Constructor to initialize simulation
		
		this.popSize = popSize;
		this.chrLength = chrLength;
		fillPopulation();

	}

	private void fillPopulation() {
		
		this.popGenomes = new HashMap<>();
		this.popPositions = new HashMap<>();
		this.ploidyLevel = new HashMap<>();
		buildGenes();
		
		for(int i = 0; i < this.popSize; i++) {
			this.popGenomes.put(i, this.chromosome); //At start all individuals share the exact same genetic structure
			int posX = (int) (Math.random()*128.0);
			int posY = (int) (Math.random()*128.0);
			int[] position = {posX, posY};
			this.popPositions.put(i, position);
			ploidyLevel.put(i, 2);
		}
	}

	private void buildGenes() {

		this.chromosome = new int[2][this.chrLength]; //Starts with diploid organisms
		
		for(int i = 0; i < this.chromosome[0].length; i++) {
			int nucleotide = (int) (Math.random()*4);
			this.chromosome[0][i] = nucleotide;
			//nucleotide = (int) (Math.random()*4);
			this.chromosome[1][i] = nucleotide;
		}
		
		
	}
	
}
