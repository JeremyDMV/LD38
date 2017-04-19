package tek.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_sample_offset;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_seek;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_seek_start;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_seconds;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

import tek.ResourceLoader;

public class Music {
	final String path;
	final Decoder decoder;
	final IntBuffer buffers;
	final int source;
	
	private boolean loop = false;
	private boolean playing = false, pause = false;
	
	private float gain = 0.8f;
	
	private boolean started = false;
	
	public Music(String path){
		this.path = path;
		buffers = BufferUtils.createIntBuffer(2);
		AL10.alGenBuffers(buffers);
		decoder = new Decoder(path, buffers);
		
		source = AL10.alGenSources();
		AL10.alGenBuffers(buffers);
	}
	
	public void pause(){
		pause = true;
		playing = true;
		AL10.alSourcePause(source);
	}
	
	public boolean isPaused(){
		return pause;
	}
	
	public void play(){
		playing = true;
		if(!started){
			decoder.play(source);
			started = true;
		}else if(pause){
			decoder.update(source, loop);
			alSourcePlay(source);
			pause = false;
		}
	}
	
	public boolean isPlaying(){
		return playing && !pause;
	}
	
	public void stop(){
		alSourceStop(source);
		decoder.stop(source);
		playing = false;
		pause = false;
		started = false;
	}
	
	public void setLoop(boolean loop){
		this.loop = loop;
	}
	
	public boolean isLooping(){
		return loop;
	}
	
	public boolean isStopped(){
		return !pause && !playing;
	}
	
	public float getGain(){
		return gain;
	}
	
	public void setGain(float gain){
		if(this.gain == gain)
			return;
		alSourcef(source, AL_GAIN, gain);
		this.gain = gain;
	}
	
	public void update(){
		if(isPlaying()){
			decoder.update(source, loop);
			if(getRemainingTime() == 0f && !loop){
				stop();
			}
		}
	}
	
	public float getTime(){
		return decoder.getProgressTime();
	}
	
	public float getRemainingTime(){
		return decoder.lengthSeconds - decoder.getProgressTime();
	}
	
	public void destroy(){
		alDeleteBuffers(buffers);
		alDeleteSources(source);
		decoder.destroy();
	}
	
	protected static class Decoder {
		public static final int BUFFER_SIZE = 4096;
		final ByteBuffer vorbis;
		
		final long handle;
		final int channels;
		final int sampleRate;
		final int format;
		
		final int lengthSamples;
		final float lengthSeconds;

		final ShortBuffer pcm;
		
		int samplesLeft; //remaining samples
		
		IntBuffer buffers;
		
		private boolean dead = false;
		
		public Decoder(String path, IntBuffer buffers){
			this.buffers = buffers;
			
			vorbis = ResourceLoader.getBytes(path);
			IntBuffer error = BufferUtils.createIntBuffer(1);
			handle = stb_vorbis_open_memory(vorbis, error, null);
			
			if(handle == 0L){
				throw new RuntimeException("Unable to open stb");
			}
			
			STBVorbisInfo info = STBVorbisInfo.malloc();
			stb_vorbis_get_info(handle, info);
			channels = info.channels();
			sampleRate = info.sample_rate();
		
			format = (channels == 1) ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
			
			lengthSamples = stb_vorbis_stream_length_in_samples(handle);
			lengthSeconds = stb_vorbis_stream_length_in_seconds(handle);
			
			pcm = BufferUtils.createShortBuffer(BUFFER_SIZE);
			
			samplesLeft = lengthSamples;
		}
		
		public int getFormat(int format){
			switch(channels){
			case 1:
				return AL_FORMAT_MONO16;
			case 2:
				return AL_FORMAT_STEREO16;
			default:
				throw new UnsupportedOperationException("Unsupported channel count");
			}
		}
		
		private boolean stream(int buffer){
			int samples = 0;
			while(samples < BUFFER_SIZE){
				pcm.position(samples);
				int samplesPerChannel = stb_vorbis_get_samples_short_interleaved(handle, channels, pcm);
				if(samplesPerChannel == 0)
					break;
				samples += samplesPerChannel * channels;
			}
			
			if(samples == 0)
				return false;
			
			pcm.position(0);
			alBufferData(buffer, format, pcm, sampleRate);
			samplesLeft -= samples / channels;
			
			return true;
		}
		
		public float getProgress(){
			return 1.0f - (samplesLeft / (float)(lengthSamples));
		}
		
		public float getProgressTime(){
			return getProgress() * lengthSeconds;
		}
		
		public void rewind(){
			stb_vorbis_seek_start(handle);
			samplesLeft = lengthSamples;
		}
		
		public void stop(int source){
			for(int i=0;i<buffers.limit();i++)
				AL10.alSourceUnqueueBuffers(source);
			rewind();
		}
		
		public void skip(int direction){
			seek(Math.min(Math.max(0, stb_vorbis_get_sample_offset(handle) + direction * sampleRate), lengthSamples));
		}
		
		public void skipTo(float offset0to1){
			seek(Math.round(lengthSamples * offset0to1));
		}
		
		private void seek(int sampleNumber){
			stb_vorbis_seek(handle, sampleNumber);
			samplesLeft = lengthSamples - sampleNumber;
		}
		
		public boolean play(int source){
			for(int i=0;i<buffers.limit();i++)
				if(!stream(buffers.get(i)))
					return false;
			alSourceQueueBuffers(source, buffers);
			alSourcePlay(source);
			return true;
		}
		
		public boolean update(int source, boolean loop){
			if(dead)
				return false;
			
			int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
			
			for(int i=0;i<processed;i++){
				int buffer = AL10.alSourceUnqueueBuffers(source);
				if(!stream(buffer)){
					boolean close = true;
					if(loop){
						rewind();
						close = !stream(buffer);
					}
					
					if(close)
						return false;
				}
				AL10.alSourceQueueBuffers(source, buffer);
			}
			
			if(processed == buffers.limit()){
				AL10.alSourcePlay(source);
			}
			
			return true;
		}
		
		public void destroy(){
			dead = true;
			vorbis.clear();
			pcm.clear();
		}
		
		public boolean isDead(){
			return dead;
		}
	}
}