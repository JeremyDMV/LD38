package tek.game; 

import org.joml.Vector2f;

import tek.Window;
import tek.audio.Mixer;
import tek.audio.Music;
import tek.game.gameObjects.LevelBound;
import tek.game.gameObjects.PhysicsDummy;
import tek.game.levels.TestLevel;
import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.Animation;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.runtime.ParticleSystem;
import tek.runtime.Scene;
import tek.runtime.physics.BoxCollider;
import tek.runtime.physics.Collider.ColliderType;
import tek.ui.UIFont;
import tek.ui.UIScene;

public class Game implements Interface {
	
	Music music;
	ParticleSystem psystem;
	PhysicsDummy dummy, dummy2;
	
	public Level level;
	
	public UIFont test;
	
	public UIScene ui;
	
	
	public void loadLevel(Level level){
		if(this.level != null)
			level.end();
		
		this.level = level;
		level.start();
	}
	
	@Override
	public void start() {
		Keyboard.setupButton("horizontal", Keyboard.KEY_RIGHT, Keyboard.KEY_D, Keyboard.KEY_LEFT, Keyboard.KEY_A);
		
		ui = new UIScene();
		
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
		
		test = new UIFont("fonts/test.ttf", 16.0f);
		
		TextureSheet sheet = new TextureSheet(new Texture("textures/texsheet.png"), 16, 16, "test");
		TestPlayer gameObject = new TestPlayer();
		
		gameObject.texture = sheet.texture;
		gameObject.subTexture = 120;
		gameObject.transform.setSize(10f, 10f);
		gameObject.transform.setPosition(0, 0);
		
		Animation anim = new Animation(sheet, "test", 1000f / 20f, new int[]{
				112, 113, 114, 115
		});
		
		gameObject.addAnimation(anim);
		gameObject.setAnimation(0);
		
		gameObject.playAnimation();
		
		Scene.current.add(gameObject);
		
		dummy = new PhysicsDummy();
		dummy.transform.setPosition(20f, 40f);
		dummy.transform.setSize(10f, 10f);
		dummy.subTexture = 87;
		
		dummy.setCollider(new BoxCollider(dummy, dummy.transform.getSize()));
		dummy.setupCallback();
		
		Scene.current.add(dummy);
		
		dummy2 = new PhysicsDummy();
		dummy2.subTexture = 87;
		dummy2.transform.setPosition(5f, 1f);
		dummy2.setCollider(new BoxCollider(dummy2, dummy2.transform.getSize()));
		
		Scene.current.add(dummy2);
		
		psystem = new ParticleSystem();
		psystem.transform.setPosition(3f, 3f);
		psystem.transform.setSize(1f,1f);
		
		psystem.shader = pshader;
		psystem.emitLife = 2;
		psystem.particleLife = 1000f; //1 second 
		psystem.emitRate = 100;
		
		Scene.current.add(psystem);
		
		LevelBound bounds = new LevelBound();
		bounds.transform.setPosition(0f, 0f);
		bounds.setCollider(new BoxCollider(bounds, new Vector2f(100f, 1f), ColliderType.STATIC));
		
		
		Scene.current.add(bounds);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void input(long delta) {
		if(Keyboard.isClicked('c')){
			System.out.println(dummy.collider.body.getPosition());
			System.out.println(dummy.transform.getPosition().x + "," + dummy.transform.getPosition().y);
			dummy.transform.updateMatrix();
		}
		
		if(Mouse.isClicked(0)){
			System.out.println(""+Mouse.x + " : " + Mouse.y);
			Vector2f flipped = ui.flipY(Mouse.x, Mouse.y);
			System.out.println(flipped.x + " : " + flipped.y);
		}
		
		if(Keyboard.isClicked('p'))
			music.play();
		
		dummy.collider.applyLinearImpulse(new Vector2f(Keyboard.getButton("horizontal") * 10f, 0f), new Vector2f(0f, 0f));
	}

	@Override
	public void update(long delta) {
		
	}

	@Override
	public void render(long delta) {
		UIFont.prepRender();
		test.print(10, 35, "Angelina is \n\n gay 4 dix");
	}

}
