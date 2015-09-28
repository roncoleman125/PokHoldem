/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.player;

import poker.card.Card;
import poker.card.Hand;
import poker.util.Action;
import poker.util.Bank;

/**
 *
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
    protected int standingBet = 0;

    protected Action lastAction = Action.NONE;

    protected AbstractPlayer() {
        this(0,0);
    }
    
    protected AbstractPlayer(double alpha, double beta) {
        this.id = idNext++;
        
        numPlayers++;
        
        this.alpha = alpha;
        
        this.beta = beta;
    }

    public Boolean alive() {
        return lastAction != Action.FOLD;
    }
    
    public Boolean raised() {
        return lastAction == Action.RAISE;
    }
    
    protected Action fold() {
        lastAction = Action.FOLD;
        
        numPlayers--;
        
        return Action.FOLD;
    }
    
    protected Action raise() {
        return lastAction = Action.RAISE;
    }
    
    protected Action check() {
        return lastAction = Action.CHECK;
    }
    
    public void reset() {
        lastAction = Action.NONE;
        
        bankroll = Bank.getCredit();
        
        numPlayers++;
        
        clear();
    }
    
    public void clearAction() {
        lastAction = Action.NONE;
    }
    
    public void clear() {
        hand.clear();
    }
    
    public void acted(int id, Action action) {

    }
    
    public void won(int pot) {
        bankroll += pot;
    }
    
    public Card getCard() {
        return hand.getCard();
    }
    
    public int getBankroll() {
        return bankroll;
    }
    
    public void setBankroll(int bankroll) {
        this.bankroll = bankroll;
    }
    
    public void hit(Card card) {
        hand.hit(card);
    }

    public Action getAction(int betAmt) {
        double wp = hand.getWinProbability(numPlayers);

        int cushion = bankroll - betAmt;
        
        if (cushion < 0)
            return fold();

        if (wp >= alpha) {
            if (wp >= beta)
                return raise();
            else
                return check();
        }

        else if (wp < alpha && betAmt == 0)
            return check();

        return fold();
    }
    
    public Action getLastAction() {
        return lastAction;
    }

    public int bet(int minBet) {
        int cushion = bankroll - minBet;
        
        if(cushion < 0)
            return cushion;
        
        standingBet += minBet;
        
        bankroll -= minBet;

        return minBet;
    }
    
    public int getId() {
        return id;
    }
    
    public String characterize() {
        return "id: "+id+" a: "+alpha+" b: "+beta+ " b/r: "+bankroll+" hand: "+hand+" last action: "+lastAction;
    }
    
    @Override
    public String toString() {
        return "# " + id + " " + decorate(getClass().getName());
    }
    
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