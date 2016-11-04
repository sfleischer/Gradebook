import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class NavigationPanel extends JPanel{
	
	final String[] labels;
	final String[] metrics;
	final String[] results;
	int[] fitness = {};
	int[] distribution = {};
	Graph graph;
	ArrayList<JTextField> fields = new ArrayList<JTextField>();
	ArrayList<JSlider> sliders = new ArrayList<JSlider>();
	ArrayList<JLabel> outputs = new ArrayList<JLabel>();
	Calculator calculator;
	
	//option panel variables
	static final int TICK_MIN = 0;
	static final int TICK_MAX = 255;
	static final int TICK_INIT = 0;
	static final int TICK_MAJOR = 50;
	
	JPanel sliderPanel = new JPanel();
	ColorRadioButtonGroup crbg = new ColorRadioButtonGroup();
	
	int red = 0;
	int green = 0;
	int blue = 0;
	
	public NavigationPanel(Graph g){
		super();
		labels = new String[]{" STD", " mean", " median", " min", " max", " people", " personal score"};
		metrics = new String[]{" X spacing", " max freq", " # y ticks"};
		results = new String[]{" Graph STD", " Graph mean", " Graph median",
				" Percentile"};
		graph = g;
		this.setMaximumSize(new Dimension(400,1000));
		this.setMinimumSize(new Dimension(300,1000));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initialize();
		calculator = getCalculator();
	}
	
	public void initialize(){		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		//for the first tab, display all the stats fields
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
		
		//for the second tab, display all the options for the graph
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
		
		//the stats input pabel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		addTextFields(inputPanel, labels);
		
		//the panel to display the graph metrics (x spacing, etc)
		JPanel graphMetrics = new JPanel();
		graphMetrics.setLayout(new BoxLayout(graphMetrics, BoxLayout.PAGE_AXIS));
		addTextFields(graphMetrics, metrics);
		
		//the stats of the graph generated and the percentile of the input score
		JPanel graphResults = new JPanel();
		graphResults.setLayout(new BoxLayout(graphResults, BoxLayout.PAGE_AXIS));
		addLabels(graphResults, results);
		
		
		//buttons
		JButton generateButton = new JButton("Re-Generate");
		generateButton.addActionListener(new GenerateHandler());
		JPanel generateContainer = new JPanel();
		generateContainer.add(generateButton);
		
		JButton fitnessButton = new JButton("Display fitness");
		fitnessButton.addActionListener(new FitnessHandler());
		JPanel fitnessContainer = new JPanel();
		fitnessContainer.add(fitnessButton);
		
		JButton updateButton = new JButton("Update Graph");
		updateButton.addActionListener(new UpdateHandler());
		JPanel updateContainer = new JPanel();
		updateContainer.add(updateButton);
		
		
		//color sliders
		//JPanel sliderPanel = new JPanel();
		
		initializeColorSliders();
		
		graphMetrics.add(updateContainer);
		graphResults.add(generateContainer);
		graphResults.add(fitnessContainer);
		graphResults.add(Box.createRigidArea(new Dimension(10,20)));
		
		
		statsPanel.add(inputPanel);
		statsPanel.add(graphResults);
		
		//INSTRUCTIONS TAB PREPARATION
		JScrollPane instructions = new JScrollPane(new Instructions());
		instructions.getVerticalScrollBar().setUnitIncrement(16);
		
		optionsPanel.add(new JLabel("Graph Metrics:"));
		optionsPanel.add(graphMetrics);
		optionsPanel.add(createColorPanel());
		optionsPanel.add(sliderPanel);
		
		tabbedPane.addTab("Stats", statsPanel);
		tabbedPane.addTab("Options", optionsPanel);
		tabbedPane.addTab("Instructions", instructions);
		
		this.add(tabbedPane);
	}
	
	/**
	 * Adds components to the JPane that was passed. Modifies by reference
	 * @param p The panel to pass through
	 * @param list The list of strings to name the buttons/textfields
	 */
	public void addTextFields(JPanel panel, String[] list){
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
	
	public void initializeColorSliders(){
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		
		JSlider redSlider = new JSlider(JSlider.HORIZONTAL,
                TICK_MIN, TICK_MAX, TICK_INIT);
		redSlider.setName("red");
		redSlider.addChangeListener(new SliderHandler());
		redSlider.setMajorTickSpacing(TICK_MAJOR);
		redSlider.setPaintTicks(true);
		redSlider.setOpaque(true);
		redSlider.setForeground(Color.red);
		redSlider.setPaintLabels(true);
		
		JSlider greenSlider = new JSlider(JSlider.HORIZONTAL,
                TICK_MIN, TICK_MAX, TICK_INIT);
		greenSlider.setName("green");
		greenSlider.addChangeListener(new SliderHandler());
		greenSlider.setMajorTickSpacing(TICK_MAJOR);
		greenSlider.setPaintTicks(true);
		greenSlider.setOpaque(true);
		greenSlider.setForeground(new Color(0,155,0));
		greenSlider.setPaintLabels(true);
		
		JSlider blueSlider = new JSlider(JSlider.HORIZONTAL,
                TICK_MIN, TICK_MAX, TICK_INIT);
		blueSlider.setName("blue");
		blueSlider.addChangeListener(new SliderHandler());
		blueSlider.setMajorTickSpacing(TICK_MAJOR);
		blueSlider.setPaintTicks(true);
		blueSlider.setOpaque(true);
		//blueSlider.setBackground(Color.blue);
		blueSlider.setForeground(Color.blue);
		blueSlider.setPaintLabels(true);

		sliders.add(redSlider);
		sliders.add(greenSlider);
		sliders.add(blueSlider);
		
		sliderPanel.add(redSlider);
		sliderPanel.add(greenSlider);
		sliderPanel.add(blueSlider);
	}
	
	public JPanel createColorPanel(){
		JPanel cPanel = new JPanel(new GridLayout(2,2));
		
		JLabel label1 = new JLabel("bar color 1:");
		JLabel label2 = new JLabel("bar color 2:");
		
		ColorButton button1 = new ColorButton(Color.red, "bar1");
		button1.addActionListener(new ColorHandler());
		
		ColorButton button2 = new ColorButton(Color.blue, "bar2");
		button2.addActionListener(new ColorHandler());
		
		crbg.addColorButton(button1);
		crbg.addColorButton(button2);
		
		cPanel.add(label1);
		cPanel.add(button1);
		cPanel.add(label2);
		cPanel.add(button2);
		
		return cPanel;
	}
	
	public void addLabels(JPanel panel, String[] list){
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
	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public double[] getTextFieldParams(){
		double[] params = new double[10];
		params[2] = 50;
		for(JTextField field : fields){
			String text = field.getText();
			if(!isNumeric(text))
				text = "";
			double num = Double.parseDouble(text.equals("") ? "0" : field.getText());
			switch(field.getName()){
				case " STD":
					params[4] = num != 0 ? num : 10; break;
				case " mean":
					params[2] = num != 0 ? num : 50; break;
				case " median":
					params[3] = num != 0 ? num : params[2]; break;
				case " min":
					params[0] = num != 0 ? num : 0; break;
				case " max":
					params[1] = num != 0 ? num : 100; break;
				case " people":
					params[5] = num != 0 ? num : 100; break;
				case " X spacing":
					params[6] = num != 0 ? num : 10; break;
				case " max freq":
					params[7] = num != 0 ? num : 1; break;
				case " # y ticks":
					params[8] = num != 0 ? num : 10; break;
				case " personal score" :
					params[9] = num; break;
				
			}
		}
		return params;
	}
	
	public Calculator getCalculator(){
		double[] params = getTextFieldParams();
		return new Calculator(params[0], params[1], params[2], params[3], 
				params[4], (int) params[5]);
	}
	
	public void updateLabels(double[] params){
		if(distribution == null || distribution.length == 0)
			return;
		for(JLabel label : outputs){
			switch(label.getName()){
				case " Graph STD" : 
					label.setText(String.format("%2.2f", Calculator.standardDev(distribution)));
					break;
				case " Graph mean" : 
					label.setText(String.format("%2.2f", Calculator.getMean(distribution)));
					break;
				case " Graph median" :
					label.setText(Integer.toString(Calculator.getMedian(distribution)));
					break;
				case " Percentile" : 
					int percentile = Calculator.findPercentile(distribution, (int) params[9]);
					//int median = Calculator.getMedian(distribution);
					if(percentile > 50){
						label.setBackground(new Color(150,255,150));
						label.setForeground(new Color(0,100,0));
						label.setOpaque(true);
					}
					
					if(percentile < 50){
						label.setBackground(new Color(255,150,150));
						label.setForeground(new Color(100,0,0));
						label.setOpaque(true);
					}
					label.setText(Integer.toString(
							Calculator.findPercentile(distribution, (int) params[9])));
					
					break;
			}
		}
	}

	
	private class UpdateHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			//find the parameters from the text fields and create a new calculator
			double[] params = getTextFieldParams();
			Calculator calc = getCalculator();
			
			//if the old calculator does not equal the new calculator, regenerate the whole thing
			if(!calculator.equals(calc)){
				calculator = calc;
				distribution = calculator.findDistribution();
				int[] dist = calculator.generateHistogram(distribution, params[6]);
				graph.drawHistogram(dist, params[6], 100, params[7], (int) params[8]);
			} 
			
			//if the old and new calculators are the same, then just update the graph with
			//new parameters
			else {
				int[] dist = calculator.generateHistogram(distribution, params[6]);
				graph.drawHistogram(dist, params[6], 100, params[7], (int) params[8]);
			}
			updateLabels(params);
		}
	}
	
	private class FitnessHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			JFrame frame = new JFrame("Fitness Chart");
			frame.setContentPane(new FitnessGraph(
					calculator.getFitchart(), calculator.getWeakchart()));
			frame.setMinimumSize(new Dimension(500,500));
			frame.setVisible(true);
		}
	}
	
	/**
	 * Calculates a brand new distribution using the parameters.
	 * @author sfleischer
	 *
	 */
	private class GenerateHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			double[] params = getTextFieldParams();
			calculator = new Calculator(params[0], params[1], params[2], params[3], 
					params[4], (int) params[5]);
			distribution = calculator.findDistribution();
			int[] dist = calculator.generateHistogram(distribution, params[6]);
			//fitness = calc.getFitchart();
			graph.drawHistogram(dist, params[6], 100, params[7], (int) params[8]);
			updateLabels(params);
		}
		
	}
	
	private class ColorHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			crbg.selectButton((ColorButton) e.getSource());
			Color c = crbg.getSelectedColor();
			for(JSlider s : sliders){
				switch(s.getName()) {
				case "red" :
					s.setValue(c.getRed());
					break;
				case "green" :
					s.setValue(c.getGreen());
					break;
				case "blue" :
					s.setValue(c.getBlue());
					break;
				} 
			}
		}
		
	}
	
	private class SliderHandler implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			switch(source.getName()) {
			case "red" :
				red = source.getValue();
				break;
			case "green" :
				green = source.getValue();
				break;
			case "blue" :
				blue = source.getValue();
				break;
			}
			crbg.updateSelectedColor(red, green, blue);
			
			switch(crbg.getSelectedActionCommand()){
			case "bar1" :
				graph.setBarColor1(new Color(red, green, blue));
				break;
			case "bar2" :
				graph.setBarColor2(new Color(red, green, blue));
				break;
			}
		}
	}

}
