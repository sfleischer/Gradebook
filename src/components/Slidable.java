package components;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.ChangeListener;

public interface Slidable extends MouseListener, MouseMotionListener {

	public void addChangeListener(ChangeListener c);
	
}
