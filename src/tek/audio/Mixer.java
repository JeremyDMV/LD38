package tek.audio;

import java.util.ArrayList;

public class Mixer {
	public static int DEFAULT_CHANNEL_COUNT = 3;
	public static Mixer instance = null;
	
	public MixerChannel[] channels;
	private int channelCount = 0;
	
	public Listener listener;
	
	public Mixer(){
		channels = new MixerChannel[DEFAULT_CHANNEL_COUNT];

		listener = new Listener();
			
		//create OpenAL Instance
		
		instance = this;
	}
	
	public static void destroy(){
		if(instance != null)
			instance.exit();
	}
	
	public void exit(){
		channels = null;
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
	
	public void removeFrom(Source source, String channelName){
		MixerChannel channel = getChannel(channelName);
		
		if(channel != null)
			channel.remove(source);
	}
	
	public void moveTo(Source source, String src, String dst){
		MixerChannel srcc = getChannel(src);
		MixerChannel dstc = getChannel(dst);
		
		if(srcc != null && dstc != null){
			srcc.remove(source);
			dstc.add(source);
		}
	}
	
	public static class MixerChannel {
		private String name;
		
		private float volume = 80f;
		private float gain = 0.8f;
		private boolean enabled = false;
		
		private ArrayList<Source> sources;
		
		{
			sources = new ArrayList<Source>();
		}
		
		public MixerChannel(String name){
			this.name = name;
		}
		
		public void add(Source source){
			if(!sources.contains(source)){
				sources.add(source);
				
				//adjust to the volume of the channel
				source.setGain(gain);
			}
		}
		
		public void remove(Source source){
			if(sources.contains(source))
				sources.remove(source);
		}
		
		public void clear(){
			sources.clear();
		}
		
		public int getSourceCount(){
			return sources.size();
		}
		
		public void setVolume(float volume){
			this.volume = volume;
			this.gain = volume / 100f;
			
			for(Source source : sources){
				source.setGain(gain);
			}
		}
		
		public void setGain(float gain){
			this.gain = gain;
			this.volume = gain * 100f;
			
			for(Source source : sources){
				source.setGain(gain);
			}
		}
		
		public void enable(){
			if(!enabled){
				for(Source source : sources){
					source.enable();
				}
			}
			
			enabled = true;
		}
		
		public void disable(){
			if(enabled){
				for(Source source : sources){
					source.disable();
				}
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
