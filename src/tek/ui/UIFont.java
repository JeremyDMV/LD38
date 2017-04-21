package tek.ui;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import tek.ResourceLoader;
import tek.Window;
import tek.render.Shader;

public class UIFont {
	public static Shader defaultShader;
	
	private int texId;
	private float fontHeight = 16.0f;
	
	private STBTTBakedChar.Buffer cdata;
	
	public UIFont(String path, float fontHeight){
		ByteBuffer data = ResourceLoader.getBytes(path);
		
		cdata = STBTTBakedChar.malloc(96);
		
		ByteBuffer bitmap = BufferUtils.createByteBuffer(512 * 512);
		STBTruetype.stbtt_BakeFontBitmap(data, 32, bitmap, 512, 512, 32, cdata);
		
		texId = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, 512, 512, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	}

	public static void prepRender(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, Window.instance.getWidth(), Window.instance.getHeight(), 0.0, -1.0, 1.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void print(float x, float y, String text){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		FloatBuffer xbuf = BufferUtils.createFloatBuffer(1);
		FloatBuffer ybuf = BufferUtils.createFloatBuffer(1);
		
		xbuf.put(x);
		ybuf.put(y);
		
		xbuf.flip();
		ybuf.flip();
		
		STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		for(char c : text.toCharArray()){
			if(c == '\n'){
				ybuf.put(0, ybuf.get(0) + fontHeight);
				xbuf.put(0, x);
				continue;
			}else if(c < 32 || c > 128){
				continue;
			}
			
			STBTruetype.stbtt_GetBakedQuad(cdata, 512, 512, (int)(c - 32), xbuf, ybuf, q, true);
			

			GL11.glTexCoord2f(q.s0(), q.t0());
			GL11.glVertex2f(q.x0(), q.y0());

			GL11.glTexCoord2f(q.s1(), q.t0());
			GL11.glVertex2f(q.x1(), q.y0());

			GL11.glTexCoord2f(q.s1(), q.t1());
			GL11.glVertex2f(q.x1(), q.y1());

			GL11.glTexCoord2f(q.s0(), q.t1());
			GL11.glVertex2f(q.x0(), q.y1());
		}
		
		GL11.glEnd();
	}
}
