package tek.game.levels;

import tek.input.Keyboard;
import tek.ui.UIScene.UIOptions;

public class TestOptions extends UIOptions {

	public TestOptions(UIOption[] options) {
		super(options);
	}

	@Override
	public void Update(long delta) {
		
	}

	@Override
	public void Input() {
		if(Keyboard.isClicked('d') || Keyboard.isClicked(Keyboard.KEY_RIGHT)){
			select(next());
		}else if(Keyboard.isClicked('a') || Keyboard.isClicked(Keyboard.KEY_LEFT)){
			select(previous());
		}
	}

}
