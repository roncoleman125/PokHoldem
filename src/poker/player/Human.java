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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import poker.Game;
import poker.util.Action;

/**
 * This class implements a human player interface though the keyboard.
 * @author Ron.Coleman
 */
public class Human extends AbstractPlayer {    
    protected BufferedReader br;

    public Human() {       
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    
    @Override
    public Action getAction(int bet) {
        if (br == null)
            return Action.NONE;

        try {
            // Show the command prompt
            double wp = hand.getWinProbability(numPlayers);
            
            System.out.printf("Players: %d\npot: %d\n",numPlayers,Game.getPotSize());
            System.out.println("Bankroll: "+bankroll);
            System.out.printf("Hand (prob = %2.0f%%): %s \n",wp*100.0,hand.getCard());

            // If somebody is raising, player can only raise, fold, or quit.
            if(bet > 0) {              
                Action action = getCommand("Raise, Fold, or Quit","rfq");
                
                return action;
            }

            // Otherwise...player cna check, raise, fold, or quit.
            Action action = getCommand("Check, Raise, Fold, or Quit","crfq");
            
            return action;
        } catch (Exception e) {
            System.err.println(e);
        }

        // If we get here...something went wrong!
        return Action.NONE;
    }
    
    /**
     * Gets a command from the player
     * @param msg Prompt message
     * @param allowed Allowed inputs
     * @return Action
     */
    private Action getCommand(String msg, String allowed) {
        while(true) {
            try {
                System.out.print(msg+"? ");
                
                String input = br.readLine().trim().toLowerCase();
                
                char c = input.charAt(0);
                
                for(int i=0; i < allowed.length(); i++) {
                    if(c == allowed.charAt(i)) {
                        switch(c) {
                            case 'f':
                                return lastAction = Action.FOLD;
                            case 'c':
                                return lastAction = Action.CHECK;
                            case 'r':
                                return lastAction = Action.RAISE;
                            case 'q':                                
                                System.exit(0);
                        }
                    }
                }
            } catch (IOException ex) {
                return Action.NONE;
            }
        }
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
        
        System.out.println(player+" "+action+"ED");
    }
}
