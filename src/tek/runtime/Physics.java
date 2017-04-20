package tek.runtime;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Physics {
	public static Physics instance = null;
	
	public World world;
	private Vector2f gravity;

	{
		gravity = new Vector2f();
	}
	
	public Physics(){
		instance = this;
	}
	
	public void update(long delta){
		world.step((float)delta, 8, 3);
	}
	
	public void setGravity(float x, float y){
		if(x == gravity.x && y == gravity.y)
			return;
		this.gravity.set(x, y);
		world.setGravity(new Vec2(gravity.x, gravity.y));
	}
	
	public Vector2f getGravity(){
		return new Vector2f(gravity);
	}
	
	public Body getBody(Transform transform, TBodyType type, boolean box, Vector2f size){
		
		BodyDef bd = new BodyDef();
		
		bd.position.set(transform.position.x, transform.position.y);
		
		Body body = world.createBody(bd);
		
		//set the body's type (dynamic, static, kinematic)
		body.m_type = type.getValue();
		
		if(box){
			PolygonShape polygon = new PolygonShape();
			if(size == null)
				polygon.setAsBox(transform.size.x, transform.size.y);
			else
				polygon.setAsBox(size.x, size.y);
				
			body.createFixture(polygon, 1.0f);
		}else{ //otherwise it's a circle
			CircleShape circle = new CircleShape();
			if(size == null)
				circle.setRadius(transform.size.x / 0.5f);
			else
				circle.setRadius(size.x / 0.5f);
			
			body.createFixture(circle, 1.0f);
		}
		
		return body;
	}
	
	public static class PhysicsBody {
		public final Transform transform;
		private Vector2f size, offset;
		private Vector2f lastPosition, lastSize;
		
		private float mass = 1f;
		private TBodyType type = TBodyType.DYNAMIC;
		
		private Body body;
		
		{
			size = null;
			offset = new Vector2f();
			lastPosition = new Vector2f();
			lastSize = new Vector2f();
		}
		
		public PhysicsBody(Transform transform){
			this.transform = transform;
		}
		
		public PhysicsBody(Transform transform, Vector2f overrideSize){
			this.transform = transform;
			size = new Vector2f(overrideSize);
		}
		
		public void checkUpdate(){
			if(!lastPosition.equals(transform.position)){
				body.setTransform(new Vec2(transform.position.x, transform.position.y),
						(float)Math.toRadians(transform.rotation));
			}
			
			if(lastSize.equals(getSize())){
				
			}
		}
		
		public boolean isSizeOverriden(){
			return size != null;
		}
		
		public Vector2f getPosition(){
			return transform.getPosition().add(offset);
		}
		
		public void setSize(Vector2f size){
			if(this.size != null)
				this.size.set(size);
			else
				transform.setSize(size);
		}
		
		public float getMass(){
			return mass;
		}
		
		public void setMass(float mass){
			body.m_mass = mass;
			this.mass = mass;
		}
		
		public Vector2f getSize(){
			if(isSizeOverriden()){
				return new Vector2f(this.size);
			}else{
				return new Vector2f(transform.size);
			}
		}
		
		public TBodyType getType(){
			return type;
		}
		
		public void setBodyType(TBodyType type){
			body.m_type = type.getValue();
			this.type = type;
		}
	}
	
	public static enum TBodyType {
		DYNAMIC(BodyType.DYNAMIC),
		STATIC(BodyType.STATIC),
		KINEMATIC(BodyType.KINEMATIC);
		
		BodyType value;
		
		TBodyType(BodyType value){
			this.value = value;
		}
		
		public BodyType getValue(){
			return value;
		}
		
	}
}
