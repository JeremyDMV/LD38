package tek.runtime;

import java.util.Arrays;

import tek.audio.Source;
import tek.render.Animation;
import tek.render.Quad;
import tek.render.Shader;
import tek.render.Texture;
import tek.runtime.Physics.PhysicsBody;

public abstract class GameObject {
	public static Quad quad = new Quad(1f, 1f);
	
	//general rendering
	public Shader shader = Scene.current.defaultShader;
	
	//object information
	public String[] tags;
	public Transform transform;
	
	//texturing
	public Texture texture;
	public int subTexture = -1;
	
	public int currentAnimation = -1;
	public Animation[] animations;
	
	//sound 
	public Source source;
	
	//physics
	PhysicsBody physicsBody;
	
	{
		transform = new Transform(this);
	}
	
	public GameObject(){
		Start();
	}
	
	public GameObject(GameObject gameObject){
		this.shader = gameObject.shader;
		this.transform = new Transform(this, gameObject.transform);
		
		this.tags = Arrays.copyOf(gameObject.tags, gameObject.tags.length);
		
		this.texture = gameObject.texture;
		this.subTexture = gameObject.subTexture;
		
		this.animations = gameObject.animations;
		this.currentAnimation = gameObject.currentAnimation;
		
		Start();
	}
	
	public void update(long delta){
		if(currentAnimation != -1){
			animations[currentAnimation].update((float)delta);
			subTexture = animations[currentAnimation].getFrame();
		}
		
		if(source != null){
			if(!source.position.equals(transform.position)){
				source.setPosition(transform.position);
			}
		}
		
		Update(delta);
	}
	
	public abstract void Start();
	
	public abstract void Update(long delta);
	
	public void playAnimation(){
		animations[currentAnimation].play();
		animations[currentAnimation].setLoop(false);
	}
	
	public void stopAnimation(){
		if(currentAnimation != -1){
			animations[currentAnimation].stop();
		}
	}
	
	
	public void addAnimation(Animation anim){
		if(animations == null){
			animations = new Animation[1];
			animations[0] = anim;
		}else{
			animations = Arrays.copyOf(animations, animations.length + 1);
			animations[animations.length - 1] = anim;
		}
	}
	
	public void setAnimation(int animation){
		if(currentAnimation == animation)
			return;
		
		currentAnimation = animation;
		
		if(this.texture != animations[animation].getSource().texture)
			this.texture = animations[animation].getSource().texture;
	}
	
	public void setAnimation(String animationName){
		if(animations[currentAnimation].getName().equals(animationName))
			return;
		
		int index = -1;
		
		for(int i=0;i<animations.length;i++){
			if(animations[i].getName().equals(animationName)){
				index = i;
				continue;
			}
		}
		
		//if no match found
		if(index == -1)
			return;
		
		if(this.texture != animations[index].getSource().texture)
			this.texture = animations[index].getSource().texture;
	}
	
	public void destroy(){
		quad.destroy();
	}
	
	public void addTag(String tag){
		if(tags == null){
			tags = new String[1];
			tags[0] = tag;
		}else{
			tags = Arrays.copyOf(tags, tags.length + 1);
			tags[tags.length - 1] = tag;
		}
	}
	
	public boolean hasTag(String tag){
		if(tags == null)
			return false;
		for(String _tag : tags){
			if(_tag.equals(tag))
				return true;
		}
		return false;
	}
}
