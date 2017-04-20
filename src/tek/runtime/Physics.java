package tek.runtime;

import java.util.ArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Physics {
	public static Physics instance = null;
	
	public World world;
	private Vector2f gravity;

	public ArrayList<PhysicsBody> bodies;
	
	{
		gravity = new Vector2f();
		bodies = new ArrayList<PhysicsBody>();
	}
	
	public Physics(){
		instance = this;
		world = new World(new Vec2(0, 0));
	}
	
	public Physics(Vector2f gravity){
		instance = this;
		this.gravity.set(gravity);
		world = new World(new Vec2(gravity.x, gravity.y));
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
	
	public PhysicsBody findBody(Body b){
		for(PhysicsBody body : bodies){
			if(body.body == b)
				return body;
		}
		return null;
	}
	
	public void destroy(PhysicsBody body){
		world.destroyBody(body.body);
		bodies.remove(body);
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
	
	public abstract class PhysicsCallback implements ContactListener {
		
		public abstract void onCollisionEnter(PhysicsResponse response);
		public abstract void onCollisionExit(PhysicsResponse response);
		
		PhysicsBody parent;
		
		public PhysicsCallback(PhysicsBody body){
			parent = body;
		}
		
		@Override
		public void beginContact(Contact other) {
			Fixture o = other.getFixtureA();
			
			if(parent.hasFixture(o)){
				o = other.getFixtureB();
			}
			
			Body b = o.m_body;
			onCollisionEnter(new PhysicsResponse(Physics.instance.findBody(b)));
		}
		
		
		@Override
		public void endContact(Contact other) {
			
			
		}
		@Override
		public void postSolve(Contact other, ContactImpulse impulse) {
		}
		@Override
		public void preSolve(Contact other, Manifold manifold) {
		}
	}
	
	public class PhysicsResponse {
		public final PhysicsBody other;
		
		public PhysicsResponse(PhysicsBody other){
			this.other = other;
		}
	}
	
	public static class PhysicsBody {
		public final Transform transform;
		private Vector2f size;
		private Vector2f lastPosition;
		
		private float mass = 1f;
		private TBodyType type = TBodyType.DYNAMIC;
		
		private float gravityScale = 1f;
		
		public PhysicsCallback callback;
		
		private Body body;
		
		{
			size = null;
			lastPosition = new Vector2f();
		}
		
		public PhysicsBody(Transform transform){
			this.transform = transform;
			body = Physics.instance.getBody(transform, TBodyType.STATIC, true, null);
		}
		
		public PhysicsBody(Transform transform, Vector2f overrideSize){
			this.transform = transform;
			size = new Vector2f(overrideSize);
			body = Physics.instance.getBody(transform, TBodyType.STATIC, true, overrideSize);
		}
		
		public void checkUpdate(){
			if(!lastPosition.equals(transform.position)){
				body.setTransform(new Vec2(transform.position.x, transform.position.y),
						(float)Math.toRadians(transform.rotation));
			}
		}
		
		protected boolean hasFixture(Fixture fixture){
			Fixture n = body.m_fixtureList;
			
			while(n != null){
				if(n == fixture){
					return true;
				}
				n = n.m_next;
			}
			return false;
		}
		
		protected void postStep(){
			Vec2 bodyPos = body.getPosition();
			float bodyAngle = body.getAngle();
			
			transform.setPosition(bodyPos.x, bodyPos.y);
			transform.setRotation(bodyAngle);
		}
		
		public void destroy(){
			Physics.instance.destroy(this);
			body = null;
		}
		
		public boolean isSizeOverriden(){
			return size != null;
		}
		
		public Vector2f getPosition(){
			return transform.getPosition();
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
		
		public void setGravityScale(float gravityScale){
			body.setGravityScale(gravityScale);
			this.gravityScale = gravityScale;
		}
		
		public float getGravityScale(){
			return gravityScale;
		}
		
		public Vector2f getSize(){
			if(isSizeOverriden()){
				return new Vector2f(this.size);
			}else{
				return new Vector2f(transform.size);
			}
		}
		
		public void applyForce(Vector2f force){
			body.applyForceToCenter(new Vec2(force.x, force.y));
		}
		
		public void applyForce(Vector2f force, Vector2f point){
			body.applyForce(new Vec2(force.x, force.y), new Vec2(point.x, point.y));
		}
		
		public void applyAngularImpulse(float impulse){
			body.applyAngularImpulse(impulse);
		}
		
		public Vector2f getLinearVelocity(){
			Vec2 l = body.getLinearVelocity();
			return new Vector2f(l.x, l.y);
		}
		
		public float getAngularVelocity(){
			return body.getAngularVelocity();
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
