import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FormatComponent {
	
	/**
	 * Adds components to the JPane that was passed. Modifies by reference
	 * @param p The panel to pass through
	 * @param list The list of strings to name the buttons/textfields
	 */
	public static void addTextFields(JPanel panel, ArrayList<JTextField> fields, String[] list){
		for(String label : list){
			JLabel l = new JLabel(label + ":");
			
			JTextField field = new JTextField();
			field.setMaximumSize(new Dimension(150,20));
			field.setMinimumSize(new Dimension(150,20));
			//field.setBackground(new Color(200,200,255));
			field.setName(label);
			field.setAlignmentX(Component.CENTER_ALIGNMENT);
			field.setHorizontalAlignment(JTextField.CENTER);
			field.setAlignmentY(Component.CENTER_ALIGNMENT);
			fields.add(field);
			JPanel f = new JPanel(new BorderLayout());
			f.setMinimumSize(new Dimension(150,40));
			f.setPreferredSize(new Dimension(150,40));
			f.setMaximumSize(new Dimension(150,40));
			f.add(field);
			JPanel fieldContainer = new JPanel();
			fieldContainer.setLayout(new BoxLayout(fieldContainer, BoxLayout.Y_AXIS));
			//fieldContainer.add(Box.createVerticalStrut(50));
			fieldContainer.setPreferredSize(new Dimension(150,40));
			fieldContainer.setMinimumSize(new Dimension(150,40));
			fieldContainer.setMaximumSize(new Dimension(150,40));
			fieldContainer.add(Box.createVerticalGlue());
			fieldContainer.add(f);
			fieldContainer.add(Box.createVerticalGlue());
			
			JPanel p = new JPanel(new GridLayout(1,2));
			//p.setBackground(new Color(255,240,240));
			p.add(l);
			p.add(fieldContainer);
			panel.add(p);
		}
	}
	
	/**
	 * Formats the Jlabels and inserts them into the panel
	 * @param panel The JPanel for the labels to be inserted into
	 * @param outputs The list of labels added
	 * @param list The list of labels to be added
	 */
	public static void addLabels(JPanel panel, ArrayList<JLabel> outputs, String[] list){
		for(String label : list){
			JLabel l = new JLabel(label + ":");
			
			JLabel field = new JLabel("");
			field.setPreferredSize(new Dimension(100,30));
			field.setName(label);
			field.setAlignmentX(SwingConstants.EAST);
			field.setHorizontalAlignment(JTextField.CENTER);
			outputs.add(field);
			
			JPanel p = new JPanel(new GridLayout(1,2));
			p.add(l);
			p.add(field);
			panel.add(p);
		}
	}
}
