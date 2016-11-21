import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FitnessGraph extends JPanel{
	int[] fitchart;
	int[] weakchart;
	
	public static final int ORIGIN_X = 50;
	public static final int ORIGIN_Y = 50;
	
	int xlength;
	int xstart;
	int ylength;
	int ystart;
	
	public FitnessGraph(int[] fit, int[] weak){
		fitchart = fit;
		weakchart = weak;
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		
		ylength = getHeight() - 2*ORIGIN_Y;
		ystart = getHeight() - ORIGIN_Y;
		xlength = getWidth() - 2*ORIGIN_X;
		xstart = ORIGIN_X;
		g2.draw(new Line2D.Double(xstart, ystart, xstart+xlength, ystart));
		g2.draw(new Line2D.Double(xstart, ystart, xstart, ystart-ylength));
		
		drawGraph(g2);
		
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
	}
	
}
