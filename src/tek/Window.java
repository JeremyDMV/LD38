package tek;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import tek.input.Mouse;
import tek.input.Keyboard;

public class Window {
	public static Window instance;
	
	/* WINDOW DEFAULTS */
	public static String defaultTitle = "LD38 TEST";
	public static int defaultWidth, defaultHeight;
	
	
	/* WINDOW INFO */
	private static boolean allowRender = false;
	
	private long handle;
	
	private String title;
	private int width, height;
	private int x = -1, y = -1;
	
	private boolean closeRequested = false;
	
	private float cr, cg, cb;
	
	{
		defaultWidth = 720;
		defaultHeight = 480;
	}
	
	public Window(){
		this(defaultWidth, defaultHeight);
	}
	
	public Window(int width, int height){
		this(width, height, Window.defaultTitle);
	}
	
	public Window(int width, int height, String title){
		this.width = width;
		this.height = height;
		this.title = title;
		
		instance = this;
	}
	
	public Window(int x, int y, int width, int height, String title){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
		
		instance = this;
	}
	
	public void pollEvents(){
		GLFW.glfwPollEvents();
	}
	
	public void swapBuffers(){
		GLFW.glfwSwapBuffers(handle);
	}
	
	public void show(){
		GLFW.glfwShowWindow(handle);
		setHint(GLFW.GLFW_VISIBLE, true);
	}
	
	public void hide(){
		GLFW.glfwHideWindow(handle);
		setHint(GLFW.GLFW_VISIBLE, false);
	}
	
	public void pauseRender(){
		allowRender = false;
	}
	
	public void resumeRender(){
		allowRender = true;
	}
	
	public void create(){
		if(!GLFW.glfwInit())
			throw new RuntimeException("Unable to initialize GLFW");
		
		GLFW.glfwDefaultWindowHints();
		
		setHint(GLFW.GLFW_VISIBLE, false);
		setHint(GLFW.GLFW_RESIZABLE, false);
		
		handle = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
		
		GLFW.glfwSetWindowPosCallback(handle, (handle, x, y )->{
			this.x = x;
			this.y = y;
		});
		
		GLFW.glfwSetWindowSizeCallback(handle, (handle, width, height) -> {
			if(this.height != height || this.width != width){
				this.width = width;
				this.height = height;
				resized();
			}
		});
		
		GLFW.glfwSetMouseButtonCallback(handle, (handle, button, action, mods) -> {
			Mouse.setButton(button, (action == GLFW.GLFW_PRESS) ? true :
				(action == GLFW.GLFW_REPEAT) ? true : false);
		});
		
		GLFW.glfwSetCursorPosCallback(handle, (handle, x, y) -> {
			Mouse.setPos(x, y);
		});
		
		GLFW.glfwSetCharCallback(handle, (handle, keyChar) -> {
			Keyboard.addChar((char)keyChar);
		});
		
		GLFW.glfwSetKeyCallback(handle, (handle, key, scancode, action, mods) -> {
			Keyboard.setKey(key, (action == GLFW.GLFW_PRESS) ? true : (action == GLFW.GLFW_REPEAT) ? true : false);
		});
		
		GLFW.glfwSetWindowCloseCallback(handle, (handle) -> {
			closeRequested = true;
		});
			
		setHint(GLFW.GLFW_SAMPLES, 0);
		GLFW.glfwMakeContextCurrent(handle);
		
		GL.createCapabilities();
		
		GLFW.glfwSwapInterval(1);		
		
		GL11.glViewport(0, 0, width, height);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glHint(GL11.GL_POINT_SMOOTH, GL11.GL_NICEST);
		
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_POINT_SMOOTH, GL11.GL_NICEST);
		
		GL11.glEnable(GL11.GL_PERSPECTIVE_CORRECTION_HINT);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		if(x == -1 && y == -1)
			center();
		else
			setPosition(x, y);
		
		setClearColor(0.3f, 0.3f, 0.3f);
		
		show();
		resumeRender();
	}
	
	public boolean isCloseRequested(){
		return closeRequested;
	}
	
	private void resized(){
		GL11.glViewport(0, 0, width, height);
	}
	
	public void center(){
		GLFWVidMode mon = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		setPosition((mon.width() - width) / 2, (mon.height() - height) / 2);
	}
	
	public void setClearColor(float r, float g, float b){
		r = (r > 1) ? r / 255 : r;
		g = (g > 1) ? g / 255 : g;
		b = (b > 1) ? b / 255 : b;
		if(cr == r && cg == g && cb == b)
			return;
		GL11.glClearColor(r, g, b, 1);
	}
	
	public void destroy(){
		GLFW.glfwDestroyWindow(handle);
	}
	
	public void setHint(int hint, boolean value){
		setHint(hint, value ? 1 : 0);
	}
	
	public void setHint(int hint, int value){
		GLFW.glfwWindowHint(hint, value);
	}
	
	public void setPosition(int x, int y){
		if(this.x == x && this.y == y){
			return;
		}
		
		GLFW.glfwSetWindowPos(handle, x, y);
	}
	
	public void setSize(int width, int height){
		//quick escape
		if(this.width == width && this.height == height){
			return;
		}
	}
	
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public static boolean canRender(){
		return allowRender;
	}
	
	public void setIcon(String icon16Path, String icon32Path){
		//setup buffers to work with stb
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		//these will be the data buffers for the textures
		ByteBuffer icon16,icon32;
		try{
			//populate the buffers with the raw image data
			icon16 = ResourceLoader.getBytes(icon16Path);
			icon32 = ResourceLoader.getBytes(icon32Path);
			
			//setup image buffers for the images to be processed
			try(GLFWImage.Buffer icons = GLFWImage.malloc(2)){
				//process both images with stb
				//16x16 icon
				ByteBuffer p16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
				icons.position(0).width(w.get(0)).height(h.get(0)).pixels(p16);
				
				//32x32 icon
				ByteBuffer p32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
				icons.position(1).width(w.get(0)).height(h.get(0)).pixels(p32);
				
				//reset the icons buffer position
				icons.position(0);
				GLFW.glfwSetWindowIcon(handle, icons);
				
				//free the stb resources
				STBImage.stbi_image_free(p16);
				STBImage.stbi_image_free(p32);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setInputMode(int mode, int value){
		GLFW.glfwSetInputMode(instance.handle, mode, value);
	}
	
	//terminate the GLFW instance
	public static void exit(){
		GLFW.glfwTerminate();
	}
}
