package tek.render;

import java.util.ArrayList;

import org.joml.Vector2f;

public class TextureSheet {
	public static  ArrayList<TextureSheet> sheets;
	
	static {
		sheets = new ArrayList<TextureSheet>();
	}
	
	public final String name;
	
	public final Texture texture;
	public final int subWidth, subHeight;
	public final int perWidth, perHeight;
	
	public final Vector2f subSize;
	
	public final Vector2f[] offsets;
	
	public String[] names;
	
	public TextureSheet(Texture tex, int subWidth, int subHeight, String name){
		this.texture = tex;
		
		this.subWidth = subWidth;
		this.subHeight = subHeight;
		
		subSize = new Vector2f(this.subWidth, this.subHeight);
		
		this.name = name;
		
		perWidth = texture.width / subWidth;
		perHeight = texture.height / subHeight;
		
		offsets = new Vector2f[perWidth * perHeight];
		
		for(int i=0;i<offsets.length;i++){
			int x = getX(i);
			int y = getY(i);
			offsets[i] = new Vector2f(x, y);
			offsets[i].mul(subSize);
		}
		names = new String[perWidth * perHeight];
		
		sheets.add(this);
	}
	
	/** Get the index of a sub texture by name
	 * 
	 * @param name the name of a sub texture
	 * @return the index of the sub texture
	 */
	public int get(String name){
		for(int i=0;i<names.length;i++){
			if(name.equals(names[i]))
				return i;
		}
		return -1;
	}
	
	public Vector2f getOffset(int id){
		return offsets[id];
	}
	
	public int getX(int id){
		return id % perWidth;
	}
	
	public int getY(int id){
		return id / perWidth;
	}
	
	public int getId(int x, int y){
		return x + perWidth * y;
	}
	
	public void name(int id, String name){
		names[id] = name;
	}
	
	public static TextureSheet getSheet(Texture texture){
		if(sheets == null)
			return null;
		for(TextureSheet sheet : sheets)
			if(sheet.texture.equals(texture))
				return sheet;
		return null;
	}
	
	public static boolean isTextureSheet(Texture tex){
		if(sheets == null)
			return false;
		for(TextureSheet s : sheets)
			if(s.texture.equals(tex))
				return true;
		return false;
	}
	
	public static TextureSheet getSheet(String name){
		for(TextureSheet sheet : sheets){
			if(sheet.name.equals(name))
				return sheet;
		}
		return null;
	}
	
	public static TextureSheet getSheetByPath(String filePath){
		String lowercase = filePath.toLowerCase();
		for(TextureSheet sheet: sheets){
			if(sheet.texture.path.toLowerCase().equals(lowercase)){
				return sheet;
			}
		}
		return null;
	}
}