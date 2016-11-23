import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	
	private static void generateWarning(String title, String message){
		JOptionPane.showMessageDialog(new JFrame(),  
				message, 
				title,
				JOptionPane.WARNING_MESSAGE);
	}
	
	public void setSTD(double s)	{ STD = s; }
	public void setMean(double m)	{ mean = m; }
	public void setMedian(double m)	{ median = m; }
	
	public void setMin(double m){ 
		if(m < 0 || m > 100){
			generateWarning("Min Warning", "Min should be between 0 and 100");
		}
		min = m > 100 ? 100 : m;
		min = m < 0 ? 0 : min;
	}
	
	public void setMax(double m){
		if(m < 0 || m > 100){
			generateWarning("Max Warning", "Max should be between 0 and 100");
		}
		max = m > 100 ? 100 : m;
		max = m < 0 ? 0 : max;
	}
	
	public void setPeople(int p){
		if(p < 0){
			generateWarning("People Warning", "Cannot have negative people");
		} else if(p > 1000){
			generateWarning("People Warning", "Cannot handle more than a 1000 people");
		}
		people = p > 1000 ? 1000 : p;
		people = p < 0 ? 0 : people;
	}
	
	public void setPersonal(double p){ personal = p; }
	
	public void setXSpacing(double s)	{ spacing = s; }
	public void setYTicks(double y)		{ yticks = y; }
	public void setIntensity(boolean b)	{ intensity = b; }
	
	public void setGenerations(int g)   { generations = g; }
	public void setMutation(double m)   { mutation = m; }
	public void setThreshold(int t){
		if(threshold < 0 || threshold > 1000){
			generateWarning("Threshold Warning", "Threshold is between 0 and 1000");
		}
		threshold = t > 1000 ? 1000 : t; 
		threshold = t < 0 ? 0 : threshold;
	}
	public void setPopulation(int p)    { population = p; }
	
	
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
