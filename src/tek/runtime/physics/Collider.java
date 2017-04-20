package tek.runtime.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

import tek.runtime.GameObject;
import tek.runtime.Physics.CollisionCallback;

public abstract class Collider {
	public Body body;
	public Fixture fixture;
	public ColliderType type = ColliderType.DYNAMIC;
	
	protected CollisionCallback callback;
	
	protected GameObject parent;
	
	private float gravityScale = 1.0f;
	private float density = 1.0f;
	
	protected Vector2f position;
	protected float angle;
	
	{
		position = new Vector2f();
		angle    = 0f;
	}
	
	public Collider(GameObject parent){
		this.parent = parent;
	}
	
	public void setCallback(CollisionCallback callback){
		if(this.callback == callback)
			return;
		this.callback = callback;
	}
	
	public CollisionCallback getCallback(){
		return callback;
	}
	
	public void setColliderType(ColliderType type){
		if(this.type == type)
			return;
		body.setType(type.type);
		this.type = type;
	}
	
	public void applyForce(Vector2f vec){
		body.applyForceToCenter(new Vec2(vec.x, vec.y));
	}
	
	public void applyForce(Vector2f force, Vector2f point){
		body.applyForce(new Vec2(force.x, force.y), new Vec2(point.x, point.y));
	}
	
	public void applyLinearImpulse(Vector2f impulse, Vector2f point){
		body.applyLinearImpulse(new Vec2(impulse.x, impulse.y), new Vec2(point.x, point.y));
	}
	
	public void applyAngularImpulse(float impulse){
		body.applyAngularImpulse(impulse);
	}
	
	public void applyTorque(float torque){
		body.applyTorque(torque);
	}
	
	public void setGravityScale(float gravityScale){
		if(this.gravityScale == gravityScale)
			return;
		this.gravityScale = gravityScale;
		body.setGravityScale(gravityScale);
	}
	
	public float getGravityScale(){
		return gravityScale;
	}
	
	public Vector2f getPosition(){
		Vec2 vec = body.getPosition();
		return new Vector2f(vec.x, vec.y);
	}
	
	public void setPosition(Vector2f vec){
		if(this.position.equals(vec))
			return;
		
		this.position.set(vec);
		body.setTransform(new Vec2(vec.x, vec.y), angle);
	}
	
	public void setAngleDeg(float deg){
		float rad = (float)Math.toRadians(deg);
		if(rad == angle)
			return;
		
		this.angle = rad;
		body.setTransform(new Vec2(position.x, position.y), rad);
	}
	
	public void setAngle(float rad){
		if(rad == angle)
			return;
		
		this.angle = rad;
		body.setTransform(new Vec2(position.x, position.y), rad);
	}
	
	public void setDensity(float density){
		if(this.density == density)
			return;
		
		this.density = density;
		fixture.setDensity(density);
	}
	
	public float getDensity(){
		return density;
	}
	
	public static enum ColliderType {
		DYNAMIC(BodyType.DYNAMIC),
		KINEMATIC(BodyType.KINEMATIC),
		STATIC(BodyType.STATIC);
		
		BodyType type;
		
		ColliderType(BodyType type){
			this.type = type;
		}
	};

}
