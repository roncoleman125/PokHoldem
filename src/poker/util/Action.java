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
public enum Action {
    FOLD(0),
    CHECK(1),
    RAISE(2),
    NONE(-1);
    
    public final int value;
    private Action(int value) {
        this.value = value;
    }
}
