package tek.audio;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import static org.lwjgl.openal.AL10.*;

import java.nio.FloatBuffer;

public class Source {
	public final int id;
	private boolean dead = false;
	
	private boolean enabled = false;
	private boolean play = false, pause = false; //stopped = false false, paused = 0 + 1 or 1 + 1, playing = 1 + 0
	private boolean loop = false;
	
	public boolean oneshot = false;
	
	private float referenceDistance = 1.0f;
	private float maxDistance = 10.0f;
	private float rolloffFactor = 1f;
	private boolean rolloff = true;
	
	private float gain = 1.0f;
	
	public Vector3f position;
	
	{
		id = alGenSources();
		
		position = new Vector3f();
		
		if(rolloff){
			AL10.alSourcei(id, AL10.AL_DISTANCE_MODEL, AL10.AL_INVERSE_DISTANCE);
			AL10.alSourcef(id, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
			AL10.alSourcef(id, AL10.AL_ROLLOFF_FACTOR, rolloffFactor);
			AL10.alSourcef(id, AL10.AL_MAX_DISTANCE, maxDistance);
			
			AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, position.z);
		}
	}
	
	public Sound sound;
	
	public Source(){
	}
	
	public Source(Sound sound){
		this.sound = sound;
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
		
		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			System.err.println("AL ERROR");
	}
	
	public void setPosition(Vector2f position){
		this.position.set(position.x, position.y, 0f);
		AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, 0f);
	}
	
	public Vector2f getPosition(){
		return new Vector2f(position.x, position.y);
	}
	
	public void setSound(Sound sound){
		if(this.sound == sound)
			return;
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
	}
	
	public void update(){
		int state = AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE);
		if(state != AL10.AL_PLAYING && state != AL10.AL_PAUSED)
			stop();
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
	
	public void setReferenceDistance(float ref){
		if(ref == referenceDistance)
			return;
		referenceDistance = ref;
		AL10.alSourcef(id, AL10.AL_REFERENCE_DISTANCE, ref);
	}
	
	public float getReferenceDistance(){
		return referenceDistance;
	}
	
	public void setMaxDistance(float max){
		if(max == maxDistance)
			return;
		maxDistance = max;
		AL10.alSourcef(id, AL10.AL_MAX_DISTANCE, max);
	}
	
	public float getMaxDistance(){
		return maxDistance;
	}
	
	public void setRolloffFactor(float rolloffFactor){
		if(this.rolloffFactor == rolloffFactor)
			return;
		this.rolloffFactor = rolloffFactor;
		AL10.alSourcef(id, AL10.AL_ROLLOFF_FACTOR, rolloffFactor);
	}
	
	public void enableRolloff(){
		if(rolloff)
			return;
		AL10.alSourcei(id, AL10.AL_DISTANCE_MODEL, AL10.AL_INVERSE_DISTANCE);
	}
	
	public void disableRolloff(){
		if(!rolloff)
			return;
		AL10.alSourcei(id, AL10.AL_DISTANCE_MODEL, AL10.AL_NONE);
	}
	
	public float getGain(){
		return gain;
	}
	
	public void play(){
		if(isPlaying() && oneshot)
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
