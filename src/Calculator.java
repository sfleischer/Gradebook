
public class Calculator {

	//graph statistics
	private double min;
	private double max;
	private double mean;
	private double median;
	private double std;
	private int people;
	private int[] fitchart;
	private int[] weakchart;
	
	//genetics specifications
	private int generations = 300;
	private int threshold = 1000; //threshold will end the algorithm if the top graph's fitness passes this
	private int popSize = 50; //the size of each population
	private double mutation = 10;
	
	//weights. the sum of all weights should equal less than 10
	private double min_weight = 0.5;
	private double max_weight = 0.5;
	private double mean_weight = 2;
	private double median_weight = 3;
	private double std_weight = 3;
	
	
	public Calculator(double min, double max, double mean, double median, double std, int people){
		this.min = min;
		this.max = max;
		this.mean = mean;
		this.median = median;
		this.std = std;
		this.people = people;
		fitchart = new int[generations];
		weakchart = new int[generations];
	}
	
	public int[] generateHistogram(double xspacing){
		int[] raw = findDistribution();
		int[] dist = new int[(int) (100/xspacing)];
		for(int i = 0; i < dist.length; i++){
			dist[i] = countElementsInRange(raw, i*xspacing, (i+1)*xspacing);
		}
		return dist;
	}
	
	public int[] generateHistogram(int[] raw, double xspacing){
		int[] dist = new int[(int) (100/xspacing)];
		for(int i = 0; i < dist.length; i++){
			dist[i] = countElementsInRange(raw, i*xspacing, (i+1)*xspacing);
		}
		return dist;
	}
	
	private int countElementsInRange(int[] array, double min, double max){
		int total = 0;
		for(int elem : array){
			if(min < elem && elem <= max)
				total++;
		}
		return total;
	}
	
	public int[] findDistribution(){
		int[][] population = generateFirstPopulation(min, max, people, popSize);
		int i = 0;
		do {
			int[][] fittest = findTheFittest(population);
			int[][] culled = cull(fittest, population);
			population = createNextGeneration(culled);
			fitchart[i] = fittest[fittest.length-1][0];
			weakchart[i] = fittest[0][0];
			adjustMutationRate(fitchart[i]);
		} while(i < generations-1 && fitchart[i++] < threshold);
		int[][] fittest = findTheFittest(population);
		return population[fittest[fittest.length-1][1]];
	}
	
	public int[][] generateFirstPopulation(double min2, double max2, int num, int pop){
		int[][] population = new int[pop][num];
		for(int i = 0; i < pop; i++){
			for(int j = 0; j < num; j++){
				population[i][j] = (int) ((max2-min2+1)*Math.random() + min2);
			}
			Sorting.sort(population[i]);
		}
		return population;
	}
	
	public int[][] findTheFittest(int[][] population){
		int[][] fitness = new int[population.length][2];
		for(int i = 0; i < population.length; i++){
			fitness[i][0] = getFitness(population[i]);
			fitness[i][1] = i;
		}
		
		Sorting.sort(fitness);
		return fitness;
	}
	
	/*public int[][] findTheMostDiverse(int[][] population){
		
	}*/
	
	public int[][] cull(int[][] fitness, int[][] population){
		int[][] culled = new int[fitness.length/2][population[0].length];
		for(int i = 0; i < culled.length; i++){
			culled[i] = population[fitness[i+fitness.length/2][1]];
		}
		return culled;
	}
	
	public int[][] createNextGeneration(int[][] parents){
		int[][] children = new int[parents.length*2][parents[0].length];
		for(int i = 0, j = 0; i < parents.length; i++, j+=2){
			int[] parent1 = parents[(int) (parents.length*Math.random())];
			int[] parent2 = parents[(int) (parents.length*Math.random())];
			Sorting.sort(parent1);
			Sorting.sort(parent2);
			children[j] = reproduce(parent1, parent2);
			children[j+1] = reproduce(parent2, parent1);
			mutate(children[j]);
			mutate(children[j+1]);
			//injectStandardDeviation(children[j]);
			//injectStandardDeviation(children[j+1]);
		}
		children[0] = parents[parents.length - 1];
		return children;
	}
	
	/**
	 * Changes the mutation rate based on the level of fitness. The mutation
	 * rate lowers as fitness reaches the ideal fitness (1000) and is high
	 * when the fitness rating is very low. 
	 * @param fitness The fitness of the current generation
	 */
	public void adjustMutationRate(int fitness){
		double cap = fitness < 990 ? 15 : 19;
		mutation = cap / (fitness - 1000.1) + 20;
	}
	
	/**
	 * The arrays can reproduce by swapping elements
	 * @param array1
	 * @param array2
	 * @return
	 */
	public int[] reproduce(int[] array1, int[] array2){
		int[] child = new int[array1.length];
		for(int i = 0; i < array1.length; i++){
			if(i % 2 == 0)
				child[i] = array1[i];
			else
				child[i] = array2[i];
		}
		return child;
	}
	
