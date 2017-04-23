package tek.game.gameObjects;

import org.joml.Vector2f;

import tek.game.levels.MapBuilder;
import tek.render.Texture;
import tek.runtime.GameObject;
import tek.runtime.physics.BoxCollider;
import tek.runtime.physics.Collider.ColliderType;

public class TileObject extends GameObject {

	public TileObject(Texture texture, int subTexture, int x, int y, boolean collidable){
		super();
		this.texture = texture;
		this.subTexture = subTexture;
		transform.setSize(new Vector2f(MapBuilder.TILE_SIZE));
		transform.setPosition(new Vector2f(x * MapBuilder.TILE_SIZE, y * MapBuilder.TILE_SIZE));
		
		transform.updateMatrix();
		
		if(collidable)
			setCollider(new BoxCollider(this, new Vector2f(MapBuilder.TILE_SIZE), ColliderType.STATIC));
	}
	
	public TileObject(Texture texture, int subTexture, int x, int y, int layer, boolean collidable){
		super();
		transform.setLayer(layer);
		this.texture = texture;
		this.subTexture = subTexture;
		transform.setSize(new Vector2f(MapBuilder.TILE_SIZE));
		transform.setPosition(new Vector2f(x * MapBuilder.TILE_SIZE, y * MapBuilder.TILE_SIZE));
		
		transform.updateMatrix();
		if(collidable)
			setCollider(new BoxCollider(this, new Vector2f(MapBuilder.TILE_SIZE), ColliderType.STATIC));
	}
	
	@Override
	public void Start() {
	}

	@Override
	public void Update(long delta) {
	}
	
}
