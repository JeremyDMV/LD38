package tek.game.levels;

import tek.audio.Mixer;
import tek.audio.Music;
import tek.game.Level;
import tek.game.gameObjects.LargeObject;
import tek.input.Keyboard;
import tek.render.TextureSheet;
import tek.runtime.Scene;
import tek.ui.UIScene;
import tek.ui.UIText;
import tek.ui.UITexture;

public class UpperLevel extends Level {
	
	public UITexture healthbar;
	public UIText    healthtext;
	
	public LargeObject large;
	
	public Music music;
	
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
		
		music = new Music("audio/mainmenu.ogg");
		music.setLoop(true);
		
		Mixer.instance.addTo(music, "music");
		Mixer.instance.getChannel("music").setGain(1.0f);
		
		Mixer.instance.listener.setGain(1.0f);
		
		music.play();
	}
	
	public void initObjects(){
		large = new LargeObject(24, 3, 2);
		large.transform.setSize(30f, 20f);
		large.transform.setPosition(25f, 25f);
		large.transform.setLayer(10);
		
		LargeObject large2 = new LargeObject(72, 3, 2);
		large2.transform.setSize(30f, 20f);
		large2.transform.setPosition(35f, 35f);
		large2.transform.setLayer(-5);
		
		LargeObject dock = new LargeObject(67, 2, 3);
		dock.transform.setSize(20f, 30f);
		dock.transform.setPosition(50f, 50f);
		dock.transform.setLayer(-5);
		
		LargeObject dockRepeat = new LargeObject(68, 1, 3);
		dockRepeat.transform.setSize(10f, 30f);
		dockRepeat.transform.setPosition(100f, 50f);
		dockRepeat.transform.setLayer(-5);
		dockRepeat.textureRepeat.set(1f, 1f);
		
		Scene.current.add(dockRepeat);
		Scene.current.add(dock);
		Scene.current.add(large2);
		Scene.current.add(large);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void input(long delta){
		if(Keyboard.isClicked(Keyboard.KEY_SPACE)){
		}
	}
	
	@Override
	public void update(long delta) {
		
	}
}
