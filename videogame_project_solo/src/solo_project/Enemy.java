package solo_project;

public class Enemy extends Sprite{
	
	int health;
	int strength;
	int maxHealth;
	
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	public Enemy(String name, int x, int y) {
		super(name , x, y, 50, 100 , pose, 3, 10);
		
	}

}
