/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import poker.util.Action;
import poker.util.Bank;

/**
 *
 * @author Ron.Coleman
 */
public class Human extends AbstractPlayer {
    // Interactive commands
    // w = win probability
    // c = card
    // n = number of players
    // p = pot size
    // x = quit
    protected final String INTERACTIVE_ACTIONS = "wcnpx";
    
    protected BufferedReader br;

    public Human() {       
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public Action getAction(int bet) {
        if (br == null) {
            return Action.DONT_KNOW;
        }

        try {
            // Show the command prompt

            if(bet > 0) {
                System.out.print("Bet raised. Your hand: "+hand.getCard()+" Raise or fold? => ");
                Action action = getCommand("rf"+INTERACTIVE_ACTIONS);
            }


            return Action.DONT_KNOW;
        } catch (Exception e) {
            System.err.println(e);
        }

        return Action.DONT_KNOW;
    }
    
    private Action getCommand(String allowed) {
        while(true) {
            try {
                String input = br.readLine().trim().toLowerCase();
                
                char c = input.charAt(0);
                
                for(int i=0; i < allowed.length(); i++) {

                    if(c == allowed.charAt(i)) {
                        switch(c) {
                            case 'f':
                                return Action.FOLD;
                            case 'c':
                                return Action.CHECK;
                            case 'r':
                                return Action.RAISE;
                            case 'w':
                                // TODO: output winning probability here
                                break;
                            default:
                                return Action.DONT_KNOW;
                        }
                    }
                }
            } catch (IOException ex) {
                
            }
        }
    }
}
