package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class CloseButton extends JPanel implements MouseListener {

	ArrayList<ActionListener> list;
	String actionCommand;
	boolean depressed;
	
	private static int SPACING = 2;
	
	
	public CloseButton(){
		actionCommand = "default";
		depressed = false;
		list = new ArrayList<ActionListener>();
		this.addMouseListener(this);
	}
	
	public CloseButton(String action){
		actionCommand = action;
		depressed = false;
		this.addMouseListener(this);
	}
	
	public void addActionListener(ActionListener cl){
		list.add(cl);	
	}
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.red);
		
		Rectangle rect=  new Rectangle(SPACING, SPACING, 
				getWidth() - 2*SPACING, getHeight() - 2*SPACING);
		g2.fill(rect);
		
		g2.setStroke(new BasicStroke(3));
		g2.setPaint(Color.WHITE);
		double x = 3 * SPACING;
		double y = 3 * SPACING;
		double xf = getWidth() - x;
		double yf = getHeight() - y;
		g2.draw(new Line2D.Double(x, y, xf, yf));
		g2.draw(new Line2D.Double(x, yf, xf, y));
		g2.setStroke(new BasicStroke(1));
		
		g2.setColor(Color.black);
		g2.draw(rect);
		
		Color shade = new Color(0,0,0,100);
		if(depressed){
			g2.setPaint(shade);
			g2.fill(rect);
		}
	}
	
	
	public void dispatchEvent(int type){
		for(ActionListener al : list){
			al.actionPerformed(new ActionEvent(this, type, actionCommand));
		}
	}
	
	
	public boolean getDepressed(){
		return depressed;
	}
	
	public String getActionCommand(){
		return actionCommand;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		depressed = true;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		depressed = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Rectangle rect = this.getVisibleRect();
		if(rect.contains(e.getPoint()))
			dispatchEvent(3);
		depressed = false;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}