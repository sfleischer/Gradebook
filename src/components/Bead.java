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
				(p.y - position.y)*(p.y - position.y) <= RADIUS*RADIUS+2;
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
		int dx = p.x - start.x;
		int dy = p.y - start.y;
		double distance = Math.sqrt(dx*dx + dy*dy);
		int fx = end.x - start.x;
		int fy = end.y - start.y;
		double len = Math.sqrt(fx*fx + fy*fy);
		double cos = (dx * fx + dy * fy)/(distance * len);
		double value = distance * cos;
		if(value < 0){
			position.x = start.x;
			position.y = start.y;
			this.value = 0;
		} else if(value > len){
			position.x = end.x;
			position.y = end.y;
			this.value = max;
		} else {
			position.x = (int) (value * fx / len) + start.x;
			position.y = (int) (value * fy / len) + start.y;
			this.value = max * value / len;
		}
	}
	
	public double getValue(){
		return value;
	}
	
	public void setValue(double v){
		if( v < 0){
			value = 0;
		} else if(v > max){
			value = max;
		} else {
			value = v;
		}
	}
}
