import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FitnessGraph extends JPanel implements MouseMotionListener{
	int[] fitchart;
	int[] weakchart;
	int threshold; //show the threshold value
	
	public static final int ORIGIN_X = 70;
	public static final int ORIGIN_Y = 70;
	
	int xlength;
	int xstart;
	int ylength;
	int ystart;
	
	FitnessLabel label;
	
	public FitnessGraph(int[] fit, int[] weak, int threshold){
		fitchart = fit;
		weakchart = weak;
		label = new FitnessLabel();
		this.threshold = threshold;
		this.setPreferredSize(new Dimension(600, 500));
		this.addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		
		ylength = getHeight() - 2*ORIGIN_Y;
		ystart = getHeight() - ORIGIN_Y;
		xlength = getWidth() - 2*ORIGIN_X;
		xstart = ORIGIN_X;
		
		drawXAxis(g2);
		drawYAxis(g2);
		
		drawGraph(g2);
		
		//label.drawLabel(g2);
	}
	
	public void drawXAxis(Graphics2D g2){
		double xunit = xlength/5.0;
		double textwidth = 0;
		int interval = fitchart.length/5;
		int tickheight = 5;
		double availablespace = xstart;
		
		g2.draw(new Line2D.Double(xstart, ystart, xstart+xlength, ystart));
		for(double i = 0; i <= fitchart.length/interval; i++){
			//draw the tick
			g2.draw(new Line2D.Double(xstart + i*xunit, ystart-tickheight, 
					xstart + i*xunit, ystart+tickheight));
			String label = Integer.toString((int) (i*interval));
			textwidth = g2.getFontMetrics().stringWidth(label);
			
			//if there is enough space, write the hashmark
			if(availablespace > 0){
				g2.drawString(label, (int) ((xstart+i*xunit)-textwidth/2), ystart+20);
				availablespace = -textwidth/2;
			}
			availablespace += xunit/2;
		}
		
		//draw x axis label
		String message = "Generations";
		textwidth = g2.getFontMetrics().stringWidth(message);
		g2.drawString("Generations", (int) (xstart + xlength/2 - textwidth/2), 
				ystart + ORIGIN_Y/2 + 10);
	}
	
	public void drawYAxis(Graphics2D g2){
		double yunit = ylength/5.0;
		double textwidth = 0;
		int interval = 1000/5;
		int tickwidth = 5;
		//double availablespace = xstart;
		
		g2.draw(new Line2D.Double(xstart, ystart, xstart, ystart - ylength));
		for(double i = 0; i <= 1000/interval; i++){
			//draw the tick
			g2.draw(new Line2D.Double(xstart - tickwidth, ystart - i*yunit,
					xstart + tickwidth, ystart - i*yunit));
			String label = Integer.toString((int) (i*interval));
			textwidth = g2.getFontMetrics().stringWidth(label);
			g2.drawString(label, (int) (xstart - textwidth - 10), 
					(int) (ystart - i*yunit + 5));
		}
		
		//draw y axis label 
		String message = "Fitness";
		textwidth = g2.getFontMetrics().stringWidth(message);
		float x = xstart - ORIGIN_X/2;
		float y = (float) (ystart - ylength/2 + textwidth/2);
		g2.translate(x, y);
	    g2.rotate(-Math.toRadians(90));
	    g2.drawString(message, 0, 0);
	    g2.rotate(Math.toRadians(90));
	    g2.translate(-x,-y);
	}
	
	
	public void drawGraph(Graphics2D g2){
		double xunit = 1.0*xlength/fitchart.length;
		double yunit = ylength/1000.0; //fitness max
		
		g2.setColor(Color.black);
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(xstart, ystart-yunit*fitchart[0]);
		for(int i = 0; i < fitchart.length; i++){
			path.lineTo(xstart + xunit*i, ystart - yunit*fitchart[i]);
		}
		g2.draw(path);
		
		g2.setColor(Color.red);
		GeneralPath path2 = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path2.moveTo(xstart, ystart-yunit*weakchart[0]);
		for(int i = 0; i < weakchart.length; i++){
			path2.lineTo(xstart + xunit*i, ystart - yunit*weakchart[i]);
		}
		g2.draw(path2);
		
		g2.setColor(Color.blue);
		Font old = g2.getFont();
		g2.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		String text = "Threshold: " + threshold;
		int font_width = g2.getFontMetrics().stringWidth(text);
		g2.drawLine(xstart, (int) (ystart-yunit*threshold),
				xstart + xlength, (int) (ystart-yunit*threshold));
		g2.drawString(text, xstart + xlength - font_width, 
				(int) (ystart-yunit*threshold) - 1);
		g2.setFont(old);
	}
	
	
	public int getGenerationsFromX(double x){
		double p = x - xstart;
		p =  p / xlength * fitchart.length;
		if(p < 0){
			p = 0;
		} else if(p > fitchart.length - 1){
			p = fitchart.length - 1;
		}
		return (int) p;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		//get the generation number
		int gen = getGenerationsFromX(e.getX());
		//prepare the x position
		int x = e.getX();
		if(x < xstart)
			x = xstart;
		else if(x > xstart + xlength)
			x = xstart + xlength;
		
		//determine what fitchart and Color to use
		int[] chart;
		Color c;
		int yfit = ystart - ylength * fitchart[gen] / 1000;
		int yweak = ystart - ylength * weakchart[gen] / 1000;
		if(Math.abs(yfit - e.getY()) < Math.abs(yweak - e.getY())){
			chart = fitchart;
			c = Color.BLACK;
		} else {
			chart = weakchart;
			c = Color.RED;
		}
		
		//now find the y value
		int y = ystart - ylength * chart[gen] / 1000;
		
		//finally find the slope
		double slope = 0;
		if(gen == 0)
			slope = chart[gen + 1] - chart[gen];
		else if(gen == chart.length - 1)
			slope = chart[gen] - chart[gen - 1];
		else
			slope = chart[gen + 1] - chart[gen - 1];
		
		//update the label and repaint
		label.updateLabel(x, y, gen, chart[gen], slope, c);
		repaint();
	}
	
}
