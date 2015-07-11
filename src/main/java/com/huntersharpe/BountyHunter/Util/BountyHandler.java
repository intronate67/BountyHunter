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
public class BountyHandler {

    public static BountyHandler plugin;

    public static BountyHandler getPlugin(){
        return plugin;
    }

    public Map<String, Boolean> bounties = BountyUtil.getUtil().available;

    public Map<String, Integer> placedBounties = new HashMap<String, Integer>();


    public void acceptBounty(Player p, String name){
        Player player = (Player) BountyHunter.getInstance().game.getServer().getPlayer(name);
        if(!bounties.get(p.getName()).equals(true)){
            player.sendMessage(Texts.of(TextColors.RED + "No available bounty for " + p.getName()));
        }
        BountyUtil.getUtil().accepted.put(p.getName(), name);
    }

    public void abadonBounty(Player p, String name){
        Player player = (Player) BountyHunter.getInstance().game.getServer().getPlayer(name);
        if(!BountyUtil.getUtil().accepted.containsValue(p.getName())){
            player.sendMessage(Texts.of(TextColors.RED + "You do not have an accepted bounty!"));
        }
        BountyUtil.getUtil().accepted.remove(p.getName(), name);
    }
    /*
    TODO: Add location functionality.
    public void locatePlayer(Player p){



    }*/

}
