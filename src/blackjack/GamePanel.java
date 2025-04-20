package blackjack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel containing all UI components for the Blackjack game.
 * Handles user interaction and visual updates.
 */
public class GamePanel extends JPanel {
    private Game game;                  // Game logic controller
    private JButton hitButton;          // Hit action button
    private JButton standButton;        // Stand action button
    private JButton newGameButton;      // New game button
    private JTextPane gameInfo;         // Game status display
    private List<CardPanel> playerPanels; // Card panels for players
    private CardPanel dealerPanel;      // Card panel for dealer

    /**
     * Creates a new game panel with specified players.
     * @param playerNames List of player names
     */
    public GamePanel(List<String> playerNames) {
        game = new Game(playerNames); // Initialize game logic
        setupUI(); // Create UI components
        updateGameDisplay(); // Initial display update
    }

    /**
     * Initializes and arranges all UI components.
     */
    private void setupUI() {
        setLayout(new BorderLayout()); // Main layout
        
        // Player cards area setup
        int playerCount = game.getPlayers().size();
        // Use GridLayout to stack player panels vertically
        JPanel playersPanel = new JPanel(new GridLayout(playerCount, 1, 5, 10));
        // Calculate height based on number of players (180px each)
        int playerAreaHeight = playerCount * 180;
        playersPanel.setPreferredSize(new Dimension(300, playerAreaHeight));
        
        playerPanels = new ArrayList<>();
        
        // Create card panel for each player
        for (Player player : game.getPlayers()) {
            // Container for player name and cards
            JPanel playerContainer = new JPanel(new BorderLayout());
            playerContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            playerContainer.setBackground(new Color(240, 240, 240)); // Light gray
            
            // Player name label
            JLabel playerLabel = new JLabel(player.getName() + ":");
            playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
            
            // Card display panel
            CardPanel cardPanel = new CardPanel(player);
            cardPanel.setPreferredSize(new Dimension(300, 150)); // Fixed height
            playerPanels.add(cardPanel);
            
            // Add components to player container
            playerContainer.add(playerLabel, BorderLayout.NORTH); // Name at top
            playerContainer.add(cardPanel, BorderLayout.CENTER); // Cards below
            playersPanel.add(playerContainer); // Add to main player area
        }
        
        // Dealer cards area setup
        JPanel dealerContainer = new JPanel(new BorderLayout());
        dealerContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        dealerContainer.setBackground(new Color(240, 240, 240)); // Light gray
        
        JLabel dealerLabel = new JLabel("Dealer:");
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dealerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        
        dealerPanel = new CardPanel(game.getDealer());
        dealerPanel.setPreferredSize(new Dimension(300, 160)); // Slightly taller
        
        dealerContainer.add(dealerLabel, BorderLayout.NORTH); // Label at top
        dealerContainer.add(dealerPanel, BorderLayout.CENTER); // Cards below
        
        // Combine all card areas
        JPanel cardsPanel = new JPanel(new BorderLayout());
        cardsPanel.add(playersPanel, BorderLayout.CENTER); // Players in center
        cardsPanel.add(dealerContainer, BorderLayout.SOUTH); // Dealer at bottom
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        
        // Game control buttons setup
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(new Color(220, 220, 220)); // Light gray
        
        hitButton = new JButton("Hit (H)");
        standButton = new JButton("Stand (S)");
        newGameButton = new JButton("New Game (N)");
        
        // Style buttons with colors
        styleButton(hitButton, new Color(50, 150, 50)); // Green
        styleButton(standButton, new Color(200, 120, 0)); // Orange
        styleButton(newGameButton, new Color(0, 120, 200)); // Blue
        
        // Set button actions
        hitButton.addActionListener(e -> {
            game.playerHit(); // Player hits
            updateGameDisplay(); // Update UI
        });
        
        standButton.addActionListener(e -> {
            game.playerStand(); // Player stands
            updateGameDisplay(); // Update UI
        });
        
        newGameButton.addActionListener(e -> {
            game.reset(); // Reset game state
            // Reset all card panels
            for (CardPanel panel : playerPanels) {
                panel.reset();
            }
            dealerPanel.reset();
            updateGameDisplay(); // Update UI
        });
        
        // Add buttons to control panel
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        controlPanel.add(newGameButton);
        
        // Game info display setup
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        infoPanel.setBackground(new Color(220, 220, 220)); // Light gray
        
        gameInfo = new JTextPane();
        gameInfo.setEditable(false); // Read-only
        gameInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        gameInfo.setBackground(new Color(240, 240, 240)); // Off-white
        gameInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1), // Thin border
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));
        
        // Make info display scrollable
        JScrollPane infoScroll = new JScrollPane(gameInfo);
        infoScroll.setBorder(BorderFactory.createEmptyBorder());
        infoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoPanel.add(infoScroll, BorderLayout.CENTER);
        
        // Add all components to main panel
        add(controlPanel, BorderLayout.NORTH); // Controls at top
        add(cardsPanel, BorderLayout.CENTER); // Cards in middle
        add(infoPanel, BorderLayout.SOUTH); // Info at bottom
        
        // Set up keyboard shortcuts
        setupKeyBindings();
    }

    /**
     * Styles a button with consistent appearance.
     * @param button The button to style
     * @param bgColor Background color
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK); // Black text
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(), // 3D effect
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding
        ));
    }

    /**
     * Sets up keyboard shortcuts for game actions.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // Map keys to action names
        inputMap.put(KeyStroke.getKeyStroke("H"), "hit");
        inputMap.put(KeyStroke.getKeyStroke("S"), "stand");
        inputMap.put(KeyStroke.getKeyStroke("N"), "newGame");
        
        // Define actions for each key
        actionMap.put("hit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitButton.doClick(); // Simulate button click
            }
        });
        
        actionMap.put("stand", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                standButton.doClick(); // Simulate button click
            }
        });
        
        actionMap.put("newGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGameButton.doClick(); // Simulate button click
            }
        });
    }

    /**
     * Updates all visual components to reflect current game state.
     */
    private void updateGameDisplay() {
        // Update all player card panels
        for (int i = 0; i < game.getPlayers().size(); i++) {
            playerPanels.get(i).update();
        }
        
        // Reveal dealer cards if game is over
        if (game.getState() == Game.GameState.GAME_OVER) {
            dealerPanel.revealAllCards();
        }
        dealerPanel.update(); // Update dealer display
        
        // Update game info text
        StringBuilder sb = new StringBuilder();
        Player currentPlayer = game.getCurrentPlayer();
        
        // Show current player if applicable
        if (currentPlayer != null) {
            sb.append("Current turn: ").append(currentPlayer.getName()).append("\n");
        }
        
        // Show results if game is over
        if (game.getState() == Game.GameState.GAME_OVER) {
            List<String> results = game.determineWinners();
            for (String result : results) {
                sb.append(result).append("\n"); // Add each result
            }
            
            // Disable action buttons when game is over
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        } else {
            // Enable buttons during active play
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
        }
        
        gameInfo.setText(sb.toString()); // Update display text
        gameInfo.setCaretPosition(0); // Scroll to top
    }
}