import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import components.Moveable;
import components.*;

public class Graph extends JPanel implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7321566779811779800L;
	
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
		
		this.addMouseListener(this);
		this.setLayout(null);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		drawXAxis(g2, interval);
		drawYAxis(g2, divisions, max_frequency);
		drawHistogramBars(g2);
	}
	
	/**
	 * 
	 * @param g2 Graphics2D
	 * @param interval The spacing between grades on the histogram
	 */
	public void drawXAxis(Graphics2D g2, double interval){
		xlength = getWidth() - ORIGIN_X - END_SPACE;
		xstart = ORIGIN_X;
		int y = getHeight()-ORIGIN_Y;
		xunit = xlength*interval/100;
		double tickheight = 5;
		g2.draw(new Line2D.Double(xstart, y, xstart+xlength, y));
		
		for(double i = 0; i <= 100/interval; i++){
			g2.draw(new Line2D.Double(xstart + i*xunit, y-tickheight, 
					xstart + i*xunit, y+tickheight));
			g2.drawString(Integer.toString((int) (i*interval)), (int) (xstart+i*xunit)-8, y+20);
		}
		if(100 % interval != 0){
			g2.draw(new Line2D.Double(xstart + xlength, y-tickheight, xstart + xlength, y+tickheight));
			g2.drawString(Integer.toString(100), (int) (xstart+xlength)-8, y+20);
		}
	}
	
	/**
	 * 
	 * @param g2 Graphics2D
	 * @param space the number of spaces on the y axis
	 * @param maxf the highest frequency that appears on the y axis
	 */
	public void drawYAxis(Graphics2D g2, int space, double maxf){
		ylength = getHeight() - ORIGIN_Y - END_SPACE/2;
		ystart = getHeight()-ORIGIN_Y;
		int x = ORIGIN_X;
		yunit = ylength/space;
		double tickheight = 5;
		g2.draw(new Line2D.Double(x, ystart, x, ystart-ylength));
		
		int numDigits = numDigits(maxf/space);
		//System.out.println(""+numDigits);
		for(int i = 1; i <= space; i++){
			g2.draw(new Line2D.Double(x-tickheight, ystart-yunit*i, x+tickheight, ystart-yunit*i));
			double val = Math.round(maxf*i/space*Math.pow(10, numDigits))/Math.pow(10, numDigits);
			g2.drawString(Double.toString(val), x - 20 - 8*numDigits, (int) (ystart-yunit*i)+6);
		}
	}
	
	/**
	 * 
	 * @param g The distribution of grades. Each index of the array corresponds to a bin on the 
	 * histogram. The values in the grades refers to the frequency of grades ie [0,.2,.3,.2,.3]. 
	 * The sum of the frequencies should add up to 1.
	 * @param spacing
	 * @param maxf The highest frequency on the graph
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
	
	public void drawHistogramBars(Graphics2D g2){
		int numBins = grades.length;
		double max = maxval(grades);
		for(int i = 0; i < numBins; i++){
			if(grades[i] == 0)
				continue;

			
			int index = i % 2;
			g2.setColor(colorBar[index]);

			g2.fill(new Rectangle2D.Double(xstart + i*xunit+HIST_SPACE,
					ystart - (grades[i]/max)/max_frequency*ylength, 
					xunit - HIST_SPACE,
					(grades[i]/max)/max_frequency*ylength));
			g2.drawString(Integer.toString(grades[i]), 
					(int) (xstart + (i+.2)*xunit+HIST_SPACE), 
					(int) (ystart - (grades[i]/max)/max_frequency*ylength - 5));
			//g2.drawString(Integer.toString((int) (i*interval)), (int) (xstart+i*xunit)-8, ystart+20);
		}
		g2.setColor(Color.black);
		g2.drawString(Integer.toString(sum(grades)), (int) (xstart+xlength-20), ystart+50);
		
	}
	
	/*
	public void setBarColor1(Color b){
		bar1 = b;
		repaint();
	}
	
	public void setBarColor2(Color b){
		bar2 = b;
		repaint();
	}
	*/
	
	private int maxval(int[] array){
		int max = array[0];
		for(int n : array){
			max = Math.max(max, n);
		}
		return max;
	}
	
	private int sum(int[] array){
		int total = 0;
		for(int n : array){
			total += n;
		}
		return total;
	}
	
	/**
	 * 
	 * @param num
	 * @return finds the number of digits in a double
	 */
	private int numDigits(double num){
		String decimal = Double.toString(num);
		int indexdot = decimal.indexOf(".");
		int length = decimal.length() - indexdot - 1;
		return length < 4 ? length : 4;
	}

	private int getBarColorIndex(Point p){
		int numBins = grades.length;
		double max = maxval(grades);
		
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
		return 0;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//getBar(e.getPoint());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point pt = e.getPoint();
		int index = getBarColorIndex(pt);
		if(index == -1)
			return;
		if(gradient != null)
			return;
		Insets insets = this.getInsets();
		gradient = new GradientPanel(colorBar, index, 200);
		gradient.setBounds((int) pt.getX() + insets.right, (int) pt.getY() + insets.top,
				GradientPanel.WIDTH, GradientPanel.HEIGHT);
		gradient.addMoveListener(new MoveHandler());
		this.add(gradient);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	private class MoveHandler implements Moveable{

		@Override
		public void move(int dx, int dy) {
			if(gradient == null)
				throw new RuntimeException("Gradient is null?");
			//Rectangle rect = gradient.getBounds();
			gradient.setBounds(dx - getInsets().right, dy - getInsets().top, 
					GradientPanel.WIDTH, GradientPanel.HEIGHT);
		}

		@Override
		public void close() {
			remove(gradient);
			gradient = null;
			repaint();
		}
		
		public void update(){
			repaint();
		}
	}
}
