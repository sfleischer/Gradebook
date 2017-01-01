import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Color;
import components.*;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel{
	
	final String[] labels;
	final String[] metrics;
	final String[] results;
	final String[] genetics;
	final String[] fitlabels;
	final String[] intensities;
	int[] fitness = {};
	int[] distribution = {};
	boolean absoluteIntensity = true;
	
	Graph graph;
	RadarChart chart;
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
	
	
	public NavigationPanel(Graph g){
		super();
		labels = new String[]{" STD", " mean", " median", " min", 
				" max", " people", " personal score"};
		metrics = new String[]{" X spacing", " # y ticks"};
		results = new String[]{" Graph STD", " Graph mean", " Graph median",
				" Percentile"};
		genetics = new String[]{" Generations", " Threshold", " Population"};
		fitlabels = new String[]{"auto-fit", "auto-diverse", 
				"manual","slow-fit"};
		intensities = new String[]{"Absolute", "Relative"};
		graph = g;
		//this.setMaximumSize(new Dimension(400,1000));
		//this.setMinimumSize(new Dimension(300,1000));
		//this.setPreferredSize(new Dimension(300, 1000));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initialize();
		setInitialTextFieldNumbers();
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
				inputPanel, fields, labels, "Statistics:");
		
		//the panel to display the graph metrics (x spacing, etc)
		JPanel graphMetrics = new JPanel();
		graphMetrics.setLayout(new BoxLayout(graphMetrics, BoxLayout.PAGE_AXIS));
		FormatComponent.addTextFields(
				graphMetrics, fields, metrics, "Metrics:");
		//JPanel radioLabel = new JPanel(new GridLayout(1,2));
		//JLabel label = new JLabel("Intensity:");
		//radioLabel.add(label);
		FormatComponent.addRadioButtons(
				graphMetrics, new IntensityHandler(), intensities, null);
		//graphMetrics.add(radioLabel);
		
		//the stats of the graph generated and the percentile of the input score
		JPanel graphResults = new JPanel();
		graphResults.setLayout(new BoxLayout(graphResults, BoxLayout.PAGE_AXIS));
		//graphResults.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		FormatComponent.addLabels(graphResults, outputs, results, "Results:");
		
		//the panel to display genetic algorithm parameters
		JPanel geneticMetrics = new JPanel();
		geneticMetrics.setLayout(new BoxLayout(geneticMetrics, BoxLayout.PAGE_AXIS));
		FormatComponent.addTextFields(
				geneticMetrics, fields, genetics, "Genetics:");
		//JPanel radioWrapper = new JPanel();
		//radioWrapper.setMaximumSize(new Dimension(this.getSize()));
		//FormatComponent.addRadioButtons(geneticMetrics, 
		//		new RadioHandler(), fitlabels, "Mutation style:");
		//geneticMetrics.add(radioWrapper);
		
		//buttons
		JPanel buttonPanel = new JPanel(new GridLayout(2,2));
		JButton generateButton = new JButton("Generate");
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
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearHandler());
		JPanel clearContainer = new JPanel();
		clearContainer.add(clearButton);
		buttonPanel.add(clearButton);
		
		
		statsPanel.add(inputPanel);
		statsPanel.add(graphResults);
		statsPanel.add(graphMetrics);
		statsPanel.add(buttonPanel);
		
		//INSTRUCTIONS TAB PREPARATION
		JScrollPane instructions = new JScrollPane(new Instructions());
		instructions.getVerticalScrollBar().setUnitIncrement(16);
		
		//OPTIONS TAB
		//optionsPanel.add(new JLabel("Graph Metrics:"));
		//optionsPanel.add(graphMetrics);
		optionsPanel.add(geneticMetrics);
		chart = new RadarChart(
				new String[]{"STD", "Mean", "Median", "Min", "Max"}, 
				new double[]{3,2,3,0.5,0.5}, 5);
		FormatComponent.addTitleLabel(optionsPanel, "Weights:");
		optionsPanel.add(chart);

		//optionsPanel.add(initializeWeights());
		//optionsPanel.add(geneticMetrics);
		//optionsPanel.add(createColorPanel());
		//optionsPanel.add(sliderPanel);
		
		tabbedPane.addTab("Stats", statsPanel);
		//tabbedPane.addTab("Genetics", geneticMetrics);
		tabbedPane.addTab("Options", optionsPanel);
		tabbedPane.addTab("Instructions", instructions);
		
		this.add(tabbedPane);
	}
	

	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	private static boolean contains(String[] array, String target){
		for(String s : array){
			if(s.equals(target))
				return true;
		}
		return false;
	}
	
	public void setInitialTextFieldNumbers(){
		GraphState state = new GraphState();
		for(JTextField field : fields){
			switch(field.getName()){
				case " STD":
					field.setText(Integer.toString((int) state.getSTD())); break;
				case " mean":
					field.setText(Integer.toString((int) state.getMean())); break;
				case " median":
					field.setText(Integer.toString((int) state.getMedian())); break;
				case " min":
					field.setText(Integer.toString((int) state.getMin())); break;
				case " max":
					field.setText(Integer.toString((int) state.getMax())); break;
				case " people":
					field.setText(Integer.toString(state.getPeople())); break; 
				case " X spacing":
					field.setText(Integer.toString((int) state.getXSpacing())); break;
				case " # y ticks":
					field.setText(Integer.toString((int) state.getYTicks())); break;
				case " personal score" :
					field.setText(Integer.toString((int) state.getPersonal())); break;
				case " Generations" :
					field.setText(Integer.toString(state.getGenerations())); break;
				case " Threshold" :
					field.setText(Integer.toString(state.getThreshold())); break;
				case " Population" :
					field.setText(Integer.toString(state.getPopulation())); break;
				//case " Mutation" :
				//	field.setText(Integer.toString((int) state.getMutation())); break;
			}
		}
	}
	
	/**
	 * Returns the graph state of the program by reading the text fields in the program.
	 * The user can input the name of a specific text field to get the parameter for that
	 * text field or null to get all the text fields.
	 *  
	 * @param target The specific text field the user wishes to get the value of. If null
	 * then the entire graph state is returned.
	 * @return The graph state of the program.
	 */
	public GraphState getTextFieldParams(String[] targets){
		//create a new GraphState
		GraphState state = new GraphState();
		
		//Iterate through all the textfields and locate appropriate fields
		for(JTextField field : fields){
			String text = field.getText();
			//throw an exception if field not numeric
			if(!isNumeric(text)){
				throw new IllegalArgumentException("Textfield \"" + 
						field.getName().substring(1) + "\" is not numeric");
			}
			if(targets != null && !contains(targets, field.getName())){
				continue;
			}
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
					state.setYTicks(num); break;
				case " personal score" :
					state.setPersonal(num); break;
				case " Generations" :
					state.setGenerations((int) num); break;
				case " Threshold" :
					state.setThreshold((int) num); break;
				case " Population" :
					state.setPopulation((int) num); break;
			}		
		}
		state.setIntensity(absoluteIntensity);
		return state;
	}
	
	
	private static void generateWarning(String title, String message){
		JOptionPane.showMessageDialog(new JFrame(),  
				message, 
				title,
				JOptionPane.WARNING_MESSAGE);
	}
	
	
	public Calculator getCalculator() throws IllegalArgumentException {
		GraphState state = getTextFieldParams(null);
		return new Calculator(state.getMin(), state.getMax(), state.getMean(),
				state.getMedian(), state.getSTD(), state.getPeople());
	}
	
	private void prepareCalculator(Calculator calc, GraphState state){
		calc.update(state.getMin(), state.getMax(), state.getMean(),
				state.getMedian(), state.getSTD(), state.getPeople());
		calc.setGenerations(state.getGenerations());
		calc.setThresold(state.getThreshold());
		calc.setPopulation(state.getPopulation());
		
		double values[] = chart.getValues();
		//order is "STD", "Mean", "Median", "Min", "Max"
		calc.setStdWeight(values[0]);
		calc.setMeanWeight(values[1]);
		calc.setMedianWeight(values[2]);
		calc.setMinWeight(values[3]);
		calc.setMaxWeight(values[4]);
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
			try{
				GraphState state = getTextFieldParams(null);
				Calculator calc = getCalculator();
			
				//if the old calculator does not equal the new calculator, regenerate the whole thing
				if(!calculator.equals(calc)){
					prepareCalculator(calculator, state);
					distribution = calculator.findDistribution();
					int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
					double freq = state.getIntensity() ? 
							1.0 : 1.0 * Calculator.getMax(dist)/state.getPeople();
					graph.drawHistogram(dist, state.getXSpacing(), 100, freq, (int) state.getYTicks());
				}
				
				//if the old and new calculators are the same, then just update the graph with
				//new parameters
				else {
					int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
					double freq = state.getIntensity() ? 
							1.0 : 1.0 * Calculator.getMax(dist)/state.getPeople();
					graph.drawHistogram(dist, state.getXSpacing(), 100, freq, (int) state.getYTicks());
				}
				updateLabels(state);
			} catch (IllegalArgumentException o){
				generateWarning("IllegalArgumentException", o.getMessage());
			}
		}
	}
	
	private class FitnessHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			//create a new frame to show the fitness chart
			try{
				JFrame frame = new JFrame("Fitness Chart");
				frame.setContentPane(new FitnessGraph(
						calculator.getFitchart(), calculator.getWeakchart(), 
						getTextFieldParams(new String[]{" Threshold"}).getThreshold()));
				//frame.setMinimumSize(new Dimension(500,500));
				frame.pack();
				frame.setVisible(true);
			} catch (IllegalArgumentException o){
				generateWarning("IllegalArgumentException", o.getMessage());
			}
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
			try{
				GraphState state = getTextFieldParams(null);
				prepareCalculator(calculator, state);
				distribution = calculator.findDistribution();
				int[] dist = calculator.generateHistogram(distribution, state.getXSpacing());
				double freq = state.getIntensity() ? 1.0 : 1.0 * Calculator.getMax(dist)/state.getPeople();
				graph.drawHistogram(dist, state.getXSpacing(), 100, freq, (int) state.getYTicks());
				updateLabels(state);
			} catch (IllegalArgumentException o){
				generateWarning("IllegalArgumentException", o.getMessage());
			}
		}
		
	}
	
	/**
	 * Handles the event for the Clear Button. If the user clicks this, the histogram
	 * should be cleared as well as any Gradient Panel on the screen.
	 * @author sfleischer
	 *
	 */
	private class ClearHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			try{
				GraphState state = getTextFieldParams(new String[]{" X spacing"});
				int[] temp = new int[(int) (100.0/state.getXSpacing())];
				
				//must fill the array before clearing the graph or else bad things happen
				for(int i = 0; i < temp.length; i++){
					temp[i] = 0;
				}
				//clear the graph
				graph.drawHistogram(temp, state.getXSpacing(), 100, 1.0, (int) state.getYTicks());
				graph.closeGradient();
			} catch (IllegalArgumentException o){
				generateWarning("IllegalArgumentException", o.getMessage());
			}
			
			//clear the graph metrics
			for(JLabel label : outputs){
				label.setText("");
				label.setOpaque(false);
			}
		}
	}
	
	
	private class IntensityHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()){
			case "Absolute" :
				absoluteIntensity = true;
				break;
			case "Relative" :
				absoluteIntensity = false;
				break;
			}
		}
		
	}
	
	/*private class RadioHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			switch(cmd){
			case "fitness rank":  break;
			case "diversity rank": break;
			case "fitdiverse rank": break;
			case "tailed fitness rank": break;
			}
		}
	}*/

}
