import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FormatComponent {
	
	/**
	 * Adds components to the JPane that was passed. Modifies by reference
	 * @param p The panel to pass through
	 * @param list The list of strings to name the buttons/textfields
	 */
	public static void addTextFields(JPanel panel, ArrayList<JTextField> fields,
			String[] list, String title){
		
		JLabel titlelabel = new JLabel(title);
		titlelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlelabel.setOpaque(true);
		//Font old = titlelabel.getFont();
		Font header = new Font("Arial", Font.BOLD, 14);
		titlelabel.setFont(header);
		
		panel.add(titlelabel);
		
		for(String label : list){
			JLabel l = new JLabel(label + ":");
			l.setHorizontalAlignment(SwingConstants.LEFT);
			l.setOpaque(true);
			
			JTextField field = new JTextField();
			field.setPreferredSize(new Dimension(150,20));
			field.setName(label);
			field.setAlignmentX(Component.CENTER_ALIGNMENT);
			field.setHorizontalAlignment(JTextField.CENTER);
			field.setAlignmentY(Component.CENTER_ALIGNMENT);
			fields.add(field);
			
			JPanel f = new JPanel(new BorderLayout());
			f.setPreferredSize(new Dimension(150,35));
			f.setMaximumSize(new Dimension(150,35));
			f.add(field);
			
			JPanel fieldContainer = new JPanel();
			fieldContainer.setLayout(new BoxLayout(fieldContainer, BoxLayout.Y_AXIS));
			fieldContainer.setPreferredSize(new Dimension(150,35));
			fieldContainer.add(Box.createVerticalGlue());
			fieldContainer.add(f);
			fieldContainer.add(Box.createVerticalGlue());
			
			JPanel p = new JPanel(new GridLayout(1,2));
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
	 * @param title The label to add to the top of the JLabels that titles the panel. 
	 */
	public static void addLabels(JPanel panel, ArrayList<JLabel> outputs, 
			String[] list, String title){
		
		JLabel titlelabel = new JLabel(title);
		titlelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
		//titlelabel.setForeground(foreground);
		//titlelabel.setBackground(background);
		titlelabel.setOpaque(true);
		panel.add(titlelabel);
		Font header = new Font("Arial", Font.BOLD, 14);
		titlelabel.setFont(header);
		panel.add(Box.createVerticalGlue());
		
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
	/**
	 * Formats the Radio Buttons and puts it into a panel. The first String passed in
	 * will always be selected first. If there are fewer than two strings in the list
	 * then the button layout would be a FlowLayout. Otherwise it would be a GridLayout
	 * with column size 2.
	 * @param panel The panel to add the RadioButtons to
	 * @param listener The listener that will activate when a button is pressed
	 * @param list The list of strings to title the buttons with.
	 */
	public static void addRadioButtons(JPanel panel, 
			ActionListener listener, String[] list){
		ButtonGroup group = new ButtonGroup();
		
		int row = list.length/2;
		int column = 2;
		JPanel p = new JPanel();
		if(list.length <= 2)
			p.setLayout(new FlowLayout());
		else
			p.setLayout(new GridLayout(row, column));
		for(int i = 0; i < list.length; i++){
			JRadioButton button = new JRadioButton(list[i]);
			button.setActionCommand(list[i]);
			button.setPreferredSize(new Dimension(100,30));
			button.addActionListener(listener);
			button.setAlignmentX(SwingConstants.RIGHT);
			button.setHorizontalAlignment(SwingConstants.LEFT);
			//if its the first button then select it
			if(i == 0)
				button.setSelected(true);
			
			group.add(button);
			p.add(button);
		}
		panel.add(p);
	}
}
