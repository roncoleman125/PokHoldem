/*
 Copyright (c) 2015 Ron Coleman
 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:
 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
 * This class is the main process driver of the game.
 * @author Ron.Coleman
 */
public class Game {
    /**
     * Por (or "jackpot")
     */
    protected static int pot;
        
    // Configurator
    private final static Config config = Config.getInstance();
    
    // List of players never changes although the state of players might
    private final static ArrayList<AbstractPlayer> players = config.getPlayers();

    // Win frequencies
    private final static HashMap<String, Integer> winFreqs = new HashMap<>();
        
    // Launch point for game
    public static void main(String[] args) {
        signon();
        
        // Instantiate the deck
        Deck deck = new Deck();

        // Set up basic game configuration
        int numGames = config.getNumGames();
        
        int blind = config.getMinBet();
        
        int gamesPlayed = numGames;
        
        for (int game = 0; game < numGames; game++) {
            // Reset the game
            deck.shuffle();
            
            pot = 0;

            int active = 0;
            
            for(AbstractPlayer player: players) {
                player.reset();
                
                if(player.getBankroll() <= 0)
                    continue;
                
                player.bet(blind);
                
                Card card = deck.deal();
                
                player.hit(card);
                
                // Add to the jackpot
                pot += blind;
                
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
            System.out.println(">>>> GAME OVER: "+winner+" wins with "+winner.getHand()+" takes all pot = "+pot);

            // Collect win frequency data
            int frequency = winFreqs.getOrDefault(winner.toString(), 0);
            winFreqs.put(winner.toString(), frequency + 1 );
            
            // If only one player solvent, no need to play more games
            int solvent = checkSolvency();
            if(solvent == 1) {
                gamesPlayed = game;
                break;
            }
        }

        System.out.println("One solve player remains. GOODBYE!");
        signoff(gamesPlayed);
    }
    
    /**
     * Check number of solvent players.
     * @return Solvent count
     */
    private static int checkSolvency() {
        int count = 0;
        
        for(AbstractPlayer player: players) {
            if(player.getBankroll()> 0)
                count++;
        }
        
        return count;
    } 
    
    /**
     * Display signon message
     */
    private static void signon() {
        System.out.println("Welcome to Poughkeepsie Hold'em!");
        System.out.println("--------------------------------");
    }
    
    /**
     * Display signoff message
     */
    private static void signoff(int ngames) {
        System.out.println("Play statistics");
        System.out.println("---------------");
                // Report win frequency for all players

        System.out.println("played "+ngames+"");
        players.stream().forEach((player) -> {
            System.out.println(player+" wins = "+winFreqs.getOrDefault(player+"",0)+" b/r = "+player.getBankroll());
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
            if(!player.isActive())
                continue;
            
            int rank = player.getHand().getRank();
            
            if (rank > maxRank) {
                winner = player;
                maxRank = rank;
            }
        }
        
        return winner;
    }
    
    public static void doRounds(ArrayList<AbstractPlayer> players) {  
        int round = 0;
        while (true) {
            round++;
            System.out.println("#### ROUND "+round);
            int active = 0;
            int raising = 0;
            int playerIndex = 0;
                                
            int minBet = 0;

            // PASS #1: Find out who wants to CHECK, RAISE, or FOLD 
            for (AbstractPlayer player : players) {
                // End the round if I'm last player and no one is left
                // since there's no point in raising, checking, or whatever
                if(playerIndex == players.size()-1 && active == 0)
                    return;
                
                // Clear last action since this is a new round
                player.clearAction();
                
                Action action = player.getAction(minBet);

                // Inform other players
                players.stream().forEach((other) -> {
                    other.acted(player, action);
                });

                // If player did not fold, they're still in as RAISE or CHECK
                if (action != Action.FOLD)
                    active++;

                // If player is RAISING, we may need another round, depending
                // how many players remain.
                if (action == Action.RAISE) {
                    raising++;
                    
                    minBet = Config.getInstance().getMinBet();
                    
                    player.bet(minBet);
                    
                    pot += minBet;
                }
                
                playerIndex++;
            }

            // If only one player remains or no one raising, rounds are done!
            if (active == 1 || raising == 0)
                return;
            
            // PASS #2: Call in bets from those not already folded
            active = 0;
            
            for (AbstractPlayer player : players) {
                if (player.isActive()) {
                    active++;
                    
                    if (!player.isRaised()) {
                        Action action = player.getAction(minBet);
                        
                        if (action != Action.FOLD) {
                            player.bet(minBet);

                            pot += minBet;
                        }
                        else
                            active--;
                    }
                }
            }
            
            if(active == 1)
                return;
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
