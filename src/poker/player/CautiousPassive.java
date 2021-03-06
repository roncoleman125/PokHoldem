/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.player;

/**
 * This class implements a cautious passive bot player.
 * @author Ron.Coleman
 */
public class CautiousPassive extends AbstractPlayer {
    private final static double ALPHA = 0.5;
    private final static double BETA = 0.9;
    
    public CautiousPassive() {
        super(ALPHA, BETA);
    }
}
