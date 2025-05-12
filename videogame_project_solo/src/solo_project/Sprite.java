package solo_project;

import java.awt.*;


public class Sprite extends Rect{
	
		Animation[] animation;
		
		boolean moving = false;
		
		final public static int UP = 0;
		final public static int DN = 1;
		final public static int LT = 2;
		final public static int RT = 3;
		
		int current_pose = DN;
		
		
		public Sprite (String name, int x, int y, int w, int h, String[] pose, int count, int duration) {
			
			super(x, y, w ,h);
			
			animation = new Animation[pose.length];
			
			for(int i = 0; i < pose.length; i++) {
				
				animation[i] = new Animation(name + "_" + pose[i], count, duration);
				
			}
			
			
		}
		

		public void draw(Graphics pen) {
			
			if(moving) {
				pen.drawImage(animation[current_pose].nextImage(), x, y, w, h, null);
			}
			
			else {
				pen.drawImage(animation[current_pose].stillImage(), x, y, w, h,  null);
				
				moving = false;
			}
			
		}
		
		public void go_UP(int dy)
		{
			old_y = y;
			
			y -= dy;
			
			moving = true;
			
			current_pose = UP;
		}
		
		public void go_DN(int dy)
		{
			old_y = y;
			
			y += dy;		

			moving = true;
			
			current_pose = DN;
		}
		
		public void go_LT(int dx)
		{
			old_x = x;
			
			x -= dx;		

			moving = true;
			
			current_pose = LT;
		}
		
		
		public void go_RT(int dx)
		{
			old_x = x;
			
			x += dx;		
			
			moving = true;
			
			current_pose = RT;
		}
		
	}


