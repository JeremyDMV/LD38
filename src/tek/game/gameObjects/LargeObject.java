package tek.game.gameObjects;

import tek.render.TextureSheet;
import tek.runtime.GameObject;

public class LargeObject extends GameObject {
	
	public int subTextureStart = 0;
	public int tilesX = 0, tilesY = 0;
	
	public LargeObject(int subTextureStart, int tilesX, int tilesY){
		this.subTextureStart = subTextureStart;
		this.tilesX = tilesX;
		this.tilesY = tilesY;
		this.subTexture = subTextureStart;
		this.texture = TextureSheet.getSheet("default").texture;
	}
	
	@Override
	public void Start() {
		
	}

	@Override
	public void Update(long delta) {
		
	}

}
