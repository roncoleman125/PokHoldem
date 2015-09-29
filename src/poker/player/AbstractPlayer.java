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

import poker.card.Card;
import poker.card.Hand;
import poker.util.Action;
import poker.util.Config;

/**
 * Every player must subclass this class.
 * @author Ron.Coleman
 */
abstract public class AbstractPlayer {
    public static final Boolean DEBUG = true;
    
    protected static int idNext = 0;
    protected static int numPlayers = 0;
    
    protected Hand hand = new Hand();
    protected double alpha = 0.0;
    protected double beta = 0.0;
    protected int bankroll = 0;
    protected final int id;

    protected Action lastAction = Action.NONE;

    /**
     * Constructor
     */
    protected AbstractPlayer() {
        this(0,0);
    }
    
    /**
     * Constructor
     * @param alpha Alpha
     * @param beta Beta
     */
    protected AbstractPlayer(double alpha, double beta) {
        this.id = idNext++;
        
        this.alpha = alpha;
        
        this.beta = beta;
        
        numPlayers++;
    }

    /**
     * Answers whether player still has active interest in the jackpot.
     * @return Boolean 
     */
    public Boolean isActive() {
        return lastAction != Action.FOLD;
    }
    
    /**
     * Answers whether player has raised the bet.
     * @return Boolean
     */
    public Boolean isRaised() {
        return lastAction == Action.RAISE;
    }
    
    /**
     * Folds the hand.
     * @return Action.FOLD
     */
    protected Action fold() {
        lastAction = Action.FOLD;
        
        numPlayers--;
        
        return Action.FOLD;
    }
    
    /**
     * Raises the hand.
     * @return Action.RAISE
     */
    protected Action raise() {
        return lastAction = Action.RAISE;
    }
    
    /**
     * Checks the hand.
     * @return Action.CHECK
     */
    protected Action check() {
        return lastAction = Action.CHECK;
    }
    
    /**
     * Resets the hand which is to say, erases the last action and clears the hand.
     */
    public void reset() {
        lastAction = Action.NONE;
        
        numPlayers = Config.getInstance().getNumPlayers();
        
        clear();
    }
    
    /**
     * Clears the last action on the hand.
     */
    public void clearAction() {
        lastAction = Action.NONE;
    }
    
    /**
     * Clears the hard(s) in the hand.
     */
    public void clear() {
        hand.clear();
    }
    
    /**
     * Invoked when a player acts (CHECK, RAISE, or FOLD)
     * @param player
     * @param action 
     */
    public void acted(AbstractPlayer player, Action action) {

    }
    
    /**
     * Updates the players' bandroll with the pot.
     * @param pot 
     */
    public void won(int pot) {
        bankroll += pot;
    }
    
    /**
     * Get the hand rank.
     * @return Integer
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * Gets the bankroll.
     * @return Integer
     */
    public int getBankroll() {
        return bankroll;
    }
    
    /**
     * Sets the bankroll.
     * @param bankroll 
     */
    public void setBankroll(int bankroll) {
        this.bankroll = bankroll;
    }
    
    /**
     * Hits the hand with a card.
     * @param card Card
     */
    public void hit(Card card) {
        hand.hit(card);
    }

    /**
     * Implements the bet decision tree.
     * @param betAmt Represents inbound raise
     * @return 
     */
    public Action getAction(int betAmt) {
        // Get the win probability
        double wp = hand.getWinProbability(numPlayers);

        // If we have no cushion to make a bet, we must fold
        int cover = bankroll - betAmt;
        
        if (cover < 0)
            return fold();

        // Do bet decision tree
        if (wp >= alpha) {
            if (wp >= beta && cover > 0)
                return raise();
            else if(betAmt == 0 || cover == 0)
                return check();
        }

        else if (wp < alpha && betAmt == 0)
            return check();

        // We'll arrive here if we can't raise or check
        return fold();
    }
    
    /**
     * Gets the last action.
     * @return Action
     */
    public Action getLastAction() {
        return lastAction;
    }

    /**
     * Makes a bet
     * @param amt Bet amount deducted from the bankroll.
     * @return 
     */
    public int bet(int amt) {
        int cushion = bankroll - amt;
        
        if(cushion < 0)
            return cushion;
        
        bankroll -= amt;

        return amt;
    }
    
    /**
     * Gets player id
     * @return Integer
     */
    public int getId() {
        return id;
    }
    
    /**
     * Characterizes the player -- useful for debugging purposes.
     * @return String
     */
    public String characterize() {
        return "id: "+id+" a: "+alpha+" b: "+beta+ " b/r: "+bankroll+" hand: "+hand+" last action: "+lastAction;
    }
    
    /**
     * Converts this class to a string.
     * @return 
     */
    @Override
    public String toString() {
        return decorate(getClass().getSimpleName()) + " (player #"+id+")";
    }
    
    /**
     * Decorates a class name.
     * @param name Name
     * @return Decorated name
     */
    public String decorate(String name) {
        StringBuilder sb = new StringBuilder();
        
        for(int k=0; k < name.length(); k++) {
            char c = name.charAt(k);
            
            if(Character.isUpperCase(name.charAt(k)) && k != 0)
                sb.append(" ");
            
            sb.append(Character.toUpperCase(c));    
        }
        
        return sb.toString();
    }
}