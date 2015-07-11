package com.huntersharpe.BountyHunter.Util;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerDeathEvent;

/**
 * Created by Hunter Sharpe on 7/10/15.
 */
public class Events {

    public static Events events;

    public static Events getEvents(){
        return events;
    }



    @Subscribe
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getUser();
        if(!(e.getSource() instanceof Player)){
            return;
        }
        if(!(BountyUtil.getUtil().hasBounty(p.getName()))){
            return;
        }
        if(!BountyUtil.getUtil().accepted.get(p.getName()).equals(e.getSource().getName())){
            return;
        }
        double value = BountyUtil.getUtil().availBounty.get(p.getName());

        //TODO: Add functionality

    }
}
