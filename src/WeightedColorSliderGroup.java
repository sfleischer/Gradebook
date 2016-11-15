import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WeightedColorSliderGroup implements ChangeListener{
	
	ArrayList<ColorSlider> colorList;
	ArrayList<ChangeListener> changeList;
	
	public WeightedColorSliderGroup(){
		
	}
	
	public void addColorSlider(ColorSlider slider){
		colorList.add(slider);
	}
	
	public void addChangeListener(ChangeListener cl){
		changeList.add(cl);
	}
	
	public void stateChanged(ChangeEvent e){
		ColorSlider cs = (ColorSlider) e.getSource();
	}
	
	
}
