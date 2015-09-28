/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Ron.Coleman
 */
public class Config {
    protected static JSONParser parser = new JSONParser();
    protected ArrayList<AbstractPlayer> players = new ArrayList<>();
    protected int numGames;
    protected int bank;
    protected int minBet;
    protected static Config config;
    
    public static Config getInstance() {
        return getInstance("poker.json");
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
}
