package tek.audio;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

import java.nio.FloatBuffer;

public class Source {
	public final int id;
	private boolean dead = false;
	
	private boolean enabled = false;
	private boolean play = false, pause = false; //stopped = false false, paused = 0 + 1 or 1 + 1, playing = 1 + 0
	private boolean loop = false;
	
	private float gain = 1.0f;
	
	public Vector3f position;
	
	{
		position = new Vector3f();
	}
	
	public Sound sound;
	
	public Source(){
		id = alGenSources();
	}
	
	public Source(Sound sound){
		this.sound = sound;
		
		
		id = alGenSources();
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
		AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, position.z);
		
		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			System.err.println("AL ERROR");
	}
	
	public void setPosition(Vector2f position){
		this.position.set(position.x, position.y, 0f);
		AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, 0f);
	}
	
	public void setSound(Sound sound){
		if(this.sound == sound)
			return;
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
	}
	
	public void enable(){
		enabled = true;
	}
	
	public void disable(){
		enabled = false;
		if(isPlaying()){
			pause();
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setGain(float gain){
		this.gain = gain;
		AL10.alSourcef(id, AL10.AL_GAIN, gain);
	}
	
	public float getGain(){
		return gain;
	}
	
	public void play(){
		if(isPlaying())
			return;
		play = true;
		pause = false;
		alSourcePlay(id);
	}
	
	public void pause(){
		if(isPaused())
			return;
		pause = true;
		alSourcePause(id);
	}
	
	public void stop(){
		if(isStopped())
			return;
		pause = false;
		play = false;
		alSourceStop(id);
	}
	
	public boolean isPlaying(){
		return play && !pause;
	}
	
	public boolean isPaused(){
		return pause;
	}
	
	public boolean isStopped(){
		return !play && !pause;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setLoop(boolean loop){
		this.loop = loop;
		alSourcei(id, AL_LOOPING, (loop) ? 1 : 0);
	}
	
	public boolean isLooping(){
		return loop;
	}
	
	public void destroy(){
		if(dead)
			return;
		
		alDeleteSources(id);
		dead = true;
	}
}
