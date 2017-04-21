package tek.game.levels;

import org.joml.Vector2f;

import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.TextureSheet;
import tek.ui.UIElement;
import tek.ui.UIElement.UICallback;
import tek.ui.UIScene;
import tek.ui.UIScene.ClickType;
import tek.ui.UIScene.UIOptions;
import tek.ui.UITexture;

public class TestOptions extends UIOptions {

	public TestOptions(UIScene scene, UIElement[] options) {
		super(scene, options);
	}

	@Override
	public void Update(long delta) {
		
	}

	@Override
	public void Input() {
		if(Keyboard.isClicked('d') || Keyboard.isClicked(Keyboard.KEY_RIGHT)){
			select(next());
			System.out.println(this.current);
		}else if(Keyboard.isClicked('a') || Keyboard.isClicked(Keyboard.KEY_LEFT)){
			select(previous());
			System.out.println(this.current);
		}
	}

	public static TestOptions create(UIScene scene){
		UIElement[] elements = new UIElement[4];
		
		elements[0] = new UITexture(TextureSheet.getSheet("test").texture, 12);
		
		elements[0].callback = new UICallback(){
			@Override
			public void onFocusEnter() {
				System.out.println("Hello world");
			}

			@Override
			public void onFocus() {
			}

			@Override
			public void onFocusExit() {
				System.out.println("Hello world");
			}

			@Override
			public void onClick(ClickType type) {
				if(type.mouse && type.value == Mouse.LEFT_BUTTON){
					System.out.println("ouch");
				}else if(!type.mouse && type.value == Keyboard.KEY_SPACE){
					System.out.println("LUL");
				}
			}
		};
		
		elements[0].set(new Vector2f(100f, 100f), new Vector2f(100f, 100f));
		scene.center(elements[0]);
		
		elements[1] = new UITexture(TextureSheet.getSheet("test").texture, 3);
		elements[2] = new UITexture(TextureSheet.getSheet("test").texture, 45);
		elements[3] = new UITexture(TextureSheet.getSheet("test").texture, 115);
		
		return new TestOptions(scene, elements);
	}
	
}
