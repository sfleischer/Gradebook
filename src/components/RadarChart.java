package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RadarChart extends JPanel{
	int vertices;
	String[] labels;
	int[] values;
	int max;
	int radius;
	
	List<Bead> beads = new ArrayList<Bead>();
	Bead selected = null; //the selected bead
	
	public RadarChart(String[] labels, int[] values, int max){
		this.labels = labels;
		this.values = values;
		this.max = max;
		vertices = labels.length;
		this.setPreferredSize(new Dimension(300,300));
		radius = 4;
		
		//initialize the beads
		for(int i = 0; i < values.length; i++){
			beads.add(new Bead(values[i], max));
		}
		
		//set the mouse listeners
		BeadListener bl = new BeadListener();
		this.addMouseListener(bl);
		this.addMouseMotionListener(bl);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		System.out.println(getWidth());
		drawRadarChart(g2);
	}
	
	public void drawRadarChart(Graphics2D g2){
		int textheight = g2.getFontMetrics().getHeight(); //height of the text
		int textwidth = g2.getFontMetrics().stringWidth("rando"); //text width
		int space = 5; //spacing between the line and the label
		int hashwidth = 4; //hashmark width
		int x = getWidth()/2; //center of the radar chart
		int y = getHeight()/2; //center of the radar chart
		int len = Math.min(x  - textwidth, y - textheight) - space; //length of an arm
		
		drawAxes(g2, x, y, len, space, hashwidth);
		drawBeads(g2, x, y, len);
	}
	
	public void drawAxes(Graphics2D g2, int x, int y, int len, int space, int hashwidth){
		//prepare variables
		int textwidth = 0;
		int textheight = 0;
		
		//now draw the graph
		g2.setColor(Color.black);
		for(int i = 0; i < vertices; i++){
			//draw the arm first
			double angle = Math.PI/2 + 2*Math.PI / vertices * i;
			double px = x + len * Math.cos(angle);
			double py = y - len * Math.sin(angle);
			g2.draw(new Line2D.Double(x, y, px, py));
			beads.get(i).calibratePosition(new Point(x, y), new Point((int) px, (int) py));
			
			//draw hashmarks
			for(int j = 1; j <= max; j++){
				double ratio = (1.0 * j) / max;
				double bx = x + ratio * len * Math.cos(angle); //base point of hash mark
				double by = y - ratio * len * Math.sin(angle); //base point of hash mark
				double h1x = bx + hashwidth * Math.sin(angle); //start x (note sin/cos swap)
				double h2x = bx - hashwidth * Math.sin(angle); //end x
				double h1y = by + hashwidth * Math.cos(angle); //start y
				double h2y = by - hashwidth * Math.cos(angle); //end y
				g2.draw(new Line2D.Double(h1x, h1y, h2x, h2y));
			}
			
			//draw the label next
			textwidth = px > x ? space : g2.getFontMetrics().stringWidth(labels[i]) + space;
			textheight = py > y ? g2.getFontMetrics().getHeight() +space : space;
			double tx = x + (len + textwidth) * Math.cos(angle);
			double ty = y - (len + textheight) * Math.sin(angle);
			g2.drawString(labels[i], (int) tx, (int) ty); 
		}
	}
	
	
	public void drawBeads(Graphics2D g2, int x, int y, int len){
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		g2.setColor(Color.blue);
		for(int i = 0; i < vertices; i++){
			double angle = Math.PI/2 + 2*Math.PI / vertices * i;
			double ratio = (1.0 * values[i]) / max; //find the ratio up the arm of the bead
			double bx = x + ratio * len * Math.cos(angle); //base point of bead
			double by = y - ratio * len * Math.sin(angle); //base point of bead
			beads.get(i).updatePosition(new Point((int) bx, (int) by));
			beads.get(i).draw(g2);
			//g2.fill(new Ellipse2D.Double(bx - radius, by - radius, radius*2, radius*2));
			
			if(i == 0)
				path.moveTo((float) bx, (float) by);
			else
				path.lineTo((float) bx, (float) by);
		}
		g2.setColor(new Color(0, 0, 255, 120));
		path.closePath();
		g2.fill(path);
	}

	private class BeadListener extends MouseAdapter
	{
		int px; //the point where the first valid click was made
		int py; //the point where the first valid click was made
		
		@Override
		public void mouseDragged(MouseEvent e) {
			
			
		}
	
		
		@Override
		public void mouseClicked(MouseEvent e) {
			for(Bead b : beads){
				if(b.isPointOnBead(e.getPoint())){
					selected = b;
					break;
				}
			}
			px = e.getX();
			py = e.getY();
		}
	
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	

	}
	
	public class Vector{
		public final double x;
		public final double y;
		
		public Vector(double x, double y){
			this.x = x;
			this.y = y;
		}
		
		public double dot(Vector v){
			return x * v.x + y * v.y;
		}
		
		public double getMagnitude(){
			return Math.sqrt(x*x + y*y);
		}
		
		public Vector getNormal(){
			return new Vector(-y, x);
		}
		
		public double findDistanceToPoint(Point p){
			Vector n = this.getNormal();
			return this.dot(n)/n.getMagnitude();
		}
	}

	

}


