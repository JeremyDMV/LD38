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

import tek.runtime.physics.Collider;

public class Physics {
	public static Physics instance = null;
	
	public World world;
	private Vector2f gravity;
	
	public ArrayList<Collider> colliders;
	
	public ContactTracker tracker;

	
	{
		colliders = new ArrayList<Collider>();
		gravity = new Vector2f();
		
		//setup the callback trackers
		setTracker(new ContactTracker(this));
	}
	
	public Physics(){
		world = new World(new Vec2(0, -10f));
		instance = this;
	}
	
	public Physics(Vector2f gravity){
		this.gravity.set(gravity);
		world = new World(new Vec2(gravity.x, gravity.y));
		instance = this;
	}
	
	
	public void update(long delta){
		double adjusted = delta / 1000d;
		world.step((float)(adjusted), 5, 2);
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
	
	public void setTracker(ContactListener listener){
		world.setContactListener(listener);
	}
	
	public Body getSquareBody(Vector2f position, Vector2f size, BodyType type){
		BodyDef def = new BodyDef();
		def.position.set(position.x, position.y);
		
		Body body = world.createBody(def);
		
		PolygonShape square = new PolygonShape();
		square.setAsBox(size.x, size.y);
		body.createFixture(square, 1.0f);
		
		body.setType(type);
		
		return body;
	}
	
	public Body getCircleBody(Vector2f position, float radius, BodyType type){
		BodyDef def = new BodyDef();
		def.position.set(position.x, position.y);
		
		Body body = world.createBody(def);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		body.createFixture(circle, 1.0f);
		
		body.setType(type);
		
		return body;
	}
	
	public static abstract interface CollisionCallback {
		public void onCollisionEnter(Collider collider);
		public void onCollisionExit(Collider collider);
	}
	
	public static class ContactTracker implements ContactListener {

		public Physics p;
		
		public ContactTracker(Physics instance){
			p = instance;
		}
		
		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.m_fixtureA;
			Fixture b = contact.m_fixtureB;
			
			Collider ca = null;
			Collider cb = null;
			
			for(Collider c : p.colliders){
				if(c.fixture == a)
					ca = c;
				if(c.fixture == b)
					cb = c;
			}
			
			ca.getCallback().onCollisionEnter(cb);
			cb.getCallback().onCollisionEnter(ca);
		}

		@Override
		public void endContact(Contact contact) {
			Fixture a = contact.m_fixtureA;
			Fixture b = contact.m_fixtureB;
			
			Collider ca = null;
			Collider cb = null;
			
			for(Collider c : p.colliders){
				if(c.fixture == a)
					ca = c;
				if(c.fixture == b)
					cb = c;
			}
			
			ca.getCallback().onCollisionExit(cb);
			cb.getCallback().onCollisionExit(ca);
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse arg1) {
			
		}

		@Override
		public void preSolve(Contact contact, Manifold arg1) {
			
		}
		
	}
}
