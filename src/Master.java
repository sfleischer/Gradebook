import java.awt.BorderLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class Master extends JSplitPane{
	
	public Master(){
		//this.setLayout(new BorderLayout());
		Graph g = new Graph();
		this.add(g, JSplitPane.LEFT);
		this.add(new NavigationPanel(g), JSplitPane.RIGHT);
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
	}

}
