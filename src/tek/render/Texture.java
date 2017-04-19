package tek.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import tek.Application;
import tek.ResourceLoader;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

public class Texture {
	public static ArrayList<Texture> textures;
	
	static {
		textures = new ArrayList<Texture>();
	}
	
	public final int id, width, height, comp;
	public final String path;
	public final Vector2f size;
	
	private boolean dead = false;
	
	public Texture(String path){
		this.path = path;
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer c = BufferUtils.createIntBuffer(1);
		
		ByteBuffer data = ResourceLoader.getBytes(path);
		
		if(stbi_info_from_memory(data, w, h, c) != 1)
			Application.error("Unable to load:" + path);
		
		ByteBuffer formatted = stbi_load_from_memory(data, w, h, c, 0);
		
		if(formatted == null)
			Application.error("Unable to format file: " + path);
		
		width = w.get(0);
		height = h.get(0);
		comp   = c.get(0);
		
		size = new Vector2f(width, height);
		
		//load texture
		id = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, formatted);
		
		GL30.glGenerateMipmap(GL_TEXTURE_2D);
		
		stbi_image_free(formatted);
		
		formatted.clear();
		data.clear();
		
		w.clear();
		h.clear();
		c.clear();
		
		textures.add(this);
	}
	
	public void bind(){
		if(dead)
			return;
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public static void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void destroy(){
		if(dead)
			return;
		
		glDeleteTextures(id);
		dead = true;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(!o.getClass().equals(getClass())) return false;
		Texture t = (Texture)o;
		return t.id == id;
	}
}