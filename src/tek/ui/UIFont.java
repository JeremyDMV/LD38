package tek.ui;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import tek.ResourceLoader;
import tek.Window;

public class UIFont {
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
	
	public float getWidth(String text){
		float w = 0; 
		FloatBuffer xbuf = BufferUtils.createFloatBuffer(1);
		FloatBuffer ybuf = BufferUtils.createFloatBuffer(1);
		
		STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
		for(char c : text.toCharArray()){
			if(c == '\n'){
				continue;
			}else if(c < 32 || c > 128){
				continue;
			}
			
			STBTruetype.stbtt_GetBakedQuad(cdata, 512, 512, (int)(c - 32), xbuf, ybuf, q, true);
			
			w += q.x1() - q.x0();
		}
		
		return w;
	}
	
	public Vector2f getWrappedSize(String text, float scale, int maxWidth){
		float maxw = Float.MIN_VALUE;
		Vector2f s = new Vector2f();
		
		FloatBuffer xbuf = BufferUtils.createFloatBuffer(1);
		FloatBuffer ybuf = BufferUtils.createFloatBuffer(1);
		
		STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
		for(char c : text.toCharArray()){
			if(c == '\n'){
				continue;
			}else if(c < 32 || c > 128){
				continue;
			}
			
			STBTruetype.stbtt_GetBakedQuad(cdata, 512, 512, (int)(c - 32), xbuf, ybuf, q, true);
			
			float w = q.x1() - q.x0();
			s.x += w;
			
			if(s.x >= maxWidth){
				maxw = Math.max(s.x, maxw);
				s.x = 0;
				s.y += (fontHeight * 2);
				s.x += w;
				
				xbuf.put(0, 0);
				ybuf.put(0, ybuf.get(0) + (fontHeight * 2));
			}
		}
		
		s.x = maxw;
		s.mul(scale);
		return s;
	}
	
	public float getHeight(){
		return fontHeight;
	}
	
	public void printWrapped(float x, float y, float scale, String text, float r, float g, float b, int wrapWidth){
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		GL11.glColor3f(r, g, b);
		
		GL11.glScalef(scale, scale, 1f);
		
		FloatBuffer xbuf = BufferUtils.createFloatBuffer(1);
		FloatBuffer ybuf = BufferUtils.createFloatBuffer(1);
		
		xbuf.put(x);
		ybuf.put(Window.instance.getHeight() - y);
		
		xbuf.flip();
		ybuf.flip();
		
		STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		for(char c : text.toCharArray()){
			if(c == '\n'){
				ybuf.put(0, ybuf.get(0) + (fontHeight * 2f));
				xbuf.put(0, x);
				continue;
			}else if(c < 32 || c > 128){
				continue;
			}
			
			float nextX = q.x1() * scale;
			if(nextX >= wrapWidth){
				xbuf.put(0, x);
				ybuf.put(0,ybuf.get(0) + (fontHeight * 2f));
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
		GL11.glPopMatrix();
	}
	
	public void print(float x, float y, float scale, String text, float r, float g, float b){
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		GL11.glColor3f(r, g, b);
		
		GL11.glScalef(scale, scale, 1f);
		
		FloatBuffer xbuf = BufferUtils.createFloatBuffer(1);
		FloatBuffer ybuf = BufferUtils.createFloatBuffer(1);
		
		xbuf.put(x);
		ybuf.put(Window.instance.getHeight() - y);
		
		xbuf.flip();
		ybuf.flip();
		
		STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		for(char c : text.toCharArray()){
			if(c == '\n'){
				ybuf.put(0, ybuf.get(0) + (fontHeight * 2f));
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
		GL11.glPopMatrix();
	}
}
