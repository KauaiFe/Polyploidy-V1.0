
import java.util.*;

public class EvoMethods {
	
	private double meiosisCoeff;
	private double probPolyploid;
	private double mutationRate;
	private int matingRadius;
	private final double reducedFertility;
	
	public EvoMethods(double meiosisCoeff, double probPolyploid, 
						double mutationRate, int matingRadius, double reducedFertility) {
		this.meiosisCoeff = meiosisCoeff;
		this.probPolyploid = probPolyploid;
		this.mutationRate = mutationRate;
		this.matingRadius = matingRadius;
		this.reducedFertility = reducedFertility;
	}
	
	public double computeSimilarity(int[][] genome01, int[][] genome02) {
		
		double similarity = 0.0;
		
		ArrayList<Integer> queue = new ArrayList<>();
		
		/* if a chromosome (genome02) is the most similar to one from the queue (genome01)
		 * then it must not match again another genome from the queue (genome01)
		 */
		
		ArrayList<Integer> matched = new ArrayList<>(); 
		
		for(int i = 0; i < genome01.length; i++) {
			queue.add(i);
		}
		
		while(!queue.isEmpty()) {
			
			int chromosome = queue.get(0);
			queue.remove(0);
			int compare = -Integer.MAX_VALUE;
			int matchedIndex = -1;
			
			for(int i = 0; i < genome02.length; i++) {
				
				boolean notMatched = true;
				// See whether this chromosome has already a correspondence from previous elements from the queue
				for(int j = 0; j < matched.size(); j++) {
					if(i == matched.get(j)) {
						notMatched = false;
						break;
					}
				}
				
				if(notMatched == true) {
					
					int similarAlleles = 0;
					for(int k = 0; k < genome02[i].length; k++) {
						if(genome01[chromosome][k] == genome02[i][k]) {
							similarAlleles++;
						}
					}
					
					if(similarAlleles > compare) {
						compare = similarAlleles;
						matchedIndex = i;
					}
				}
				
			}
			
			if(matchedIndex != -1) {
				matched.add(matchedIndex);
				similarity += Double.valueOf(compare);
			}
			
		}
		
		return similarity/(Double.valueOf(genome01[0].length)*genome01.length); 
	}

	public int[][] meiosis(int[][] genome) {
		
		int numAlleles = (int) (genome[0].length*this.meiosisCoeff);
		int[][] gametes = null;
		
		switch (genome.length) {
		case 2:
			// Exchanges alleles between chromosomes. Exchange happens on a random linear sequence on the genome
			int marker = (int) (Math.random()*(genome[0].length - numAlleles));
			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[0][i];
				genome[0][i] = genome[1][i];
				genome[1][i] = key;
			}
			double r = Math.random();
			
			if(r < this.probPolyploid) {

				gametes = new int[2][genome[0].length];
				for(int i = 0; i < genome[0].length; i++) {
					gametes[0][i] = genome[0][i];
					gametes[1][i] = genome[1][i];
				}
			}else {
				
				gametes = new int[1][genome[0].length];
				double pair = Math.random();
				if(pair < 0.5) {
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[0][i];
					}
				}else {
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[1][i];
					}
				}
			}
			break;
		case 4:

			int homolog01 = (int) (Math.random()*4.0);
			int homolog02 = (int) (Math.random()*4.0);
			
			while(homolog02 == homolog01) {
				homolog02 = (int) (Math.random()*4.0);
			}
			
			int homolog03 = 0;
			int homolog04 = 0;
			
			for(int i = 0; i < 4; i++) {
				if(i != homolog01 && i != homolog02) {
					homolog03 = i;
					break;
				}
			}
			
			for(int i = 0; i < 4; i++) {
				if(i != homolog01 && i != homolog02 && i != homolog03) {
					homolog04 = i;
					break;
				}
			}

			marker = (int) (Math.random()*(genome[0].length - numAlleles));

			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[homolog01][i];
				genome[homolog01][i] = genome[homolog02][i];
				genome[homolog02][i] = key;
			}
			marker = (int) (Math.random()*(genome[0].length - numAlleles));

			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[homolog03][i];
				genome[homolog03][i] = genome[homolog04][i];
				genome[homolog04][i] = key;
			}
			
			double error = Math.random();
			if(error < this.reducedFertility) {
				gametes = null;
			}else {
				
				int gameteVector01 = 0;
				int gameteVector02 = 0;
				
				double t = Math.random();
				if(t < 0.5) {
					gameteVector01 = homolog01;
					gameteVector02 = homolog02;
				}else {
					gameteVector01 = homolog03;
					gameteVector02 = homolog04;
				}
				
				gametes = new int[2][genome[0].length];
				for(int i = 0; i < genome[0].length; i++) {
					gametes[0][i] = genome[gameteVector01][i];
				}

				for(int i = 0; i < genome[0].length; i++) {
					gametes[1][i] = genome[gameteVector02][i];
				}
			}
			
			break;
		}
		try {
			mutate(gametes);
		}catch(NullPointerException e) {
			
		}
		return gametes;
	}

	public ArrayList<Integer> findMates(HashMap<Integer, int[]> positions, int parent) {
		
		ArrayList<Integer> potentialMates = new ArrayList<>();
		int[] ref = positions.get(parent);
		for(int i = 0; i < positions.size(); i++) {
			int[] pos = positions.get(i);
			if(Math.abs(pos[0] - ref[0]) <= this.matingRadius && Math.abs(pos[1] - ref[1]) <= this.matingRadius) {
				potentialMates.add(i);
			}
		}
		
		return potentialMates;
	}
	
	private void mutate(int[][] genome) {
		
		for(int i = 0; i < genome.length; i++) {
			for(int j = 0; j < genome[i].length; j++) {
				double r = Math.random();
				if(r < this.mutationRate) {
					boolean changed = false;
					while(changed == false) {
						int nucleotide = (int) (Math.random()*4);
						if(nucleotide != genome[i][j]) {
							genome[i][j] = nucleotide;
							changed = true;
						} 
					}			
				}
			}
		}
	}
	
	public double[][] buildAdjMatrix(HashMap<Integer, int[][]> population){
		
		double[][] adjMatrix = new double[population.size()][population.size()];
		
		for(int i = 0; i < population.size(); i++) {
			for(int j = i; j < population.size(); j++) {
				if(i == j) {
					adjMatrix[i][j] = 1.0;
				}else if(population.get(i).length != population.get(j).length) {
					adjMatrix[i][j] = 0.0;
					adjMatrix[j][i] = 0.0;
				}else {
					adjMatrix[i][j] = computeSimilarity(population.get(i), population.get(j));
					adjMatrix[j][i] = adjMatrix[i][j];
					
				}
			}
		}
		
		return adjMatrix;
	}
	
}
