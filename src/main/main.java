package main;

import javax.swing.*;

public class main {
	
	public static JFrame window;
	
    public static void main(String[] args) {
        
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // lets the window properly close when user clicks the cross button
        window.setResizable(false);
        window.setTitle("Adventure Game");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        
        gamePanel.config.loadConfig();
        if(gamePanel.fullScreenOn == true) {
            window.setUndecorated(true);
        }
        
        window.pack();
        
        window.setLocationRelativeTo(null);  // To center the window
        window.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread();  // Start the game loop
        
        gamePanel.requestFocusInWindow();  // Ensure the panel gets keyboard focus
    }
}
