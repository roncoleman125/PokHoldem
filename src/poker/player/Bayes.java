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
package poker.player;

import poker.util.Action;

/**
 * This class implements the Bayesian opponent model.<p>
 * Handles a max of 5 players.
 * @author Ron.Coleman
 */
public class Bayes extends AbstractPlayer {
    // Some convenientce constants
    
    // Bluffing aggressive
    private final static int BA = 0;
    
    // Bluffing passive
    private final static int BP = 1;
    
    // Cautious agressive
    private final static int CA = 2;
    
    // Cautious passive
    private final static int CP = 3;
    
    // Anti-style alphas, see Backer and Cowling (2007)
    private final static double[] antiAlphas = {
        /* BA */ 0.6,
        /* BP */ 0.8,
        /* CA */ 0.0,
        /* CP */ 0.7
    };
    
    // Anti-style betas, see Backer and Cowling (2007)
    private final static double[] antiBetas = {
        /* BA */ 0.6,
        /* BP */ 0.8,
        /* CA */ 0.0,
        /* CP */ 0.7
    };

    // Prior probabilities, see Backer and Cowling (2007)
    private final static double[][] priorProbs = {
        // player =  0     1     2     3     4
        /* BA */    {0.25, 0.25, 0.25, 0.25, 0.25},
        /* BP */    {0.25, 0.25, 0.25, 0.25, 0.25},
        /* CA */    {0.25, 0.25, 0.25, 0.25, 0.25},
        /* CP */    {0.25, 0.25, 0.25, 0.25, 0.25}
    };
    
    // Play style probabilies from historical observatioms, see Backer and Cowling (2007)
    private final static double[][] playProbs =
        {   //        FOLD    CHECK   BET
            /* BA */ {0.36,   0.05,   0.59},
            /* BP */ {0.60,   0.29,   0.11},
            /* CA */ {0.73,   0.02,   0.25},
            /* CP */ {0.87,   0.07,   0.06}
        };
    
    /**
     * Constructor
     */
    public Bayes() {
        super(0, 0);
    }
    
    /**
     * Tracks each player's actions.
     * @param player Player
     * @param action Move
     */
    @Override
    public void acted(AbstractPlayer player, Action action) { 
        if(player.getId() == this.id)
            return;
        
        // Calculate the unnormalized style probability for opponent i
        // P(action | style)
        int k = player.getId();
        double ba = playProbs[BA][action.value] * priorProbs[BA][k];
        double bp = playProbs[BP][action.value] * priorProbs[BP][k];
        double ca = playProbs[CA][action.value] * priorProbs[CA][k];
        double cp = playProbs[CP][action.value] * priorProbs[CP][k];
        
        // Calculate the normalizaing constant, P(action)
        double prob = ba + bp + ca + cp;
        
        if(prob < 1) {
            ba = ba / prob;
            bp = bp / prob;
            ca = ca / prob;
            cp = cp / prob;
        }
        
        // Update the prior -- is not the posterior probability, P(style | action)
        priorProbs[BA][k] = ba;
        priorProbs[BP][k] = bp;
        priorProbs[CA][k] = ca;
        priorProbs[CP][k] = cp;
    }   
    
    /**
     * Gets an action which will vary from game to game.<p>
     * In other words, we're going to calculate alpha / beta dynamically.
     * @param betAmt
     * @return 
     */
    @Override
    public Action getAction(int betAmt) {
        // Initially we don't know the population of players
        int[] demographics = { 0, 0, 0, 0 };
        
        // For each player look at their priors to see what style they're playing
        for(int playerId=0; playerId < 4; playerId++) {
            
            // Search for the largest prior by style for the player
            double maxActionProb = -1;
            
            int maxStyle = -1;
            
            int[] styles = { BA, BP, CA, CP };
            for(int style: styles) {
                if(priorProbs[style][playerId] > maxActionProb) {
                    maxActionProb = priorProbs[style][playerId];
                    maxStyle = style;
                }
            }
            
            // Update the demographics for this style the plaer is using
            demographics[maxStyle]++;
        }
        
        // Select the style (ie, alpha / beta) so we can make an anti-play

        // The cautious-passive player is the most important to counter so if there's
        // at least one, we'll use the corresponding anti-player style.
        if(demographics[CP] >= 1) {
            alpha = antiAlphas[CP];
            beta = antiBetas[CP];
        }
        // The cautious-aggressive player is the next most important to counter so if there's
        // at least one, we'll use the corresponding anti-player style.
        else if(demographics[CA] >= 1) {
            alpha = antiAlphas[CA];
            beta = antiBetas[CA];
        }
        // If there are more bluffing-aggressive than bluffing-passive
        // players, we'll use the corresponding anti-player style.
        else if(demographics[BA] > demographics[BP]) {
            alpha = antiAlphas[BA];
            beta = antiBetas[BA];
        }
        // If we get to this point, it may be too early to tell so we use the
        // anti-bluffing-passive style.
        else {
            alpha = antiAlphas[BP];
            beta = antiBetas[BP];
        }

        return super.getAction(betAmt);
    }
}