	/**
	 * Mutates the organism by introducing potential changes to each element in the array.
	 * @param array The "organism" or distribution to be 
	 */
	public void mutate(int[] array){
		for(int i = 0; i < array.length; i++){
			int fate = (int) (100*Math.random());
			if(fate < mutation*2){
				array[i] = array[i] + (int) (2*Math.random() - 1);
			} else if(fate < mutation * 3.5){
				array[i] = array[i] + (int) (4*Math.random() - 2);
			} else if(fate < mutation * 4.0){
				array[i] = array[i] + (int) (6*Math.random() - 3);
			} else if(fate < mutation * 4.5){
				array[i] = array[i] + (int) (8*Math.random() - 4);
			} else if(fate < mutation * 5.0){
				array[i] = array[i] + (int) (10*Math.random() - 5);
			}
			if(array[i] < min)
				array[i] = (int) min;
			if(array[i] > max)
				array[i] = (int) max;
			
		}
	}
	
	
	/**
	 * This is the fitness function of the genetic algorithm
	 * @param array The "organism" that is being evaluated for fitness. The organism is
	 * a potential distribution that fits the stats parameters
	 * @return A fitness score. A fitness score of 1000 is perfect and numbers that are
	 * lower indicate poorer fitness
	 */
	public int getFitness(int[] array){
		double fitness = 1000;
		Sorting.sort(array);
		fitness -= 2 * std_weight * Math.pow(std - standardDev(array), 2);
		fitness -= mean_weight * Math.pow(mean - getMean(array), 2);
		fitness -= median_weight * Math.pow(median - array[array.length/2], 2);
		fitness -= min_weight * Math.pow(min - array[0], 2);
		fitness -= max_weight * Math.pow(max - array[array.length-1], 2);
		return (int) fitness;
	}
	
	/**
	 * Returns the fitchart of the calculator. A fitchart plots the fitness of the most
	 * fit organism for each generation. Hopefully the fitness trend increases over the 
	 * generations.
	 * @return The fitchart of the calculator.
	 */
	public int[] getFitchart(){
		return fitchart;
	}
	
	public int[] getWeakchart(){
		return weakchart;
	}
	
	/**
	 * Overriding equals method
	 */
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(!this.getClass().equals(o.getClass()))
			return false;
		Calculator calc = (Calculator) o;
		return 	min == calc.getMin() &&
				max == calc.getMax() &&
				std == calc.getSTD() &&
				mean == calc.getMean() &&
				median == calc.getMedian() &&
				people == calc.getPeople();
	}
	
	
	/**
	 * Measures population standard deviation
	 * @param sample
	 * @return
	 */
	public static double standardDev(int[] sample){
		double mean = getMean(sample);
		
		double std = 0.0;
		for(int i = 0; i < sample.length; i++){
			std += (mean - sample[i])*(mean - sample[i]);
		}
		
		return Math.sqrt(std/sample.length);
	}
	
	public static double getMean(int[] sample){
		int total = 0;
		
		for(int i = 0; i < sample.length; i++){
			   total += sample[i];
		}

		return 1.0*total / sample.length;
	}
	
	public static int getMin(int[] sample){
		int min = sample[0];
		for(int n : sample){
			min = Math.min(min, n);
		}
		return min;
	}
	
	public static int getMax(int[] sample){
		int max = sample[0];
		for(int n : sample){
			max = Math.max(max, n);
		}
		return max;
	}
	
	public static int getMedian(int[] sample){
		Sorting.sort(sample);
		return sample[sample.length/2];
	}
	
	public static int sum(int[] sample){
		int sum = 0;
		for(int n : sample){
			sum += n;
		}
		return sum;
	}
	
	/**
	 * @param sample The sample that you wish to find the percentile in. sample does
	 * not have to be sorted
	 * @param num The number you wish to find the percentile of
	 * @return The percentile of the number.
	 */
	public static int findPercentile(int[] sample, int num){
		Sorting.sort(sample);
		return search(sample, 0, sample.length-1, num);
	}
	
	/**
	 * Precondition: The sample is already sorted. This algorithm uses
	 * sorted list invariants to make calculations faster.
	 * @param sample The sample to search the index of
	 * @param min The lower bound for the search (should be 0 to start)
	 * @param max The upper bound for the search (should be sample.length - 1)
	 * @param num The number to search the index of
	 * @return The index of where the number appears in the sample.
	 */
	private static int search(int[] sample, int min, int max, int num){
		if(min >= max || max >= sample.length)
			return (int) (100.0*max/sample.length);
		
		int mid = (int) (min+max)/2;
		if(sample[mid] == num)
			return (int) (100.0*mid/sample.length);
		else if(sample[mid] < num)
			return search(sample, mid+1, max, num);
		else 
			return search(sample, min, mid, num);
	}	
	
	//ALL TRIVIAL GETTERS
	public double getMin(){ return min; }
	public double getMax(){ return max; }
	public double getSTD(){ return std; }
	public double getMean(){ return mean; }
	public double getMedian(){ return median; }
	public int getPeople(){ return people; }
	
	//IMPORTANT SETTERS
	
	public void update(double min, double max, double mean, double median, double std, int people){
		this.min = min;
		this.max = max;
		this.mean = mean;
		this.median = median;
		this.std = std;
		this.people = people;
	}
	
	public void setGenerations(int g){
		generations = g;
		fitchart = new int[generations];
		weakchart = new int[generations];
	}
	
	public void setThresold(int threshold){
		this.threshold = threshold;
	}
	
	public void setPopulation(int pop){
		popSize = pop;
	}
	
	public void setStdWeight(double weight){
		std_weight = weight;
	}
	
	public void setMeanWeight(double weight){
		mean_weight = weight;
	}
	
	public void setMedianWeight(double weight){
		median_weight = weight;
	}
	
	public void setMinWeight(double weight){
		min_weight = weight;
	}
	
	public void setMaxWeight(double weight){
		max_weight = weight; 
	}
	
}
