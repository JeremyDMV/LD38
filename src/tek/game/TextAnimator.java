package tek.game;

import tek.ui.UIText;

public class TextAnimator {
	public String animated;
	
	private String full;
	
	private float currentLength;
	
	public int speedMod = 6;
	
	private float length = 100f;
	private float timer = 0f;
	
	private int index = 0;
	
	private boolean lengthPerChar = true;
	
	private boolean running = false;
	private boolean completed = false;
	
	public boolean skipSpaces = true;
	
	public UIText target;
	
	public TextAnimator(String message, float length){
		this.full = message;
		this.length = length;
	}
	
	public void setLengthPerChar(boolean lengthPerChar){
		if(this.lengthPerChar == lengthPerChar)
			return;
		index = 0;
		this.lengthPerChar = lengthPerChar;
	}
	
	public boolean isLengthPerChar(){
		return lengthPerChar;
	}
	
	public void setText(String text){
		this.full = text;
		reset();
	}
	
	public String getFull(){
		return full;
	}
	
	public void reset(){
		completed = false;
		index = 0;
		currentLength = length;
	}
	
	public void increaseSpeed(){
		currentLength = length / speedMod;
	}
	
	public boolean isCompleted(){
		return completed;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void start(){
		running = true;
		completed = false;
		index = 0;
		currentLength = length;
		timer = currentLength;
	}
	
	public void update(long delta){
		if(!running)
			return;
		
		timer -= delta;
		if(timer <= 0){
			timer = currentLength;
			index++;
		}
		
		if(index == full.length()){
			completed = true;
			animated = full;
		}else{
			char c = full.charAt(index);
			while(c == ' ' || c == '\n'){
				index++;
				c = full.charAt(index);
			}
			animated = full.substring(0, index);
		}
		
		if(completed){
			running = false;
			currentLength = length;
		}
		
		if(target != null){
			target.setText(animated);
		}
	}
}
