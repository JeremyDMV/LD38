package tek.game.levels;

import java.util.ArrayList;

import tek.game.gameObjects.TileObject;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.runtime.GameObject;

public class MapBuilder {
	public static float TILE_SIZE = 10f;
	
	public final int width, height, layers;
	public MapTile[][] tiles;
	
	{
	}
	
	public MapBuilder(int width, int height, int layers){
		this.width = width;
		this.height = height;
		this.layers = layers;
		tiles = new MapTile[layers][];
		for(int i=0;i<layers;i++){
			tiles[i] = new MapTile[width * height];
		}
	}
	
	public MapTile get(int x, int y){
		return get(x, y, 0);
	}
	
	public MapTile get(int x, int y, int layer){
		return tiles[layer][x + width * y];
	}
	
	public void add(MapTile tile, int x, int y){
		add(tile, x, y, 0);
	}
	
	public void add(MapTile tile, int x, int y, int layer){
		tiles[layer][x + width * y] = tile;
	}
	
	public void add(MultiTile multi, int x, int y){
		add(multi, x, y, 0);
	}
	
	public void add(MultiTile multi, int x, int y, int layer){
		for(int w = 0; w < multi.width; w++){
			for(int h = 0; h < multi.height; h++){
				int tex = multi.get(w, h);
				if(tex != -1)
					add(new MapTile(multi.get(w, h)), x + w, y + h, layer);
			}
		}
	}
	
	public void setAll(MapTile tile, int layer){
		for(int i=0;i<width*height;i++){
			tiles[layer][i] = tile;
		}
	}
	
	public ArrayList<GameObject> export(){
		ArrayList<GameObject> exports = new ArrayList<GameObject>();
		Texture tex = TextureSheet.getSheet("default").texture;
		for(int i=0;i<layers;i++){
			for(int x = 0 ; x < width; x ++){
				for(int y = 0; y < height; y ++){
					MapTile t = get(x, y, i);
					if(t != null)
						exports.add(new TileObject(tex, t.id, x, y, i+1, t.collidable));
				}
			}
		}
		return exports;
	}
	
	public static class MapTile {
		public int id;
		public boolean collidable = false;
		
		public MapTile(int id){
			this.id = id;
		}
		
		public MapTile(int id, boolean collidable){
			this.id = id;
			this.collidable = collidable;
		}
	}
	
	public static class MultiTile {
		public int width, height;
		public boolean singleCollider = false;
		public int[] textures;
		public boolean[] collidable;
		
		public MultiTile(int width, int height){
			this.width = width;
			this.height = height;
			textures = new int[width * height];
			for(int i=0;i<textures.length;i++)
				textures[i] = -1;
		}
		
		public void setRange(int originX, int originY, int subID, int width, int height){
			TextureSheet tex = TextureSheet.getSheet("default");
			for(int x = 0 ; x < height; x ++){
				for(int y = 0 ; y< width ; y ++){
					set(subID + (y * tex.perWidth) + x, originX + x, originY + y);
				}
			}
		}
		
		public void set(int sub, int x, int y){
			if(x > width || y > height)
				return;
			textures[x + width * y] = sub;
		}
		
		public int get(int x, int y){
			return textures[x + width * y];
		}
		
		public boolean getCollider(int x, int y){
			return collidable[x + width * y];
		}
		
		public void setSingleCollider(boolean singleCollider){
			this.singleCollider = singleCollider;
		}
	}
}
