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
	public int framesSinceLastSpawn= 0; //frame counter for enemy spawn, used for delays
	
	public int collisionFrameCounter = 0; //tracks frames passed, used to delay collision detection per frame
	public int weaponsActivated = 1; // tracks weapons activated, TODO used for capping weapons obtained
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
	boolean levelUpWaiting = false; //flag for prompting level up screen
	
	
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
	long bowPositionTimer = 0; //this timer is used to reset the arrow's position towards the player once expired
	int framesSinceFired = 0; //tracks frames passed when last fired
	
	//axe
	
	boolean axeActive = false;
	boolean axeAnimating = false; 
	long axeAnimationEndTime = 0;
	long axePositionTimer = 0; //this timer is used to reset the 's position towards the player once expired
	int framesSinceThrown = 0; //tracks frames passed when last fired
	
	
	
	//activated passives
	
	
	Player player = new Player((90 * 128) / 2, (28 * 128) / 2); //make player in the middle of the world (tilemap is 90x28, size = 128)
	
	
	//weapon variable and list setup
	ArrayList<WeaponSprite> weapons = new ArrayList<>();
	Sword sword; 
	Bow bow;
	Axe axe;

	
	
	//set up weapon timers
	
	long swordActivationTimer = currentTime;
	long bowActivationTimer = currentTime;
	long axeActivationTimer = currentTime;
	
	//set up list containing enemies
	
	ArrayList<Enemy> enemies = new ArrayList<>(); //contains all enemies
	ArrayList<Enemy> enemiesToAdd = new ArrayList<>(); //queue of enemies to add
	
	
	//set up reward list
	Reward[] levelUpChoices = new Reward[8];
	Reward[] displayedChoices = new Reward [3];
	int rewardsRandomized = 0; //tracks reward randomization
	
	//Strings for tiles
	final int size = 128;
	
	Image[] tile = new Image[2];
	
	
	String[] map =
		{	    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
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
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
			
		};
	
	//list of wall tiles
	
	ArrayList<Rect> walls = new ArrayList<>();
	boolean wallsBuilt = false; //boolean to trigger wall object creation


	//game over flag
	boolean gameOver = false;
	
	
	public void inGameLoop() {
		//first check for level up rewards on freeze screen
		if (levelUpWaiting) {
		    if ((pressing[_1]) && levelUpChoices.length > 0) chooseReward(0);
		    if ((pressing[_2]) && levelUpChoices.length > 1) chooseReward(1);
		    if ((pressing[_3]) && levelUpChoices.length > 2) chooseReward(2);
		}
		
		
		if (levelUpWaiting) return; //freezes game when level up is available
		
		currentTime = System.currentTimeMillis(); //internal game timer
		currentTimeSeconds = (int) (currentTime / 1000) -  startTime ;
		
		if (player!= null) { //core loop if player is not dead
			
	        Random random = new Random();
	        double r = random.nextDouble(); // Generates a random number between 0.0 and 1.0, this is used to adjust enemy type spawn rates
	        int result;
	        
	        SoundPlayer.playMusic("game music.wav");

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
			int spawnDistance = 2000;
			int maxOffset = 800;
			int randomDistance = (int)(((spawnDistance) + Math.random() * (maxOffset) + 1)); //random distance using offsets
			int randomSign = Math.random() < 0.5 ? -1 : 1; // gives either random -1 or 1, used for -y or -x

			//enemy spawns
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				if (enemyCount <= 20 && framesSinceLastSpawn > 90) {

				    int spawnX = player.x + randomDistance * randomSign; //random x with offsets
				    int spawnY = player.y + randomDistance * randomSign; //random y with offsets
				    
				    switch(result) {
				    
				    case 1:  enemiesToAdd.add(new Skeleton("skeleton", spawnX, spawnY, 100 * (int)Math.pow(1.3, roundNumber), 10, 100, 100));
				    enemyCount++;
				    enemiesSpawned++;
				    break;
				    
				    case 2:  enemiesToAdd.add(new Skeleton("skeleton_red_eye", spawnX, spawnY, 200 * (int)Math.pow(1.3, roundNumber), 15, 200, 200));
				    enemyCount++;
				    enemiesSpawned++;
				    break;
				    
				    case 3:  enemiesToAdd.add(new Skeleton("skeleton_red", spawnX, spawnY, 300 * (int)Math.pow(1.3, roundNumber), 35, 300, 300));
				    enemyCount++;
				    enemiesSpawned++;
				    break;
				    }
				    
				    framesSinceLastSpawn = 0; //reset spawn frame counter
				   
				}
			
				
			
			enemies.addAll(enemiesToAdd);
			enemiesToAdd.clear();
			framesSinceLastSpawn++;
			}
			
			if (enemiesKilledThisRound > 20) {
				enemiesKilledThisRound = 0;
				roundNumber++;
			}

		
			player.moving = false;
	
		//change directions, including weapon direction & play sound
			if(pressing[LT]) {   
				player.go_LT(6);
				for (WeaponSprite weapon : weapons) {
					if(weapon.weaponName == "arrow") {  //lock bow's direction
						bow.locked_pose = WeaponSprite.LT;
					}
					
					if(weapon.weaponName == "axe") { //lock axe's direction
						axe.locked_pose = WeaponSprite.LT;
					}
						
						else	{
					weapon.current_pose = WeaponSprite.LT; //regular weapon pose, changeable during animation
					}
				}
			}
			if(pressing[RT])   {
				player.go_RT(6);
				for (WeaponSprite weapon : weapons) {
					
					if(weapon.weaponName == "arrow") { 
						bow.locked_pose = WeaponSprite.RT;
					}
					
					if(weapon.weaponName == "axe") {
						axe.locked_pose = WeaponSprite.RT;
					}
						
						else	{
					weapon.current_pose = WeaponSprite.RT;
						}
				}
			}
			if(pressing[UP])   {
				player.go_UP(6);
				for (WeaponSprite weapon : weapons) {
					if(weapon.weaponName == "arrow") {
						bow.locked_pose = WeaponSprite.UP;
					}
					
					if(weapon.weaponName == "axe") {
						axe.locked_pose = WeaponSprite.UP;
					}
						
						else	{
					weapon.current_pose = WeaponSprite.UP;	
						}
				}
			}
			if(pressing[DN])   {
				player.go_DN(6);
				for (WeaponSprite weapon : weapons) {
					if(weapon.weaponName == "arrow") {
						bow.locked_pose = WeaponSprite.DN;
					}
					
					if(weapon.weaponName == "axe") {
						axe.locked_pose = WeaponSprite.DN;
					}
						
						else {
					weapon.current_pose = WeaponSprite.DN;	
						}
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
				
				if(enemy.health <= 0) { //enemy kill counts
					enemyCount--;
					enemiesKilledThisRound++;
					enemiesKilledTotal++;
					exp += enemy.enemyExp;
					iter.remove();

					
				}
				
				if (enemy.overlaps(player) && currentTime - player.timeSinceLastHit > 1000) { //calculate damage
					player.health -= enemy.strength;
					player.timeSinceLastHit = currentTime;
					SoundPlayer.playSound("hit_sound.wav");
					
					
					
					//flash character red
					
					player.flashRed = true;
					player.flashEndTime = currentTime + 200;
				}
					
				if(player.flashRed && currentTime > player.flashEndTime) { //stop red flash
						player.flashRed = false;
						
				}
				
				//wall collision
				
				for (Rect wall : walls) {
					if (player.overlaps(wall)){ //use player speeds to push back
						if (player.cameFromAbove()){
							player.pushBy(0, 6);
						}
						
						if (player.cameFromBelow()){
							player.pushBy(0, -6);
						}
						
						if (player.cameFromLeft()){
							player.pushBy(-6, 0);
						}
						
						if (player.cameFromRight()){
							player.pushBy(6, 0);
						}
					}
				}
				
				
				//enemy collision
				
				collisionFrameCounter++; //increments frame counter for collision detection
				
				if (collisionFrameCounter == 30) {
					collisionFrameCounter = 0; //reset frame counter for collisions
					for (int i = 0; i < enemies.size(); i++) { //get first enemy
					    Enemy e1 = enemies.get(i);
	
					    for (int j = i + 1; j < enemies.size(); j++) { //compare with second enemy
					        Enemy e2 = enemies.get(j);
	
					        if (e1.overlaps(e2)) {
	
					        	if (e1.cameFromAbove()){
									e1.pushBy(0, 8);
									e2.pushBy(0, -8);
								}
								
								if (e1.cameFromBelow()){
									e1.pushBy(0, -8);
									e2.pushBy(0, 8);
								}
								
								if (e1.cameFromLeft()){
									e1.pushBy(-8, 0);
									e2.pushBy(8, 0);
								}
								
								if (e1.cameFromRight()){
									e1.pushBy(8, 0);
									e2.pushBy(-8, 0);
								}
					            
					        }
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
						
						SoundPlayer.playSound("hit_sound.wav");
						
						enemy.flashRed = true;
						enemy.flashEndTime = currentTime + 200;
					}
					

					
				}
				
				//update bow's coordinates
				
				if (bowAnimating && !bow.midAir) {
					
					//update bow's position timer
					if (currentTime >= bowPositionTimer) bowPositionTimer = currentTime + bowAnimationEndTime;
					
					
				    // Update bow position and hitbox based on direction
				   
				    bow.fireTime = currentTime;
				    bow.midAir = true;
				    framesSinceFired = 0;
				    bow.current_pose = player.current_pose;
					
				    switch (bow.locked_pose) {
				        case WeaponSprite.RT:
					            bow.x = player.x + player.w;
					            bow.y = player.y + player.h / 2 - bow.h / 2;
					            bow.vx = 6;
					            bow.vy = 0;
				            break;
				        case WeaponSprite.LT:
					        	bow.x = player.x - 200;
					        	bow.y = player.y + player.h / 2 - bow.h / 2;
					        	bow.vx = -6;
					            bow.vy = 0;
				            break;
				        case WeaponSprite.UP:
					        	bow.x = player.x + player.w / 2 - bow.w / 2;
					        	bow.y = player.y - 200;
					        	bow.vx = 0;
					            bow.vy = -6;
				            break;
				        case WeaponSprite.DN:
				        	bow.x = player.x + player.w / 2 - bow.w / 2;
				        	bow.y = player.y + player.h;
				        	bow.vx = 0;
				            bow.vy = 6;
				            break;
				           
				    }
				    

					
				}
				
			    if (bow.midAir) {
				    if (currentTime - bow.fireTime > bow.flightDuration) { //stops arrow when timer expires
				        bow.midAir = false;
				    }
				    
				    
				    else {
				    	
				    	//if (framesSinceFired >= 1){
				        // Update arrow position
					        bow.x += bow.vx;
					        bow.y += bow.vy;
					  
				    	//}
				        
				        framesSinceFired++;
				        
				        
				        
				        
					if (bow.overlaps(enemy) || bow.contains(enemy.x, enemy.y)){	
						enemy.health -= bow.Strength;
						
						SoundPlayer.playSound("hit_sound.wav");
						
						enemy.flashRed = true;
						enemy.flashEndTime = currentTime + 200;
					}
				
			    }
			}
			    
			if (currentTime >= bowAnimationEndTime) {  //set animating false
				bowAnimating = false;

			}
			
			
			//Axe logic
			
			if (axeAnimating && !axe.midAir) {
				
				//update bow's position timer
				if (currentTime >= axePositionTimer) axePositionTimer = currentTime + axeAnimationEndTime;
				
				
			    // Update axe position and hitbox based on direction
			   
			    axe.throwTime = currentTime;
			    axe.midAir = true;
			    framesSinceThrown = 0;
			    axe.current_pose = player.current_pose;
				
			    switch (axe.locked_pose) {
			        case WeaponSprite.RT:
				            axe.x = player.x + player.w;
				            axe.y = player.y + player.h / 2 - axe.h / 2;
				            axe.vx = 1;
			            break;
			        case WeaponSprite.LT:
				        	axe.x = player.x - 200;
				        	axe.y = player.y + player.h / 2 - axe.h / 2;
				        	axe.vx = -1;
			            break;
			        case WeaponSprite.UP:
				        	axe.x = player.x + player.w / 2 - axe.w / 2;
				        	axe.y = player.y - 200;
				        	axe.vx = -1;
			            break;
				        case WeaponSprite.DN:
				        	axe.x = player.x + player.w / 2 - axe.w / 2;
				        	axe.y = player.y + player.h;
				        	axe.vx = -1;
				            break;
			           
			    	}
				    

					
				}
				
			    if (axe.midAir) {
				    if (currentTime - axe.throwTime > axe.flightDuration) { //stops arrow when timer expires
				        axe.midAir = false;
				    }
				    
				    
				    else {
				    	
				    	if (framesSinceThrown >= 10){
				        // Update axe position, increment vy to positive to create an arch
					        axe.x += axe.vx; 
					        axe.y += (axe.vy--);
					  
				    	}
				        
				        framesSinceThrown++;
				        
				        
				        
				        
					if (axe.overlaps(enemy) || axe.contains(enemy.x, enemy.y)){	
						enemy.health -= axe.Strength;
						
						SoundPlayer.playSound("hit_sound.wav");
						
						enemy.flashRed = true;
						enemy.flashEndTime = currentTime + 200;
					}
				
			    }
			}
			    
				if (currentTime >= axeAnimationEndTime) {  //set animating false
					axeAnimating = false;
					axe.vy = 10;

				}
		   
				
				
		}
			
			
			
			
			
			//level check
			if (exp >= scaledLevelUpExpNeeded && (!levelUpWaiting)) {
				levelUp();
				
				
				
			}
			
			
			if (player != null && player.health <= 0) {
				player = null;
				gameOver  = true;
			}
			
		} //end of player death check
	}
		
	
	
	public void levelUp() {
		levelUpWaiting = true;
		currentLevel++;
		exp = 0;
		
	    

	    
	   
	}
	
	
	public void chooseReward(int index) {
		
		if (index >= 0 && index < displayedChoices.length) {
			displayedChoices[index].chosenReward.run();
			levelUpWaiting = false;
			rewardsRandomized = 0;
			
			//scale exp requirement
			scaledLevelUpExpNeeded = (int)(baseLevelUpExpNeeded * Math.pow(1.3, currentLevel));

		}
	}
	


	public void paint (Graphics pen) {
		
		//draw tilemap
		
		for (int i = 0; i <map.length; i++) {
			for (int j = 0; j < map[i].length(); j++) {
				char c = map[i].charAt(j);
				
				
				if (c == 'A') pen.drawImage(tile[0], j * size - camX, i * size - camY, size, size, null);
				if (c == 'W') {
					pen.drawImage(tile[1], j * size - camX, i * size - camY, size, size, null); //these are walls
					if (!wallsBuilt) walls.add(new Rect(j * size, i * size, size, size)); //don't use camX, camY. Need world coordinates.
				}

			}
		}
		
		wallsBuilt = true; //no more walls objects built after loop finishes
		
		
		if (player!= null) {  //no drawing if player is dead
			
			//draw level up menu
			if (levelUpWaiting) {
				int[] chosenNumbers = {-1, -1, -1}; // Use -1 to signify unused
			    if (rewardsRandomized <= 3) {
			        for (int i = 0; i < displayedChoices.length; i++) {
			            int randomRewardNumber;

			            // Repeat until we find a unique and valid number
			            while (true) {
			                randomRewardNumber = (int)(Math.random() * levelUpChoices.length);
			                boolean alreadyChosen = false;

			                // Reject if it's a duplicate
			                for (int j = 0; j < i; j++) {
			                    if (chosenNumbers[j] == randomRewardNumber) {
			                        alreadyChosen = true;
			                        break;
			                    }
			                }

			                // Reject if it's an already unlocked unique weapon reward
			                if ((randomRewardNumber == 1 && bowActive) || (randomRewardNumber == 2 && axeActive)) {
			                    alreadyChosen = true;
			                }

			                if (!alreadyChosen) {
			                    break; // Found a valid, unique reward
			                }
			            }

			            chosenNumbers[i] = randomRewardNumber;
			            displayedChoices[i] = levelUpChoices[randomRewardNumber];
			            rewardsRandomized++;
			        }
			    }
			    	
			    pen.setColor(new Color(0, 0, 0, 200)); // semi-transparent overlay
			    pen.fillRect(0, 0, screenWidth, screenHeight);

			    pen.setColor(Color.WHITE);
			    pen.setFont(new Font("Arial", Font.BOLD, 40));
			    pen.drawString("Level Up!", screenWidth / 2 - 250, 200);
			    
			    for (int i = 0; i < displayedChoices.length; i++) {
			        Reward r = displayedChoices[i];
			        pen.drawString((i + 1) + ". " + r.name, screenWidth / 2 - 200, 300 + i * 100);
			    }
			}
			
			//draw characters
			player.draw(pen, camX, camY);
			
			
			//draw enemies
			
			synchronized(enemies) { //must synchronize with enemy creation to not create a concurrentModification error
				for(Enemy enemy : enemies) {
					if (enemy != null) enemy.draw(pen, camX, camY);
					
					

					
					
					
					//draw enemy health
					
					int barWidth = 40;
					int barHeight = 6;
					int healthBarX = enemy.x - camX + (((enemy.w + Sprite.sizeOffset) / 2) ) - (barWidth / 2); //add 25 back, offset from sprite
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
			
			
			//draw xp bar
			
			int expBarWidth = 300;
			int expBarHeight = 60;
			
			//bar background
			pen.setColor(Color.DARK_GRAY);
			pen.fillRect(1300, 100, expBarWidth, expBarHeight);
			
			//red bar set up
			
	        pen.setColor(Color.BLUE);
	        int expCurrentBarWidth = (int)((exp / (double) scaledLevelUpExpNeeded) * expBarWidth);
	        pen.fillRect(1300, 100, expCurrentBarWidth, expBarHeight);
	        
			//draw player health
			
			pen.setColor(Color.RED);
			pen.setFont(new Font("Arial", Font.BOLD, 36));
			pen.drawString("HP: " + player.health, 20, 40);
			
			
			//draw game timer
			pen.drawString(String.valueOf(currentTimeSeconds), 1300, 40); 
			
			// draw round number
			
			pen.drawString("Round " + roundNumber, 2500, 40);
			
			//draw player level
			pen.drawString("lv " + String.valueOf(currentLevel), 20, 80);
			
			//draw total kills
			pen.drawString("Enemies defeated: " + String.valueOf(enemiesKilledTotal), 20, 120);
			

			
			//flash red drawer
			if (player.flashRed) {
		
				int index = (int)(Math.random() * 3); // gives 0, 1, or 2
				pen.drawImage(Toolkit.getDefaultToolkit().getImage("hit_" + String.valueOf(index) + ".png"), player.x - 20 - camX, player.y -camY, 100, 100, null); //draws hit indicator on player
				
			}
			
			synchronized(enemies) {
				for (Enemy enemy : enemies) {
					if (enemy.flashRed) {
						int index = (int)(Math.random() * 3); // gives 0, 1, or 2
						pen.drawImage(Toolkit.getDefaultToolkit().getImage("hit_" + String.valueOf(index) + ".png"), enemy.x - 20 - camX, enemy.y -camY, 100, 100, null); //draws hit indicator on player
					}
					
					if(enemy.flashRed && currentTime > enemy.flashEndTime) { //stop red flash
						enemy.flashRed = false;
						
				}
						
					
				}
			}
			
			
			//weapon drawing logic
			
			
			//sword
			if(swordActive) { //check if sword is active
				
				if (currentTime >= swordActivationTimer + Sword.weaponDelay) {		
					SoundPlayer.playSound("sword_sfx.wav"); // play sound
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
			
		if(bowActive) { //check if bow is active
				
				if (currentTime >= bowActivationTimer + Bow.weaponDelay) {	
					SoundPlayer.playSound("shoot.wav");
					bow.animation.reset();
					bowAnimating = true;
					bowAnimationEndTime = currentTime + bow.animation.numFrames * bow.animation.duration;
					bowActivationTimer = currentTime;
				}
				
				if (bowAnimating || bow.midAir) {
					bow.draw(pen, camX, camY);
					

				}
				
				
			} //end of bow logic
		
		if(axeActive) { //check if axe is active
			
			if (currentTime >= axeActivationTimer + Axe.weaponDelay) {	
				SoundPlayer.playSound("sword2.wav");
				axe.animation.reset();
				axeAnimating = true;
				axeAnimationEndTime = currentTime + axe.animation.numFrames * axe.animation.duration;
				axeActivationTimer = currentTime;
			}
			
			if (axeAnimating || axe.midAir) {
				axe.draw(pen, camX, camY);
				
				
				

			}
			
			
		} //end of axe logic
			
		
		
	} //end of player death check
		
	if (gameOver) {
		
	    pen.setColor(new Color(0, 0, 0, 200)); // semi-transparent overlay
	    pen.fillRect(0, 0, screenWidth, screenHeight);

	    pen.setColor(Color.WHITE);
	    pen.setFont(new Font("Arial", Font.BOLD, 100));
	    pen.drawString("GAME OVER!", screenWidth / 2 - 250, 200);
	    pen.drawString("you died on round " + String.valueOf(roundNumber) , screenWidth / 2 - 250, 400);
	    pen.drawString("and took down " + String.valueOf(enemiesKilledTotal), screenWidth / 2 - 250, 600);
	    pen.drawString("enemies with you" , screenWidth / 2 - 250, 800);

		}
		
		
	

		

		
		
		

		
		
	}
	public void initialize() {
		startTime = (int) (System.currentTimeMillis() / 1000); //start time in seconds
		

		
		//fetch tilemap data
		for (int i = 0; i < tile.length; i++) {
			tile[i] = Toolkit.getDefaultToolkit().getImage("tile" + (i+1) + ".png");
		}
		
		sword = new Sword(player.x, player.y, 9, false);  //start with sword as default
		weapons.add(sword);
		
		bow = new Bow(player.x, player.y, 4, true);
		weapons.add(bow);
		
		axe = new Axe(player.x, player.y, 4, true);
		weapons.add(axe);
		
		//load up reward list
		
		 levelUpChoices[0] = (new Reward("Heal 100 HP", () -> player.health += 100));
		 levelUpChoices[1] = (new Reward("Activate Bow", () -> bowActive = true));
		 levelUpChoices[2] = (new Reward("Activate Axe", () -> axeActive = true));
		 levelUpChoices[3] = (new Reward("Increase Sword damage", () -> sword.Strength *= 1.10));
		 levelUpChoices[4] = (new Reward("Decrease Sword delay", () -> sword.weaponDelay *= 0.90));
		 levelUpChoices[5] = (new Reward("Increase Bow strength", () -> bow.Strength *= 1.10));
		 levelUpChoices[6] = (new Reward("Increase Axe strength", () -> axe.Strength *= 1.10));
	
		
	}



}
