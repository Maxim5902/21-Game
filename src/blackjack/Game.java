package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game logic controller for Blackjack.
 * Manages game state, player turns, and win determination.
 */
public class Game {
    private Deck deck;                  // The deck of cards
    private List<Player> players;      // List of players
    private Dealer dealer;             // The dealer
    private int currentPlayerIndex;     // Index of current active player
    private GameState state;           // Current game state

    /**
     * Possible game states
     */
    public enum GameState {
        PLAYER_TURN,    // Players are taking their turns
        DEALER_TURN,    // Dealer is playing
        GAME_OVER       // Round is complete
    }

    /**
     * Creates a new game with specified players.
     * @param playerNames List of player names
     */
    public Game(List<String> playerNames) {
        deck = new Deck(); // Initialize new shuffled deck
        players = new ArrayList<>();
        // Create player objects for each name
        for (String name : playerNames) {
            players.add(new Player(name));
        }
        dealer = new Dealer(); // Create dealer
        currentPlayerIndex = 0; // Start with first player
        state = GameState.PLAYER_TURN; // Initial state
        dealInitialCards(); // Deal starting hands
    }

    /**
     * Deals initial two cards to each player and dealer.
     * Dealer's second card is face down.
     */
    private void dealInitialCards() {
        // Deal two cards to each player
        for (Player player : players) {
            player.hit(deck.dealCard()); // First card
            player.hit(deck.dealCard()); // Second card
        }
        
        // Deal to dealer (first card face up, second face down)
        dealer.hit(deck.dealCard()); // Face up
        Card secondCard = deck.dealCard();
        secondCard.flip(); // Face down
        dealer.hit(secondCard);
    }

    /**
     * Current player hits (receives another card).
     * Advances turn if player busts or gets 21.
     */
    public void playerHit() {
        // Only allow during player turn phase
        if (state == GameState.PLAYER_TURN) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.hit(deck.dealCard()); // Deal new card
            
            // Auto-stand on 21 or bust
            if (currentPlayer.isBusted() || currentPlayer.getHand().calculateValue() == 21) {
                nextPlayer(); // Move to next player
            }
        }
    }

    /**
     * Current player stands (ends their turn).
     */
    public void playerStand() {
        // Only allow during player turn phase
        if (state == GameState.PLAYER_TURN) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.stand(); // Mark as standing
            nextPlayer(); // Move to next player
        }
    }

    /**
     * Advances to next player or dealer's turn.
     */
    private void nextPlayer() {
        currentPlayerIndex++;
        // Check if all players have gone
        if (currentPlayerIndex >= players.size()) {
            state = GameState.DEALER_TURN; // Switch to dealer's turn
            dealerTurn(); // Start dealer's play
        }
    }

    /**
     * Dealer takes their turn (hits until 16+).
     */
    private void dealerTurn() {
        dealer.play(deck); // Dealer follows house rules
        state = GameState.GAME_OVER; // End the game
    }

    /**
     * Determines winners against the dealer.
     * @return List of result messages for each player
     */
    public List<String> determineWinners() {
        List<String> results = new ArrayList<>();
        int dealerValue = dealer.getHand().calculateValue();
        boolean dealerBusted = dealer.isBusted();
        
        // Evaluate each player's hand against dealer
        for (Player player : players) {
            int playerValue = player.getHand().calculateValue();
            boolean playerBusted = player.isBusted();
            
            if (playerBusted) {
                results.add(player.getName() + " busted! Dealer wins.");
            } else if (dealerBusted) {
                results.add("Dealer busted! " + player.getName() + " wins.");
            } else if (playerValue > dealerValue) {
                results.add(player.getName() + " wins! " + playerValue + " vs " + dealerValue);
            } else if (playerValue < dealerValue) {
                results.add("Dealer wins against " + player.getName() + "! " + dealerValue + " vs " + playerValue);
            } else {
                results.add(player.getName() + " pushes with dealer. Both have " + playerValue);
            }
        }
        
        return results;
    }

    // Accessor methods with documentation
    
    /** 
     * @return Current active player or null if not player turn 
     */
    public Player getCurrentPlayer() {
        if (state == GameState.PLAYER_TURN) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }

    /** @return List of all players */
    public List<Player> getPlayers() { return players; }

    /** @return The dealer */
    public Dealer getDealer() { return dealer; }

    /** @return Current game state */
    public GameState getState() { return state; }
    
    /**
     * Resets the game for a new round:
     * - New shuffled deck
     * - Reset player/dealer states
     * - Deal new initial cards
     */
    public void reset() {
        deck = new Deck(); // Fresh deck
        // Reset all players
        for (Player player : players) {
            player.reset();
        }
        dealer.reset(); // Reset dealer
        currentPlayerIndex = 0; // Back to first player
        state = GameState.PLAYER_TURN; // Reset state
        dealInitialCards(); // Deal new hands
    }
}