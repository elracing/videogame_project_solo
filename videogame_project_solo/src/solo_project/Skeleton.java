package solo_project;

public class Skeleton extends Enemy {
	
	
	
	public Skeleton(String name, int x, int y, int health, int strength, int maxHealth) {
		super(name, x, y);
		
		this.health = health;
		this.strength = strength;
		this.maxHealth = maxHealth;
		
		
	}

}
