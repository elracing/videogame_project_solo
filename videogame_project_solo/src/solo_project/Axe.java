package solo_project;

public class Axe extends WeaponSprite{
	
	final int numFrames = 4;
	int Strength = 30;
	public static long weaponDelay = 3000; //weapon damage delay in milliseconds
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	//variables for axe positioning, very similar mechanics to the bow
	boolean midAir = false;
	int vx = 0;
	int vy = 8;
	long throwTime = 0;
	int flightDuration = 3000; //ms
	int locked_pose = WeaponSprite.RT; //locks pose
	
	
	
	public Axe(int x, int y, int numFrames, boolean loopable) {
		super("axe", x, y, 150, 150 , numFrames, 70, loopable);
	}

}
