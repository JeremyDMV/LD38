package tek.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import tek.Application;

import static org.lwjgl.openal.ALC10.*;

public class Mixer {
	public static int DEFAULT_CHANNEL_COUNT = 3;
	public static Mixer instance = null;
	
	public MixerChannel[] channels;
	private int channelCount = 0;
	
	public Listener listener;
	
	private long device; //current OpenAL Device
	private long context;
	
	public Mixer(){
		channels = new MixerChannel[DEFAULT_CHANNEL_COUNT];

		listener = new Listener();
			
		//create OpenAL Instance
		initialize();
		
		instance = this;
	}
	
	private void initialize(){
		device = alcOpenDevice((ByteBuffer)null);
		if(device == 0L){
			Application.error("Unable to open default audio device");
			return;
		}
		
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		
		if(!deviceCaps.OpenALC10){
			Application.error("OpenALC10 Unsupported");
			return;
		}
		
		
		context = alcCreateContext(device, (IntBuffer)null);
		if(context == 0L){
			Application.error("Unable to create ALC Context");
			return;
		}
		
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
	}
	
	public void update(long delta){
		for(int i=0;i<channelCount; i++){
			channels[i].update(delta);
		}
	}
	
	public void checkError(){
		int error = AL10.alGetError();
		if(error == AL10.AL_NO_ERROR)
			return;
		Application.error(AL10.alGetString(error));
	}
	
	public static void destroy(){
		if(instance != null)
			instance.exit();
	}
	
	public void exit(){
		channels = null;
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
	
	public void createChannel(String name){
		if(channelCount == channels.length)
			return;
			
		MixerChannel c = new MixerChannel(name);
		channels[channelCount] = c;
		channelCount ++;
	}
	
	public int getChannelIndex(String channelName){
		for(int i=0;i<channelCount;i++){
			if(channels[i].getName().equals(channelName))
				return i;
		}
		return -1;
	}
	
	public MixerChannel getChannel(String channelName){
		if(channelCount == 0)
			return null;
		
		for(MixerChannel c : channels){
			if(c.getName().equals(channelName))
				return c;
		}
		return null;
	}	
	
	public void addTo(Source source, String channelName){
		MixerChannel channel = getChannel(channelName);
		//check to make sure the channel exists
		if(channel != null){
			channel.add(source);
		}
	}
	
	public void addTo(Music music, String channelName){
		MixerChannel channel = getChannel(channelName);
		//check to make sure the channel exists
		if(channel != null){
			channel.add(music);
		}
	}
	
	public void removeFrom(Source source, String channelName){
		MixerChannel channel = getChannel(channelName);
		
		if(channel != null)
			channel.remove(source);
	}
	
	public void removeFrom(Music music, String channelName){
		MixerChannel channel = getChannel(channelName);
		
		if(channel != null)
			channel.remove(music);
	}
	
	public void moveTo(Source source, String src, String dst){
		MixerChannel srcc = getChannel(src);
		MixerChannel dstc = getChannel(dst);
		
		if(srcc != null && dstc != null){
			srcc.remove(source);
			dstc.add(source);
		}
	}
	
	public void moveTo(Music music, String src, String dst){
		MixerChannel srcc = getChannel(src);
		MixerChannel dstc = getChannel(dst);
		
		if(srcc != null && dstc != null){
			srcc.remove(music);
			dstc.add(music);
		}
	}
	
	public static class MixerChannel {
		private String name;
		
		private float volume = 80f;
		private float gain = 0.8f;
		private boolean enabled = false;
		
		private ArrayList<Source> sources;
		private ArrayList<Music> musics;
		
		{
			sources = new ArrayList<Source>();
			musics = new ArrayList<Music>();
		}
		
		public MixerChannel(String name){
			this.name = name;
		}
		
		public void update(long delta){
			for(Music music : musics){
				music.update();
			}
			
			for(Source source : sources){
				source.update();
			}
		}
		
		public void add(Source source){
			if(!sources.contains(source)){
				sources.add(source);
				
				//adjust to the volume of the channel
				source.setGain(gain);
			}
		}
		
		public void add(Music music){
			if(!musics.contains(music)){
				musics.add(music);
				music.setGain(gain);
			}
		}
		
		public void remove(Source source){
			if(sources.contains(source))
				sources.remove(source);
		}
		
		public void remove(Music music){
			if(musics.contains(music))
				musics.remove(music);
		}
		
		public void clear(){
			sources.clear();
			musics.clear();
		}
		
		public int getSourceCount(){
			return sources.size() + musics.size();
		}
		
		public void setVolume(float volume){
			this.volume = volume;
			this.gain = volume / 100f;
			
			for(Source source : sources){
				source.setGain(gain);
			}
			
			for(Music music : musics)
				music.setGain(gain);
		}
		
		public void setGain(float gain){
			this.gain = gain;
			this.volume = gain * 100f;
			
			for(Source source : sources){
				source.setGain(gain);
			}
			
			for(Music music : musics)
				music.setGain(gain);
		}
		
		public void enable(){
			if(!enabled){
				for(Source source : sources){
					source.enable();
				}
				
				for(Music music : musics){
					music.enable();
				}
			}
			
			enabled = true;
		}
		
		public void disable(){
			if(enabled){
				for(Source source : sources){
					source.disable();
				}
				
				for(Music music : musics)
					music.disable();
			}
			
			enabled = false;
		}
		
		public boolean isEnabled(){
			return enabled;
		}
		
		public float getGain(){
			return gain;
		}
		
		public float getVolume(){
			return volume;
		}
		
		public String getName(){
			return name;
		}
	}
}
