package tek.input;

import java.util.ArrayList;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import tek.Window;

public class Mouse {
	public static final int LEFT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int RIGHT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int MIDDLE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	
	public static double x, y;
	private static double dx, dy;
	public static double scrollX, scrollY;
	public static Vector2d pos, delta, scroll;
	
	public static boolean entered = false;
	private static CursorMode cursorMode = CursorMode.NORMAL;
	
	public static ArrayList<Integer> events;
	public static ArrayList<Integer> lastFrame;
	public static ArrayList<Integer> thisFrame;
	
	static {
		pos   = new Vector2d();
		delta = new Vector2d();
		scroll = new Vector2d();
		
		events    = new ArrayList<Integer>();
		lastFrame = new ArrayList<Integer>();
		thisFrame = new ArrayList<Integer>();
	}
	
	public static void destroy(){
		events.clear();
		lastFrame.clear();
		thisFrame.clear();
	}
	
	public static void update(){
		lastFrame.clear();
		lastFrame.addAll(thisFrame);
		
		thisFrame.clear();
		thisFrame.addAll(events);
		
		events.clear();
	}
	
	public static double getDX(){
		double t = dx;
		dx = 0;
		return t;
	}
	
	public static double getDY(){
		double t = dy;
		dy = 0;
		return t;
	}
	
	public static boolean isDown(int button){
		return events.contains((Integer)button);
	}
	
	public static boolean isClicked(int button){
		return thisFrame.contains((Integer)button) && !lastFrame.contains((Integer)button);
	}
	
	public static boolean isReleased(int button){
		return !thisFrame.contains((Integer)button) && lastFrame.contains((Integer)button);
	}
	
	public static boolean isUp(int button){
		return !isDown(button);
	}
	
	public static void setButton(int button, boolean down){
		if(down)
			if(!events.contains((Integer)button))
				events.add((Integer)button);
		else
			if(events.contains((Integer)button))
				events.remove((Integer)button);
	}
	
	public static void setPos(double x, double y){
		if(Mouse.x == x && Mouse.y == y)
			return;
		
		dx = x - Mouse.x;
		dy = y - Mouse.y;
		delta.set(dx, dy);
		
		pos.set(x, y);
		Mouse.x = x;
		Mouse.y = y;
	}
	
	public static void setScroll(double x, double y){
		scrollX = x;
		scrollY = y;
		scroll.set(x, y);
	}
	
	public static void setEntered(boolean entered){
		Mouse.entered = entered;
	}
	
	public static void setCursorMode(CursorMode mode){
		if(Mouse.cursorMode == mode)
			return;
		Mouse.cursorMode = mode;
		Window.setInputMode(GLFW.GLFW_CURSOR, mode.getGLFWValue());
	}
	
	public static void setCursorMode(int val){
		setCursorMode(CursorMode.getMode(val));
	}
	
	public static CursorMode getCursorMode(){
		return cursorMode;
	}
	
	public static int getCursorModeInt(){
		return CursorMode.getInt(cursorMode);
	}
	
	/* SUPERFICIAL TEST */
	public static boolean isGrabbed(){
		return cursorMode == CursorMode.DISABLED;
	}
	
	public static enum CursorMode {
		DISABLED(GLFW.GLFW_CURSOR_DISABLED),
		HIDDEN(GLFW.GLFW_CURSOR_HIDDEN),
		NORMAL(GLFW.GLFW_CURSOR_NORMAL);
		
		private int value;
		
		CursorMode(int value){
			this.value = value;
		}
		
		int getGLFWValue(){
			return value;
		}
		
		static CursorMode getMode(int v){
			switch(v){
			case 0:
				return DISABLED;
			case 1:
				return HIDDEN;
			default:
				return NORMAL;
			}
		}
		
		static int getInt(CursorMode mode){
			switch(mode){
			case DISABLED:
				return 0;
			case HIDDEN:
				return 1;
			default:
				return 2;
			}
		}
	}
}
