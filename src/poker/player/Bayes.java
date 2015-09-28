
package poker.player;

import poker.util.Action;
import poker.util.Bank;

/**
 *
 * @author Ron.Coleman
 */
public class Bayes extends AbstractPlayer {
    final static int BA = 0;
    final static int BP = 1;
    final static int CA = 2;
    final static int CP = 3;

    private final static double[][] priorProbs = {
        // player =  0     1     2     3
        /* BA */    {0.25, 0.25, 0.25, 0.25},
        /* BP */    {0.25, 0.25, 0.25, 0.25},
        /* CA */    {0.25, 0.25, 0.25, 0.25},
        /* CP */    {0.25, 0.25, 0.25, 0.25}
    };
    
    private final static double[][] playProbs =
        {   //        FOLD    CHECK   BET
            /* BA */ {0.36,   0.05,   0.59},
            /* BP */ {0.60,   0.29,   0.11},
            /* CA */ {0.73,   0.02,   0.25},
            /* CP */ {0.87,   0.07,   0.06}
        };
    
    
    private final static double[] antiAlphas = {
        /* BA */ 0.6,
        /* BP */ 0.8,
        /* CA */ 0.0,
        /* CP */ 0.7
    };
    
    private final static double[] antiBetas = {
        /* BA */ 0.6,
        /* BP */ 0.8,
        /* CA */ 0.0,
        /* CP */ 0.7
    };
    

    
    public Bayes() {
        super(0, 0);
    }
    
    @Override
    public void acted(int id, Action action) { 
        if(id == this.id)
            return;
        
        double ba = playProbs[BA][action.value] * priorProbs[BA][id];
        double bp = playProbs[BP][action.value] * priorProbs[BP][id];
        double ca = playProbs[CA][action.value] * priorProbs[CA][id];
        double cp = playProbs[CP][action.value] * priorProbs[CP][id];
        
        double prob = ba + bp + ca + cp;
        
        if(prob < 1) {
            ba = ba / prob;
            bp = bp / prob;
            ca = ca / prob;
            cp = cp / prob;
        }
        
        priorProbs[BA][id] = ba;
        priorProbs[BP][id] = bp;
        priorProbs[CA][id] = ca;
        priorProbs[CP][id] = cp;
    }   
    
    @Override
    public Action getAction(int betAmt) {
        // Initially we don't know the population of players
        int[] demographics = { 0, 0, 0, 0 };
        
        // For each player look at their priors to see what style they're playing
        for(int playerId=0; playerId < 4; playerId++) {
            
            // Search for the largest prior by style for the player
            double maxActionProb = -1;
            
            int maxStyle = -1;
            
            for(int style=0; style < 3; style++) {
                if(priorProbs[style][playerId] > maxActionProb) {
                    maxActionProb = priorProbs[style][playerId];
                    maxStyle = style;
                }
            }
            
            // Update the demographics for this style the plaer is using
            demographics[maxStyle]++;
        }
        
        // Select the style (ie, alpha / beta) so we can make an anti-play

        // The cautious-passive player is the most dangerous so if there's
        // at least one, we'll use the corresponding anti-player style.
        if(demographics[CP] >= 1) {
            alpha = antiAlphas[CP];
            beta = antiBetas[CP];
        }
        // The cautious-aggressive player is the next most dangerous so if there's
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
