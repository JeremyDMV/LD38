package tek.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import tek.game.gameObjects.LargeObject;
import tek.render.Camera;
import tek.render.Shader;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem.Particle;
import tek.ui.UIScene;

public class Scene {
	public static Scene current = null;
	
	public Shader defaultShader = null;	
	
	public Camera camera;
	
	public ArrayList<GameObject> gameObjects;
	public HashMap<Shader, ArrayList<GameObject>> renderables; 
	
	public Physics physics;
	public ArrayList<ParticleSystem> particleSystems;
	
	public UIScene uiScene;
	
	{
		camera = new Camera();
		
		gameObjects = new ArrayList<GameObject>();
		renderables = new HashMap<Shader, ArrayList<GameObject>>();
		
		particleSystems = new ArrayList<ParticleSystem>();
		
		physics = new Physics();
		
		uiScene = new UIScene();
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
	
	public void input(long delta){
		uiScene.input(delta);
	}
	
	public void update(long delta){
		for(GameObject gameObject : gameObjects){
			gameObject.update(delta);
		}
		
		for(ParticleSystem system : particleSystems){
			system.update(delta);
		}
		
		uiScene.update(delta);
		
		//check for updates and fix any world positioning
		physics.prep();
		
		//update the physics world
		physics.update(delta);
		
		//update the transforms to move with
		//their physical representations
		physics.step();
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
					shader.set("FLIP_X", gameObject.flipX);
					shader.set("FLIP_Y", gameObject.flipY);
					
					//shader.set("TEXTURE_REPEAT", gameObject.textureRepeat);
					
					if(TextureSheet.isTextureSheet(gameObject.texture)){
						if(gameObject.subTexture != -1){
							shader.set("SUB_TEXTURE", true);
							
							TextureSheet sheet = TextureSheet.getSheet(gameObject.texture);
							
							if(gameObject instanceof LargeObject){
								LargeObject l = (LargeObject)gameObject;
								shader.set("SUB_SIZE", sheet.subSize.mul(new Vector2f(l.tilesX,l.tilesY),
										new Vector2f()));
							}else{
								shader.set("SUB_SIZE", sheet.subSize);
							}
							
						
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
		
		uiScene.render();
	}
	
	public void render(ParticleSystem psystem){
		if(psystem.particles.size() == 0)
			return;
		
		psystem.render();
		
		psystem.shader.bind();
		psystem.shader.set("PROJECTION_MAT", camera.getProjection());
		psystem.shader.set("VIEW_MAT", camera.getView());
		
		Matrix4f src = psystem.transform.mat;
		
		psystem.texture.bind();
		psystem.shader.set("SUB_TEXTURE", 1);
		TextureSheet sheet = TextureSheet.getSheet(psystem.texture);
		
		psystem.shader.set("SUB_SIZE", sheet.subSize);
		psystem.shader.set("TEXTURE_OFFSET", sheet.getOffset(psystem.subTexture));
		psystem.shader.set("TEXTURE_SIZE", sheet.texture.size);
		
		for(Particle particle : psystem.particles){
			Matrix4f mat = new Matrix4f(src);
			mat.translate(particle.position.x, particle.position.y, 0);
			psystem.shader.set("MODEL_MAT", mat);
			
			GameObject.quad.draw();
		}
		
		Shader.unbind();
	}
	
	public void swap(){
		gameObjects.clear();
	}
	
	public void destroy(boolean fullExit){
		gameObjects.clear();
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
