package blackjack;

import java.util.ArrayList;

/**
 * Represents a hand of cards held by a player or dealer in Blackjack,
 * with methods to calculate hand value and manage card visibility.
 */
public class Hand {
    private ArrayList<Card> cards; // List of cards in the hand

    /**
     * Constructs an empty hand.
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand.
     * @param card The card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Calculates the optimal Blackjack value of the hand:
     * - Sums values of all face-up cards
     * - Adjusts for aces (counts as 11 unless it would cause bust, then 1)
     * @return The best possible hand value without busting
     */
    public int calculateValue() {
        int value = 0;      // Running total of hand value
        int aces = 0;       // Count of aces in hand

        // Sum values of all face-up cards
        for (Card card : cards) {
            if (card.isFaceUp()) {
                value += card.getValue();
                if (card.getRank().equals("ace")) {
                    aces++; // Track aces for potential value adjustment
                }
            }
        }

        // Adjust for aces if hand would otherwise bust
        while (value > 21 && aces > 0) {
            value -= 10; // Change ace value from 11 to 1
            aces--;
        }

        return value;
    }

    /**
     * Gets all cards in the hand.
     * @return ArrayList of Card objects
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Reveals all cards in the hand by flipping them face-up.
     * Used when dealer reveals their hand at end of round.
     */
    public void revealAllCards() {
        for (Card card : cards) {
            if (!card.isFaceUp()) {
                card.flip(); // Flip any face-down cards
            }
        }
    }
}