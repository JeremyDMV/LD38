package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.physics.BoxCollider;
import tek.runtime.physics.Collider.ColliderType;

public class LargeObject extends GameObject {
	
	public int subTextureStart = 0;
	public int tilesX = 0, tilesY = 0;
	
	public LargeObject(int subTextureStart, int tilesX, int tilesY){
		this.setCollider(new BoxCollider(this, new Vector2f(tilesX * 10f, tilesY * 10f), ColliderType.STATIC));
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
