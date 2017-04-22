package tek.runtime.physics;

import tek.runtime.GameObject;
import tek.runtime.Physics;

public class CircleCollider extends Collider {
	private float radius;
	
	public CircleCollider(GameObject parent, float radius){
		super(parent);
		this.radius = radius;
		this.body = Physics.instance.getCircleBody(position, radius, this.type.type);
		this.fixture = body.getFixtureList();
	}
	
	public float getRadius(){
		return radius;
	}
}
