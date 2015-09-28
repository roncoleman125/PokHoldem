package poker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Performs Monte Carlo simulation of the Poughkeepsie hold'em win probability.
 * @author Ron.Coleman
 */
public class MonteCarlo {
    /**
     * Set this to true to enable debugging
     */
    private final static Boolean DEBUG = false;

    /**
     * Number of players.
     * 
     */
    private final static int NUM_PLAYERS = 4;
    
    /**
     * Number of games to play. It should not be less than 1,000 to get
     * stable statistics.
     */
    private final static int NUM_GAMES = 100000;
    
    /**
     * We'll run this many trials just to see how the numbers settle.
    */
    private final static int NUM_TRIALS = 11;

    public static void main(String[] args) {
        int numPlayers = NUM_PLAYERS;
        if(args.length != 0)
            numPlayers = Integer.parseInt(args[0]);

        if(!DEBUG)
            System.out.print(numPlayers+" players: ");
        
        long t0 = System.currentTimeMillis();
        
        int[] hands = new int[numPlayers];

        Random ran = new Random();

        // Test each card
        for (int myCard = 2; myCard <= 11; myCard++) {
            if(DEBUG)
                System.out.printf("card %2d ",myCard);

            // Run through the trials of N games each
            ArrayList<Double> wps = new ArrayList<>();
            
            for (int trial = 0; trial < NUM_TRIALS; trial++) {
                
                int wins = 0;
                
                // Play N games
                for (int game = 0; game < NUM_GAMES; game++) {
                    // Set up the Pok hold'em deck
                    ArrayList<Integer> deck =
                            new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

                    // Draw a card -- that I'm always first in this case doesn't matter
                    hands[0] = myCard;

                    // Take my card from the deck
                    deck.remove(new Integer(myCard));

                    // Shuffle the deck
                    Collections.shuffle(deck, ran);

                    // Deal the cards
                    for (int hand = 1; hand < hands.length; hand++) {
                        int card = deck.remove(0);

                        hands[hand] = card;
                    }

                    // Determine if I win
                    Boolean won = true;

                    for (int hand = 1; hand < hands.length; hand++) {
                        if (hands[0] < hands[hand]) {
                            won = false;
                            break;
                        }
                    }

                    if (won) {
                        wins++;
                    }
                }

                // Report the win probability for each trial
                double wp = (double) wins / NUM_GAMES;
                if(DEBUG)
                    System.out.printf("%6.3f ",wp);
                
                wps.add(wp);
            }
            
            Collections.sort(wps);
            
            String format = "%6.3f ";
            if(DEBUG)
                format = " %6.3f\n";
            System.out.printf(format,wps.get(NUM_TRIALS/2));

        }
        
        long t1 = System.currentTimeMillis();
        
        if(!DEBUG)
            System.out.println("");
        
        System.out.println("time: "+(t1-t0)/1000.+" s");
    }

}
