package main;

import javax.swing.*;

public class main {

    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Project R");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        
        window.setLocationRelativeTo(null);  // To center the window
        window.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread();  // Start the game loop
        
        gamePanel.requestFocusInWindow();  // Ensure the panel gets keyboard focus
    }
}
