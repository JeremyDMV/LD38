package tek.runtime;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Transform {
	public static final float LAYER_MOD = .01f;
	
	private boolean updateNeeded = false;
	
	protected Matrix4f mat;
	
	protected Vector2f position, size;
	protected float rotation;
	protected int layer;
	
	protected GameObject parent;
	
	{
		mat = new Matrix4f();
		position = new Vector2f();
		size    = new Vector2f();
		rotation = 0;
		layer    = 0;
	}
	
	public Transform(GameObject parent){
		this.parent = parent;
	}
	
	public Transform(GameObject parent, Vector2f position){
		this.parent = parent;
		this.position.set(position);
		
		updateMatrix();
	}
	
	public Transform(GameObject parent, Vector2f position, float rotation){
		this.parent = parent;
		this.position.set(position);
		this.rotation = rotation;
		
		updateMatrix();
	}
	
	public Transform(GameObject parent, Vector2f position, float rotation, Vector2f size){
		this.parent = parent;
		this.position.set(position);
		this.rotation = rotation;
		this.size.set(size);
		
		updateMatrix();
	}
	
	public Transform(GameObject parent, Transform t){
		this.parent = parent;
		this.mat.set(t.mat);
		this.position.set(t.position);
		this.size.set(t.size);
		this.rotation = t.rotation;
	}
	
	public void setParent(GameObject gameObject){
		this.parent = gameObject;
	}
	
	public boolean isUpdateNeeded(){
		return updateNeeded;
	}
	
	public void updateMatrix(){
		mat.identity();
		mat.translate(position.x, position.y, -layer * LAYER_MOD);
		mat.rotateZ((float)Math.toRadians(rotation));
		mat.scale(size.x, size.y, 1f);
		updateNeeded = false;
	}
	
	public void setRotation(float deg){
		rotation = deg;
		updateNeeded = true;
	}
	
	public void setLayer(int layer){
		if(this.layer == layer)
			return;
		this.layer = layer;
		updateMatrix();
	}
	
	public int getLayer(){
		return layer;
	}
	
	public void setSize(Vector2f vec){
		setSize(vec.x, vec.y);
	}
	
	public void setSize(float x, float y){
		size.set(x, y);
		updateNeeded = true;
	}
	
	public void setPosition(float x, float y){
		position.set(x, y);
		updateNeeded = true;
	}
	
	public void setPosition(Vector2f vec){
		setPosition(vec.x, vec.y);
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
	
	public Vector2f getSize(){
		return new Vector2f(size);
	}
	
	public Matrix4f getMat(){
		return new Matrix4f(mat);
	}
}
