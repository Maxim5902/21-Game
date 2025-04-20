package blackjack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the Blackjack (21) game application.
 * Handles game initialization and player setup.
 */
public class Main {
    
    /**
     * Main entry point for the application.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread-safe GUI operations
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the look and feel to match the operating system
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
                // Continue with default look and feel if there's an error
            }
            
            // Create and configure the main application window
            JFrame frame = new JFrame("21 Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window closes
            frame.setSize(800, 800); // Initial window size
            frame.setLocationRelativeTo(null); // Center the window on screen
            
            // Get player names through dialog prompts
            List<String> playerNames = getPlayerNames(frame);
            
            // Exit if user canceled player name input
            if (playerNames == null) {
                System.exit(0);
            }

            // Adjust window height based on number of players
            // Taller window for multiplayer games to accommodate more cards
            frame.setSize(800, playerNames.size() > 1 ? 800 : 600);

            // Create the main game panel with the collected player names
            GamePanel gamePanel = new GamePanel(playerNames);
            frame.add(gamePanel); // Add game panel to the window
            
            frame.setVisible(true); // Make the window visible
        });
    }
    
    /**
     * Prompts the user to enter player information through dialog boxes.
     * @param parent The parent frame for dialog positioning
     * @return List of player names, or null if user canceled
     */
    private static List<String> getPlayerNames(JFrame parent) {
        // Available options for number of players
        String[] options = {"1", "2", "3", "4"};
        
        // Show dialog to select number of players
        String selection = (String) JOptionPane.showInputDialog(
            parent,
            "How many players? (1-4)", // Prompt message
            "Player Count",            // Dialog title
            JOptionPane.QUESTION_MESSAGE, // Message type
            null,                      // No custom icon
            options,                   // Selection options
            options[0]);               // Default selection
        
        // Return null if user canceled the dialog
        if (selection == null) {
            return null;
        }
        
        // Convert selection to integer
        int playerCount = Integer.parseInt(selection);
        List<String> playerNames = new ArrayList<>();
        
        // Collect names for each player
        for (int i = 1; i <= playerCount; i++) {
            // Show input dialog for each player's name
            String name = JOptionPane.showInputDialog(
                parent,
                "Enter name for Player " + i + ":", // Prompt
                "Player " + i + " Name",            // Dialog title
                JOptionPane.PLAIN_MESSAGE);         // Message type
            
            // Return null if user canceled name entry
            if (name == null) {
                return null;
            }
            
            // Set default name if input was empty
            if (name.trim().isEmpty()) {
                name = "Player " + i;
            }
            
            // Add the name to our list
            playerNames.add(name);
        }
        
        return playerNames;
    }
}