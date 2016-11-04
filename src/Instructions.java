import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Instructions extends JPanel implements ActionListener{
	String message = "Welcome to Gradebook! Here you can find what "
			+ "percentile you fall under on any of your exams. "
			+ "To enter exam statistics, click on the stats tab and "
			+ "enter the appropriate statistics. Note that the "
			+ "distribution generator only considers whole number "
			+ "minimum, maximum, and median scores. Also entering "
			+ "characters instead of numbers automatically replaces"
			+ "user input with default values. Below is the default" 
			+ "value for each statistic.";
			
	String stats = "Standard Deviation : 10 \n"
			+ "Mean : 50 \n"
			+ "Median : 50 \n"
			+ "Min : 0 \n"
			+ "Max : 100 \n"
			+ "People : 100 \n"
			+ "Personal Score : 0 \n"
			+ "b";
	
	String message2 = "Note: There are no bounds placed on the textfields."
			+ "When you are ready to create your distribution, click on the "
			+ "regenerate button. Below the stats text fields are the stats "
			+ "for the generated graph. If you are unsatisfied with the "
			+ "generated stats, click on the regenerate button to create a "
			+ "new distribution. To view the fitness of the graph (a metric "
			+ "I used to evaulate the accuracy of the graph) over time, click "
			+ "on the display fitness button.";
	
	String message3 = "If you want to change any property of the graph, click on "
			+ "the Options tab. To change the spacing between x ticks, enter "
			+ "the appropriate x spacing in the text field. After any change "
			+ "is made, click on the Update button to view the changes. Using "
			+ "other fields should be self explanatory. To "
			+ "change the color of the bars, click on the box with"
			+ "the corresponding color you wish "
			+ "to change. Then use the RGB sliders to change the color to the "
			+ "appropriate color. There is no need to click the update button "
			+ "to save changes. Congradulations, now you know how to use "
			+ "Gradebook!";
	
	String credits = "Author: Samuel Fleischer \n"
			+ "Date: 11/4/2016 \n"
			+ "Version: 1.1 \n"
			+ "b";

	
	public Instructions(){
		this.setPreferredSize(new Dimension(300,1120));
	}

	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		int y = 20;
		int x =20;
		g2.setColor(Color.black);
		y += drawMessage(g2, message, x, y);
		y += drawNewlineString(g2, stats, x, y);
		y += drawMessage(g2, message2, x, y);
		y += 20;
		y += drawMessage(g2, message3, x, y);
		y += 40;
		y += drawNewlineString(g2, credits, x, y);
	}
	
	/**
	 * 
	 * @param g2 The Graphics2D context
	 * @param m The message to be drawn
	 * @param x The starting x value
	 * @param ystart The starting y value
	 * @return The total y value covered in the drawstring
	 */
	public int drawMessage(Graphics2D g2, String m, int x, int ystart){
		int last = m.length();
		int leading = 0;
		int current = 0;
		int prev = 0;
		int y = ystart;
		while(current < last){
			
			leading = (leading >= last) ? last : leading;
			String sub = m.substring(prev, leading);
			if(g2.getFontMetrics().stringWidth(sub) > getWidth()-40){
				g2.drawString(m.substring(prev, current), x, y);
				prev = current;
				y += 20;
			}
			
			if(leading >= last){
				g2.drawString(m.substring(prev, last), x, y);
				y += 20;
				break;
			}
			
			int index = m.substring(leading).indexOf(" ");
			index = index > 0 ? index : last;
			current = leading;
			leading = index + current + 1;
		}
		return y - ystart;
	}
	
	/**
	 * 
	 * @param g2 The Graphics2D context
	 * @param m The message to be drawn 
	 * @param x The x position to be drawn at
	 * @param ystart The y position to be drawn at
	 * @return The total y area covered by the draw string.
	 */
	public int drawNewlineString(Graphics2D g2, String m, int x, int ystart){
		int last = m.length();
		int current = 0;
		int prev = 0;
		int y = ystart;
		while(current < last){
			g2.drawString(m.substring(prev, current), x, y);
			prev = current;
			y += 20;
			int index = m.substring(current).indexOf("\n");
			index = index > 0 ? index : last;
			current = current + index + 1;
		}
		y+= 20;
		return y - ystart;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		
	}
	
}
