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
import poker.util.Helper;

/**
 * This player is not a "real" player but instead an observer.
 * @author Ron.Coleman
 */
public class Observer extends AbstractPlayer {
    public Observer() {        
        numPlayers--;
    }
    
    @Override
    public Action getAction(int raise) {
        return Action.NONE;
    }
    
    /**
     * Report what other players are doing.
     * @param player Player
     * @param action Action
     */
    @Override
    public void acted(AbstractPlayer player, Action action) {
        if(player == this)
            return;
        
        System.out.print(player+" "+Helper.getGrammatical(action));
        
        if(action == Action.FOLD)
            System.out.println(" with "+player.getHand()+".");
        else
            System.out.println(".");
    } 
}
