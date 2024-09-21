package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//screen settings
	final int originalTileSize= 16; // 16*16 tile
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; //48*48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	//world settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;

	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	Thread gameThread;
	
//Entity and Object	
	public Player player= new Player(this, keyH);
	public Entity obj[] = new Entity[10];
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[20];
	ArrayList<Entity> entityList = new ArrayList<>();
	 
//Game State
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState= 3;
	
	public GamePanel () {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	
	public void setupGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		
		playMusic(0);
		stopMusic();
		gameState = titleState;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();	
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while (gameThread != null) {
						
			//update info such as character positions
			update();
			
			repaint();
			// draw the screen with the updated info
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;
				
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long)remainingTime);
				
				nextDrawTime += drawInterval;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
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
					monster[i].update();
				}
			}
		}
		
		if (gameState == pauseState) {
			//nothing
		}
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//title screen
		if(gameState == titleState) {
			
			ui.draw(g2);
		}
		else {		
			
			//tile
			tileM.draw(g2);
			
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
			for (int i = 0; i< entityList.size(); i++) {
				entityList.remove(i);
			}
			//UI
			ui.draw(g2);
			
			g2.dispose();
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









