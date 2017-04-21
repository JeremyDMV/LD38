package tek.ui;

import tek.render.Texture;

public class UITexture extends UIElement {
	
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