package solo_project;

public class Skeleton extends Enemy {
	
	public static String skeletonA = "skeleton";
	public static String skeletonB = "skeleton_red_eye";
	public static String skeletonC = "skeleton_red";

	
	
	public Skeleton(String name, int x, int y, int health, int strength, int maxHealth, int enemyExp) {
		super(name, x, y, enemyExp);
		
		this.health = health;
		this.strength = strength;
		this.maxHealth = maxHealth;
		
		
	}

}
