package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_Interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable{
	//screen settings
	final int originalTileSize= 16; // 16*16 tile
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; //48*48 tile
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	//world settings
	public final int maxWorldCol = 100;
	public final int maxWorldRow = 100;

	//for full screen
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreenOn = false;
	
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	Config config = new Config(this);
	Thread gameThread;
	
//Entity and Object	
	public Player player= new Player(this, keyH);
	public Entity obj[] = new Entity[20];
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[20];
	public InteractiveTile iTile []= new InteractiveTile[50]; 
	public ArrayList<Entity> particleList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	 
//Game State
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState= 3;
	public final int characterState = 4;
	public final int optionsState = 5;
	public final int gameOverState = 6;
	
	public GamePanel () {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	public void setupGame() {	
		aSetter.setObject ();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		playMusic(0);
		stopMusic();
		gameState = titleState;
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();	
		
		if(fullScreenOn == true) {			
			setFullScreen();
		}
	 }
	public void retry() {
		player.setDefaultPosition();
		player.restoreLife();
		aSetter.setNPC();
		aSetter.setMonster();
	}
	
	public void restart() {
		
		player.setDefaultValues();
		player.setDefaultPosition();
		player.restoreLife();
		player.setItems();
		aSetter.setObject ();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
	}
	
	public void setFullScreen() {
		
		//get local screen device
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(main.window);
		
		//get full screen width and height
		screenWidth2 = main.window.getWidth();
		screenHeight2 = main.window.getHeight();
		
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();	
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer+= (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >=1) {
				update();
				drawToTempScreen(); //draw Everything to buffered image
				drawToScreen(); //draw the buffered image to the screen
				delta--;
				drawCount++;
			}
			if(timer >= 1000000) {
				drawCount = 0;
				timer = 0;
			}
		}
	}
	public void drawToScreen() {
		Graphics g =  getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}
	public void update () {
		if(gameState == playState) {			
			player.update();
			
			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
			for(int i = 0; i< monster.length; i++) {
				if(monster[i] != null) {
					if(monster[i].alive == true && monster[i].dying == false) {
						monster[i].update();
					}
					if(monster[i].alive == false) {		
						monster[i].checkDrop();
						monster[i] = null;
					}
				}
			}
			
			for (int i = 0; i< particleList.size(); i++) {
				if(particleList.get(i) != null) {
					if(particleList.get(i).alive == true) {
						particleList.get(i).update();
					}
					if(particleList.get(i).alive == false) {
						particleList.remove(i);
					}
				}
			}
			for (int i = 0; i < iTile.length; i++) {
				if(iTile[i] != null) {
					iTile[i].update();
				}
			}
		}
		if (gameState == pauseState) {
			//nothing
		}
	}
	
	public void drawToTempScreen () {
		//DEBUG
		long drawStart = 0;
		if(keyH.showDebugText == true) {
			drawStart = System.nanoTime();
		}
		//title screen
		if(gameState == titleState) {
			ui.draw(g2);
		}
		else {		
			//tile
			tileM.draw(g2);
					
			//Interactive Tiles
			for(int i = 0; i < iTile.length; i++) {
				if(iTile[i] != null) {
					iTile[i].draw(g2);
				}
			}
			//ADD ENTITTIES TO THE LIST
			entityList.add(player);
			for(int i = 0; i< npc.length; i++) {
				if(npc[i] != null ) {
					entityList.add(npc[i]);
				}
			}
			for(int i = 0; i < obj.length; i++) {
				if(obj[i] != null) {
					entityList.add(obj[i]);
				}
			}
			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					entityList.add(monster[i]);
				}
			}
			for(int i = 0; i < particleList.size(); i++) {
				if(particleList.get(i) != null) {
					entityList.add(particleList.get(i));
				}
			}
			//SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
							
					int result = Integer.compare(e1.worldX, e2.worldY);
					return 0;
				}
			});
			//DRAW ENTITIES
			for (int i = 0; i< entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
				//EMPTY ENTITY LIST
			entityList.clear();
			//UI
			ui.draw(g2);
			}
				
		if(keyH.showDebugText == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
					
			g2.setFont(new Font("Arial", Font.PLAIN,20)); 
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineHeight = 20;
			g2.drawString("WorldX" + player.worldX,  x, y); y+= lineHeight;
			g2.drawString("WorldY" + player.worldY,  x, y); y+= lineHeight;
			g2.drawString("Col" + (player.worldX + player.solidArea.x)/tileSize,  x, y); y+= lineHeight;
			g2.drawString("Row" + (player.worldY + player.solidArea.y)/tileSize,  x, y); y+= lineHeight;	
			g2.drawString("Draw Time:" + passed,  x, y);	
		}	
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}