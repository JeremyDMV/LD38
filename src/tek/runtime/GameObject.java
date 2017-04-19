package tek.runtime;

import tek.render.Quad;

public class GameObject {
	public static Quad quad;
	
	public Transform transform;
	
	public GameObject(){
		
	}
	
	public void destroy(){
		quad.destroy();
	}
}
