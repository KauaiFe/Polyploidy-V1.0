
Authors: Felipe Kauai, Frederik Mortier, Silvija Milosavljevic, Yves Van de Peer, Dries Bonte

Ghent, Belgium, 2022

Language: Java

The implementation is provided.

The source code include four classes: Main.java, EvoMethods.java, Population.java and SpTracker.java

###### Main.java ######

This class includes the main program where the fundamental processes are executed, as described in Fig. 01 in the manuscript.

All the relevant variables for the execution of the program are provided here:

int numGenerations; /* Integer variable for the number of generations that the simulation must run
int popSize; /* Size of the population, which sets the carrying capacity of the system
int genomeSize; /* Size of the artificial genome (number of elements in a single chromosome vector) 
int matingRadius; /* Radius in which all potential partners are found
int dispersalRadius; /* Radius in which offspring can be dispersed
double similarityThreshold; /* Genetic similarity threshold needed for organisms to mate (from 0 to 1)
double meiosisCoeff; /* Size of the linear sequence which will be exchanged between chromosomes during meiosis (described as a fraction of the genome size)
double probPolyploid; /* Probability that gametes will be unreduced following meiosis in diploid organisms
double mutationRate; /* Sets the probability of a mutation per nucleotide in the artificial genome
double probDispersal; /*Sets the probability that offspring will be dispersed in any location within a radius defined by "dispersalRadius"
double probMate; /* Q = 0.30. This is a constant, but is allowed to be a variable for the user to test the influence of the value in the program under different scenarios.
double reducedFertility; /* Probability that gametes will be abnormal (null) following meiosis in tetraploid individuals.

The class starts by instantiating three Objects: pop, solve and sp.
pop: Used to initialize the population (calls class Population.java)
solve: All methods related to evolutionary dynamics are implemented in class EvoMethods.java
sp: Uses SpTracker.java to count the number of species (does not distinguish between ploidies) in the system.

The population is stored in a HashMap<> which is updated at every generation. Individuals' positions are stored in a second HashMap<>.

Auxiliary static methods at the end of the class are described in the script.

###### Population.java ######

Returns a population of size popSize. Each individual is diploid by default, i.e., is represented by two vectors of size genomeSize each. Initial positions are provided.

###### EvoMethods.java ######

This class contains all fundamental methods related to the evolutionary dynamics of the system.

The constructor takes in 5 variables: meiosisCoeff, probPolyploid, mutationRate, matingRadius, reducedFertility.

The class contains the following methods:

computeSimilarity() /* Computes the genetic similarity between two genomes passed as arguments. Implements the heuristic HeuristicGS described in the main text. Returns a real number between 0 and 1 (>0.95 -> compatible mates).
meiosis() /* Executes meiosis. Verifies whether the genome is diploid or tetraploid and produces gametes accordingly. Gametes are imediately subject to mutation upon formation. Abnormal gametes are just null*.
findMates() /*Returns the set of potential partners given matingRadius.
mutation() /* Mutates gametes given mutationRate
buildAdjMatrix() /* Takes in the population as argument and produces an adjacency matrix (genetic similarities). Uses internally computeSimilarity() method. 

###### SpTracker.java ######

countSp() /* Counts the number of species of a population passed as argument. The algorithm follows the description of species identification given in the main text.
removeIds() /* Private auxiliary method to reduce the size of the matrix returned by countSp() to the number of species.
