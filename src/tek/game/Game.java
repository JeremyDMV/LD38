package tek.game;

import tek.audio.Mixer;
import tek.game.gameObjects.Player;
import tek.game.levels.StartLevel;
import tek.game.levels.UpperLevel;
import tek.input.Keyboard;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem;
import tek.runtime.Scene;
import tek.ui.UIFont;
import tek.ui.UIScene;
import tek.ui.UIText;

public class Game implements Interface {
	
	public Level level;
	
	public Player player;
	
	public void loadLevel(Level level){
		if(this.level != null)
			level.end();
		
		this.level = level;
		
		level.start();
	}
	
	@Override
	public void start() {
		setupDefaults();
		
		loadLevel(new UpperLevel());
		
		player = new Player();
		player.transform.setPosition(10, 10);
		player.transform.setLayer(-1);
		Scene.current.add(player);
	}
	
	private void setupDefaults(){
		Keyboard.setupButton("horizontal", Keyboard.KEY_RIGHT, Keyboard.KEY_D, Keyboard.KEY_LEFT, Keyboard.KEY_A);
		Keyboard.setupButton("vertical", Keyboard.KEY_UP, Keyboard.KEY_W, Keyboard.KEY_DOWN, Keyboard.KEY_S);
		
		UIText.defaultFont = new UIFont("fonts/font.ttf", 24f);
		
		Shader d = new Shader("default", "shaders/default.vs", "shaders/default.fs");
		Scene.current.defaultShader = d;
		
		Shader u = new Shader("ui", "shaders/ui.vs", "shaders/ui.fs");
		UIScene.defaultShader = u;
		
		ParticleSystem.defaultShader = d;
		
		//default spritesheet
		Texture t = new Texture("textures/spritesheet.png");
		TextureSheet sheet = new TextureSheet(t, 16, 16, "default");
		
		//music 0, sfx 1
		Mixer.instance.createChannel("music");
		Mixer.instance.createChannel("sfx");
	}

	@Override
	public void end() {
		
	}

	@Override
	public void input(long delta) {
		player.input(delta);
		level.input(delta);
	}

	@Override
	public void update(long delta) {
		level.update(delta);
	}

	@Override
	public void render(long delta) {
	}

}
