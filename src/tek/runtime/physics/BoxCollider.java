package tek.runtime.physics;

import org.joml.Vector2f;

import tek.runtime.GameObject;
import tek.runtime.Physics;

public class BoxCollider extends Collider {
	private Vector2f size;
	
	public BoxCollider(GameObject parent, Vector2f size){
		super(parent);
		this.size = new Vector2f(size);
		this.body = Physics.instance.getSquareBody(this.position, size, this.type.type);
	}
	
	public BoxCollider(GameObject parent, Vector2f size, ColliderType type){
		super(parent);
		this.type = type;
		this.size = new Vector2f(size);
		this.body = Physics.instance.getSquareBody(this.position, size, this.type.type);
	}
	
	public Vector2f getSize(){
		return new Vector2f(size);
	}
}
