package tek.ui;

import java.util.ArrayList;

import org.joml.Vector2f;

import tek.Window;
import tek.input.Keyboard;
import tek.input.Mouse;

public class UIScene {
	private Vector2f size;
	
	public ArrayList<UITexture> textures;
	public ArrayList<UIText>    texts;
	
	public ArrayList<ClickType> clicks;
	
	public UIOptions options;
	
	{
		textures = new ArrayList<UITexture>();
		texts    = new ArrayList<UIText>();
		clicks   = new ArrayList<ClickType>();
		
		size = new Vector2f();
		resized();
	}
	
	public UIScene(){
		
	}
	
	public void resized(){
		size.set(Window.instance.getWidth(), Window.instance.getHeight());
	}
	
	public void input(long delta){
		if(options != null)
			options.input();
		
		clicks.clear();
		for(int i : Keyboard.events){
			clicks.add(new ClickType(false, i));
		}
		
		for(int i : Mouse.events){
			clicks.add(new ClickType(true, i));
		}
	}
	
	public Vector2f flipY(double x, double y){
		return new Vector2f((float)(x), (float)(size.y - y));
	}
	
	public void render(){
		
	}
	
	public void update(long delta){
		if(options != null)
			options.update(delta);
	}
	
	public abstract static class UIOptions {
		public boolean wrap = true;
		public final UIOption[] options;
		
		public int current = 0;
		
		public UIOptions(UIOption[] options){
			this.options = options;
		}
		
		public abstract void Input();
		public abstract void Update(long delta);
		
		public void update(long delta){
			options[current].element.focus();
			Update(delta);
		}
		
		public void input(){
			
		}
		
		public int next(){
			int next = current + 1;
			if(current == options.length){
				next = (wrap) ? 0 :  current;
			}
			
			return next;
		}
		
		public int previous(){
			int prev = current - 1;
			if(current == 0){
				prev = (wrap) ? options.length - 1 : current;
			}
			
			return prev;
		}
		
		public void select(int index){
			if(index < 0 || index > options.length - 1){
				return;
			}
			options[current].element.focusExit();
			current = index;
			options[current].element.focusEnter();
		}
		
		public static class UIOption {
			public UIElement element;
			
			public UIOption(UIElement element){
				this.element = element;
			}
		}
	}
	
	public static class ClickType {
		public final boolean mouse;
		
		public final int value;
		
		public ClickType(boolean mouse, int value){
			this.mouse = mouse;
			this.value = value;
		}
	}
	
}
