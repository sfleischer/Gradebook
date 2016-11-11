import javax.swing.JTextField;

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
	
	public void setSTD(double s)	{ STD = s; }
	public void setMean(double m)	{ mean = m; }
	public void setMedian(double m)	{ median = m; }
	public void setMin(double m)	{ min = m; }
	public void setMax(double m)	{ max = m; }
	public void setPeople(int p)	{ people = p; }
	public void setPersonal(double p){ personal = p; }
	
	public void setXSpacing(double s)	{ spacing = s; }
	public void setYTicks(double y)		{ yticks = y; }
	public void setIntensity(boolean b)	{ intensity = b; }
	
	public void setGenerations(int g)   {generations = g; }
	public void setMutation(double m)   { mutation = m; }
	public void setThreshold(int t)     { threshold = t; }
	
	
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
	
}