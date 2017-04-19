package tek.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Quad {
	public final float width, height;
	public final int vao, vbo, vto, vboi;
	
	public Quad(float width, float height){
		this.width = width;
		this.height = height;
		
		vao = GL30.glGenVertexArrays();
		
		GL30.glBindVertexArray(vao);
		
		vbo = GL15.glGenBuffers();
		vto = GL15.glGenBuffers();
		
		vboi = GL15.glGenBuffers();
		
		float hw = width * 0.5f, 
			  hh = height * 0.5f;
		
		float[] verts = new float[]{
			-hw, -hh, 0, //bottom left
			-hw,  hh, 0, //top left
			 hw,  hh, 0, //top right
			 hw, -hh, 0, //bottom right	
		};
		
		float[] texcoords = new float[]{
			0,1,	
			0,0,
			1,0,
			1,1
		};
		
		int[] indices = new int[]{
			0,1,2,
			2,3,0
		};
		
		FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(12);
		vertBuffer.put(verts).flip();
		
		FloatBuffer texBuffer = BufferUtils.createFloatBuffer(8);
		texBuffer.put(texcoords).flip();
		
		IntBuffer indBuffer = BufferUtils.createIntBuffer(6);
		indBuffer.put(indices).flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vto);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
	}
	
	public void draw(){
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(vao);
	}
	
	public void destroy(){
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(vto);
		GL15.glDeleteBuffers(vboi);
	}
}
