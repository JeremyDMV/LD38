package tek.game;

import java.io.File;

import tek.ResourceLoader;
import tek.Util;

public class Preferences {
	public static final String DEFAULT_PATH = "gameprefs.txt";
	
	//window size
	public static final int DEFAULT_WIDTH = 720;
	public static final int DEFAULT_HEIGHT = 480;
	
	//window position
	public static final int DEFAULT_X = -1;
	public static final int DEFAULT_Y = -1;
	
	public static final boolean DEFAULT_FULLSCREEN = false;
	
	public static final int DEFAULT_REFRESH_RATE = 60;
	public static final boolean DEFAULT_VSYNC = true;
	
	//default everything is -1 so we know when an attribute isn't set
	public int width = -1,height = -1,x = -1,y = -1,refreshRate = -1;
	
	public boolean fullscreen = false, vsync = false;
	
	/** Create game preferences
	 * 
	 * @param useDefaults if to use the defaults in the file
	 */
	public Preferences(boolean useDefaults){
		if(useDefaults){
			//size of the window
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
			//position of the window
			x = DEFAULT_X;
			y = DEFAULT_Y;
			//if to use fullscreen
			fullscreen = DEFAULT_FULLSCREEN;
			//refresh rate of the window
			refreshRate = DEFAULT_REFRESH_RATE;
			//whether or not to cap frame rate to display refresh rate
			//(vertical syncing)
			vsync = DEFAULT_VSYNC;
		}
	}
	
	/** Set Preferences
	 * 
	 * @param x x position of the window
	 * @param y y position of the window
	 * @param width width of the window (x axis size)
	 * @param height height of the window (y axis size)
	 * @param fullscreen if the window should be fullscreen
	 */
	public void set(int x, int y, int width, int height, boolean fullscreen){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
	}
	
	/** Set Preferences
	 * 
	 * @param width width of the window (x axis size)
	 * @param height height of the window (y axis size)
	 * @param fullscreen if the window should be fullscreen
	 * @param refreshRate the refreshRate of the window
	 * @param vsync if to enable vsync
	 */
	public void set(int width, int height, boolean fullscreen, int refreshRate, boolean vsync){
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.refreshRate = refreshRate;
		this.vsync = vsync;
	}
	
	/** Set the position of the window
	 * 
	 * @param x x position of the window
	 * @param y y position of the window
	 */
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Save the preferences to DEFAULT_PATh
	 */
	public void save(){
		ResourceLoader.writeString(DEFAULT_PATH, toString());
	}
	
	/** Save to the specified path
	 * 
	 * @param path the path to save the preferences
	 */
	public void save(String path){
		//the object by default goes to toString() in a properly formatted
		//way, so we can interpret it as such
		//so we just take advantage of `ResourceLoader` and write the string
		ResourceLoader.writeString(path, toString());
	}
	
	/** Get a Preferences Object from String
	 * 
	 * @param prefs game preferences in string form
	 * @return Preferences object
	 */
	public static Preferences processPreferences(String prefs){
		//the object to return
		Preferences ret = new Preferences(false);
		//get the lines for us to process
		String[] split = ResourceLoader.getText(prefs).split("\n");
		
		for(String line : split){
			//clean up spaces
			String[] attribs = line.split(":");
			for(int i=0;i<attribs.length;i++){
				attribs[i] = attribs[i].trim(); //remove any spaces
				attribs[i] = attribs[i].replaceAll(":", ""); //remove any colons
				attribs[i] = attribs[i].replace("\\u00A0", ""); //remove any spaces
			}
			
			//switch between each definition
			switch(attribs[0].toLowerCase()){
			case "width":
				ret.width = Util.getInt(attribs[1]);
				break;
			case "height":
				ret.height = Util.getInt(attribs[1]);
				break;
			case "x":
				ret.x = Util.getInt(attribs[1]);
				break;
			case "y":
				ret.y = Util.getInt(attribs[1]);
				break;
			case "fullscreen":
				ret.fullscreen = Util.getBool(attribs[1]);
				break;
			case "refreshrate":
				ret.refreshRate = Util.getInt(attribs[1]);
				break;
			case "vsync":
				ret.vsync = Util.getBool(attribs[1]);
				break;
			}
		}
		//return the `Preferences` variable
		return ret;
	}
	
	/** Get preferences if they're saved on file, otherwise
	 *  return default preferences
	 * 
	 * @return preferences
	 */
	public static Preferences getPrefs(){
		Preferences prefs = null;
		//if the file exists setup preferences from the file
		if(new File(DEFAULT_PATH).exists()){
			prefs = processPreferences(DEFAULT_PATH);
		}
		
		//if the preferences are still null/invalud then setup defaults
		if(prefs == null)
			prefs = new Preferences(true);
		
		//return the preferences
		return prefs;
	}
	
	/**
	 * Format:
	 * 	width: width
	 * 	height: height
	 * 	x: x
	 *  y: y
	 *  fullscreen: fullscreen
	 *  refreshrate: refreshRate
	 */
	@Override
	public String toString(){
		return "width: "+width+Util.getNL()+"height: "+height+Util.getNL()+"x: "+x+Util.getNL()+"y: "+y+Util.getNL()+"fullscreen: "+fullscreen+Util.getNL()+"refreshRate: "+refreshRate+Util.getNL()+"vsync:"+vsync;
	}
	
}
