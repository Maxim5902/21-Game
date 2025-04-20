package blackjack;

/**
 * Represents a player in a Blackjack game, tracking their hand
 * and game state (standing/busted).
 */
public class Player {
    private String name;      // Player's name
    private Hand hand;        // Player's current hand of cards
    private boolean isStanding; // Whether player has chosen to stand
    private boolean isBusted;   // Whether player has busted (exceeded 21)

    /**
     * Creates a new player with specified name.
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();  // Initialize empty hand
        this.isStanding = false; // Player starts not standing
        this.isBusted = false;   // Player starts not busted
    }

    /**
     * Adds a card to the player's hand (hit action).
     * Checks for bust condition if hand value exceeds 21.
     * @param card The card to add to hand
     */
    public void hit(Card card) {
        // Only allow hit if player hasn't stood or busted
        if (!isStanding && !isBusted) {
            hand.addCard(card); // Add card to hand
            
            // Check if hand value exceeds 21 (bust)
            if (hand.calculateValue() > 21) {
                isBusted = true;
            }
        }
    }

    /**
     * Player chooses to stand (end their turn).
     */
    public void stand() {
        isStanding = true;
    }

    // Accessor methods with brief documentation
    
    /** @return Whether player has chosen to stand */
    public boolean isStanding() { return isStanding; }
    
    /** @return Whether player has busted (hand value > 21) */
    public boolean isBusted() { return isBusted; }
    
    /** @return The player's current hand */
    public Hand getHand() { return hand; }
    
    /** @return The player's name */
    public String getName() { return name; }

    /**
     * Resets the player for a new round:
     * - Creates new empty hand
     * - Resets standing/busted status
     */
    public void reset() {
        hand = new Hand();    // New empty hand
        isStanding = false;   // Reset standing status
        isBusted = false;     // Reset busted status
    }
}