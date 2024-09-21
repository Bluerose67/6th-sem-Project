 package entity;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 -  (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		setDefaultValues();
		getPlayerImage();
	}
	public void setDefaultValues() {
		worldX= gp.tileSize * 23;
		worldY= gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		//player status
		maxLife = 6;
		life = maxLife;
	}
	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1");
		up2 = setup("/player/boy_up_2");
		down1 = setup("/player/boy_down_1");
		down2 = setup("/player/boy_down_2");
		left1 = setup("/player/boy_left_1");
		left2 = setup("/player/boy_left_2");
		right1 = setup("/player/boy_right_1");
		right2 = setup("/player/boy_right_2");
	}
	
	
	public void update() {
		
		if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
			
			if(keyH.upPressed == true) {
				direction = "up";				
			} 
			else if (keyH.downPressed ==  true) {
				direction = "down";				
			} 
			else if (keyH.leftPressed == true) {
				direction = "left";				
			} 
			else if (keyH.rightPressed == true) {
				direction = "right";				
			}
			
			//check tile collision
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			// check monster collision
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//CHECK EVENT
			gp.eHandler.checkEvent();
			
			gp.keyH.enterPressed = false;

			
			//if collision is false, player can move 
			if(collisionOn == false) {
				
				switch (direction) {
				
				case"up":
					worldY -= speed;
					break;
				case"down":
					worldY += speed;
					break;
				case"left":
					worldX -= speed;
					break;
				case"right":
					worldX += speed;
					break;
				
				}
			}
			spritCounter ++;
			if(spritCounter > 12) {
				if(spritNum == 1) {
					spritNum = 2;
				} 
				else if (spritNum == 2) {
					spritNum = 1;
				}
				spritCounter = 0;
			}
		}
		
		// This needs to be outside of key if statement
		
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
	}
	
	public void pickUpObject(int i) {
		
		if (i != 999) {
			
		}
	}
	
	public void interactNPC(int i) {
		
		if(i != 999) {
			
			if(gp.keyH.enterPressed == true) {				
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			}
		}
	}
	
	public void contactMonster(int i) {
		
		if(i != 999) {
			if(invincible == false) {
				life -= 1;
				invincible = true;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		BufferedImage image = null;
		
		switch (direction) {
		case "up":
			if(spritNum == 1) {				
				image = up1;
			}
			if(spritNum == 2) {				
				image = up2;
			}
			break;
		case "down":
			if(spritNum == 1) {
				image = down1;
			}
			if(spritNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if(spritNum == 1) {				
				image = left1;
			}
			if(spritNum == 2) {				
				image = left2;
			}
			break;
		case "right":
			if(spritNum == 1) {				
				image = right1;
			}
			if(spritNum == 2) {				
				image = right2;
			}
			break;
			
		}
		
		if(invincible == true) {	
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		
		g2.drawImage(image, screenX, screenY, null);
		
		//RESET ALPHA
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
	}
}


















