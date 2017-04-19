package tek.game;

import tek.audio.Listener;
import tek.audio.Mixer;
import tek.audio.Source;
import tek.input.Keyboard;
import tek.runtime.GameObject;

public class TestPlayer extends GameObject {
	public float movespeed = 6f;
	
	public Listener listener;
	
	@Override
	public void Start(){
		source = new Source();
		listener = Mixer.instance.listener;
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
		
		transform.move((float)(mx * movespeed * adjustedDelta),(float)(my * movespeed * adjustedDelta));
	}
}
