package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a standard 52-card deck for Blackjack with
 * shuffling and dealing capabilities.
 */
public class Deck {
    private ArrayList<Card> cards; // Stores the cards in the deck

    /**
     * Constructs a new deck, initializes it with 52 cards,
     * and shuffles them.
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck(); // Create all 52 cards
        shuffle();        // Randomize card order
    }

    /**
     * Initializes the deck with 52 standard playing cards:
     * 13 ranks in each of 4 suits with appropriate Blackjack values.
     */
    private void initializeDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", 
                         "jack", "queen", "king", "ace"};
        
        // Create cards for each suit and rank combination
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                // Calculate card value (face cards = 10, ace = 11, others = rank value)
                int value = (i >= 9) ? 10 : i + 2; // 10, J, Q, K all worth 10
                if (i == 12) value = 11; // Ace is worth 11
                
                // Create and add new card to deck
                cards.add(new Card(suit, ranks[i], value));
            }
        }
    }

    /**
     * Shuffles the deck using current system time as random seed.
     */
    public void shuffle() {
        // Create random number generator with current time as seed
        Random rnd = new Random(System.currentTimeMillis());
        // Shuffle the cards using Collections utility
        Collections.shuffle(cards, rnd);
    }

    /**
     * Deals the top card from the deck. If deck is empty,
     * creates and shuffles a new deck before dealing.
     * @return The dealt Card object
     */
    public Card dealCard() {
        // If deck is empty, recreate and shuffle
        if (cards.isEmpty()) {
            initializeDeck();
            shuffle();
        }
        // Remove and return first card from deck
        return cards.remove(0);
    }
}