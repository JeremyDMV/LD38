package tek.runtime;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Transform {
	public static final float LAYER_MOD = 0.001f;
	
	private boolean updateNeeded = false;
	
	protected Matrix4f mat;
	protected Vector2f position, scale;
	protected float rotation;
	protected int layer;
	
	
	{
		mat = new Matrix4f();
		position = new Vector2f();
		scale    = new Vector2f();
		rotation = 0;
		layer    = 0;
	}
	
	public Transform(Vector2f position){
		this.position.set(position);
		
		updateMatrix();
	}
	
	public Transform(Vector2f position, float rotation){
		this.position.set(position);
		this.rotation = rotation;
		
		updateMatrix();
	}
	
	public Transform(Vector2f position, float rotation, Vector2f scale){
		this.position.set(position);
		this.rotation = rotation;
		this.scale.set(scale);
		
		updateMatrix();
	}
	
	public Transform(Transform t){
		this.mat.set(t.mat);
		this.position.set(t.position);
		this.scale.set(t.scale);
		this.rotation = t.rotation;
	}
	
	public boolean isUpdateNeeded(){
		return updateNeeded;
	}
	
	public void updateMatrix(){
		mat.identity();
		mat.scale(scale.x, scale.y, 1f);
		mat.translate(position.x, position.y, layer * LAYER_MOD);
		mat.rotateZ((float)Math.toRadians(rotation));
		updateNeeded = false;
	}
	
	public void move(float x, float y){
		position.add(x, y);
		updateNeeded = true;
	}
	
	public void move(Vector2f vec){
		move(vec.x, vec.y);
	}
	
	public void rotate(float deg){
		rotation += deg;
		updateNeeded = true;
	}
	
	public Vector2f getPosition(){
		return new Vector2f(position);
	}
	
	public Vector2f getScale(){
		return new Vector2f(scale);
	}
}
