package tek.game.levels;

import org.joml.Vector2f;

import tek.game.Level;
import tek.game.TextAnimator;
import tek.input.Keyboard;
import tek.runtime.Scene;
import tek.ui.UIElement;
import tek.ui.UIScene;
import tek.ui.UIScene.ClickType;
import tek.ui.UIScene.UIOptions;
import tek.ui.UIText;
import tek.ui.UITexture;

public class StartLevel extends Level {

	
	StartOptions uiOptions;
	TextAnimator anim;
	
	@Override
	public void start() {
		UIText t = new UIText("this is a test");
		Scene.current.uiScene.add(t);
		
		String msg = "I wonder how all of this will play out in the end.";
			
		Vector2f s = UIText.defaultFont.getWrappedSize(msg, 1f, 200);
		System.out.println(s.x + " : " + s.y);
		
		UIElement[] options = new UIElement[] {
			new UIText(msg),			
			new UIText("OPTIONS"),			
			new UIText("EXIT"),			
		};
		
		options[0].position.set(0, 100);
		((UIText)options[0]).setScale(0.4f);
		
		options[1].position.set(0, 66);
		options[2].position.set(0, 33);
		
		anim = new TextAnimator(msg, 100);
		anim.target = (UIText)options[0];
		
		options[0].setCallback(new UIElement.UICallback(){
			public UIElement e;
			
			@Override
			public void initialize(UIElement e){
				this.e = e;
			}
			
			@Override
			public void onFocusEnter() {
				System.out.println("hihihihi");
				if(e instanceof UIText){
					((UIText) e).setRGB(1f, 0.5f, 0.5f);
				}
			}

			@Override
			public void onFocus() {
				
			}

			@Override
			public void onFocusExit() {
				if(e instanceof UIText){
					((UIText) e).setRGB(1f, 1f, 1f);
				}
			}

			@Override
			public void onClick(ClickType type) {
				
			}
			
		});
		
		//centerOnWidest(options);
		
		Scene.current.uiScene.center(options);
		
		uiOptions = new StartOptions(Scene.current.uiScene, options);
		Scene.current.uiScene.options = uiOptions;
		
		anim.start();
	}
	
	public void centerOnWidest(UIElement[] elements){
		float max_w = 0;
		for(UIElement e : elements){
			float w = -1f;
			if(e instanceof UITexture){
				w = e.size.x;
			}else if(e instanceof UIText){
				w = ((UIText)e).getWidth();
			}
			
			if(w > max_w){
				max_w = w;
			}
		}
		
		for(UIElement e : elements){
			float w = -1f;
			if(e instanceof UITexture){
				w = e.size.x;
			}else if(e instanceof UIText){
				w = ((UIText)e).getWidth();
			}
			
			if(w == max_w)
				continue;
			
			e.position.x += (max_w - w) / 2f;
		}
	}
		

	@Override
	public void end() {
		
	}


	@Override
	public void input(long delta){
		if(Keyboard.isClicked(Keyboard.KEY_SPACE)){
			if(!anim.isCompleted())
				anim.increaseSpeed();
			else{
				anim.reset();
				anim.start();
			}
		}
	}
	
	@Override
	public void update(long delta) {
		anim.update(delta);
	}
	
	public static class StartOptions extends UIOptions {

		public StartOptions(UIScene scene, UIElement[] options) {
			super(scene, options);
		}

		@Override
		public void Input() {
			float v = Keyboard.getButton("vertical");
			
			if(v > 0){
				select(next());
			}else if(v < 0){
				select(previous());
			}
		}

		@Override
		public void Update(long delta) {
			
		}
		
	}
	
}
