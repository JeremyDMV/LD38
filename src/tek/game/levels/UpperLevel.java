package tek.game.levels;

import tek.game.Level;
import tek.game.gameObjects.LargeObject;
import tek.render.TextureSheet;
import tek.runtime.Scene;
import tek.ui.UIScene;
import tek.ui.UIText;
import tek.ui.UITexture;

public class UpperLevel extends Level {
	
	public UITexture healthbar;
	public UIText    healthtext;
	
	public LargeObject large;
	
	@Override
	public void start() {
		initUI();
		initObjects();
	}
	
	private void initUI(){
		Scene.current.uiScene.clear();
		
		UIScene scene = Scene.current.uiScene;
		
		healthbar = new UITexture(TextureSheet.getSheet("default").texture, 22);
		healthtext = new UIText("10");
		
		scene.add(healthbar);
		scene.add(healthtext);
	}
	
	public void initObjects(){
		large = new LargeObject(1, 3, 3);
		large.transform.setSize(30f, 30f);
		large.transform.setPosition(25f, 25f);
		large.transform.setLayer(10);
		Scene.current.add(large);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void update(long delta) {
		
	}
}
