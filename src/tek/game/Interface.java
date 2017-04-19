package tek.game;

public interface Interface {
	//state changes
	public void start();
	public void end();
	
	//delta affected functions 
	public void input (long delta);
	public void update(long delta);
	public void render(long delta);
}
