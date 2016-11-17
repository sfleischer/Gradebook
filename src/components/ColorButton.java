package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class ColorButton extends JPanel implements MouseListener {

	ArrayList<ActionListener> list = new ArrayList<ActionListener>();
	String actionCommand;
	Color color;
	boolean selected;
	
	
	public ColorButton(Color c){
		actionCommand = "default";
		color = c;
		selected = false;
		this.addMouseListener(this);
	}
	
	public ColorButton(Color c, String action){
		color = c;
		actionCommand = action;
		selected = false;
		this.addMouseListener(this);
	}
	
	public void addActionListener(ActionListener cl){
		list.add(cl);
		
	}
	
	public void updateColor(int red, int green, int blue){
		color = new Color(red, green, blue);
		repaint();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fill(new Rectangle2D.Double(2.5,2.5,getWidth()-5, getHeight()-5));
		
		if(selected){
			g2.setColor(new Color(180,180,255));
			g2.setStroke(new BasicStroke(5));
			g2.draw(new Rectangle2D.Double(0,0,getWidth(), getHeight()));
			g2.setStroke(new BasicStroke(1));
		}
	}
	
	public void dispatchEvent(int type){
		for(ActionListener al : list){
			al.actionPerformed(new ActionEvent(this, type, actionCommand));
		}
	}
	
	public void setSelected(boolean b){
		selected = b;
		repaint();
	}
	
	public Color getColor(){
		return color;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	public String getActionCommand(){
		return actionCommand;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		dispatchEvent(1);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		dispatchEvent(2);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dispatchEvent(3);

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
