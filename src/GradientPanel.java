import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GradientPanel extends JPanel{
	
	Color corner1;
	Color corner2;
	Color shade;
	int width;
	
	public GradientPanel(Color c, int size){
		corner1 = Color.white;
		corner2 = c;
		width = size;
	}
	
	public BufferedImage getGradient(){
		BufferedImage result = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = result.createGraphics();
		GradientPaint paint = new GradientPaint(0f, 0f, corner1, width, 0f, corner2);
		GradientPaint shade = new GradientPaint(0f,0f, new Color(0,0,0,0), 0f, width,
				new Color(0,0,0,255));
		g2.setPaint(paint);
		g2.fillRect(0, 0, width, width);
		g2.setPaint(shade);
		g2.fillRect(0, 0, width, width);
		
		g2.dispose();
		return result;
	}
	
	public void setColor(Color c){
		corner2 = c;
	}

}
