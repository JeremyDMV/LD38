package tek.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import tek.render.Camera;
import tek.render.Shader;
import tek.render.TextureSheet;

public class Scene {
	public static Scene current = null;
	
	public Shader defaultShader = null;	
	
	public Camera camera;
	
	public ArrayList<GameObject> gameObjects;
	public HashMap<Shader, ArrayList<GameObject>> renderables; 
	
	{
		camera = new Camera();
		
		gameObjects = new ArrayList<GameObject>();
		renderables = new HashMap<Shader, ArrayList<GameObject>>();
	}
	
	public Scene(){
	}
	
	public void makeCurrent(){
		current = this;
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
