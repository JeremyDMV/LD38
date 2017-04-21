package tek.game;

import org.joml.Vector2f;

import tek.audio.Listener;
import tek.audio.Mixer;
import tek.audio.Source;
import tek.input.Keyboard;
import tek.render.Camera;
import tek.runtime.GameObject;
import tek.runtime.Scene;

public class TestPlayer extends GameObject {
	public float movespeed = 6f;
	
	public Listener listener;
	public Camera camera;
	
	@Override
	public void Start(){
		source = new Source();
		listener = Mixer.instance.listener;
		camera = Scene.current.camera;
	}
	
	@Override
	public void Update(long delta){
		if(!listener.getPosition().equals(transform.getPosition())){
			listener.setPosition(transform.getPosition());
		}
		
		double adjustedDelta = delta / 1000d; //1s / delta time
		
		float mx = 0, my = 0;
		if(Keyboard.isDown('a')){
			mx = -1;
		}else if(Keyboard.isDown('d')){
			mx = 1;
		}
		
		if(Keyboard.isDown('w')){
			my = 1;
		}else if(Keyboard.isDown('s')){
			my = -1;
		}
		
		if(Keyboard.isClicked(Keyboard.KEY_SPACE)){
			playAnimation();
		}
		
		Vector2f pos = transform.getPosition();
		
		//transform.move((float)(mx * movespeed * adjustedDelta),(float)(my * movespeed * adjustedDelta));
	}
}
