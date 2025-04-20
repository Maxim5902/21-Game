package blackjack;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

/**
 * Custom JPanel that displays a player's or dealer's hand of cards.
 * Handles card display, layout, and visual updates.
 */
public class CardPanel extends JPanel {
    // Constants for card display dimensions
    private static final int CARD_WIDTH = 80;     // Width of each card in pixels
    private static final int CARD_HEIGHT = 120;   // Height of each card in pixels
    private static final int CARD_OVERLAP = -10;  // Negative overlap for card stacking effect
    
    private Player player;       // The player whose cards are displayed
    private boolean isDealer;    // Whether this panel shows dealer's cards
    private boolean revealAll;   // Whether to show all cards (face-up)
    private JLabel totalLabel;   // Displays the hand's total value
    private JPanel cardsPanel;   // Panel containing the card images

    /**
     * Constructs a CardPanel for a specific player.
     * @param player The player whose cards to display (can be Player or Dealer)
     */
    public CardPanel(Player player) {
        this.player = player;
        this.isDealer = player instanceof Dealer; // Determine if player is actually the dealer
        this.revealAll = false;  // Start with cards hidden if dealer
        
        // Configure main panel properties
        setLayout(new BorderLayout()); // Use BorderLayout for label above cards
        setOpaque(false); // Transparent background
        setPreferredSize(new Dimension(300, CARD_HEIGHT + 50)); // Fixed size with room for total
        
        // Initialize and configure the total display label
        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bold font for visibility
        totalLabel.setForeground(Color.BLACK); // Black text for contrast
        totalLabel.setHorizontalAlignment(JLabel.CENTER); // Center the total
        totalLabel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding below label
        
        // Create panel for card images with overlapping layout
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -CARD_OVERLAP, 0));
        cardsPanel.setOpaque(false); // Transparent background
        cardsPanel.setPreferredSize(new Dimension(300, CARD_HEIGHT)); // Fixed size
        
        // Add components to main panel
        add(totalLabel, BorderLayout.NORTH); // Total at top
        add(cardsPanel, BorderLayout.CENTER); // Cards below
        
        updateCardDisplay(); // Initial display of cards
    }

    /**
     * Resets the panel for a new round (hides cards if dealer)
     */
    public void reset() {
        this.revealAll = false; // Hide dealer's cards again
        updateCardDisplay(); // Refresh display
    }

    /**
     * Reveals all cards in the hand (used when dealer shows cards)
     */
    public void revealAllCards() {
        this.revealAll = true; // Show all cards
        updateCardDisplay(); // Refresh display
    }

    /**
     * Public method to trigger display update
     */
    public void update(){
        updateCardDisplay(); // Refresh display
    }

    /**
     * Updates the visual display of cards and total value
     */
    private void updateCardDisplay() {
        cardsPanel.removeAll(); // Clear current card display
        
        Hand hand = player.getHand();
        // Display each card in the hand
        for (int i = 0; i < hand.getCards().size(); i++) {
            Card card = hand.getCards().get(i);
            // Determine if card should be face-up:
            // - Always show if revealAll is true
            // - Show if card is face-up
            // - Show if not dealer
            // - Show dealer's first card only
            boolean showCard = revealAll || card.isFaceUp() || !isDealer || (isDealer && i == 0);
            
            try {
                // Create and add card image label
                ImageIcon cardIcon = new ImageIcon(getCardImage(card, showCard));
                JLabel cardLabel = new JLabel(cardIcon);
                cardLabel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
                cardsPanel.add(cardLabel); // Add to display
            } catch (IOException e) {
                // Fallback if card image fails to load
                JLabel errorLabel = createErrorLabel(card);
                cardsPanel.add(errorLabel);
            }
        }
        
        // Update total value display
        int value = hand.calculateValue();
        if (revealAll || !isDealer) {
            // Show actual total for players or when revealed
            totalLabel.setText("Total: " + value);
            // Show total in red if player busted
            totalLabel.setForeground(player.isBusted() ? Color.RED: Color.BLACK);
        } else {
            // Show "?" for dealer's hidden total
            totalLabel.setText(isDealer ? "Total: ?" : "Total: " + value);
        }
        
        // Refresh the display
        revalidate();
        repaint();
    }

    /**
     * Gets the appropriate card image (face or back)
     * @param card The card to display
     * @param showCard Whether to show card face (true) or back (false)
     * @return The scaled card image
     * @throws IOException If image file cannot be read
     */
    private Image getCardImage(Card card, boolean showCard) throws IOException {
        BufferedImage image;
        if (showCard) {
            // Load card face image from resources
            String imageName = card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
            URL imageUrl = getClass().getResource("cards/" + imageName);
            image = imageUrl != null ? 
                ImageIO.read(imageUrl) : // Load from file
                createFallbackCardImage(card); // Fallback if image missing
        } else {
            image = createCardBackImage(); 
        }
        // Scale image to standard size
        return image.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
    }

    /**
     * Creates an error label when card image fails to load
     * @param card The card that failed to load
     * @return JLabel displaying error information
     */
    private JLabel createErrorLabel(Card card) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        label.setOpaque(true); // Make background visible
        label.setBackground(Color.RED); // Error background
        label.setForeground(Color.WHITE); // White text
        // Display error message with card info
        label.setText("<html><center>Error<br>" + card.toString() + "</center></html>");
        return label;
    }

    /**
     * Creates a fallback card image when image file is missing
     * @param card The card to represent
     * @return Generated card image
     */
    private BufferedImage createFallbackCardImage(Card card) {
        BufferedImage image = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw white card with black border
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, CARD_WIDTH-1, CARD_HEIGHT-1);
        
        // Draw card rank and suit
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(card.getRank(), 10, 20); // Rank at top
        g2d.drawString(card.getSuit(), 10, 40); // Suit below
        
        g2d.dispose(); // Clean up graphics
        return image;
    }

    /**
     * Creates a fallback card back image
     * @return Generated card back image
     */
    private BufferedImage createCardBackImage() {
        BufferedImage image = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Dark blue background with red border
        g2d.setColor(new Color(0, 0, 139)); // Dark blue
        g2d.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3)); // Thick border
        g2d.drawRect(5, 5, CARD_WIDTH-10, CARD_HEIGHT-10);
        
        g2d.dispose(); // Clean up graphics
        return image;
    }
}