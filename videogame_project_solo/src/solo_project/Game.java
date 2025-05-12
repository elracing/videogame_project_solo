package solo_project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends GameBase{
	
	public long currentTime = 0;
	public int enemyCount = 0;
	public int weaponsActivated = 1;
	int camX = 0; //camera x
	int camY = 0; //camera y
	int screenWidth = 2736;
	int screenHeight = 1824;
	
	
	//activated weapons
	
	boolean swordActive = true;
	boolean swordAnimating = false;
	long swordAnimationEndTime = 0;
	
	//activated passives
	
	
	Player player = new Player(screenWidth / 2, screenHeight / 2); //make player in middle of screen
	
	
	//weapon variable and list setup
	ArrayList<WeaponSprite> weapons = new ArrayList<>();
	Sword sword; 

	
	
	//set up weapon timers
	
	long swordActivationTimer = currentTime;
	
	
	//set up list containing enemies
	
	ArrayList<Enemy> enemies = new ArrayList<>(); //contains all enemies
	ArrayList<Enemy> enemiesToAdd = new ArrayList<>(); //queue of enemies to add
	
	
	final int size = 64;
	
	Image[] tile = new Image[1];
	
	
	String[] map = 
		{	
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
			
		};

	
	
	public void inGameLoop() {
		currentTime = System.currentTimeMillis(); //internal game timer
		
		if (player!= null) { //core loop if player is not dead
			
			
			//camera settings
			
			camX = player.x - screenWidth / 2 + player.w / 2;
			camY = player.y - screenHeight / 2 + player.h / 2;
			
			//distances for spawn
			int spawnDistance = 4000;
			int maxOffset = 800;
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				if(enemyCount <= 100) enemiesToAdd.add(new Skeleton("skeleton", player.x + (int)(Math.random() * maxOffset + spawnDistance), (int)(Math.random() * maxOffset + spawnDistance), 100, 10, 100)); //spawn randomly around player out of sight
				enemyCount++;
			}
			
			enemies.addAll(enemiesToAdd);
			enemiesToAdd.clear();

		
			player.moving = false;
	
		//change directions, including weapon direction
			if(pressing[LT]) {   
				player.go_LT(6);
				for (WeaponSprite weapon : weapons) {
					weapon.current_pose = WeaponSprite.LT;
				}
			}
			if(pressing[RT])   {
				player.go_RT(6);
				for (WeaponSprite weapon : weapons) {
					weapon.current_pose = WeaponSprite.RT;		
				}
			}
			if(pressing[UP])   {
				player.go_UP(6);
				for (WeaponSprite weapon : weapons) {
					weapon.current_pose = WeaponSprite.UP;			
				}
			}
			if(pressing[DN])   {
				player.go_DN(6);
				for (WeaponSprite weapon : weapons) {
					weapon.current_pose = WeaponSprite.DN;		
				}
			}
			
			
			//enemy movement 
			
			for (Enemy enemy : enemies) {
			
				enemy.moving = false;
				enemy.chase(player);
				
				if(enemy.moving_up) {
					enemy.moving = true;
					enemy.current_pose = Sprite.UP;
					
				}
				if(enemy.moving_dn) {
					enemy.moving = true;
					enemy.current_pose = Sprite.DN;
					
				}
				
				if(enemy.moving_lt) {
					enemy.moving = true;
					enemy.current_pose = Sprite.LT;
					
				}
				
				if(enemy.moving_rt) {
					enemy.moving = true;
					enemy.current_pose = Sprite.RT;
					
				}
			}
			
			
			
			//Damage logic section
			Iterator<Enemy> iter = enemies.iterator(); //use iterator to safety remove enemies
			while (iter.hasNext()) {
				Enemy enemy = iter.next();
				
				if (enemy.overlaps(player) && currentTime - player.timeSinceLastHit > 1000) { //calculate damage
					player.health -= enemy.strength;
					player.timeSinceLastHit = currentTime;
					
					//flash character red
					
					player.flashRed = true;
					player.flashEndTime = currentTime + 200;
				}
					
				if(player.flashRed && currentTime > player.flashEndTime) { //stop red flash
						player.flashRed = false;
						
				}
				
				//calculate damage agaisn't enemies
				
				//update sword's coordinates
				sword.x = player.x + 10;
				sword.y = player.y; 
				if (sword.overlaps(enemy) || sword.contains(enemy.x, enemy.y)){	
					enemy.health -= sword.Strength;
				}
				
				if(enemy.health <= 0) {
					iter.remove();
					enemyCount--;
				}
				
			}
			
			
			//weapon activation checks
			

			
		} //end of player death check
	}
		
	
	
	


	public void paint (Graphics pen) {
		
		//draw tilemap
		
		for (int i = 0; i <map.length; i++) {
			for (int j = 0; j < map[i].length(); j++) {
				char c = map[i].charAt(j);
				
				
				if (c == 'A') pen.drawImage(tile[0], j * size, i * size, size, size, null);
			}
		}
		
		
		if (player!= null) {  //no drawing if player is dead
			//draw characters
			player.draw(pen);
			
			
			//draw enemies
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				for(Enemy enemy : enemies) {
					if (enemy != null) enemy.draw(pen);
					
					
					
					//draw enemy health
					
					int barWidth = 50;
					int barHeight = 6;
					int healthBarX = enemy.x + (enemy.w / 2) - (barWidth / 2);
					int healthBarY = enemy.y - 10;
					
					
					//bar background
					pen.setColor(Color.DARK_GRAY);
					pen.fillRect(healthBarX, healthBarY, barWidth, barHeight);
					
					//red bar set up
					
			        pen.setColor(Color.RED);
			        int currentBarWidth = (int)((enemy.health / (double)enemy.maxHealth) * barWidth);
			        pen.fillRect(healthBarX, healthBarY, currentBarWidth, barHeight);
				}
			}
			
			
			//draw player health
			
			pen.setColor(Color.RED);
			pen.setFont(new Font("Arial", Font.BOLD, 36));
			pen.drawString("HP: " + player.health, 20, 40);
			pen.drawString(String.valueOf(currentTime), 1000, 40);
			

			
			//flash red drawer
			if (player.flashRed) {
				//pen.setColor(new Color(255,0,0,100));
				//pen.fillRect(player.x,  player.y, player.w, player.h); //draw red overlay   THIS IS THE PREVIOUS FLASHING RED VERSION, REPLACED WITH AN IMAGE NOW
				
				int index = (int)(Math.random() * 3); // gives 0, 1, or 2
				pen.drawImage(Toolkit.getDefaultToolkit().getImage("hit_" + String.valueOf(index) + ".png"), player.x - 20, player.y, 100, 100, null); //draws hit indicator on player
				
			}
			
			//weapon drawing logic
			
			
			//sword
			if(swordActive) { //check if sword is active
				
				if (currentTime >= swordActivationTimer + Sword.weaponDelay) {		
					sword.animation.reset();
					swordAnimating = true;
					swordAnimationEndTime = currentTime + sword.animation.numFrames * sword.animation.duration;
					swordActivationTimer = currentTime;
				}
				
				if (swordAnimating) {
					sword.draw(pen);
					
					if (currentTime >= swordAnimationEndTime) {
						swordAnimating = false;
					}
				}
				
				
			} //end of sword logic
			
			
			//Bow
			
		}
		
		
		
		//handle player death
		
		if (player != null && player.health <= 0) {
			player = null;
		}
		

		
		
		

		
		
	}
	public void initialize() {
		
		//fetch tilemap data
		for (int i = 0; i < tile.length; i++) {
			tile[i] = Toolkit.getDefaultToolkit().getImage("tile" + (i+1) + ".png");
		}
		
		sword = new Sword(player.x, player.y, 9);  //start with sword as default
		weapons.add(sword);
	
		
	}



}
