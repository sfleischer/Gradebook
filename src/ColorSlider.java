import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ColorSlider extends JPanel implements MouseListener, MouseMotionListener {
	
	ArrayList<ChangeListener> list = new ArrayList<ChangeListener>();
	
	Color base;
	int value;
	int x;
	int y;
	
	public ColorSlider(Color c){
		base = c;
		value = 50;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		x = getWidth();
		y = getHeight();
		
		//draw filling
		g2.setPaint(base);
		g2.fill(new Rectangle2D.Double(0,0,value/255.0 * x, y));
		
		//draw border
		g2.setPaint(Color.black);
		g2.draw(new Rectangle2D.Double(0,0,x,y));
		
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
		value = (int) (255 * (px/x));
		repaint();
		dispatchEvent();
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
