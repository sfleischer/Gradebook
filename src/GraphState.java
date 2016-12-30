
/**
 * This class maintains the state of the program by keeping track of all the fields
 * that the user has entered. This class will also throw IllegalArgumentExceptions
 * if the field is too large or small for its scope
 * @author sfleischer
 *
 */
public class GraphState {
	private double STD;
	private double mean;
	private double median;
	private double min;
	private double max;
	private int people;
	private double personal;
	
	private double spacing;
	private double yticks;
	private boolean intensity;
	
	private int generations;
	private double mutation;
	private int threshold;
	private int population;
	
	public GraphState(){
		STD = 10;
		mean = 50;
		median = 50;
		min = 0;
		max = 100;
		people = 100;
		personal = 0;
		
		spacing = 10;
		yticks = 10;
		intensity = true;
		
		generations = 300;
		mutation = 8;
		threshold = 1000;
		population = 50;
	}
	
	
	
	public void setSTD(double s){ 
		if(s < 0){
			throw new IllegalArgumentException("Standard Deviation must be positive");
		}
		STD = s; 
	}
	
	public void setMean(double m) {
		if(m < 0 || m > 100)
			throw new IllegalArgumentException("Mean must be between 0 and 100");
		mean = m; 
	}
	
	public void setMedian(double m)	{ 
		if(m < 0 || m > 100)
			throw new IllegalArgumentException("Median must be between 0 and 100");
		median = m; 
	}
	
	public void setMin(double m){ 
		if(m < 0 || m > 100){
			throw new IllegalArgumentException("Min must be between 0 and 100");
		}
		min = m;
	}
	
	public void setMax(double m){
		if(m < 0 || m > 100){
			throw new IllegalArgumentException(
					"Max should be between 0 and 100");
		}
		max = m;
	}
	
	public void setPeople(int p){
		if(p < 0){
			throw new IllegalArgumentException(
					"There cannot be negative people");
		} else if(p > 1000){
			throw new IllegalArgumentException(
					"Program cannot handle more than 1000 people");
		}
		people = p;
	}
	
	public void setPersonal(double p){ personal = p; }
	
	public void setXSpacing(double s)	{ spacing = s; }
	public void setYTicks(double y)		{ yticks = y; }
	public void setIntensity(boolean b)	{ intensity = b; }
	
	public void setGenerations(int g){ 
		if(g < 0){
			throw new IllegalArgumentException("Generations cannot be negative");
		} else if (g > 1000){
			throw new IllegalArgumentException("Too many Generations. Limit is 1000.");
		}
		generations = g; 
	}
	
	public void setMutation(double m)   { mutation = m; }
	public void setThreshold(int t){
		if(t < 0 || t > 1000){
			throw new IllegalArgumentException("Threshold must be between 0 and 1000");
		}
		threshold = t;
	}
	
	public void setPopulation(int p){
		if(p < 0){
			throw new IllegalArgumentException("Population cannot be negative");
		} else if(p > 500){
			throw new IllegalArgumentException(
					"Too high a population. Population must be at most 500");
		}
		population = p; 
	}
	
	
	/*
	 * GETTERS
	 */
	public double  getSTD()		{ return STD; }
	public double  getMean()	{ return mean; }
	public double  getMedian()	{ return median;  }
	public double  getMin()		{ return min;  }
	public double  getMax()		{ return max; }
	public int     getPeople()	{ return people; }
	public double  getPersonal(){ return personal; }
	
	public double  getXSpacing() { return spacing; }
	public double  getYTicks()	 { return yticks; }
	public boolean getIntensity(){ return intensity; }
	
	public int 	   getGenerations(){ return generations; }
	public double  getMutation()   { return mutation; }
	public int 	   getThreshold()  { return threshold; }  
	public int 	   getPopulation() { return population; }
	
}
