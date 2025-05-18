package solo_project;

public class Enemy extends Sprite{
	
	int health;
	int strength;
	int maxHealth;
	int enemyExp; //amount of exp gained from enemy
	boolean flashRed;
	long flashEndTime = 0;
	
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	public Enemy(String name, int x, int y, int enemyExp) {
		super(name , x, y, 80, 100 , pose, 3, 10);
		this.enemyExp = enemyExp;
		
	}

}
