/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.HashMap;
import poker.card.Card;
import poker.card.Deck;
import poker.player.AbstractPlayer;
import poker.util.Action;
import poker.util.Config;

/**
 *
 * @author Ron.Coleman
 */
public class Game {
    public static int pot;
        
    private final static Config config = Config.getInstance();
    
    public static void main(String[] args) {
        ArrayList<AbstractPlayer> players = config.getPlayers();
        
        HashMap<String,Integer> winFreqs = new HashMap<>( );
        
        Deck deck = new Deck();

        int numGames = config.getNumGames();
        
        int minBet = config.getMinBet();
        
        for (int game = 0; game < numGames; game++) {
            // Reset the game
            deck.shuffle();

            pot = 0;

            int active = 0;
            
            for(AbstractPlayer player: players) {
                if(player.getBankroll() <= 0)
                    continue;
                
                player.bet(minBet);
                
                Card card = deck.deal();
                
                player.hit(card);
                
                pot++;
                
                active++;
            }
            
            // Nobody left but one player!
            if(active == 1)
                break;
            
            // Do betting rounds
            doRounds(players);

            // Identify the winner
            AbstractPlayer winner = selectWinner(players);
            
            // Update the winner's bankroll
            winner.won(pot);
            
            // Collect win frequency
            int frequency = winFreqs.getOrDefault(winner.toString(), 0);
            winFreqs.put(winner.toString(), frequency + 1 );
        }

        // Report win frequency for all players
        players.stream().forEach((player) -> {
            System.out.println(player+" wins = "+winFreqs.get(player.toString())+" b/r = "+player.getBankroll());
        });

    }

    /**
     * Selects the game winner
     * @param players Active players
     * @return Winning player
     */
    public static AbstractPlayer selectWinner(ArrayList<AbstractPlayer> players) {
        AbstractPlayer winner = null;
        
        int maxRank = -1;

        for (AbstractPlayer player : players) {
            if(!player.alive())
                continue;
            
            int rank = player.getCard().getRank();
            
            if (rank > maxRank) {
                winner = player;
                maxRank = rank;
            }
        }
        
        return winner;
    }
    
    public static void doRounds(ArrayList<AbstractPlayer> players) {   
        while (true) {
            int active = 0;
            int raising = 0;

            for (AbstractPlayer player : players) {
                // Check in with player to see what they want to do next
                Action action = player.getAction(0);

                // Inform other players
                players.stream().forEach((other) -> {
                    other.acted(player.getId(), action);
                });

                // If player did not fold, they're still in as RAISE or CHECK
                if (action != Action.FOLD)
                    active++;

                // If player is RAISING, we may need another round, depending
                // how many players remain.
                if (action == Action.RAISE) {
                    raising++;
                }
            }

            // If only one player remains or no one raising, rounds are done!
            if (active == 1 || raising == 0)
                return;

            // At this point >1 players ramain and someone is RAISING
            int minBet = config.getMinBet();
            
            players.stream().filter((player) -> (player.alive())).forEach((player) -> {
                Action action = player.getAction(minBet);
                if (!(action == Action.FOLD)) {
                    player.bet(minBet);

                    pot++;
                }
            });
        }
    }
    
    /**
     * Gets the pot 
     * @return 
     */
    public static int getPotSize() {
        return pot;
    }
}
