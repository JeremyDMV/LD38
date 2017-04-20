package tek.audio;

import static org.lwjgl.openal.AL10.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

import tek.Application;
import tek.ResourceLoader;

import static org.lwjgl.stb.STBVorbis.*;

/*
 * ALL SOUNDS ARE LIMITED TO OGG
 * FOR FILE SIZE REASONS
 */

public class Sound {
	public static final int MAX_SOURCES = 6;

	public static ArrayList<Sound> sounds;
	
	static{
		sounds = new ArrayList<Sound>();
	}
	
	public final int id;
	public final String path;
	
	private boolean dead = false;
	
	public int[] sources;
	private int sourceIndex = 0;
	
	public SoundType type = SoundType.SFX;
	
	{
		sources = new int[MAX_SOURCES];
	}
	
	public Sound(String path){
		this(path, SoundType.SFX);
	}
	
	public Sound(String path, SoundType type){
		this.path = path;
		this.type = type;
		
		if(ResourceLoader.exists(path))
			id = AL10.alGenBuffers();
		else
			id = -1;
		
		allocate();
		
		sounds.add(this);
	}
	
	private void allocate(){
		ByteBuffer buffer = ResourceLoader.getBytes(path);
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(buffer, error, null);
		if(decoder == 0L){
			Application.error("Unable to open STB Vorbis");
			return;
		}
		
		STBVorbisInfo info = STBVorbisInfo.malloc();
		
		stb_vorbis_get_info(decoder, info);
		
		int channels = info.channels();
		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);
		ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);
		pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
		stb_vorbis_close(decoder);
		
		AL10.alBufferData(id, info.channels() == 1? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
	}
	
	public boolean canListen(){
		return sourceIndex > MAX_SOURCES;
	}

	public void update(){
	}
	
	public void addSource(int id){
		sources[sourceIndex] = id;
		sourceIndex ++;
	}
	
	public void removeSource(int id){
		for(int i=0;i<MAX_SOURCES;i++){
			if(sources[i] == id){
				sources[i] = -1;
				sourceIndex --;
			}
		}
		
		int[] temp = new int[MAX_SOURCES];
		int tindex = 0;
		for(int i=0;i<MAX_SOURCES;i++){
			if(sources[i] != -1){
				temp[tindex] = sources[i];
				tindex++;
			}
		}
		
		sources = temp;
	}
	
	public void destroy(){
		alDeleteBuffers(id);
		dead = true;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public enum SoundType {
		SFX,
		ENVIRONMENTAL;
	}
	
	public static Sound get(String path){
		String lwr = path.toLowerCase();
		for(Sound sound : sounds){
			if(sound.path.toLowerCase().equals(lwr))
				return sound;
		}
		return null;
	}
	
}
