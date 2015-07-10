package com.huntersharpe.BountyHunter.EconAPI;

import com.huntersharpe.BountyHunter.BountyHunter;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Hunter Sharpe on 7/9/15.
 */
public class EconAPI {

    private static BountyHunter plugin = EconManager.getPlugin();

    public static void saveBalances(){

        for(String p : EconManager.getBalanceMap().keySet()){
            plugin.getConfigNode().getNode("balance").setValue(p + ":"  + EconManager.getBalanceMap().get(p));
        }
        try{
            plugin.getConfigLoader().save(plugin.getConfigNode());
        } catch (IOException e){
            plugin.getLogger().log(Level.SEVERE, "Could not save configuration file!");
        }
    }

    public static void loadBalances(){
        if(!plugin.getConfigNode().getNode("balance").getValue("intronate67").equals(1)) return;
        //TODO: Add foreach statement to load balances.
        //Edit
    }

}
