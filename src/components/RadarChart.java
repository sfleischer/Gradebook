package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RadarChart extends JPanel{
	int vertices;
	String[] labels;
	double[] values; //the values of the beads
	double[] defaults; //the default values of the beads
	int max;
	int radius;
	
	List<Bead> beads = new LinkedList<Bead>();
	Bead selected = null; //the selected bead
	
	public RadarChart(String[] labels, double[] values, int max){
		this.labels = labels;
		this.values = values;
		this.max = max;
		defaults = new double[values.length];
		vertices = labels.length;
		this.setPreferredSize(new Dimension(300,300));
		this.setMinimumSize(new Dimension(300,300));
		radius = 4;
		
		//initialize the beads
		for(int i = 0; i < values.length; i++){
			beads.add(new Bead(values[i], max));
			defaults[i] = values[i];
		}
		
		//set the mouse listeners
		BeadListener bl = new BeadListener();
		this.addMouseListener(bl);
		this.addMouseMotionListener(bl);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JButton button = new JButton("restore defaults");
		button.setPreferredSize(new Dimension(150,30));
		//clicking the button will reset the values to default
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i = 0; i < values.length; i++){
					beads.get(i).setValue(defaults[i]);
					values[i] = defaults[i];
				}
				repaint();
			}
			//comment
		});
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel cont = new JPanel();
		cont.setPreferredSize(new Dimension(300,30));
		cont.setBackground(new Color(255,255,255,0));
		cont.setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
		cont.add(Box.createHorizontalGlue());
		cont.add(button);
		cont.add(Box.createHorizontalGlue());
		this.add(Box.createVerticalGlue());
		this.add(cont);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		drawRadarChart(g2);
	}
	
	/**
	 * Draws the radar chart by preparing values to pass to drawAxes and
	 * drawBeads mehods
	 * @param g2
	 */
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
	
	/**
	 * Draws the axes of the radar chart
	 * @param g2
	 * @param x The x position of the center of the radar chart
	 * @param y The y position of the center of the radar chart 
	 * @param len The length of one arm
	 * @param space The space between the arm and label text
	 * @param hashwidth The width of the hashmarks
	 */
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
			values[i] = beads.get(i).getValue();
			double ratio = (1.0 * beads.get(i).getValue()) / max; //find the ratio up the arm of the bead
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
	
	public double[] getValues(){
		return values;
	}

	private class BeadListener extends MouseAdapter
	{
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(selected == null){
				return;
			}
			selected.updatePosition(e.getPoint());
			repaint();
		}
	
	
		@Override
		public void mousePressed(MouseEvent e) {
			for(Bead b : beads){
				if(b.isPointOnBead(e.getPoint())){
					selected = b;
					return;
				}
			}
			selected = null;
		}
	}
	

}


