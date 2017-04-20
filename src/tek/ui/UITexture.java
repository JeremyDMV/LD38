package tek.ui;

import org.joml.Vector2d;
import org.joml.Vector2f;

import tek.render.Texture;

public abstract class UITexture extends UIElement {
	
	public Texture texture;
	public int subTexture = -1;
	
	
	public UITexture(Texture texture){
		this.texture = texture;
	}
	
	public UITexture(Texture texture, int subTexture){
		this.texture = texture;
		this.subTexture = subTexture;
	}
}