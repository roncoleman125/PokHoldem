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
import poker.util.Helper;

/**
 * This class implements a human player interface though the keyboard.
 * @author Ron.Coleman
 */
public class Human extends AbstractPlayer {    
    protected BufferedReader br;

    /**
     * Constructor
     */
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
            Action action;               
            
            // If someone is raising...
            if(bet > 0) {
                // If player has no chips, player can only fold or quit
                if(bankroll == 0)
                    action = getCommand("fq");
                
                // Otherwise, player has chips and can raise, fold, or quit 
                else
                    action = getCommand("rfq");
            }
            // No one is raising but if player has no chips, as above,
            // player can only check, fold, or quit
            else if(bankroll == 0) {
                action = getCommand("cfq");
            }
            else
                // Otherwise...no one raising but player has some chips so...
                // player can check, raise, fold, or quit.
                action = getCommand("crfq");
            
            System.out.println("You "+Helper.getGrammatical(action)+".");
            
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
    private Action getCommand( String allowed) {
        String msg = decode(allowed);
        
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
                                if(bankroll > 0)
                                    return lastAction = Action.RAISE;
                                else
                                    return lastAction = Action.FOLD;
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
        
        System.out.print(player+" "+Helper.getGrammatical(action));
        
        if(action == Action.FOLD)
            System.out.println(" with "+player.getHand()+".");
        else
            System.out.println(".");
    }
    
    /**
     * Decodes the allowed commands
     * @param allowed Allowed command
     * @return Decoded command
     */
    protected String decode(String allowed) {
        StringBuilder sb = new StringBuilder();
        for(int k=0; k < allowed.length(); k++) {
            if(k != 0 && k != allowed.length()-1)
                sb.append(", ");
            
            else if(k == allowed.length()-1)
                sb.append(" or ");
            switch(allowed.charAt(k)) {
                case 'c':
                    sb.append("Check");
                    break;
                case 'r':
                    sb.append("Raise");
                    break;
                case 'f':
                    sb.append("Fold");
                    break;
                case 'q':
                    sb.append("Quit");
                    break;                    
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Gets string representation of this class.
     * @return String
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName().toUpperCase()+" (player #"+id+")";
    }
    
    /**
     * Gets decorated name
     * @return String
     */
    @Override
    public String decorated() {
        return this.getClass().getSimpleName().toUpperCase();
    }
}
