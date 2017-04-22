package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.input.Keyboard;
import tek.render.Animation;
import tek.render.TextureSheet;
import tek.runtime.GameObject;

public class Player extends GameObject {
	public int maxHealth = 10;
	public int health = maxHealth;
	
	public long hitTimer = 5000; //5 seconds
	public long lastHit;
	private boolean lastHitActive = false;
	
	public float moveSpeed = 6.0f;
	public float walkSpeed = 6.0f;
	public float runSpeed = 8.0f;
	
	
	public Player(){
		this.transform.setSize(10f, 10f);
		this.subTexture = 1;
		TextureSheet sheet = TextureSheet.getSheet("default");
		this.texture = sheet.texture;
		addAnimations(new Animation[]{
				Animation.getAnimation("walk", sheet, 0, 3, 1000f / 20f), 
				Animation.getAnimation("walk1", sheet, 4, 3, 1000f / 20f), 
				Animation.getAnimation("walk2", sheet, 8, 3, 1000f / 20f), 
		});
	}
	
	public void set(int health, Vector2f position){
		this.health = health;
		this.transform.setPosition(position);
	}
	
	public void damage(int damage){
		if(damage < 0){
			lastHit = hitTimer;
			lastHitActive = true;
		}
		health -= damage;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public float getHealthRatio(){
		return health / maxHealth;
	}
	
	public boolean isLastHitActive(){
		return lastHitActive;
	}

	@Override
	public void Start() {
		
	}

	public void input(long delta){
		double adjustedDelta = (double)delta/1000d;
		float v = Keyboard.getButton("vertical");
		float h = Keyboard.getButton("horizontal");
		
		if(h > 0){
			flipX = false;
		}else if(h < 0){
			flipX = true;
		}
		
		transform.move((float)(h * moveSpeed * adjustedDelta),
				(float)(v * moveSpeed * adjustedDelta));
		
		if(Keyboard.isClicked('x')){
			this.setAnimation(0);
			this.playAnimation();
		}
	}
	
	@Override
	public void Update(long delta) {
		if(lastHit > 0L){
			lastHit -= delta;
			lastHitActive = lastHit < 0 ? false : true;
		}
		
	}
}
