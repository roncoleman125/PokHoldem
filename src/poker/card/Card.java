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

/**
 *
 * @author Ron.Coleman
 */
public class Card {
    public final static int ACE = 11;
    
    protected int rank = -1;
    
    /**
     * Constructor
     * @param rank Card rank
     */
    public Card(int rank) {
        this.rank = rank;
    }
    
    /**
     * Gets the rank of a card.
     * @return Integer
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * Decodes a card to stringify it.
     * @return 
     */
    public String decode() {
        if(rank >= 2 && rank <= 10)
            return rank+"";
        
        return "Ace";
    }
    
    /**
     * Returns representation as a string.
     * @return String
     */
    @Override
    public String toString() {
        return decode();
    }
}
