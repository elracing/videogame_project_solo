package solo_project;

public class Bow extends WeaponSprite{
	
	final int numFrames = 4;
	int Strength = 15;
	public static long weaponDelay = 3000; //weapon damage delay in milliseconds
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	//variables for arrow positioning
	boolean midAir = false;
	int vx = 0;
	int vy = 0;
	long fireTime = 0;
	int flightDuration = 3000; //ms
	int locked_pose = WeaponSprite.RT; //locks pose
	
	
	public Bow(int x, int y, int numFrames) {
		super("arrow", x, y, 200, 200 , numFrames, 250);
	}

}
