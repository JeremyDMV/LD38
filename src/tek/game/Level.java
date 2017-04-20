package tek.game;

import java.io.File;
import java.util.ArrayList;

import tek.ResourceLoader;
import tek.audio.Music;
import tek.audio.Sound;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;

public abstract class Level {
	public abstract void start();
	public abstract void end();
	public abstract void update(long delta);
	
	public Music createMusic(String path){
		Music m = Music.get(path);
		if(m == null)
			m = new Music(path);
		return m;
	}
	
	public Sound createSound(String path){
		Sound s = Sound.get(path);
		if(s == null)
			s = new Sound(path);
		return s;
	}
	
	public Shader createShader(String name, String vertex, String fragment){
		if(Shader.exists(name) || Shader.exists(vertex, fragment)){
			return null;
		}
		
		return new Shader(name, vertex, fragment);
	}
	
	public TextureSheet createTextureSheet(String path, int subWidth, int subHeight, String name){
		TextureSheet get = TextureSheet.getSheet(name);
		
		if(get == null){
			get = TextureSheet.getSheetByPath(path);
			
			if(get != null)
				return get;
			
			return new TextureSheet(Texture.get(path), subWidth, subHeight, name);
		}
		
		return get;
	}
	
	public Texture createTexture(String path){
		return Texture.get(path);
	}
}
