package solo_project;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Sword extends WeaponSprite{
	
	final int numFrames = 9;
	int Strength = 30;
	public static long weaponDelay = 1200; //weapon damage delay in milliseconds
	static String[] pose = {"up", "dn", "lt", "rt"};
	
	
	public Sword(int x, int y, int numFrames, boolean loopable) {
		super("sword_slash", x, y, 300, 300 , numFrames, 10, loopable);
	}



	
}
