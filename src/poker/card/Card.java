/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.card;

/**
 *
 * @author Ron.Coleman
 */
public class Card {
    public final static int ACE = 11;
    
    protected int rank = -1;
    
    public Card(int rank) {
        this.rank = rank;
    }
    
    public int getRank() {
        return rank;
    }
    
    public String decode() {
        if(rank >= 2 && rank <= 10)
            return rank+"";
        
        return "Ace";
    }
    @Override
    public String toString() {
        return decode();
    }
}
