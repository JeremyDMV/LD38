package tek.game.levels;

import java.util.ArrayList;

public class MapBuilder {
	public static float TILE_SIZE = 10f;
	
	public final int width, height;
	
	public MapTile[] tiles;
	private ArrayList<MultiTile> multis;
	
	{
		multis = new ArrayList<MultiTile>();
	}
	
	public MapBuilder(int width, int height){
		this.width = width;
		this.height = height;
		tiles = new MapTile[width * height];
	}
	
	
	public MapTile get(int x, int y){
		return tiles[x + width * y];
	}
	
	public void add(MapTile tile, int x, int y){
		tiles[x + width * y] = tile;
	}
	
	public void add(MultiTile multi, int x, int y){
		for(int w = 0; w < multi.width; w++){
			for(int h = 0; h < multi.height; h++){
				add(new MapTile(multi.get(w, h)), x + w, y + h);
			}
		}
	}
	
	public static class MapTile {
		public int id;
		public boolean collidable = false;
		
		public MapTile(int id){
			this.id = id;
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
		}
		
		public void set(int sub, int x, int y){
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
