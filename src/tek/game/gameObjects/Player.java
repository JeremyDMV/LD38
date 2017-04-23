package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.input.Keyboard;
import tek.render.Animation;
import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.physics.BoxCollider;
import tek.runtime.physics.Collider.ColliderType;

public class Player extends GameObject {
	public static Player instance;
	
	public int maxHealth = 10;
	public int health = maxHealth;
	
	public long hitTimer = 500; //5 seconds
	public long lastHit;
	private boolean lastHitActive = false;
	
	public float moveSpeed = 6.0f;
	public float walkSpeed = 6.0f;
	public float runSpeed = 8.0f;
	
	private float idleRate = 5000f;
	private float idleTimer = 0f;
	
	public Player(){
		setCollider(new BoxCollider(this, new Vector2f(10f, 10f), ColliderType.DYNAMIC));
		this.transform.setSize(10f, 10f);
		
		this.transform.setLayer(5);
		TextureSheet sheet = TextureSheet.getSheet("default");
		this.texture = sheet.texture;
		addAnimations(new Animation[]{
				Animation.getAnimation("walk", sheet, 112, 4, 1000f / 10f), 
				Animation.getAnimation("idle", sheet, 128, 5, 1000f / 20f), 
		});
		
		this.animations.get(0).setLoop(true);
		this.animations.get(1).setLoop(false);
		instance = this;
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
		//double adjustedDelta = (double)delta/1000d;
		float v = Keyboard.getButton("vertical");
		float h = Keyboard.getButton("horizontal");
		
		if(h > 0){
			flipX = false;
		}else if(h < 0){
			flipX = true;
		}
		
		if(h == 0 && v == 0){
			idleTimer -= delta;
			
			if(currentAnimation != 1){
				this.stopAnimation();
				this.setAnimation(1);
				this.playAnimation();
				
				idleTimer = idleRate;
			}else{
				setAnimation(1);
			}
			
			if(idleTimer <= 0){
				this.playAnimation();
				idleTimer = idleRate;
			}
		}else{
			if(!this.animations.get(currentAnimation).getName().equals("walk")){
				this.setAnimation(0);
				this.playAnimation();
			}
		}
		
		float moveSpeed = (float)(this.moveSpeed * 50);
		collider.setVelocity(new Vector2f(h * moveSpeed, v * moveSpeed));
	}
	
	@Override
	public void Update(long delta) {
		if(lastHit > 0L){
			lastHit -= delta;
			lastHitActive = lastHit < 0 ? false : true;
		}
		
	}
}
