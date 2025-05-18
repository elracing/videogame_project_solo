package solo_project;

import java.awt.Image;
import java.awt.Toolkit;

public class WeaponAnimation {
	
	Image[] image;
	int current = 0;
	int duration; //in ms
	int numFrames;
	long lastFrameTime = 0;
	boolean loopable; //flag for animation looping
	
	public WeaponAnimation(String name, int numFrames, int duration, boolean loopable)
	{
		image = new Image[numFrames];
		
		for(int i = 0; i < numFrames ; i++) {
			image[i] = Toolkit.getDefaultToolkit().getImage(name + "_" + (i + 1) + ".png");
		}
		
		this.duration = duration;
		this.numFrames = numFrames;
		this.lastFrameTime = System.currentTimeMillis();
		this.loopable = loopable;
		
	}
	
	public Image nextImage() {
		long now = System.currentTimeMillis(); //set current time
		
		if(now - lastFrameTime >= duration) { //current time indicates we are past the duration time
			
			current++;
			lastFrameTime = now;  //reset last frame time
			
			if(current >= image.length) {
				
					if(loopable) {
						current = 1; //loop
					}
					else {
						current = image.length - 1; //stay on last frame
					}
			}
		    
		}
		
		return image[current];	
		
	}
	
	public void reset() { //helps reset animation
	    current = 0;
	    lastFrameTime = System.currentTimeMillis();
	}

	

}
