package tek.ui;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import tek.Window;
import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.Camera;
import tek.render.Shader;
import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.Scene;

public class UIScene {
	public static Shader defaultShader;
	
	private Vector2f size;
	
	public ArrayList<UITexture> textures;
	public ArrayList<UIText>    texts;
	
	public ArrayList<ClickType> clicks;
	
	public UIOptions options;
	
	private Camera camera;
	
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
		Camera camera = Scene.current.camera;
		
		defaultShader.bind();
		
		//get the UI Projection matrix
		Matrix4f mat = camera.getUI();
		
		defaultShader.set("projection", mat);
		
		for(UITexture texture : textures){
			defaultShader.set("model", texture.getMatrix());
			
			if(texture.subTexture != -1){
				defaultShader.set("SUB_TEXTURE", true);
				
				TextureSheet sheet = TextureSheet.getSheet(texture.texture);
				
				defaultShader.set("SUB_SIZE", sheet.subSize);
				defaultShader.set("TEXTURE_OFFSET", sheet.getOffset(texture.subTexture));
				defaultShader.set("TEXTURE_SIZE", sheet.texture.size);
			}
			
			//totally not confusing
			texture.texture.bind();
			
			GameObject.quad.draw();
		}
		
		Shader.unbind();
		
		UIFont.prepRender();
		
		for(UIText text : texts){
			text.draw();
		}
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
