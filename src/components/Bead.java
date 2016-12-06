package components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Bead {

	public static final int RADIUS = 4;
	Color color;
	
	Point position;
	Point start;
	Point end;
	
	double value; //the value of the bead
	double max; //the maximum value of the bead (would be 5)
	
	public Bead(double value, double max){
		this.value = value;
		this.max = max;
		position = new Point(0,0);
		start = new Point(0,0);
		end = new Point(0,0);
	}
	
	public void draw(Graphics2D g2){
		g2.setPaint(color);
		g2.fill(new Ellipse2D.Double(position.x - RADIUS, position.y - RADIUS, RADIUS * 2, RADIUS * 2));
	}
	
	public boolean isPointOnBead(Point p){
		return (p.x - position.x)*(p.x - position.x) + 
				(p.y - position.y)*(p.y - position.y) < RADIUS*RADIUS;
	}
	
	/**
	 * Calibrate the bead so that it can only go along the start of the rail to the end
	 * @param start The position of the start of the bead rail
	 * @param end The poistion of the end of the bead rail
	 */
	public void calibratePosition(Point start, Point end){
		this.start = start;
		this.end = end;
	}
	
	public void updatePosition(Point p){
		/*if(p.x > end.x || p.x < end.x)
			position.x = end.x;
		else if(p.x > start.x || p.y < start.x)
			position.x = start.x;
		else*/
			position.x = p.x;
		
		/*if(p.y > end.y || p.y < end.y)
			position.y = end.y;
		else if(p.y > start.y || p.y < start.y)
			position.y = start.y;
		else*/
			position.y = p.y;
	}
}
