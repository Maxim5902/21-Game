package blackjack;

/**
 * Represents a single playing card in a Blackjack game with suit, rank,
 * value, and face-up/down state.
 */
public class Card {
    // Immutable card properties
    private final String suit;   // Card suit (hearts, diamonds, etc.)
    private final String rank;   // Card rank (2, 3, king, ace, etc.)
    private final int value;     // Numeric value in Blackjack
    private boolean faceUp;      // Whether card is face-up or face-down

    /**
     * Constructs a new Card with specified properties.
     * @param suit  The card's suit (hearts/diamonds/clubs/spades)
     * @param rank  The card's rank (2-10, jack, queen, king, ace)
     * @param value The card's Blackjack value
     */
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.faceUp = true; // Cards are face-up by default
    }

    // Accessor methods with documentation

    /** @return The card's suit */
    public String getSuit() { return suit; }

    /** @return The card's rank */
    public String getRank() { return rank; }

    /** 
     * @return The card's Blackjack value 
     * (Note: Aces may be counted as 1 or 11 elsewhere)
     */
    public int getValue() { return value; }

    /** @return true if card is face-up, false if face-down */
    public boolean isFaceUp() { return faceUp; }

    /**
     * Toggles the card's face-up/down state.
     * Face-down cards are typically not visible to players.
     */
    public void flip() { faceUp = !faceUp; }

    /**
     * Returns a string representation of the card.
     * @return String in format "[rank] of [suit]" (e.g. "ace of spades")
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}