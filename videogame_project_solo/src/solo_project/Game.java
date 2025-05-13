package solo_project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game extends GameBase{
	
	public long currentTime = 0;
	public long currentTimeSeconds = 0;
	public long startTime = 0; //grabs system time at start
	
	

	public int enemyCount = 0; //count of enemies added
	public int enemiesThisFrame= 0; //count of enemies to add
	public int weaponsActivated = 1;
	int camX = 0; //camera x
	int camY = 0; //camera y
	int roundNumber = 1; //tracks current round
	boolean roundEnd = true; //controls whether round has ended
	int enemiesSpawned = 0; //tracks amount of spawns per round
	int enemiesKilledThisRound = 0; //tracks kills per round
	int enemiesKilledTotal = 0; //tracks kills total
	
	int currentLevel = 1; //character level
	final int baseLevelUpExpNeeded = 1000; //base exp needed to level up
	int scaledLevelUpExpNeeded = 1000; //scaled exp requirement
	
	
	int exp = 0; //tracks experience points, used for upgrades
	
	
	
	//activated weapons
	
	//sword (default on)
	boolean swordActive = true;
	boolean swordAnimating = false;
	long swordAnimationEndTime = 0;
	
	//bow
	
	boolean bowActive = false;
	boolean bowAnimating = false; 
	long bowAnimationEndTime = 0;
	
	
	
	//activated passives
	
	
	Player player = new Player((90 * 128) / 2, (28 * 128) / 2); //make player in the middle of the world (tilemap is 90x28, size = 128)
	
	
	//weapon variable and list setup
	ArrayList<WeaponSprite> weapons = new ArrayList<>();
	Sword sword; 

	
	
	//set up weapon timers
	
	long swordActivationTimer = currentTime;
	
	
	//set up list containing enemies
	
	ArrayList<Enemy> enemies = new ArrayList<>(); //contains all enemies
	ArrayList<Enemy> enemiesToAdd = new ArrayList<>(); //queue of enemies to add
	
	
	//Strings for tiles
	final int size = 128;
	
	Image[] tile = new Image[2];
	
	
	String[] map =
		{	
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
			
		};


	
	
	public void inGameLoop() {
		currentTime = System.currentTimeMillis(); //internal game timer
		currentTimeSeconds = (int) (currentTime / 1000) -  startTime ;
		
		if (player!= null) { //core loop if player is not dead
			
	        Random random = new Random();
	        double r = random.nextDouble(); // Generates a random number between 0.0 and 1.0, this is used to adjust enemy type spawn rates
	        int result;

	        if (r < 0.6) { // 60% chance
	            result = 1;
	        } else if (r < 0.85) { // 25% chance
	            result = 2;
	        } else { // 15% chance
	            result = 3;
	        }

			
			
			//camera settings
			
			camX = player.x - screenWidth / 2 + player.w / 2;
			camY = player.y - screenHeight / 2 + player.h / 2;
			
			//distances for spawn
			int spawnDistance = 500;
			int maxOffset = 800;
			
			//enemy spawns
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				if (enemyCount <= 100 && enemiesThisFrame <= 0 && enemiesSpawned <= 100) {
				    double angle = Math.random() * 2 * Math.PI; // Random angle
				    double radius = spawnDistance + Math.random() * maxOffset; // Random distance away

				    int spawnX = player.x + (int)(Math.cos(angle) * radius);
				    int spawnY = player.y + (int)(Math.sin(angle) * radius);
				    
				    switch(result) {
				    
				    case 1:  enemiesToAdd.add(new Skeleton("skeleton", spawnX, spawnY, 100 + (roundNumber * 5), 10, 100, 100));
				    enemiesThisFrame++;
				    enemyCount++;
				    break;
				    
				    case 2:  enemiesToAdd.add(new Skeleton("skeleton_red_eye", spawnX, spawnY, 200 + (roundNumber * 5), 15, 200, 200));
				    enemiesThisFrame++;
				    enemyCount++;
				    break;
				    
				    case 3:  enemiesToAdd.add(new Skeleton("skeleton_red", spawnX, spawnY, 300 + (roundNumber * 5), 35, 300, 300));
				    enemiesThisFrame++;
				    enemyCount++;
				    break;
				    }
				   
				}
				
				
		
			
			
			enemies.addAll(enemiesToAdd);
			enemiesToAdd.clear();
			enemiesThisFrame = 0;
			}
			
			if (enemiesKilledThisRound == 100) {
				enemiesKilledThisRound = 0;
				enemiesSpawned = 0;
				roundNumber++;
			}

		
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
				
				//enemy collision 
				
				//enemy collision
				for (int i = 0; i < enemies.size(); i++) { //get first enemy
				    Enemy e1 = enemies.get(i);

				    for (int j = i + 1; j < enemies.size(); j++) { //compare with second enemy
				        Enemy e2 = enemies.get(j);

				        if (e1.overlaps(e2)) {

				            // Compute the pushback direction
				            int dx = e1.x - e2.x;
				            int dy = e1.y - e2.y;

				            // Normalize the direction to avoid diagonal stretching
				            double dist = Math.sqrt(dx * dx + dy * dy);
				            
				            if (dist == 0) dist = 1; // avoid division by zero

				            double pushStrength = 2.0; // how strong the separation is

				            int pushX = (int)((dx / dist) * pushStrength);
				            int pushY = (int)((dy / dist) * pushStrength);

				            e1.pushBy(pushX, pushY);
				            e2.pushBy(-pushX, -pushY);
				        }
				    }
				}
				
				
				//calculate damage agaisn't enemies
				
				//update sword's coordinates
				
				if (swordAnimating) {
				    // Update sword position and hitbox based on direction
				    switch (sword.current_pose) {
				        case WeaponSprite.RT:
				            sword.x = player.x + player.w;
				            sword.y = player.y;
				            sword.w = 200;
				            sword.h = 100;
				            break;
				        case WeaponSprite.LT:
				            sword.x = player.x - 200;
				            sword.y = player.y;
				            sword.w = 200;
				            sword.h = 100;
				            break;
				        case WeaponSprite.UP:
				            sword.x = player.x;
				            sword.y = player.y - 200;
				            sword.w = 100;
				            sword.h = 200;
				            break;
				        case WeaponSprite.DN:
				            sword.x = player.x;
				            sword.y = player.y + player.h;
				            sword.w = 100;
				            sword.h = 200;
				            break;
				    }
					if (sword.overlaps(enemy) || sword.contains(enemy.x, enemy.y)){	
						enemy.health -= sword.Strength;
					}
					
					if(enemy.health <= 0) { //enemy kill counts
						iter.remove();
						enemyCount--;
						enemiesKilledThisRound++;
						enemiesKilledTotal++;
						exp += enemy.enemyExp;
						
					}
					
				}
				
			}
			
			//weapon activation checks
			
			//level check
			if (exp >= scaledLevelUpExpNeeded) {
				//levelUp();
				scaledLevelUpExpNeeded = baseLevelUpExpNeeded + (roundNumber * 100);
			}
			
		} //end of player death check
	}
		
	
	
	


	public void paint (Graphics pen) {
		
		//draw tilemap
		
		for (int i = 0; i <map.length; i++) {
			for (int j = 0; j < map[i].length(); j++) {
				char c = map[i].charAt(j);
				
				
				if (c == 'A') pen.drawImage(tile[0], j * size - camX, i * size - camY, size, size, null);
				if (c == 'W') pen.drawImage(tile[1], j * size - camX, i * size - camY, size, size, null);

			}
		}
		
		
		if (player!= null) {  //no drawing if player is dead
			//draw characters
			player.draw(pen, camX, camY);
			
			
			//draw enemies
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				for(Enemy enemy : enemies) {
					if (enemy != null) enemy.draw(pen, camX, camY);
					
					

					
					
					
					//draw enemy health
					
					int barWidth = 50;
					int barHeight = 6;
					int healthBarX = enemy.x - camX + (enemy.w / 2) - (barWidth / 2);
					int healthBarY = enemy.y - camY - 10;
					
					
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
			pen.drawString(String.valueOf(currentTimeSeconds), 1300, 40); //display timer on top of the screen in seconds
			
			// draw round number
			
			pen.drawString("Round " + roundNumber, 2500, 40);
			
			//draw player level
			pen.drawString("lv " + String.valueOf(currentLevel), 20, 40);
			

			
			//flash red drawer
			if (player.flashRed) {
		
				int index = (int)(Math.random() * 3); // gives 0, 1, or 2
				pen.drawImage(Toolkit.getDefaultToolkit().getImage("hit_" + String.valueOf(index) + ".png"), player.x - 20 - camX, player.y -camY, 100, 100, null); //draws hit indicator on player
				
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
					sword.draw(pen, camX, camY);
					
					
					
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
		startTime = (int) (System.currentTimeMillis() / 1000); //start time in seconds
		

		
		//fetch tilemap data
		for (int i = 0; i < tile.length; i++) {
			tile[i] = Toolkit.getDefaultToolkit().getImage("tile" + (i+1) + ".png");
		}
		
		sword = new Sword(player.x, player.y, 9);  //start with sword as default
		weapons.add(sword);
	
		
	}



}
