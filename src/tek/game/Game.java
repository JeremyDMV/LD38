package tek.game; 

import tek.Window;
import tek.render.Animation;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.runtime.GameObject;
import tek.runtime.Scene;

public class Game implements Interface {

	@Override
	public void start() {
		Shader shader = new Shader("default", "shaders/default.vs", "shaders/default.fs");
		Scene.current.defaultShader = shader;
		
		Window.instance.setIcon("textures/icon16.png", "textures/icon32.png");
		
		TextureSheet sheet = new TextureSheet(new Texture("textures/texsheet.png"), 16, 16, "test");
		TestPlayer gameObject = new TestPlayer();
		
		gameObject.texture = sheet.texture;
		gameObject.subTexture = 3;
		gameObject.transform.setSize(10f, 10f);
		gameObject.transform.setPosition(1f, 1f);
		
		Animation anim = new Animation(sheet, "test", 1000f / 20f, new int[]{
				112, 113, 114, 115
		});
		
		gameObject.addAnimation(anim);
		gameObject.setAnimation(0);
		
		gameObject.playAnimation();
		
		Scene.current.add(gameObject);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void input(long delta) {
		
	}

	@Override
	public void update(long delta) {
		
	}

	@Override
	public void render(long delta) {
		
	}

}
