
import java.awt.Dimension;

import javax.swing.JFrame;

public class Gradebook {

	public static void main(String args[]){
		JFrame frame = new JFrame("Gradebook");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Master());
		frame.setMinimumSize(new Dimension(1000,700));
		frame.setVisible(true);
	}
}
