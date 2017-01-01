package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class takes an image as an argument and creates a slider over it.
 * @author sfleischer
 * @version created 11/17/16
 */
@SuppressWarnings("serial")
public class ImageSlider extends JPanel implements MouseListener, MouseMotionListener{
	
	List<ChangeListener> list = new ArrayList<ChangeListener>();
	private double value;
	private double max;
	
	private BufferedImage image;
	//double value;
	
	public ImageSlider(BufferedImage im, double val, double max) {
		this.max = max;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		image = im;
		value = val;
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, null, 0, 0);
		drawPointer(g2);
	}
	
	
	
	private void drawPointer(Graphics2D g2){
		int y = (int) ((value / max) * getHeight());
		int x = getWidth()/2;
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(x, y);
		path.lineTo(x * 2, y + x/2);
		path.lineTo(x * 2, y - x/2);
		path.closePath();
		
		g2.setColor(Color.black);
		g2.draw(path);
		g2.setPaint(Color.white);
		g2.fill(path);
	}
	
	
	public void handleChange(MouseEvent e){
		double py = e.getY();
		if(py > getHeight() || py < 0)
			return;
		value = (py / getHeight()) * max;
		//System.out.println("im value: " + value);
		//System.out.println(value);
		dispatchEvent();
		repaint();
	}
	
	public void dispatchEvent(){
		for(ChangeListener cl : list){
			cl.stateChanged(new ChangeEvent(this));
		}
	}
	
	public double getValue(){
		return value;
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

	@Override
	public void mouseDragged(MouseEvent e) {
		handleChange(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void addChangeListener(ChangeListener l){
		list.add(l);
	}
	
	

}
