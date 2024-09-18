package entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	int hasKey = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
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
	}
	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream ("/player/boy_right_2.png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
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
		
		
	}
	
	public void pickUpObject(int i) {
		
		if (i != 999) {
			String ObjectName = gp.obj[i].name;
			
			switch (ObjectName) {
			
			case"Key":
				gp.playSE(1);
				hasKey++;
				gp.obj[i] = null;
				break;
			case "Door":
				gp.playSE(3);
				if(hasKey > 0) {
					gp.obj[i] = null;
					hasKey--;
				}
				break;
			case "Boots":
				gp.playSE(2);
				speed += 2;
				gp.obj[i] = null;
				break;
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
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}
}

















