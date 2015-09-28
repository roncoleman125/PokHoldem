/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Ron.Coleman
 */
public class Deck {
    protected ArrayList<Card> cards = new ArrayList<>();
    protected int nextIndex = 0;
    
    public Deck() {
        for(int rank=2; rank <= Card.ACE; rank++)
            cards.add(new Card(rank));
    }
    
    public void shuffle() {
        nextIndex = 0;
        
        Collections.shuffle(cards,new Random(0));
    }
    
    public Card deal() {
        return cards.get(nextIndex++);
    }
}
