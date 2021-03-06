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
package poker.card;

import java.util.ArrayList;

/**
 * This class represents a hand.
 * @author Ron.Coleman
 */
public class Hand {
//    // Set to true for "exact" win probability using combinatorial method
//    public final static Boolean USE_COMBINATORIAL = false;
    
    // Win probability matrix
    protected static double[][] wpMatrix = {
        /*         hand: 2       3       4       5       6       7       8       9       10      Ace */
        /* 2 players */ {0.000,  0.111,  0.222,  0.333,  0.444,  0.555,  0.667,  0.777,  0.889,  1.000},
        /* 3 players */ {0.000,  0.000,  0.028,  0.083,  0.167,  0.278,  0.416,  0.583,  0.778,  1.000},
        /* 4 players */ {0.000,  0.000,  0.000,  0.012,  0.048,  0.119,  0.238,  0.417,  0.666,  1.000},
        /* 5 players */ {0.000,  0.000,  0.000,  0.000,  0.008,  0.040,  0.119,  0.278,  0.556,  1.000}
    };
    
    /**
     * Cards in this hand
     */
    protected ArrayList<Card> cards = new ArrayList<>();
    
    /**
     * Get hand's card.
     * @return Card
     */
    public Card getCard() {
        assert(!cards.isEmpty());
        
        return cards.get(0);
    }
    
    /**
     * Gets the rank of the hand which is just the rank of the one card
     * in the hand.
     * @return Rank as an integer
     */
    public int getRank() {
        return getCard().getRank();
    }
    
    /**
     * Hits hand with a card
     * @param card Card
     */
    public void hit(Card card) {
        cards.add(card);
    }
    
    /**
     * Clears the cards in hand.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Gets the win probability given the number of hands in the game
     * @param numHands Number of hands 2-5.
     * @return Probability
     */
    public double getWinProbability(int numHands) {
        assert(numHands >=2 && numHands <=5);
        assert(!cards.isEmpty());
        
        int rank = cards.get(0).getRank();
        assert(rank >=2 && rank <= 11);
        
//        if(USE_COMBINATORIAL)
//            return combinatorial(rank);

        // numHands-2 as there must be a min of two hands
        // rank-2 as ranks range from 2 - 11
        double wp = wpMatrix[numHands-2][rank-2];
        
        return wp;
    }

    /**
     * Calculate win probability assuming four players. NOT USED
     * @param rank Card rank
     * @return win probability
     */
    protected double combinatorial(int rank) {        
        // Card in my hand is the first card thus we'll need to
        // calculate in turn the probably that my card loses against
        // three subsequent cards in other player's hands.
        
        // Assuming two cards have been dealt (mine and another), my card
        // is less than the other. For instance, if my card is a 9, then
        // there are (11-rank) or 2 cards that I can lose to.
        double card2LoseProb = (11 - rank) / 9.;
        
        // Assuming three cards have been dealt (mine plus two others), my card
        // is greater than one but less than the other.
        double card3LoseProb = (1 - (11-rank)/9.) *  (11 - rank) / 8.;
        
        // Assuming all four cards have been dealt (mine plus three others), my card
        // is greater than two but less than one
        double card4LoseProb = (1 - (11-rank)/9.) * (1 - (11-rank)/8.) * (11 - rank) / 7.;
        
        // Add all the lose probabilities
        double loseProb = card2LoseProb + card3LoseProb + card4LoseProb;
        
        // Winning probability is one less than the lose probability.
        double winProb = 1 - loseProb;
        
        return winProb;
    }
    
    @Override
    public String toString() {
        return "card: "+ (!cards.isEmpty() ? cards.get(0) : "NA");
    }
}
