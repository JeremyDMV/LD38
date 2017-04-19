package tek.input;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
	public static final int KEY_SPACE         = GLFW_KEY_SPACE;
    public static final int KEY_APOSTROPHE    = GLFW_KEY_APOSTROPHE;
    public static final int KEY_COMMA         = GLFW_KEY_COMMA;
    public static final int KEY_MINUS         = GLFW_KEY_MINUS;
    public static final int KEY_PERIOD        = GLFW_KEY_PERIOD;
    public static final int KEY_SLASH         = GLFW_KEY_SLASH;
    public static final int KEY_0             = GLFW_KEY_0;
    public static final int KEY_1             = GLFW_KEY_1;
    public static final int KEY_2             = GLFW_KEY_2;
    public static final int KEY_3             = GLFW_KEY_3;
    public static final int KEY_4             = GLFW_KEY_4;
    public static final int KEY_5             = GLFW_KEY_5;
    public static final int KEY_6             = GLFW_KEY_6;
    public static final int KEY_7             = GLFW_KEY_7;
    public static final int KEY_8             = GLFW_KEY_8;
    public static final int KEY_9             = GLFW_KEY_9;
    public static final int KEY_SEMICOLON     = GLFW_KEY_SEMICOLON;
    public static final int KEY_EQUAL         = GLFW_KEY_EQUAL;
    public static final int KEY_A             = GLFW_KEY_A;
    public static final int KEY_B             = GLFW_KEY_B;
    public static final int KEY_C             = GLFW_KEY_C;
    public static final int KEY_D             = GLFW_KEY_D;
    public static final int KEY_E             = GLFW_KEY_E;
    public static final int KEY_F             = GLFW_KEY_F;
    public static final int KEY_G             = GLFW_KEY_G;
    public static final int KEY_H             = GLFW_KEY_H;
    public static final int KEY_I             = GLFW_KEY_I;
    public static final int KEY_J             = GLFW_KEY_J;
    public static final int KEY_K             = GLFW_KEY_K;
    public static final int KEY_L             = GLFW_KEY_L;
    public static final int KEY_M             = GLFW_KEY_M;
    public static final int KEY_N             = GLFW_KEY_N;
    public static final int KEY_O             = GLFW_KEY_O;
    public static final int KEY_P             = GLFW_KEY_P;
    public static final int KEY_Q             = GLFW_KEY_Q;
    public static final int KEY_R             = GLFW_KEY_R;
    public static final int KEY_S             = GLFW_KEY_S;
    public static final int KEY_T             = GLFW_KEY_T;
    public static final int KEY_U             = GLFW_KEY_U;
    public static final int KEY_V             = GLFW_KEY_V;
    public static final int KEY_W             = GLFW_KEY_W;
    public static final int KEY_X             = GLFW_KEY_X;
    public static final int KEY_Y             = GLFW_KEY_Y;
    public static final int KEY_Z             = GLFW_KEY_Z;
    public static final int KEY_LEFT_BRACKET  = GLFW_KEY_LEFT_BRACKET;
    public static final int KEY_BACKSLASH     = GLFW_KEY_BACKSLASH;
    public static final int KEY_RIGHT_BRACKET = GLFW_KEY_RIGHT_BRACKET;
    public static final int KEY_GRAVE_ACCENT  = GLFW_KEY_GRAVE_ACCENT;
    public static final int KEY_WORLD_1       = GLFW_KEY_WORLD_1;
    public static final int KEY_WORLD_2       = GLFW_KEY_WORLD_2; // FUNCTION KEYS ----
    public static final int KEY_ESCAPE        = GLFW_KEY_ESCAPE;
    public static final int KEY_ENTER         = GLFW_KEY_ENTER;
    public static final int KEY_TAB           = GLFW_KEY_TAB;
    public static final int KEY_BACKSPACE     = GLFW_KEY_BACKSPACE;
    public static final int KEY_INSERT        = GLFW_KEY_INSERT;
    public static final int KEY_DELETE        = GLFW_KEY_DELETE;
    public static final int KEY_RIGHT         = GLFW_KEY_RIGHT;
    public static final int KEY_LEFT          = GLFW_KEY_LEFT;
    public static final int KEY_DOWN          = GLFW_KEY_DOWN;
    public static final int KEY_UP            = GLFW_KEY_UP;
    public static final int KEY_PAGE_UP       = GLFW_KEY_PAGE_UP;
    public static final int KEY_PAGE_DOWN     = GLFW_KEY_PAGE_DOWN;
    public static final int KEY_HOME          = GLFW_KEY_HOME;
    public static final int KEY_END           = GLFW_KEY_END;
    public static final int KEY_CAPS_LOCK     = GLFW_KEY_CAPS_LOCK;
    public static final int KEY_SCROLL_LOCK   = GLFW_KEY_SCROLL_LOCK;
    public static final int KEY_NUM_LOCK      = GLFW_KEY_NUM_LOCK;
    public static final int KEY_PRINT_SCREEN  = GLFW_KEY_PRINT_SCREEN;
    public static final int KEY_PAUSE         = GLFW_KEY_PAUSE;
    public static final int KEY_F1            = GLFW_KEY_F1;
    public static final int KEY_F2            = GLFW_KEY_F2;
    public static final int KEY_F3            = GLFW_KEY_F3;
    public static final int KEY_F4            = GLFW_KEY_F4;
    public static final int KEY_F5            = GLFW_KEY_F5;
    public static final int KEY_F6            = GLFW_KEY_F6;
    public static final int KEY_F7            = GLFW_KEY_F7;
    public static final int KEY_F8            = GLFW_KEY_F8;
    public static final int KEY_F9            = GLFW_KEY_F9;
    public static final int KEY_F10           = GLFW_KEY_F10;
    public static final int KEY_F11           = GLFW_KEY_F11;
    public static final int KEY_F12           = GLFW_KEY_F12;
    public static final int KEY_F13           = GLFW_KEY_F13;
    public static final int KEY_F14           = GLFW_KEY_F14;
    public static final int KEY_F15           = GLFW_KEY_F15;
    public static final int KEY_F16           = GLFW_KEY_F16;
    public static final int KEY_F17           = GLFW_KEY_F17;
    public static final int KEY_F18           = GLFW_KEY_F18;
    public static final int KEY_F19           = GLFW_KEY_F19;
    public static final int KEY_F20           = GLFW_KEY_F20;
    public static final int KEY_F21           = GLFW_KEY_F21;
    public static final int KEY_F22           = GLFW_KEY_F22;
    public static final int KEY_F23           = GLFW_KEY_F23;
    public static final int KEY_F24           = GLFW_KEY_F24;
    public static final int KEY_F25           = GLFW_KEY_F25;
    public static final int KEY_KP_0          = GLFW_KEY_KP_0;
    public static final int KEY_KP_1          = GLFW_KEY_KP_1;
    public static final int KEY_KP_2          = GLFW_KEY_KP_2;
    public static final int KEY_KP_3          = GLFW_KEY_KP_3;
    public static final int KEY_KP_4          = GLFW_KEY_KP_4;
    public static final int KEY_KP_5          = GLFW_KEY_KP_5;
    public static final int KEY_KP_6          = GLFW_KEY_KP_6;
    public static final int KEY_KP_7          = GLFW_KEY_KP_7;
    public static final int KEY_KP_8          = GLFW_KEY_KP_8;
    public static final int KEY_KP_9          = GLFW_KEY_KP_9;
    public static final int KEY_KP_DECIMAL    = GLFW_KEY_KP_DECIMAL;
    public static final int KEY_KP_DIVIDE     = GLFW_KEY_KP_DIVIDE;
    public static final int KEY_KP_MULTIPLY   = GLFW_KEY_KP_MULTIPLY;
    public static final int KEY_KP_SUBTRACT   = GLFW_KEY_KP_SUBTRACT;
    public static final int KEY_KP_ADD        = GLFW_KEY_KP_ADD;
    public static final int KEY_KP_ENTER      = GLFW_KEY_KP_ENTER;
    public static final int KEY_KP_EQUAL      = GLFW_KEY_KP_EQUAL;
    public static final int KEY_LEFT_SHIFT    = GLFW_KEY_LEFT_SHIFT;
    public static final int KEY_LEFT_CONTROL  = GLFW_KEY_LEFT_CONTROL;
    public static final int KEY_LEFT_ALT      = GLFW_KEY_LEFT_ALT;
    public static final int KEY_LEFT_SUPER    = GLFW_KEY_LEFT_SUPER;
    public static final int KEY_RIGHT_SHIFT   = GLFW_KEY_RIGHT_SHIFT;
    public static final int KEY_RIGHT_CONTROL = GLFW_KEY_RIGHT_CONTROL;
    public static final int KEY_RIGHT_ALT     = GLFW_KEY_RIGHT_ALT;
    public static final int KEY_RIGHT_SUPER   = GLFW_KEY_RIGHT_SUPER;
    public static final int KEY_MENU          = GLFW_KEY_MENU;
    public static final int KEY_LAST          = GLFW_KEY_LAST;
	
	public static ArrayList<Integer> events;
	public static ArrayList<Integer> eventsThisFrame;
	public static ArrayList<Integer> eventsLastFrame;
	
	public static ArrayList<Button> buttons;
	
	public static ArrayList<KeyTracker> trackers;
	
	public static ArrayList<Character> chars;
	public static ArrayList<Character> nextChars;
	private static boolean charTracking = false;
	
	static {
		events = new ArrayList<Integer>();
		eventsThisFrame = new ArrayList<Integer>();
		eventsLastFrame = new ArrayList<Integer>();
		
		buttons = new ArrayList<Button>();
		trackers = new ArrayList<KeyTracker>();
		chars = new ArrayList<Character>();
		nextChars = new ArrayList<Character>();
	}
	
	private Keyboard(){	}
	
	public static void destroy(){
		events.clear();
		eventsThisFrame.clear();
		eventsLastFrame.clear();
		buttons.clear();
		trackers.clear();
		chars.clear();
		nextChars.clear();
	}		
	
	public static void update(long delta){
		eventsLastFrame.clear();
		eventsLastFrame.addAll(eventsThisFrame);
		
		events.removeAll(Collections.singleton(null));
		
		eventsThisFrame.clear();
		eventsThisFrame.addAll(events);
		
		long current = System.currentTimeMillis();
		
		if(trackers.size() > 0){
			for(int i=0;i<trackers.size();i++){
				KeyTracker tracker = trackers.get(i);
				boolean down = isDown(tracker.key);
				if(down != tracker.down)
					tracker.set(current, down);
			}
		}
		
		if(charTracking){
			nextChars.addAll(chars);
			chars.clear();
		}
	}
	
	public static void setCharTracking(boolean tracking){
		charTracking = tracking;
	}
	
	public static boolean isCharTracking(){
		return charTracking;
	}
	
	public static ArrayList<Character> getChars(){
		if(!charTracking)
			return null;
		
		ArrayList<Character> t = new ArrayList<Character>();
		t.addAll(nextChars);
		nextChars.clear();
		return t;
	}
	
	public static int convertChar(char c){
		if(Character.isAlphabetic(c)){
			return (int)Character.toUpperCase(c);
		}else{
			return (int)c;
		}
	}
	
	public static void addChar(char c){
		if(!charTracking)
			return;
		chars.add((Character)c);
	}
	
	public static String getKeyName(int key){
		return GLFW.glfwGetKeyName(key, 0);
	}
	
	public static void setKey(int key, boolean down){
		Integer conv = (Integer)key;
		if(down){
			if(!events.contains(conv)){
				events.add(conv);
			}
		}else{
			if(events.contains(conv))
				events.remove(conv);
		}
		
		if(trackers.size() == 0)
			return;
		
		long current = System.currentTimeMillis();
		for(KeyTracker tracker : trackers){
			if(tracker.key == key){
				tracker.set(current, down);
				break;
			}
		}
	}
	
	public static boolean isDown(int key){
		return events.contains(key);
	}
	
	public static boolean isDown(char key){
		return events.contains((int)Character.toUpperCase(key));
	}
	
	public static boolean isUp(int key){
		return !isDown(key);
	}
	
	public static boolean isUp(char key){
		return !isDown((int)Character.toUpperCase(key));
	}
	
	public static boolean isClicked(int key){
		return eventsThisFrame.contains((Integer)key) && !eventsLastFrame.contains((Integer)key);
	}
	
	public static boolean isClicked(char key){
		return isClicked((int)Character.toUpperCase(key));
	}
	
	public static boolean isReleased(int key){
		return !eventsThisFrame.contains((Integer)key) && eventsLastFrame.contains((Integer)key);
	}
	
	public static boolean isReleased(char key){
		return isReleased((int)Character.toUpperCase(key));
	}
	
	public static float getButton(String buttonName){
		return getButton(buttonName, false);
	}
	
	public static float getButton(String buttonName, boolean ignoreGravity){
		Button button = null;
		for(Button b : buttons){
			if(b.name.equals(buttonName)){
				button = b;
				break;
			}
		}
		
		if(button.tracked){
			KeyTracker[] trackers = gatherTrackers(button);
			
			long cur = System.currentTimeMillis();
			
			switch(button.keySize){
			case 1:
				return Math.min(1f, trackers[0].duration(cur) / button.gravity);
			case 2:
			case 4:
				if(button.keySize == 2){
					if(trackers[0].down){
						if(!trackers[1].down){
							return 1f * Math.min(1f, trackers[1].duration(cur) / button.gravity); //clamp to 1.0
						}else{
							return 0f;
						}
					}else if(trackers[1].down){
						return -1f * Math.min(1f, trackers[1].duration(cur) / button.gravity); //clamp to -1.0 
					}
				}else if(button.keySize == 4){
					if(trackers[0].down || trackers[1].down){
						if(!trackers[2].down && !trackers[3].down){
							long earliest = cur;
							if(!trackers[0].down){
								earliest = trackers[1].lastEvent;
							}else if(!trackers[1].down){
								earliest = trackers[0].lastEvent;
							}else{
								earliest = (trackers[0].lastEvent > trackers[1].lastEvent) ? trackers[1].lastEvent : trackers[0].lastEvent;
							}
							return 1f * Math.min(1f, (cur - earliest) / button.gravity); //clamp to 1.0
						}
					}else{
						if(trackers[2].down || trackers[3].down){
							long earliest = cur;
							if(!trackers[2].down){
								earliest = trackers[3].lastEvent;
							}else if(!trackers[3].down){
								earliest = trackers[2].lastEvent;
							}else{
								earliest = (trackers[2].lastEvent > trackers[3].lastEvent) ? trackers[3].lastEvent : trackers[2].lastEvent;
							}
							return -1f * Math.min(1f, (cur - earliest) / button.gravity); //clamp to -1.0
						}
					}
				}
				return 0f;
			default:
				return 0f;
			}
		}else{
			switch(button.keySize){
			case 1:
				return isDown(button.key) ? 1 : 0;
			case 2:
			case 4:
				float v = 0;
				if(button.keySize == 2){
					v += isDown(button.key) ? 1 : 0;
					v -= isDown(button.neg) ? 1 : 0;
				}else if(button.keySize == 4){
					boolean k = isDown(button.key) || isDown(button.keyAlt);
					boolean n = isDown(button.neg) || isDown(button.negAlt);
					v += k ? 1 : 0;
					v -= n ? 1 : 0;
				}
				
				return v;
			default:
				return 0f;
			}
		}
	}
	
	public static Button setupButton(String buttonName, int...keys){
		return setupButton(buttonName, true, keys);
	}
	
	public static Button setupButton(String buttonName, boolean track, int...keys){
		Button newbutton = new Button(buttonName, keys);
		
		String lc = buttonName.toLowerCase();
		
		for(Button button : buttons){
			if(button.name.toLowerCase().equals(lc)){
				buttons.remove(button);
				break;
			}
		}
		
		buttons.add(newbutton);
		
		newbutton.tracked = track;
		
		if(track)
			setupTrackers(keys);
		
		return newbutton;
	}
	
	public static Button getButtonInstance(String buttonName){
		for(Button b : buttons)
			if(b.name.equals(buttonName))
				return b;
		return null;
	}
	
	public static KeyTracker[] gatherTrackers(Button button){
		KeyTracker[] trackers = new KeyTracker[button.keySize];
		int[] keys = new int[button.keySize];
		
		if(keys.length > 0)
			keys[0] = button.key;
		
		if(keys.length == 2)
			keys[1] = button.neg;
		
		if(keys.length == 4){
			keys[1] = button.keyAlt;
			keys[2] = button.neg;
			keys[3] = button.negAlt;
		}
		
		int gathered = 0;
		for(int i=0;i<keys.length;i++){
			int key = keys[i];
			for(int j=0;j<Keyboard.trackers.size();j++){
				KeyTracker existing = Keyboard.trackers.get(j);
				if(existing.key == key){
					trackers[gathered] = existing;
					gathered ++;
					break;
				}
			}
		}
		
		//if all of the trackers are found, return them
		if(gathered == trackers.length)
			return trackers;
		
		//quick release in case it doesn't require tracking
		if(!button.requiresTracking){
			button.updateTracking(false);
			return trackers;
		}
		
		//if not all of the trackers exist and tracking is required
		if(gathered < trackers.length){
			int[] remaining = new int[trackers.length - gathered];
			int rindex = 0;
			
			for(int i=0;i<trackers.length;i++){
				if(trackers[i] == null){
					remaining[rindex] = keys[i];
					rindex ++;
				}
				
				if(rindex == remaining.length)
					break;
			}
			
			for(int i=0;i<remaining.length;i++){
				int rkey = remaining[i];
				for(int k=0;k<keys.length;k++){
					if(rkey == keys[k]){
						trackers[k] = new KeyTracker(rkey);
						Keyboard.trackers.add(trackers[k]);
						break;
					}
				}
			}
		}
		
		return trackers;
	}
	
	public static void destroyTrackers(int ... keys){
		//list of trackers to remove
		ArrayList<KeyTracker> affected = new ArrayList<KeyTracker>();
		//check each key in the array passed
		for(int key : keys){
			for(KeyTracker tracker : trackers){
				if(tracker.key == key){
					boolean dying = true;
					for(Button b : buttons){
						if(b.tracked)
							if(b.uses(key)){
								if(!b.requiresTracking()){
									b.updateTracking(false);
								}else{
									dying = false;
									break;
								}
							}
						
					} //end of buttons
					//if allowed to die
					if(dying)
						affected.add(tracker);
					break;
				}
			} // end of trackers
		} // end of keys
		
		//remove all affected trackers
		trackers.removeAll(affected);
	}
	
	public static void setupTrackers(int ... keys){
		ArrayList<Integer> nonexisting = new ArrayList<Integer>();
		//if trackers exist, modify list to make
		if(trackers.size() > 0){
			for(int i=0;i<keys.length;i++){
				boolean usable = true;
				
				for(int j=0;j<trackers.size();j++){
					if(trackers.get(j).key == keys[i]){
						usable = false;
						break;
					}
				}
				
				if(usable){
					nonexisting.add(keys[i]);
				}
			}
		}else{
			for(int i=0;i<keys.length;i++)
				nonexisting.add(keys[i]);
		}
		
		for(int m : nonexisting){
			KeyTracker n = new KeyTracker(m);
			trackers.add(n);
		}
	}
	
	public static class KeyTracker {
		public final int key;
		protected long lastEvent = System.currentTimeMillis();
		protected boolean down = false;
		
		public KeyTracker(int key){
			this.key = key;
		}
		
		protected void set(long current, boolean down){
			if(this.down == down)
				return;
			this.down = down;
			lastEvent = current;
		}
		
		public boolean isDown(){
			return down;
		}
		
		public long duration(long currentTime){
			return currentTime - lastEvent;
		}
		
		public long getLastEvent(){
			return lastEvent;
		}
	}
	
	public static class Button {
		public final String name;
		int key, keyAlt, neg, negAlt;
		float gravity = 700.0f; //instant
		private boolean tracked = false;
		public boolean requiresTracking = false;
		public final int keySize;
		
		public Button(String name, boolean track, int ... keys){
			this.name = name;
			
			if(keys.length == 1){
				key = keys[0];
				keySize = 1;
			}else if(keys.length >= 2 && keys.length < 4){
				key = keys[0];
				neg = keys[1];
				keySize = 2;
			}else if(keys.length >= 4){
				key    = keys[0];
				keyAlt = keys[1];
				neg    = keys[2];
				negAlt = keys[3];
				keySize = 4;
			}else{
				keySize = 0;
			}
			
			this.tracked = track;
		}
		
		public Button(String name, int ... keys){
			this(name, true, keys);
		}
		
		protected void updateTracking(boolean tracking){
			this.tracked = tracking;
		}
		
		protected boolean uses(int key){
			return this.key == key || keyAlt == key ||
					neg == key || negAlt == key;
		}
		
		public boolean isTracked(){
			return tracked;
		}
		
		public boolean requiresTracking(){
			return requiresTracking;
		}
	}
}
