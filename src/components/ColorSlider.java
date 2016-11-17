package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ColorSlider extends JPanel implements Slidable{
	
	List<ChangeListener> list = new ArrayList<ChangeListener>();
	
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	
	protected Color base;
	protected String label;
	protected double value;
	protected double max;
	protected int orientation;
	
	public ColorSlider(Color c, String title, double max){
		base = c;
		value = 50;
		label = title;
		this.max = max;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		int x = getWidth();
		int y = getHeight();
		int height = g2.getFont().getSize();
		int space = getHeight() - height;
		space = space / 2;
		g2.drawString(label + " " + value, 0, getHeight() - space);
		//draw filling
		g2.setPaint(base);
		g2.fill(new Rectangle2D.Double(x/2, 3, value/max * x/2, y-3));
		
		//draw border
		g2.setPaint(Color.black);
		g2.draw(new Rectangle2D.Double(x/2, 3, x/2, y-3));
		
	}
	
	public void addChangeListener(ChangeListener l){
		list.add(l);
	}
	
	public void dispatchEvent(){
		for(ChangeListener cl : list){
			cl.stateChanged(new ChangeEvent(this));
		}
	}

	public void handleChange(MouseEvent e){
		double px = e.getX();
		int x = getWidth();
		if(px > x/2 && px <= x){
			value = (max * (2*(px - x/2)/x));
			value = Math.round(value*100)/100.0;
			repaint();
			dispatchEvent();
		} else if(px <= x/2)
			value = 0;
	}
	
	public double getValue(){
		return value;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		 handleChange(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		handleChange(e);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		handleChange(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		handleChange(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
