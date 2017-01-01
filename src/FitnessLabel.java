import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class FitnessLabel {
	
	public static final int RADIUS = 2;
	int generation;
	int fitness;
	int x;
	int y;
	double slope;
	Color color;
	
	public void drawLabel(Graphics2D g2){
		g2.setColor(color);
		g2.fillOval(x - RADIUS, y - RADIUS, 2*RADIUS, 2*RADIUS);
		
		g2.setColor(Color.black);
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(x + 2, y);
		path.lineTo(x + 20, y - 5);
		path.lineTo(x + 20,  y - 30);
		path.lineTo(x + 120, y - 30);
		path.lineTo(x + 120, y + 30);
		path.lineTo(x + 20, y + 30);
		path.lineTo(x + 20, y + 5);
		path.lineTo(x + 2, y);
		path.closePath();
		g2.setPaint(Color.LIGHT_GRAY);
		g2.fill(path);
	}
	
	public void updateLabel(int x, int y, int gen, int fit, double slope, Color c){
		this.x = x;
		this.y = y;
		generation = gen;
		fitness = fit;
		this.slope = slope;
		this.color = c;
	}
}
