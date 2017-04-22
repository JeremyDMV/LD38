package tek.ui;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;

import tek.runtime.Transform;
import tek.ui.UIScene.ClickType;

public abstract class UIElement {
	public Vector2f position, size;
	private boolean focused = false;
	
	public int layer;
	
	private Matrix4f mat;
	
	public UICallback callback;
	
	{
		position = new Vector2f();
		size = new Vector2f();
		mat = new Matrix4f();
	}

	public void setCallback(UICallback callback){
		callback.initialize(this);
		this.callback = callback;
	}
	
	public void onFocusEnter(){
		if(callback != null)
			callback.onFocusEnter();
	}
	
	public void onFocus(){
		if(callback != null)
			callback.onFocus();
	}
	
	public void onFocusExit(){
		if(callback != null)
			callback.onFocusExit();
	}
	
	public void onClick(ClickType type){
		if(callback != null)
			callback.onClick(type);
	}
	
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
	
	public void updateMatrix(){
		mat.identity();
		mat.translate(position.x, position.y, layer * Transform.LAYER_MOD);
		//mat.rotateZ((float)Math.toRadians(rotation));
		mat.scale(size.x, size.y, 1f);
	}
	
	protected Matrix4f getMatrix(){
		return mat;
	}
	
	public static interface UICallback {
		public void initialize(UIElement e);
		public void onFocusEnter();
		public void onFocus();
		public void onFocusExit();
		public void onClick(ClickType type);
	}
}
