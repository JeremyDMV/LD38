package tek.game; 

import tek.Window;
import tek.audio.Mixer;
import tek.audio.Music;
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
	
	@Override
	public void start() {
		Mixer.instance.createChannel("music");
		Mixer.instance.createChannel("sfx");
		
		music = new Music("audio/bg.ogg");
		
		Mixer.instance.addTo(music, "music");
		
		music.setLoop(true);
		//music.play();
		
		Shader shader = new Shader("default", "shaders/default.vs", "shaders/default.fs");
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
			psystem.emit();
	}

	@Override
	public void update(long delta) {
		
	}

	@Override
	public void render(long delta) {
		
	}

}
