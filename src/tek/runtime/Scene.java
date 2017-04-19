package tek.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import tek.render.Shader;

public class Scene {
	
	public ArrayList<Transform> transforms;
	public HashMap<Shader, ArrayList<GameObject>> renderables; 
	
	{
		transforms = new ArrayList<Transform>();
		renderables = new HashMap<Shader, ArrayList<GameObject>>();
	}
	
	public Scene(){
		
	}
	
	public void update(long delta){
		
	}
	
	public void render(long delta){
		
	}
	
	public void destroy(boolean fullExit){
		if(fullExit){
			for(Shader shader : renderables.keySet()){
				shader.destroy();
			}
			
			for(ArrayList<GameObject> gameObjects : renderables.values()){
				gameObjects.forEach((gameObject)-> gameObject.destroy());
			}
		}
	}
}
