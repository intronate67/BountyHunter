package com.huntersharpe.BountyHunter.Util;

import com.huntersharpe.BountyHunter.BountyHunter;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hunter Sharpe on 7/10/15.
 */
public class BountyUtil {

    public static BountyUtil plugin;

    public static BountyUtil getUtil(){
        return plugin;
    }

    //Map contains Target, Setter, & Value
    public HashMap<String, Map<String, Double>> bounties = new HashMap();

    //Map contains Setter and value
    public Map<String, Double> bounty = new HashMap<String, Double>();

    //Map contains target and value.
    public Map<String, Double> availBounty = new HashMap<String, Double>();

    //Map contains Target and true or false
    public Map<String, Boolean> available = new HashMap<String, Boolean>();

    //Contains Accepted Target and targeter.
    public Map<String, String> accepted = new HashMap<String, String>();

    public HashMap<String, Map<String, Double>> getBountyMap(){
        return bounties;
    }

    public void setBounty(String name, double value, String setter){
        bounty.put(setter, value);
        bounties.put(name, bounty);
        availBounty.put(name, value);
        available.put(name, true);
    }

    public void removeBounty(String target, String setter, double value){
        Map<String, Double> x = new HashMap<String, Double>();
        x.put(setter, value);
        Player p = (Player) BountyHunter.getInstance().game.getServer().getPlayer(setter);
        if(!bounties.get(target).equals(x)){
            p.sendMessage(Texts.of(TextColors.RED + "You did not set this bounty!"));
        }
        bounties.remove(target, x);
        bounty.remove(setter, value);
        availBounty.remove(setter, value);
        accepted.remove(setter, accepted.get(setter));
    }
    public Map<String, Double> getBounty(String name){
        if(hasBounty(name)){
            return bounties.get(name);
        }else{
            return null;
        }
    }

    public boolean hasBounty(String name){
        if(bounties.containsKey(name)){
            return false;
        }
        return true;
    }

}
