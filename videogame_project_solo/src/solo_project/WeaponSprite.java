package solo_project;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class WeaponSprite extends Rect{
	
	//WeaponAnimation[] animation;
	
	WeaponAnimation animation;
	
	final public static int UP = 0;
	final public static int DN = 1;
	final public static int LT = 2;
	final public static int RT = 3;
	
	int current_pose = RT;
	
	String weaponName = "";
	
	
	
	
	public WeaponSprite (String name, int x, int y, int w, int h, int numFrames, int duration) {
		
		
		super(x, y, w ,h);
		
		this.weaponName = name;
		
		
			animation = new WeaponAnimation(name, numFrames , duration);
			
		
																	
	}
	

	public void draw(Graphics pen, int camX, int camY) {
		Graphics2D g2d = (Graphics2D) pen; //used to transform images
		AffineTransform old = g2d.getTransform(); 
		
		Image img = animation.nextImage();
		
		
	    if (current_pose == RT) {
	        g2d.drawImage(img, x - camX, y - camY, w, h, null);
	    }

	    if (current_pose == LT) {
	        g2d.drawImage(img, x + w - camX, y - camY, -w, h, null); // mirror horizontally
	    }

	    if (current_pose == UP) {
	        g2d.translate(x - camX + w / 2, y - camY + h / 2); // move to center
	        g2d.rotate(Math.toRadians(-90));     // rotate -90 degrees
	        g2d.drawImage(img, -w / 2, -h / 2, w, h, null); // draw centered
	        g2d.setTransform(old); // reset
	    }

	    if (current_pose == DN) {
	        g2d.translate(x - camX + w / 2, y - camY + h / 2); // move to center
	        g2d.rotate(Math.toRadians(90));     // rotate 90 degrees
	        g2d.drawImage(img, -w / 2, -h / 2, w, h, null); // draw centered
	        g2d.setTransform(old); // reset
	    }
	}
	
	
	
	public void face_UP(int dy)
	{
		
		current_pose = UP;
	}
	
	public void face_DN(int dy)
	{		
		
		current_pose = DN;
	}
	
	public void face_LT(int dx)
	{	
		
		current_pose = LT;
	}
	
	
	public void face_RT(int dx)
	{
		
		current_pose = RT;
	}

}
