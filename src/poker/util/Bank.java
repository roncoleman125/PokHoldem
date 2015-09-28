/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker.util;

/**
 *
 * @author Ron.Coleman
 */
public class Bank {    
    public static int getCredit() {
        int chips = Config.getInstance().getBank();
        
        int numPlayers = Config.getInstance().getNumPlayers();
        
        return chips / numPlayers;
    }
}
