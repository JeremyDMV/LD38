package tek.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import tek.render.Camera;
import tek.render.Shader;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem.Particle;
import tek.runtime.Physics.PhysicsBody;

public class Scene {
	public static Scene current = null;
	
	public Shader defaultShader = null;	
	
	public Camera camera;
	
	public ArrayList<GameObject> gameObjects;
	public HashMap<Shader, ArrayList<GameObject>> renderables; 
	
	//public UIScene uiScene;
	
	public Physics physics;
	public ArrayList<ParticleSystem> particleSystems;
	
	{
		camera = new Camera();
		
		gameObjects = new ArrayList<GameObject>();
		renderables = new HashMap<Shader, ArrayList<GameObject>>();
		
		particleSystems = new ArrayList<ParticleSystem>();
		
		physics = new Physics();
	}
	
	public Scene(){
		
	}
	
	public void makeCurrent(){
		current = this;
	}
	
	public void add(ParticleSystem particleSystem){
		particleSystems.add(particleSystem);
	}
	
	public void remove(ParticleSystem particleSystem){
		particleSystems.remove(particleSystem);
	}
	
	public void add(GameObject gameObject){
		Shader s = gameObject.shader;
		
		if(renderables.containsKey(s)){
			renderables.get(s).add(gameObject);
		}else{
			ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
			gameObjects.add(gameObject);
			renderables.put(s, gameObjects);
		}
		this.gameObjects.add(gameObject);
	}
	
	public void remove(GameObject gameObject){
		Shader s = gameObject.shader;
		if(!renderables.containsKey(s)){
			return;
		}
		renderables.get(s).remove(gameObject);
		this.gameObjects.remove(gameObject);
	}
	
	public void removeAll(GameObject...gameObjects){
		for(GameObject gameObject : gameObjects)
			remove(gameObject);
	}
	
	public void removeAll(ArrayList<GameObject> gameObjects){
		for(GameObject gameObject : gameObjects)
			remove(gameObject);
	}
	
	public void addAll(GameObject...gameObjects){
		for(GameObject gameObject : gameObjects)
			add(gameObject);
	}
	
	public void addAll(ArrayList<GameObject> gameObjects){
		for(GameObject gameObject : gameObjects)
			add(gameObject);
	}
	
	public void update(long delta){
		for(GameObject gameObject : gameObjects){
			gameObject.update(delta);
		}
		
		for(ParticleSystem system : particleSystems){
			system.update(delta);
		}
		
		//prepare the physics world for anything that has moved
		for(PhysicsBody body : physics.bodies){
			body.checkUpdate();
		}
		
		//update the physics world
		physics.update(delta);
		
		//update the transforms to realistically move with
		//their physical representations
		for(PhysicsBody body : physics.bodies){
			body.postStep();
		}
		
	}
		
	public void render(long delta){
		for(Shader shader : renderables.keySet()){
			ArrayList<GameObject> objects = renderables.get(shader);
			
			shader.bind();
			
			shader.set("PROJECTION_MAT", camera.getProjection());
			shader.set("VIEW_MAT", camera.getView());
			
			for(GameObject gameObject : objects){
				if(gameObject.transform.isUpdateNeeded()){
					gameObject.transform.updateMatrix();
				}
				
				shader.set("MODEL_MAT", gameObject.transform.mat);
				
				if(gameObject.texture != null){
					if(TextureSheet.isTextureSheet(gameObject.texture)){
						if(gameObject.subTexture != -1){
							shader.set("SUB_TEXTURE", true);
							
							TextureSheet sheet = TextureSheet.getSheet(gameObject.texture);
							
							shader.set("SUB_SIZE", sheet.subSize);
							shader.set("TEXTURE_OFFSET", sheet.getOffset(gameObject.subTexture));
							shader.set("TEXTURE_SIZE", sheet.texture.size);
						}
					}
					
					gameObject.texture.bind();
				}
				
				//static quad
				GameObject.quad.draw();
			}
			
			Shader.unbind();
		}
		
		for(ParticleSystem system : particleSystems){
			render(system);
		}
	}
	
	public void render(ParticleSystem psystem){
		if(psystem.particles.size() == 0)
			return;
		
		psystem.render();
		
		psystem.shader.bind();
		psystem.shader.set("projection", camera.getProjection());
		psystem.shader.set("view", camera.getView());
		psystem.shader.set("model", psystem.transform.mat);
		
		psystem.shader.set("particleZ", psystem.transform.layer * Transform.LAYER_MOD);
		
		for(Particle particle : psystem.particles){
			psystem.shader.set("particlePos", particle.position);
			//TODO Update the color transitions
			psystem.shader.set("particleColor", psystem.startColor);
			
			GameObject.quad.draw();
		}
		
		Shader.unbind();
	}
	
	public void destroy(boolean fullExit){
		if(fullExit){
			for(Shader shader : renderables.keySet()){
				if(shader != null)
					shader.destroy();
			}
			
			for(ArrayList<GameObject> gameObjects : renderables.values()){
				gameObjects.forEach((gameObject)-> gameObject.destroy());
			}
		}
	}
}
