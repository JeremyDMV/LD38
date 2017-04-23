package tek.runtime;

import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Vector2f;

import tek.audio.Source;
import tek.render.Animation;
import tek.render.Quad;
import tek.render.Shader;
import tek.render.Texture;
import tek.runtime.physics.Collider;

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
	
	public int currentSubTexture = -1;
	
	public int currentAnimation = -1;
	public ArrayList<Animation> animations;
	
	public Vector2f textureRepeat;
	
	//sound 
	public Source source;
	
	//physics
	public Collider collider;
	
	public boolean flipX = false;
	public boolean flipY = false;
	
	
	{
		transform = new Transform(this);
		animations = new ArrayList<Animation>();
		textureRepeat = new Vector2f(1f);
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
		
		this.animations = new ArrayList<Animation>(gameObject.animations);
		this.currentAnimation = gameObject.currentAnimation;
		
		Start();
	}
	
	public void setCollider(Collider collider){
		if(this.collider == collider)
			return;
		this.collider = collider;
		Physics.instance.colliders.add(collider);
	}
	
	public void update(long delta){
		if(currentAnimation != -1){
			animations.get(currentAnimation).update((float)delta);
			currentSubTexture = animations.get(currentAnimation).getFrame();
		}else{
			currentSubTexture = subTexture;
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
		animations.get(currentAnimation).play();
	}
	
	public void stopAnimation(){
		if(currentAnimation != -1){
			animations.get(currentAnimation).stop();
			currentSubTexture = subTexture;;
		}
	}
	
	
	public void addAnimations(Animation[] anims){
		for(Animation anim : anims)
			addAnimation(anim);
	}
	
	public void addAnimation(Animation anim){
		if(animations == null){
			animations = new ArrayList<Animation>();
		}
		animations.add(anim);
	}
	
	public void setAnimation(int animation){
		if(currentAnimation == animation)
			return;
		
		currentAnimation = animation;
		
		if(this.texture != animations.get(animation).getSource().texture)
			this.texture = animations.get(animation).getSource().texture;
	}
	
	public void setAnimation(String animationName){
		if(currentAnimation != -1){
			if(animations.get(currentAnimation).getName().equals(animationName))
				return;
		}
		
		int index = -1;
		
		for(int i=0;i<animations.size();i++){
			if(animations.get(i).getName().toLowerCase().equals(animationName.toLowerCase())){
				index = i;
			}
		}
		
		if(this.texture != animations.get(index).getSource().texture)
			this.texture = animations.get(index).getSource().texture;
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
