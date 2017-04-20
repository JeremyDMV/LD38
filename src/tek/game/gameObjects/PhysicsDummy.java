package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.Physics.PhysicsBody;
import tek.runtime.Physics.PhysicsCallback;
import tek.runtime.Physics.PhysicsResponse;
import tek.runtime.Physics.TBodyType;

public class PhysicsDummy extends GameObject {
	
	{
	}
	
	public PhysicsDummy(){
		super();
		
		texture = TextureSheet.sheets.get(0).texture;
		subTexture = 3;
		transform.setSize(10f, 10f);
		transform.setPosition(1f, 1f);
		
		this.setupPhysics(TBodyType.DYNAMIC);
		physicsBody.callback = new CollisionCallback(physicsBody);
	}
	
	public PhysicsDummy(Vector2f overrideSize){
		super();
		
		texture = TextureSheet.sheets.get(0).texture;
		subTexture = 3;
		transform.setSize(10f, 10f);
		transform.setPosition(1f, 1f);
		
		
		this.setupPhysics(TBodyType.DYNAMIC, overrideSize);
		physicsBody.callback = new CollisionCallback(physicsBody);
	}
	
	@Override
	public void Start() {
		
	}

	@Override
	public void Update(long delta) {
		
	}
	
	
	public class CollisionCallback extends PhysicsCallback {
		
		public CollisionCallback(PhysicsBody body) {
			super(body);
		}

		@Override
		public void onCollisionEnter(PhysicsResponse response) {
			System.out.println("COLLISION");
		}

		@Override
		public void onCollisionExit(PhysicsResponse response) {
			System.out.println("Collision left");
		}
	};
}
