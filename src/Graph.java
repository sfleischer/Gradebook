import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.*;
import javax.swing.JPanel;
import components.Moveable;
import components.*;

/**
 * This class graphs the Histogram. When a user clicks on a bar, a GradientPanel will
 * appear to allow the user to change the color of the bar. This class does not have
 * a laymout manager, which allows the user can move the GradientPanel over the Graph
 * panel.
 * @author sfleischer
 *
 */
@SuppressWarnings("serial")
public class Graph extends JPanel{
	
	
	public static final int STD = 4;
	public static final int ORIGIN_X = 100;
	public static final int ORIGIN_Y = 100;
	public static final int END_SPACE = 100;
	public static final int HIST_SPACE = 1;
	
	int[] grades = {0};
	
	double interval = 10;       //the bin interval
	double max_frequency = 1.0;  //the highest frequency on the y axis
	int divisions  = 10;       //the number of ticks on the y axis
	
	double xunit = 0;
	double yunit = 0;
	int xstart = 0;
	int ystart = 0;
	int xlength = 0;
	int ylength = 0;
	
	Color current;
	Color[] colorBar = new Color[2];
	boolean showGradient = false;
	GradientPanel gradient;
	
	public Graph(){
		colorBar[0] = Color.red;
		colorBar[1] = Color.blue;
		
		this.setPreferredSize(new Dimension(700,700));
		this.addMouseListener(new MouseHandler());
		this.setLayout(null);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		drawXAxis(g2, interval);
		drawYAxis(g2, divisions, max_frequency);
		drawHistogramBars(g2);
	}
	
	/**
	 * Draws the X Axis of the histogram with appropriate tick spacing
	 * @param g2 Graphics2D
	 * @param interval The spacing between grades on the histogram
	 */
	public void drawXAxis(Graphics2D g2, double interval){
		//update instance values
		xlength = getWidth() - ORIGIN_X - END_SPACE;
		xstart = ORIGIN_X;
		xunit = xlength*interval/100;
		
		//prepare local values
		int y = getHeight()-ORIGIN_Y;
		double tickheight = 5;  //the height of the tick marks
		double textwidth = g2.getFontMetrics().stringWidth("99"); //the width of each label
		double availablespace = xunit; //the available space to write such label
		g2.draw(new Line2D.Double(xstart, y, xstart+xlength, y));
		
		//draw the tick marks with appropriate labels
		for(double i = 0; i <= 100/interval; i++){
			//draw the tick
			g2.draw(new Line2D.Double(xstart + i*xunit, y-tickheight, 
					xstart + i*xunit, y+tickheight));
			String label = Integer.toString((int) (i*interval));
			textwidth = g2.getFontMetrics().stringWidth(label);
			
			//if there is enough space, write the hashmark
			if(availablespace > 0){
				g2.drawString(label, (int) ((xstart+i*xunit)-textwidth/2), y+20);
				availablespace = -textwidth/2;
			}
			availablespace += xunit/2;
		}
		
		//if the hashtick 100 wasn't printed on the X axis, force print one
		if(100 % interval != 0 && availablespace > 0){
			g2.draw(new Line2D.Double(xstart + xlength, y-tickheight, xstart + xlength, y+tickheight));
			g2.drawString(Integer.toString(100), (int) (xstart+xlength)-8, y+20);
		}
		
		//draw x-axis label
		String message = "Score";
		textwidth = g2.getFontMetrics().stringWidth(message);
		g2.drawString(message, (int) (xstart + xlength/2 - textwidth/2), 
				y + ORIGIN_Y/2);
	}
	
	/**
	 * Draws the Y Axis of the Histogram with appropriate tick spacing
	 * @param g2 Graphics2D
	 * @param space the number of spaces on the y axis
	 * @param maxf the highest frequency that appears on the y axis
	 */
	public void drawYAxis(Graphics2D g2, int space, double maxf){
		//update instance variables
		ylength = getHeight() - ORIGIN_Y - END_SPACE/2;
		ystart = getHeight()-ORIGIN_Y;
		yunit = ylength/space;
		
		//prepare local variables
		double tickheight = 5;
		
		//draw Y axis
		g2.draw(new Line2D.Double(ORIGIN_X, ystart, ORIGIN_X, ystart-ylength));
		
		int numDigits = numDigits(maxf/space);
		
		//draw the tick and appropriate hashmarks
		for(int i = 1; i <= space; i++){
			g2.draw(new Line2D.Double(ORIGIN_X-tickheight, ystart-yunit*i, 
					ORIGIN_X+tickheight, ystart-yunit*i));
			double val = Math.round(
					maxf*i/space*Math.pow(10, numDigits))/Math.pow(10, numDigits);
			g2.drawString(Double.toString(val), 
					ORIGIN_X - 20 - 8*numDigits, (int) (ystart-yunit*i)+6);
		}
		
		//draw the y-axis label 
		String message = "Frequency";
		double textwidth = g2.getFontMetrics().stringWidth(message);
		float x = ORIGIN_X - 20 - 8*numDigits - 10;
		float y = (float) (ystart - ylength/2 + textwidth/2);
		g2.translate(x, y);
	    g2.rotate(-Math.toRadians(90));
	    g2.drawString(message, 0, 0);
	    g2.rotate(Math.toRadians(90));
	    g2.translate(-x,-y);
	}
	
