/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import poker.Game;
import poker.util.Action;
import poker.util.Config;

/**
 *
 * @author Ron.Coleman
 */
public class Human extends AbstractPlayer {
    // Interactive commands
    // n = number of players
    // p = pot size
    // x = quit
    protected final String INTERACTIVE_ACTIONS = "wcnpx";
    
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
            
            System.out.printf("players: %d pot: %d ",numPlayers,Game.getPotSize());
            System.out.printf("hand: %s (prob %5.2f)\n",hand.getCard(),wp);

            if(bet > 0) {              
                Action action = getCommand("raise, fold, or quit","rfq");
                
                return action;
            }

            Action action = getCommand("check, raise, fold, or quit","crfq");
            return action;
        } catch (Exception e) {
            System.err.println(e);
        }

        return Action.NONE;
    }
    
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
                                return Action.FOLD;
                            case 'c':
                                return Action.CHECK;
                            case 'r':
                                return Action.RAISE;
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
}
