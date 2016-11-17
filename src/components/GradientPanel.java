package components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.geom.*;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GradientPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	Color color;
	Color shade;
	int width;
	Point selected;
	
	List<Moveable> movelist;
	
	//official dimensions of the Gradient Panel
	public static final int WIDTH = 220;
	public static final int HEIGHT = 210;
	public static final int IMAGE_SIZE = 200;
	
	//dimensions of the bar
	private static final int BAR_WIDTH = WIDTH;
	private static final int BAR_HEIGHT = 25;
	
	//when dragging the bar, must keep track of the initial point
	int relative_x = 0; 
	int relative_y = 0; 
	
	//new coordinates
	int px = 0;
	int py = 0;
	
	//array of colors for the slider image
	Color[] array = {Color.red, Color.magenta, Color.blue, Color.cyan, Color.green,
			Color.yellow, Color.red};
	private static final int COLOR_MAX = 600; //max image slider value
	
	public GradientPanel(Color c, int size){
		color = c;
		width = size;
		selected = new Point(200,200);
		movelist = new ArrayList<Moveable>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBackground(new Color(0,0,0,255));
		
		BufferedImage image = getSliderImage(WIDTH - IMAGE_SIZE, HEIGHT - BAR_HEIGHT);
		ImageSlider slider = new ImageSlider(image, COLOR_MAX);
		slider.setBounds(IMAGE_SIZE, BAR_HEIGHT,
				WIDTH - IMAGE_SIZE, HEIGHT - BAR_HEIGHT);
		slider.addChangeListener(new ImageSliderHandler());
		this.add(slider);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setPaint(Color.WHITE);
		g2.fillRect(0,0,getWidth(),getHeight());
		g2.setPaint(Color.black);
		//g2.draw(new Line2D.Double(0, 0, -10, -10));
		//g2.draw(new Line2D.Double(0, 0, 10, 10));
		drawBar(g2, 0, 0, BAR_WIDTH, BAR_HEIGHT);
		g2.drawImage(getGradient(width, color), 0, BAR_HEIGHT, null);
		drawColorSelector(g2);
	}
	
	public void drawColorSelector(Graphics2D g2){
		int radius = 4;
		g2.setPaint(Color.black);
		g2.draw(new Ellipse2D.Double(selected.getX() - 2*radius, 
				selected.getY() - 2*radius , 2*radius, 2*radius));
		radius = radius - 1;
		g2.setPaint(new Color(255,255,255,200));
		g2.draw(new Ellipse2D.Double(selected.getX() - 2*radius - 1, 
				selected.getY() - 2*radius - 1, 2*radius, 2*radius));
	}
	
	public void drawBar(Graphics2D g2, int startx, int starty, int width, int height){
		g2.setPaint(new Color(230,230,230));
		g2.fillRect(startx, starty, width, height);
		int h = g2.getFont().getSize();
		int space = height - h;
		space = space / 2;
		
		String text = "Color changer";
		int w = g2.getFontMetrics().stringWidth(text);
		g2.setPaint(Color.black);
		g2.drawString(text, startx + width / 2 - w/2, starty + height - space);
	}
	
	public BufferedImage getGradient(int width, Color colo){
		BufferedImage result = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = result.createGraphics();
		GradientPaint paint = new GradientPaint(0f, 0f, Color.WHITE, width, 0f, colo);
		GradientPaint shade = new GradientPaint(0f,0f, new Color(0,0,0,0), 0f, width,
				new Color(0,0,0,255));
		g2.setPaint(paint);
		g2.fillRect(0, 0, width, width);
		g2.setPaint(shade);
		g2.fillRect(0, 0, width, width);
		
		g2.dispose();
		return result;
	}
	
	/**
	 * Creates a color gradient for the slider. The colors gradient from
	 * red (255, 0, 0) -> magenta (255, 0, 255) -> blue (0, 0, 255) ->
	 * cyan (0, 255, 255) -> green (0, 255, 0) -> yellow (255, 255, 0) ->
	 * red (255, 0, 0).
	 * The image is oriented vertically.
	 * 
	 * @param width The width of the slider image
	 * @param height The height of the slider image
	 * @return The slider image
	 */
	public BufferedImage getSliderImage(int width, int height){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		
		Graphics2D g2 = image.createGraphics();
		int yunit = height / (array.length-1);
		for(int i = 0; i < array.length - 1; i++){
			GradientPaint paint = new GradientPaint
					(0f, yunit * i, array[i], 0f, yunit * (i + 1), array[i+1]);
			g2.setPaint(paint);
			g2.fillRect(0, yunit * i, width, yunit * (i+1));
		}
		
		g2.dispose();
		return image;
	}
	
	public void addMoveListener(Moveable m){
		movelist.add(m);
	}
	
	/**
	 * Moves this panel around in the Graph JPanel
	 */
	public void movePanel(){
		for(Moveable m : movelist){
			m.move(px, py);
		}
	}
	
	/**
	 * Closes this panel in the Graph JPanel
	 */
	public void closePanel(){
		for(Moveable m : movelist){
			m.close();
		}
	}
	
	public void setColor(Color c){
		color = c;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		relative_x = e.getX();
		relative_y = e.getY();
		
		if(relative_y > BAR_HEIGHT)
			selected = e.getPoint();
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		relative_x = e.getX();
		relative_y = e.getY();
		
		if(relative_y > BAR_HEIGHT)
			selected = e.getPoint();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		if(relative_y > BAR_HEIGHT) {
			selected = e.getPoint();
		} else{
			Rectangle rect = this.getBounds();
			px = rect.x + e.getX() - relative_x;
			py = rect.y + e.getY() - relative_y;
			movePanel();
		}
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class ImageSliderHandler implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			ColorSlider s = (ColorSlider) e.getSource();
			double value = s.getValue();
			int index = (int) (value / 100);
			double weight2 = (value - index * 100) / 100;
			double weight1 = 1 - weight2;
			System.out.println("weight1 " + weight1);
			int red = (int) (array[index].getRed()*weight1 
					+ array[index+1].getRed()*weight2);
			int green = (int) (array[index].getGreen()*weight1 
					+ array[index+1].getGreen()*weight2);
			int blue = (int) (array[index].getBlue()*weight1 
					+ array[index+1].getBlue()*weight2);
			color = new Color(red, green, blue);
			repaint();
		}
		
	}

}
