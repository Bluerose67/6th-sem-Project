package main;

import java.awt.BasicStroke;
import entity.Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import object.OBJ_Heart;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font arial_40, arial_80B;
	BufferedImage heart_full, heart_half, heart_blank;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public int commandNum = 0;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
//		OBJ_Key key = new OBJ_Key(gp);
//		keyImage = key.image;
		
		//create HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;	
	}

	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		
		//title state
		if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		
		//play state
		if(gp.gameState == gp.playState) {
			
			drawPlayerLife();
		}
		//pause state
		if(gp.gameState == gp.pauseState) {
			drawPauseScreen();
			drawPlayerLife();
			
		}
		// Dialogue state
		if(gp.gameState == gp.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
	}
	
	public void drawPlayerLife() {
//		gp.player.life = 3;
		
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		//DRAW MAX LIFE
		while (i < gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		//RESET
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0; 
		//DRAW CURRENT LIFE
		
		while(i < gp.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			
			if(i< gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gp.tileSize;
		}
	}
	
	public void drawTitleScreen() {
		
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
		String text = "Treasure Hunt Game";
		int x = getXforCenteredText(text) ;
		int y = gp.tileSize*3;
		
		// shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		
		//main color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//Blue Boy Image
		x= gp.screenWidth/2 - (gp.tileSize*2) /2;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		//menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

		text = "New Game";
		x = getXforCenteredText(text) ;
		y += gp.tileSize*3.5;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "Load Game";
		x = getXforCenteredText(text) ;
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "Quit";
		x = getXforCenteredText(text) ;
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		
	}
	
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text = "PAUSED";
		int x = getXforCenteredText(text);	
		int y = gp.screenHeight/2;
		
		g2.drawString(text,  x,  y);	
	}
	
	public void drawDialogueScreen() {
		//window
		int x = gp.tileSize*2;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;
		drawSubWindow(x, y, width, height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		x+= gp.tileSize;
		y+= gp.tileSize;
		
		for (String line : currentDialogue.split("\n")) {			
			g2.drawString(line, x, y);
			y += 40;
		}
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c= new Color(0,0,0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
		
	}
	public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	
}
