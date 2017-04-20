package tek.ui;

import org.joml.Vector2d;
import org.joml.Vector2f;

import tek.render.Texture;

public class UITexture {
	public Vector2f position, size;
	public Texture texture;
	public int subTexture = -1;
	
	{
		position = new Vector2f();
		size     = new Vector2f();
	}
	
	public UITexture(Texture texture){
		this.texture = texture;
	}
	
	public UITexture(Texture texture, int subTexture){
		this.texture = texture;
		this.subTexture = subTexture;
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