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
package poker.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import poker.player.AbstractPlayer;

/**
 * This class implements the configurator which gets loaded at run time.
 * @author Ron.Coleman
 */
public class Config {
    protected static JSONParser parser = new JSONParser();
    protected ArrayList<AbstractPlayer> players = new ArrayList<>();
    protected int numGames;
    protected int bank;
    protected int minBet;
    protected Boolean debug = false;
    protected static Config config;
    
    public static Config getInstance() {
        return getInstance("pokpoker.json");
    }
    
    public static Config getInstance(String path) {
        if(config != null)
            return config;
        
        config = new Config();
        
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(path));
            
            config.numGames = ((Long) json.get("numGames")).intValue();
            
            config.bank = ((Long) json.get("bank")).intValue();
            
            config.minBet = ((Long) json.get("minBet")).intValue();
            
            config.debug = (Boolean) json.get("debug");
            
            JSONArray playersArray = (JSONArray)json.get("players");
            
            Iterator<JSONObject> iter = playersArray.iterator();
            
            while(iter.hasNext()) {
                String className = (String) iter.next().get("player");
                
                AbstractPlayer player = (AbstractPlayer) Class.forName(className).newInstance();
                
                config.players.add(player);
            }
            
            config.getPlayers().stream().forEach((player) -> {
                player.setBankroll(Bank.getCredit());
            });
            
            return config;
            
        } catch (IOException | ParseException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public ArrayList<AbstractPlayer> getPlayers() {
        return players;
    }
    
    public int getNumPlayers() {
        return players.size();
    }
    
    public int getNumGames() {
        return numGames;
    }
    
    public int getBank() {
        return bank;
    }
    
    public int getMinBet() {
        return minBet;
    }
    
    public Boolean getDebug() {
        return debug;
    }
}
