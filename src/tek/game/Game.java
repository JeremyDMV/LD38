package tek.game; 

import tek.Window;
import tek.audio.Mixer;
import tek.audio.Music;
import tek.game.levels.TestLevel;
import tek.input.Keyboard;
import tek.render.Animation;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem;
import tek.runtime.Scene;

public class Game implements Interface {
	
	Music music;
	ParticleSystem psystem;
	
	public Level level;
	
	public void loadLevel(Level level){
		if(this.level != null)
			level.end();
		
		this.level = level;
		level.start();
	}
	
	@Override
	public void start() {
		loadLevel(new TestLevel());
		Mixer.instance.createChannel("music");
		Mixer.instance.createChannel("sfx");
		
		music = new Music("audio/bg.ogg");
		
		Mixer.instance.addTo(music, "music");
		
		music.setLoop(true);
		//music.play();
		
		Shader shader = Shader.get("default");
		Scene.current.defaultShader = shader;
		
		Shader pshader = new Shader("particle", "shaders/particle.vs", "shaders/particle.fs");
		ParticleSystem.defaultShader = pshader;
		
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
		
		psystem = new ParticleSystem();
		psystem.transform.setPosition(3f, 3f);
		psystem.transform.setSize(1f,1f);
		
		psystem.shader = pshader;
		psystem.emitLife = 2;
		psystem.particleLife = 1000f; //1 second 
		psystem.emitRate = 100;
		
		Scene.current.add(psystem);

	}

	@Override
	public void end() {
		
	}

	@Override
	public void input(long delta) {
		if(Keyboard.isClicked('c'))
			System.out.println(psystem.particles.size());
		
		if(Keyboard.isClicked('p'))
			music.play();
	}

	@Override
	public void update(long delta) {
		
	}

	@Override
	public void render(long delta) {
		
	}

}
