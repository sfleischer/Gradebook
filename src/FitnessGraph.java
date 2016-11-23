import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FitnessGraph extends JPanel{
	int[] fitchart;
	int[] weakchart;
	int threshold; //show the threshold value
	
	public static final int ORIGIN_X = 70;
	public static final int ORIGIN_Y = 70;
	
	int xlength;
	int xstart;
	int ylength;
	int ystart;
	
	public FitnessGraph(int[] fit, int[] weak, int threshold){
		fitchart = fit;
		weakchart = weak;
		this.threshold = threshold;
		this.setPreferredSize(new Dimension(600, 500));
	}
	
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
	
}
