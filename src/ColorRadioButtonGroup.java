import java.awt.Color;
import java.util.ArrayList;

public class ColorRadioButtonGroup {
	ArrayList<ColorButton> list = new ArrayList<ColorButton>();
	ColorButton selected;
	
	public ColorRadioButtonGroup(){
		
	}
	
	public void addColorButton(ColorButton cb){
		list.add(cb);
	}
	
	public void selectButton(ColorButton cb){
		for(ColorButton buttons : list){
			if(buttons.getSelected())
				buttons.setSelected(false);
		}
		cb.setSelected(true);
		selected = cb;
	}
	
	public void updateSelectedColor(int r, int g, int b){
		if(selected == null)
			return;
		selected.updateColor(r, g, b);
	}
	
	public Color getSelectedColor(){
		return selected.getColor();
	}
	
	public String getSelectedActionCommand(){
		if(selected == null)
			return "";
		return selected.getActionCommand();
	}
	
}
