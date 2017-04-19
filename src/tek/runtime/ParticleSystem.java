package tek.runtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import tek.render.Shader;

public class ParticleSystem {
	public static Shader defaultShader = null;
	
	public Transform transform;
	public Vector2f particleVelocity;
	public float particleSize = 1; //in units
	public float particleLife = 1; //in seconds (1000ms)
	
	public Vector3f startColor, endColor;  
	
	
	public Shader shader = defaultShader; 
	
	public float emitRate = 20; //x per second 
	public float emitLife = 10; //how long the emission continues
	
	private float emitRemaining = 0; //how much longer the system will emit
	
	private float emitDelta = emitRate / 1000; //convert to MS
	private float emitAccumulator = 0;
	
	private boolean emitting = false; // is it emitting
	
	public Random random; //
	private long seed; // the seed of the generator
	
	public  ArrayList<Particle> particles;
	private ArrayList<Particle> toRemove;

	{
		transform = new Transform();
		particleVelocity = new Vector2f();
		seed = new Date().getTime();
		random = new Random(seed);
		
		startColor = new Vector3f(1f);
		endColor = new Vector3f(1f);
		
		particles = new ArrayList<Particle>();
		toRemove = new ArrayList<Particle>();
	}
	
	public ParticleSystem(){
	}
	
	public ParticleSystem(Vector2f position){
		transform.setPosition(position);
	}
	
	public ParticleSystem(Vector2f position, Vector2f size){
		transform.setPosition(position);
		transform.setSize(size);
	}
	
	public ParticleSystem(Vector2f position, Vector2f size, Vector2f particleVelocity){
		transform.setPosition(position);
		transform.setSize(size);
		this.particleVelocity.set(particleVelocity);
	}
	
	public void setSeed(long seed){
		this.seed = seed;
		random.setSeed(seed);
	}
	
	public long getSeed(){
		return seed;
	}
	
	public void emit(){
		emitting = true;
		emitRemaining = emitLife * 1000;
		emitDelta = 1000 / emitRate;
		emitAccumulator = emitDelta;
	}
	
	public float getRemaining(){
		return emitRemaining;
	}
	
	public void update(long delta){
		emitRemaining -= delta;
		
		if(emitRemaining <= 0){
			emitting = false;
		}
		
		double adjustedDelta = delta / 1000d;
		
		Vector2f velDelta = particleVelocity.mul((float)adjustedDelta, new Vector2f());
		
		for(Particle particle : particles){
			particle.position.add(velDelta);
			particle.life -= delta;
			
			if(particle.life <= 0)
				toRemove.add(particle);
		}
		
		particles.removeAll(toRemove);
		toRemove.clear();
		
		if(emitting){
			emitAccumulator -= delta;
			
			if(emitAccumulator <= 0){
				int toMax = (int)(emitAccumulator / emitDelta);
				toMax ++;
				for(int i=0;i<toMax; i++){
					//amount of time the particle has died before appearing
					float calcDeath = i * emitDelta; 
					
					//get a random position in the boundaries
					Vector2f pos = getRandomPosition();
					
					//add the back dated velocity 
					pos.add(particleVelocity.mul(calcDeath, new Vector2f()));
					
					//add the particle to the array
					particles.add(new Particle(pos, particleLife - calcDeath));
				}
				
				emitAccumulator = emitDelta;
			}
		}
	}
	
	public void render(){
		if(particles.size() == 0)
			return;
		
		Matrix4f mat = transform.mat;
		if(transform.isUpdateNeeded()){
			mat.identity();
			mat.scale(particleSize, particleSize, 1f);
			mat.translate(transform.position.x, transform.position.y, transform.layer);
			mat.rotateZ((float)Math.toRadians(transform.rotation));
		}
	}
	
	public boolean isEmitting(){
		return emitting;
	}
	
	public Vector2f getRandomPosition(){
		float x = random.nextFloat() * transform.size.x;
		float y = random.nextFloat() * transform.size.y;
		return new Vector2f(transform.position.x + x, transform.position.y + y).sub(transform.size.mul(0.5f, new Vector2f()));
	}
	
	public class Particle {
		public Vector2f position;
		float life;
		
		public Particle(Vector2f position, float life){
			this.position = new Vector2f(position);
			this.life = life;
		}
	}
	
}
