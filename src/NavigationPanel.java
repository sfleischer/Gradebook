import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import components.ColorRadioButtonGroup;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import components.*;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel{
	
	final String[] labels;
	final String[] metrics;
	final String[] results;
	final String[] genetics;
	final String[] fitlabels;
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
	
	final Color background = Color.gray;
	final Color foreground = Color.white;
	
	JPanel sliderPanel = new JPanel();
	ColorRadioButtonGroup crbg = new ColorRadioButtonGroup();
	
	int red = 0;
	int green = 0;
	int blue = 0;
	
	public NavigationPanel(Graph g){
		super();
		labels = new String[]{" STD", " mean", " median", " min", " max", " people", " personal score"};
		metrics = new String[]{" X spacing", " # y ticks"};
		results = new String[]{" Graph STD", " Graph mean", " Graph median",
				" Percentile"};
		genetics = new String[]{" Generations", " Threshold", " Population"};
		fitlabels = new String[]{"fitness rank", "diversity rank", 
				"fitdiverse rank","tailed fitness rank"};
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
		//statsPanel.setBackground(background);
		
		//for the second tab, display all the options for the graph
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
		
		//the stats input pabel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		//inputPanel.setBackground(background);
		//inputPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		FormatComponent.addTextFields(
				inputPanel, fields, labels, background, foreground, "Statistics:");
		
		//the panel to display the graph metrics (x spacing, etc)
		JPanel graphMetrics = new JPanel();
		graphMetrics.setLayout(new BoxLayout(graphMetrics, BoxLayout.PAGE_AXIS));
		FormatComponent.addTextFields(
				graphMetrics, fields, metrics, background, foreground, "Metrics:");
		
		//the stats of the graph generated and the percentile of the input score
		JPanel graphResults = new JPanel();
		graphResults.setLayout(new BoxLayout(graphResults, BoxLayout.PAGE_AXIS));
		//graphResults.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		FormatComponent.addLabels(graphResults, outputs, results, background,
				foreground, "Results:");
		
		//the panel to display genetic algorithm parameters
		JPanel geneticMetrics = new JPanel();
		geneticMetrics.setLayout(new BoxLayout(geneticMetrics, BoxLayout.PAGE_AXIS));
		FormatComponent.addTextFields(
				geneticMetrics, fields, genetics, background, foreground, "Genetics:");
		FormatComponent.addRadioButtons(geneticMetrics, new RadioHandler(), fitlabels);
		
		//buttons
		JPanel buttonPanel = new JPanel(new GridLayout(2,2));
		JButton generateButton = new JButton("Re-Generate");
		generateButton.addActionListener(new GenerateHandler());
		JPanel generateContainer = new JPanel();
		generateContainer.add(generateButton);
		buttonPanel.add(generateButton);
		
		JButton fitnessButton = new JButton("Display fitness");
		fitnessButton.addActionListener(new FitnessHandler());
		JPanel fitnessContainer = new JPanel();
		fitnessContainer.add(fitnessButton);
		buttonPanel.add(fitnessButton);
		
		JButton updateButton = new JButton("Update Graph");
		updateButton.addActionListener(new UpdateHandler());
		JPanel updateContainer = new JPanel();
		updateContainer.add(updateButton);
		buttonPanel.add(updateButton);
		
		JButton geneticsButton = new JButton("Update Evolution");
		updateButton.addActionListener(new UpdateHandler());
		buttonPanel.add(updateButton);
		
		
		//initializeColorSliders();
		
		graphMetrics.add(updateContainer);
		
		//graphResults.add(generateContainer);
		//graphResults.add(fitnessContainer);
		//graphResults.add(Box.createRigidArea(new Dimension(10,20)));
		
		statsPanel.add(inputPanel);
		statsPanel.add(graphResults);
		statsPanel.add(graphMetrics);
		statsPanel.add(buttonPanel);
		//statsPanel.add(generateContainer);
		//statsPanel.add(fitnessContainer);
		//statsPanel.add(geneticMetrics);
		
		//INSTRUCTIONS TAB PREPARATION
		JScrollPane instructions = new JScrollPane(new Instructions());
		instructions.getVerticalScrollBar().setUnitIncrement(16);
		
		//OPTIONS TAB
		//optionsPanel.add(new JLabel("Graph Metrics:"));
		//optionsPanel.add(graphMetrics);
		optionsPanel.add(geneticMetrics);
		optionsPanel.add(initializeWeights());
		//optionsPanel.add(geneticMetrics);
		optionsPanel.add(createColorPanel());
		//optionsPanel.add(sliderPanel);
		
		tabbedPane.addTab("Stats", statsPanel);
		//tabbedPane.addTab("Genetics", geneticMetrics);
		tabbedPane.addTab("Options", optionsPanel);
		tabbedPane.addTab("Instructions", instructions);
		
		this.add(tabbedPane);
	}
	
	public JPanel initializeWeights(){
		String[] weights = {" STD", " mean", " median", " min", " max"};
		JPanel wPanel = new JPanel();
		wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.PAGE_AXIS));
		for(String title : weights){
			ColorSlider s = new ColorSlider(Color.black, title, 100);
			wPanel.add(s);
		}
		return wPanel;
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
	

	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public GraphState getTextFieldParams(){
		GraphState state = new GraphState();
		for(JTextField field : fields){
			String text = field.getText();
			if(!isNumeric(text))
				continue;
			double num = Double.parseDouble(text);
			switch(field.getName()){
				case " STD":
					state.setSTD(num); break;
				case " mean":
					state.setMean(num); break;
				case " median":
					state.setMedian(num); break;
				case " min":
					state.setMin(num); break;
				case " max":
					state.setMax(num); break;
				case " people":
					state.setPeople((int) num); break; 
				case " X spacing":
					state.setXSpacing(num); break;
				case " # y ticks":
					state.setYTicks( num); break;
				case " personal score" :
					state.setPersonal(num); break;
				case " Generations" :
					state.setGenerations((int) num); break;
				case " Threshold" :
					state.setThreshold((int) num);
				case " Population" :
					state.setPopulation((int) num);
			}
		}
		return state;
	}
	
	public Calculator getCalculator(){
		GraphState state = getTextFieldParams();
		return new Calculator(state.getMin(), state.getMax(), state.getMean(),
				state.getMedian(), state.getSTD(), state.getPeople());
	}
	
	public void updateLabels(GraphState state){
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
					int percentile = Calculator.findPercentile(distribution, (int) state.getPersonal());
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
							Calculator.findPercentile(distribution, (int) state.getPersonal())));
					
					break;
			}
		}
	}

	
	private class UpdateHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			//find the parameters from the text fields and create a new calculator
			GraphState state = getTextFieldParams();
			Calculator calc = getCalculator();
			
			//if the old calculator does not equal the new calculator, regenerate the whole thing
			if(!calculator.equals(calc)){
				calculator = calc;
				distribution = calculator.findDistribution();
				int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
				graph.drawHistogram(dist, state.getXSpacing(), 100, 1.0, (int) state.getYTicks());
			} 
			
			//if the old and new calculators are the same, then just update the graph with
			//new parameters
			else {
				int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
				graph.drawHistogram(dist, state.getXSpacing(), 100, 1.0, (int) state.getYTicks());
			}
			updateLabels(state);
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
			GraphState state = getTextFieldParams();
			calculator = new Calculator(state.getMin(), state.getMax(), state.getMean(),
					state.getMedian(), state.getSTD(), state.getPeople());
			distribution = calculator.findDistribution();
			int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
			//fitness = calc.getFitchart();
			graph.drawHistogram(dist, state.getXSpacing(), 100, 1.0, (int) state.getYTicks());
			updateLabels(state);
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
			
			if(source == null)
				return;
			
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
				//graph.setBarColor1(new Color(red, green, blue));
				break;
			case "bar2" :
				//graph.setBarColor2(new Color(red, green, blue));
				break;
			}
		}
		
	}
	
	private class EvolutionHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			GraphState state = getTextFieldParams();
			calculator.setGenerations(state.getGenerations());
			calculator.setThresold(state.getThreshold());
		}
	}
	
	private class RadioHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			switch(cmd){
			case "fitness rank":  break;
			case "diversity rank": break;
			case "fitdiverse rank": break;
			case "tailed fitness rank": break;
			}
		}
	}

}
