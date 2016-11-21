import java.awt.BorderLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class Master extends JPanel{
	
	public Master(){
		this.setLayout(new BorderLayout());
		Graph g = new Graph();
		this.add(g, BorderLayout.CENTER);
		this.add(new NavigationPanel(g), BorderLayout.EAST);
	}

}
