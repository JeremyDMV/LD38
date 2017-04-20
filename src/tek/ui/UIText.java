package tek.ui;

import org.joml.Vector3f;

import tek.ui.UIScene.ClickType;

public class UIText extends UIElement {
	
	public String text;
	public Vector3f color;
	
	{
		color = new Vector3f(1f);
	}
	
	public UIText(String text){
		this.text = text;
		initialize();
	}
	
	private void initialize(){
		
	}
	
	@Override
	public void onFocusEnter() {
		
	}

	@Override
	public void onFocus() {
		
	}

	@Override
	public void onFocusExit() {
		
	}

	@Override
	public void onClick(ClickType type) {
		
	}

}
