package tek.game.levels;

import org.joml.Vector2f;

import tek.audio.Mixer;
import tek.audio.Music;
import tek.game.Level;
import tek.game.TextAnimator;
import tek.game.gameObjects.LargeObject;
import tek.game.gameObjects.Player;
import tek.game.levels.MapBuilder.MapTile;
import tek.game.levels.MapBuilder.MultiTile;
import tek.input.Keyboard;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem;
import tek.runtime.Scene;
import tek.ui.UIScene;
import tek.ui.UIText;
import tek.ui.UITexture;

public class UpperLevel extends Level {
	
	public UITexture healthbar;
	public UIText    healthtext;
	
	public LargeObject large;
	
	public ParticleSystem p;
	
	public MapBuilder map;
	
	//public Music music;
	
	public TextAnimator animator;
	public UIText center;
	
	public String[] listMessages;
	public int listIndex = 0; 
	
	@Override
	public void start() {
		map = new MapBuilder(20, 20, 3);
		
		map.setAll(new MapTile(4, false), 0);
		map.add(new MapTile(6, true), 4, 4, 1);
		map.add(new MapTile(6, false), 5, 4, 1);
		map.add(new MapTile(6, false), 6, 4, 1);
		map.add(new MapTile(6, true), 7, 4, 1);
		
		MultiTile m = new MultiTile(5, 5);
		m.setRange(0, 0, 3, 3, 3);
		
		map.add(m, 4, 4, 1);
		
		Scene.current.addAll(map.export());
		
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
		
		p = new ParticleSystem();
		p.texture = TextureSheet.getSheet("default").texture;
		p.subTexture = 13;
		p.particleSize = 1.0f;
		p.transform.setSize(10f, 10f);
		p.emitRate = 200;
		p.transform.setPosition(10f, 10f);
		
		//music = new Music("audio/mainmenu.ogg");
		//music.setLoop(true);
		
		//Mixer.instance.addTo(music, "music");
		Mixer.instance.getChannel("music").setGain(1.0f);
		
		Mixer.instance.listener.setGain(1.0f);
		
		listMessages = new String[]{
				"I wonder how I really even got here..",
				"Wouldn't you like to know...\nStalker.",
				"Anyways, I'm just here because I really like the color green."
		};
		
		String centerMsg = listMessages[listIndex];
		center = new UIText(centerMsg);
		center.setRGB(0f, 0.f, 0f);
		center.layer = 20;
		
		center.wrap = true;
		center.wrapWidth = 500f;
		
		Scene.current.uiScene.center(center);
		Scene.current.uiScene.add(center);
		center.position.y = 100f;
		
		animator = new TextAnimator(centerMsg, 100);
		animator.target = center;
		animator.start();
	}
	
	public void initObjects(){
		large = new LargeObject(24, 3, 2);
		large.transform.setSize(30f, 20f);
		large.transform.setPosition(25f, 25f);
		large.transform.setLayer(10);
		
		LargeObject large2 = new LargeObject(72, 3, 2);
		large2.transform.setSize(30f, 20f);
		large2.transform.setPosition(35f, 35f);
		large2.transform.setLayer(5);
		
		LargeObject dock = new LargeObject(67, 2, 3);
		dock.transform.setSize(20f, 30f);
		dock.transform.setPosition(50f, 50f);
		dock.transform.setLayer(5);
		
		LargeObject dockRepeat = new LargeObject(68, 1, 3);
		dockRepeat.transform.setSize(10f, 30f);
		dockRepeat.transform.setPosition(100f, 50f);
		dockRepeat.transform.setLayer(5);
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
			animator.increaseSpeed();
			if(animator.isCompleted() && listIndex != listMessages.length - 1){
				if(listIndex < listMessages.length){
					listIndex++;
				}
				String nmsg = listMessages[listIndex];
				center.setText(nmsg);
				animator.setText(nmsg);
				animator.start();
			}
		}
	}
	
	@Override
	public void update(long delta) {
		Vector2f vec = Player.instance.transform.getPosition();
		Scene.current.camera.setPosition(vec.x - 30f, vec.y - 30f, 0f);
		animator.update(delta);
	}
}
