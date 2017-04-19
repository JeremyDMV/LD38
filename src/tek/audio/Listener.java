package tek.audio;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class Listener {
	public static Listener instance = null;
	
	private Vector3f position, orientation;
	private FloatBuffer posBuffer, oriBuffer;
	private float gain = 1.0f;
	
	{
		position = new Vector3f();
		orientation = new Vector3f();
		posBuffer = BufferUtils.createFloatBuffer(3);
		oriBuffer = BufferUtils.createFloatBuffer(6);
	}
	
	protected Listener(){
		instance = this;
	}
	
	public float getGain(){
		return gain;
	}
	
	public void setGain(float gain){
		if(this.gain == gain)
			return;
		this.gain = gain;
		AL10.alListenerf(AL10.AL_GAIN, gain);
	}
	
	public Vector3f getPosition(){
		return new Vector3f(position);
	}
	
	public Vector3f getOrientation(){
		return new Vector3f(orientation);
	}
	
	public void setPosition(Vector3f position){
		if(position.equals(this.position))
			return;
		
		posBuffer.clear();
		posBuffer.put(new float[]{
			position.x,
			position.y,
			position.z,
		});
		posBuffer.flip();
		
		this.position.set(position);
		
		AL10.alListenerfv(AL10.AL_POSITION, posBuffer);
	}
	
	public void setPosition(Vector2f position){
		if(position.equals(this.position))
			return;
		
		posBuffer.clear();
		posBuffer.put(new float[]{
			position.x,
			position.y,
			0f,
		});
		posBuffer.flip();
		
		this.position.set(position.x, position.y, 0f);
		
		AL10.alListenerfv(AL10.AL_POSITION, posBuffer);
	}
	
	public void setOrientation(Vector3f orientation){
		if(this.orientation.equals(orientation)){
			return;
		}
		
		oriBuffer.clear();
		oriBuffer.put(new float[]{
				0f,1f,0f,
				orientation.x,
				orientation.y,
				orientation.z
		});
		oriBuffer.flip();
		
		this.orientation.set(orientation);
		
		AL10.alListenerfv(AL10.AL_ORIENTATION, oriBuffer);
	}
}
