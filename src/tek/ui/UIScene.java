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
	
	public Vector2f flipY(double x, double y){
		return new Vector2f((float)(x), (float)(size.y - y));
	}
	
	public void add(UIElement[] elements){
		for(UIElement e : elements)
			add(e);
	}
	
	public void add(ArrayList<UIElement> elements){
		for(UIElement e : elements)
			add(e);
	}
	
	public void add(UIElement element){
		if(element instanceof UITexture){
			textures.add((UITexture)element);
		}else if(element instanceof UIText){
			texts.add((UIText)element);
		}
	}
	
	public void remove(UIElement[] elements){
		for(UIElement e : elements)
			remove(e);
	}
	
	public void remove(ArrayList<UIElement> elements){
		for(UIElement e : elements)
			remove(e);
	}
	
	public void remove(UIElement element){
		if(element instanceof UITexture){
			textures.remove((UITexture)element);
		}else if(element instanceof UIText){
			texts.remove((UIText)element);
		}
	}
	
	public void center(UIElement element){
		if(element instanceof UITexture){
			UITexture t = (UITexture)element;
			float x = (size.x - t.size.x)  / 2f;
			float y = (size.y - t.size.y)  / 2f;
			t.position.set(x, y);
			
			t.updateMatrix();
		}else if(element instanceof UIText){
			UIText t = (UIText)element;
			float x = (size.x - t.getWidth()) / 2f;
			float y = (size.y - t.getHeight()) / 2f;
			t.position.set(x, y);
		}
	}
	
	public void input(long delta){
		if(options != null)
			options.input();
	}
	
	public void update(long delta){
		if(options != null)
			options.update(delta);
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
	
	public abstract static class UIOptions {
		public boolean wrap = true;
		public final UIElement[] options;
		public final UIScene scene;
		
		public int current = 0;
		
		public UIOptions(UIScene scene, UIElement[] options){
			this.options = options;
			this.scene = scene;
			
			scene.options = this;
			scene.add(options);
		}
		
		public abstract void Input();
		public abstract void Update(long delta);
		
		public void update(long delta){
			options[current].focus();
			Update(delta);
		}
		
		public void input(){
			ArrayList<ClickType> clicks = new ArrayList<ClickType>();
			
			for(int k : Keyboard.events){
				clicks.add(new ClickType(false, k));
			}
			
			//focused element listens to the keyboard clicks
			for(ClickType click : clicks){
				options[current].onClick(click);
			}
			
			clicks.clear();
			
			for(int m : Mouse.events){
				clicks.add(new ClickType(true, m));
			}
			
			Vector2f mPos = scene.flipY(Mouse.x, Mouse.y);
			
			if(clicks.size() != 0){
				for(UIElement option : options){
					if(option.contains(mPos)){
						clicks.forEach((click)->option.onClick(click));
					}
				}
				
				clicks.clear();
			}
			
			Input();
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
			options[current].focusExit();
			current = index;
			options[current].focusEnter();
		}
		
		public void destroy(){
			scene.remove(options);
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
