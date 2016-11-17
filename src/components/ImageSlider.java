package components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class takes an image as an argument and creates a slider over it.
 * @author sfleischer
 * @version created 11/17/16
 */
public class ImageSlider extends ColorSlider{
	
	BufferedImage image;
	
	public ImageSlider(BufferedImage im, double max) {
		super(null, "", max);
		image = im;
	}

	int width;
	int height;
	double value;
	
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
	
	
	@Override
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
	
	@Override
	public double getValue(){
		return value;
	}

}
