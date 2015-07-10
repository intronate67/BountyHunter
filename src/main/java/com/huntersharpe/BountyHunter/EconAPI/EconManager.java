package com.huntersharpe.BountyHunter.EconAPI;

import com.huntersharpe.BountyHunter.BountyHunter;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hunter Sharpe on 7/9/15.
 */
public class EconManager {

    private static BountyHunter plugin;

    public EconManager(BountyHunter plugin){
        this.plugin = plugin;
    }

    public static HashMap<String, Double> bal = new HashMap<String, Double>();

    public static void setBalance(String player, double amount){
        bal.put(player, amount);
    }

    public static Double getBalance(String player){
       return  bal.get(player);
    }

    public static boolean hasAccount(String player){
        return bal.containsKey(player);
    }

    public static HashMap<String, Double> getBalanceMap(){
        return bal;
    }

    public static BountyHunter getPlugin(){
        return plugin;
    }
    public static void getTopBal(Player p){
        Map.Entry<String, Double> maxEntry = null;
        for(Map.Entry<String, Double> entry : bal.entrySet()){
            if(maxEntry == null || entry.getValue() > maxEntry.getValue()){
                maxEntry = entry;
            }
        }
        String maxName = maxEntry.getKey();
        double maxValue = maxEntry.getValue();
        p.sendMessage(ChatTypes.CHAT, String.format("%s[%sTop Balance%s] " + "%s" + maxName + " %s: %s" + maxValue,
                TextColors.DARK_GRAY,
                TextColors.BLUE,
                TextColors.DARK_GRAY,
                TextColors.DARK_RED,
                TextColors.DARK_GRAY,
                TextColors.GREEN
        ));
        return;
    }
}
