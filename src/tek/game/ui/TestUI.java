package tek.game.ui;

import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.Texture;
import tek.ui.UIScene.ClickType;
import tek.ui.UITexture;

public class TestUI extends UITexture {
	
	public TestUI(Texture texture) {
		super(texture);
	}

	@Override
	public void onFocusEnter() {
		
	}

	@Override
	public void onFocus() {
		
	}

	@Override
	public void onFocusExit() {
		
	}

	@Override
	public void onClick(ClickType type) {
		if(type.mouse){
			if(contains(Mouse.pos))
				System.out.println("ok");
		}else if(!type.mouse){
			if(type.value == Keyboard.KEY_SPACE){
				System.out.println("okkkk");
			}
		}
	}

}
