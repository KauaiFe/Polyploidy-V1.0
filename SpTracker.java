package speciesmodel;

public class SpTracker {

	private double geneticThreshold;
	
	public SpTracker(double geneticThreshold) {
		this.geneticThreshold = geneticThreshold;
	}
	
	public int[][] countSp(double[][] adjMatrix) {
		
		int[][] species = new int[(int) (adjMatrix.length/2)][adjMatrix.length];
		int numIndividuals = 0;
		
		for(int i = 0; i < species.length; i++) {
					
			for(int j = 0; j < species[i].length; j++) {
						
				boolean free = true;
				for(int f = 0; f < species.length; f++) {
					if(species[f][j] == 1) {
						free = false;
							break;
					}
				}
						
				if(free == true) {
					species[i][j] = 1;

					boolean adding = true;
					while(adding) {
								
						int addedIds = 0;
								
						for(int l = 0; l < adjMatrix.length; l++) {
							if(species[i][l] == 1) {
										
								for(int h = 0; h < adjMatrix.length; h++) {
									if(l != h) {

										if((adjMatrix[l][h] - this.geneticThreshold) >= -0.00001 && species[i][h] == 0) {

											species[i][h] = 1;
											addedIds++;
											numIndividuals++;
										}
									}
											
								}
							}
						}
								
						if(addedIds == 0) {
							adding = false;
						}
					}
					
					if(adding == false) {
						break;
					}
				}
			}
			if(numIndividuals == adjMatrix.length) {
				break;
			}
		}
		
		for(int k = 0; k < species.length; k++) {
			int countIds = 0;
			for(int l = 0; l < species[k].length; l++) {
				if(species[k][l] == 1) {
					countIds++;
				}
			}
			if(countIds < 2) {
				species = removeId(species, k);
				k = 0;
			}
		}
		
		return species;
	}
	
	private static int[][] removeId(int[][] pop, int id) {
		
		int[][] newPop = new int[pop.length-1][pop[0].length];
		
		for(int i = 0; i < id; i++) {
			for(int j = 0; j < pop[i].length; j++) {
				newPop[i][j] = pop[i][j];
			}
		}
		for(int i = id+1; i < pop.length; i++) {
			for(int j = 0; j < pop[i].length; j++) {
				newPop[i-1][j] = pop[i][j];
			}
		}

		return newPop;
	}
	
}
