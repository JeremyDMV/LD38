package tek.ui;

import org.joml.Vector3f;

import tek.ui.UIScene.ClickType;

public class UIText extends UIElement {
	public static UIFont defaultFont;
	
	private float width = 0f;
	
	private String text;
	public Vector3f color;
	
	private float scale = 1.0f;
	
	public UIFont font = defaultFont;
	
	{
		color = new Vector3f(1f);
	}
	
	public UIText(UIFont font){
		this.font = font;
		initialize();
	}
	
	public UIText(String text){
		this.text = text;
		initialize();
	}
	
	public UIText(String text, UIFont font){
		this.font = font;
		this.text = text;
		initialize();
	}
	
	private void initialize(){
		if(text != null){
			width = font.getWidth(text);
		}
	}
	
	public void setText(String text){
		if(this.text.equals(text))
			return;
		this.text = text;
		this.width = font.getWidth(text);
	}
	
	public String getText(){
		return text;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getScale(){
		return scale;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
	
	public float getHeight(){
		return font.getHeight() * scale;
	}
	
	public void setFont(UIFont font){
		this.font = font;
	}
	
	public UIFont getFont(){
		return font;
	}
	
	public void draw(){
		font.print(this.position.x, this.position.y, text);
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