	/**
	 * This method updates the state of the Graph and calls repaint() to repaint the Graph
	 * @param g The distribution of grades. Each index of the array corresponds to a bin on the 
	 * histogram. The values in the grades refers to the frequency of grades ie [0,.2,.3,.2,.3]. 
	 * The sum of the frequencies should add up to 1.
	 * @param spacing The spacing between ticks on the x-axis (spacing of 10 is 10, 20, ... 100)
	 * @param maxf The highest frequency on the graph
	 * @param div The number of divisions on the y axis (10 divisions list 1.0, 0.9, ... 0.1)
	 */
	public void drawHistogram(int[] g, double xspacing, int xmax, double maxf, int div){
		int roughspace = (int) (100/xspacing);
		if(g.length != roughspace && g.length != roughspace + 1)
			xspacing = 100.0/g.length;
		this.interval = xspacing;
		divisions = div;
		max_frequency = maxf;
		grades = g;
		repaint();
	}
	
	/**
	 * Draws the histogram bars with the appropriate frequency on top
	 * @param g2 The graphics to be passed in 
	 */
	public void drawHistogramBars(Graphics2D g2){
		//prepare local variables
		int numBins = grades.length;
		double max = Calculator.getMax(grades); //find the highest frequency bar
		
		//iterate through all the bins and draw each histogram bar
		for(int i = 0; i < numBins; i++){
			//if the bar height is 0, don't draw it
			if(grades[i] == 0)
				continue;

			//find the color of the bar
			int index = i % 2;
			g2.setColor(colorBar[index]);
			
			//find the text width of every bar frequency
			String num = Integer.toString(grades[i]);
			int font_width = g2.getFontMetrics().stringWidth(num);
			double space = (xunit - font_width)/2.0;

			//fill and draw the bar
			g2.fill(new Rectangle2D.Double(xstart + i*xunit+HIST_SPACE,
					ystart - (grades[i]/max)/1.0*ylength, 
					xunit - HIST_SPACE,
					(grades[i]/max)/1.0*ylength));
			g2.drawString(num, 
					(int) (xstart + i*xunit+HIST_SPACE + space), 
					(int) (ystart - (grades[i]/max)/1.0*ylength - 5));
		}
		
		//print the sum of all frequencies on the bottom
		g2.setColor(Color.black);
		g2.drawString(Integer.toString(Calculator.sum(grades)), (int) (xstart+xlength-20), ystart+50);
		
	}
	
	/**
	 * Gets the number of digits given a double. If the number of digits after the decimal
	 * is greater than 4, then the method caps the digit count at 4
	 * @param num the number to count the digits
	 * @return finds the number of digits in a double
	 */
	private int numDigits(double num){
		String decimal = Double.toString(num);
		int indexdot = decimal.indexOf(".");
		int length = decimal.length() - indexdot - 1;
		return length < 4 ? length : 4;
	}

	/**
	 * Gets the index of the color bar that the point is over.
	 * @param p The point that is over the color bar
	 * @return The index of the color bar array that corresponds to the appropriate bar
	 */
	private int getBarColorIndex(Point p){
		double max = Calculator.getMax(grades);
		
		double x = p.getX();
		x = x - xstart;
		int xbin = (int) (x/xunit);
		
		if(xbin >= grades.length || xbin < 0)
			return -1;
		
		double y = p.getY();
		double ylower = ylength + END_SPACE/2 - (grades[xbin]/max)/max_frequency*ylength;
		double yupper = ylength + END_SPACE/2;
		if(ylower < y && y < yupper){
			return xbin % 2;
		}
		return -1;
	}
	
	/**
	 * Closes the GradientPanel. Closing can be done from the GradientPanel itself or
	 * it can be forcefully closed from the Navigation Panel
	 */
	public void closeGradient(){
		if(gradient != null){
			remove(gradient);
		}
		gradient = null;
		repaint();
	}
	
	private class MouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e) {
			Point pt = e.getPoint();
			int index = getBarColorIndex(pt);
			if(index == -1)
				return;
			if(gradient != null)
				return;
			Insets insets = getInsets();
			gradient = new GradientPanel(colorBar, index, 200);
			gradient.setBounds((int) pt.getX() + insets.right, (int) pt.getY() + insets.top,
					GradientPanel.WIDTH, GradientPanel.HEIGHT);
			gradient.addMoveListener(new MoveHandler());
			add(gradient);
			repaint();
		}
	}
	
	/**
	 * This handling class is designed for GradientPanel. It allows gradient panel
	 * to move around within the window of the Graph panel without accessing all of
	 * Graph panel's methods.
	 * @author sfleischer
	 *
	 */
	private class MoveHandler implements Moveable{

		@Override
		public void move(int dx, int dy) {
			if(gradient == null)
				throw new RuntimeException("Gradient is null?");
			gradient.setBounds(dx - getInsets().right, dy - getInsets().top, 
					GradientPanel.WIDTH, GradientPanel.HEIGHT);
		}

		@Override
		public void close() {
			closeGradient();
		}
		
		public void update(){
			repaint();
		}
	}
}
