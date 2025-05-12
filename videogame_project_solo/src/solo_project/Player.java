package solo_project;

public class Player extends Sprite{
	
	int health = 100;
	public long timeSinceLastHit = 0;
	
	//damage indicator
	
	public boolean flashRed = false;
	public long flashEndTime = 0;
	
	
	
	//pose array
	
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	
	
	//constructor
	
	public Player(int x, int y) {
		super("player" , x, y, 50, 100 , pose, 3, 10);
	}

}
