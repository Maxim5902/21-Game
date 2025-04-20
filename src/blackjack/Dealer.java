package blackjack;

/**
 * Represents the dealer in a Blackjack game, extending the Player class
 * with dealer-specific behavior for automatic play.
 */
public class Dealer extends Player {
    
    /**
     * Constructs a new Dealer with the name "Dealer".
     */
    public Dealer() {
        super("Dealer"); // Calls Player constructor with "Dealer" name
    }

    /**
     * Implements the dealer's automatic playing strategy:
     * 1. Reveals all cards (shows face-down cards)
     * 2. Hits until hand value is at least 16
     * 3. Stands when hand value is 16 or higher
     * @param deck The deck of cards to draw from
     */
    public void play(Deck deck) {
        // Reveal all cards (including any face-down cards)
        getHand().revealAllCards();
        
        // Dealer hits on 16 or less (stands on 17 or higher)
        while (getHand().calculateValue() < 16) {
            // Draw a card from deck and add to dealer's hand
            hit(deck.dealCard());
        }
        
        // Dealer stands when done hitting
        stand();
    }
}