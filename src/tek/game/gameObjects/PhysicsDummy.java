package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.Physics.CollisionCallback;
import tek.runtime.physics.Collider;

public class PhysicsDummy extends GameObject {
	
	{
		
	}
	
	public PhysicsDummy(){
		super();
		
		texture = TextureSheet.sheets.get(0).texture;
		subTexture = 3;
		transform.setSize(10f, 10f);
		transform.setPosition(1f, 1f);
	}
	
	public PhysicsDummy(Vector2f overrideSize){
		super();
		
		texture = TextureSheet.sheets.get(0).texture;
		subTexture = 3;
		transform.setSize(10f, 10f);
		transform.setPosition(1f, 1f);
	}
	
	public void setupCallback(){
		collider.setCallback(new CollisionCallback(){
			@Override
			public void onCollisionEnter(Collider collider) {
				System.out.println("OW");
			}

			@Override
			public void onCollisionExit(Collider collider) {
				System.out.println("FINALLY");
			}
		});
	}
	
	@Override
	public void Start() {
		
	}

	@Override
	public void Update(long delta) {
		
	}
}
