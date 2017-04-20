package tek.ui;

import org.joml.Vector2d;
import org.joml.Vector2f;

import tek.ui.UIScene.ClickType;

public abstract class UIElement {
	public Vector2f position, size;
	private boolean focused = false;
	
	{
		position = new Vector2f();
		size = new Vector2f();
	}

	//focus events
	public abstract void onFocusEnter();
	public abstract void onFocus();
	public abstract void onFocusExit();
	
	//click events
	public abstract void onClick(ClickType type);
	
	public void focusEnter(){
		focused = true;
		onFocusEnter();
	}
	
	public void focus(){
		onFocus();
	}
	
	public void focusExit(){
		focused = false;
		onFocusExit();
	}
	
	public boolean isFocused(){
		return focused;
	}
	
	public void set(Vector2f position, Vector2f size){
		this.position.set(position);
		this.size.set(size);
	}
	
	public boolean contains(Vector2d position){
		if(position.y >= position.y - (size.y / 2f) && position.y <= position.y + (size.y / 2f)){
			if(position.y >= position.y - (size.y / 2f) && position.y <= position.y + (size.y / 2f)){
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Vector2f position){
		if(position.y >= position.y - (size.y / 2f) && position.y <= position.y + (size.y / 2f)){
			if(position.y >= position.y - (size.y / 2f) && position.y <= position.y + (size.y / 2f)){
				return true;
			}
		}
		return false;	
	}
}
