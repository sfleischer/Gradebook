package components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Bead {

	public static final int RADIUS = 4;
	Color color;
	//the position must be on the same line as the initial and final points
	double px; //position x
	double py; //position y
	double ix; //initial scaling x (where the chart starts)
	double iy; //initial y
	double fx; //final x
	double fy; //final y
	
	double value; //the value of the bead
	
	public Bead(double positionx, double positiony, double initialx, double initialy,
			double finalx, double finaly, double value){
		px = positionx;
		py = positiony;
		ix = initialx;
		iy = initialy;
		fx = finalx;
		fy = finaly;
		this.value = value;
		color = Color.blue;
	}
	
	public void draw(Graphics2D g2){
		g2.setPaint(color);
		g2.draw(new Ellipse2D.Double(px - RADIUS, py - RADIUS, RADIUS * 2, RADIUS * 2));
	}
	
	public boolean isPointOnBead(Point p){
		return (p.x - px)*(p.x - px) + (p.y - py)*(p.y - py) < RADIUS*RADIUS;
	}
	
	public void updatePosition(Point p){
		if(p.x > fx || p.x < fx)
			px = fx;
		else if(p.x > ix || px < ix)
			px = ix;
		else
			px = p.x;
		
		if(p.y > fy || p.y < fy)
			py = fy;
		else if(p.y > iy || py < iy)
			py = iy;
		else
			py = p.y;
		
	}
}
