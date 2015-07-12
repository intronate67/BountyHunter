package com.huntersharpe.BountyHunter;

import com.google.inject.Inject;
import com.huntersharpe.BountyHunter.EconAPI.EconManager;
import com.huntersharpe.BountyHunter.Util.BountyHandler;
import com.huntersharpe.BountyHunter.Util.BountyUtil;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.text.sink.MessageSinks;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Hunter Sharpe on 7/9/15.
 */
public class BountyCommand implements CommandExecutor{

    @Inject
    Game game;

    public Map<String, String> acceptedBounties = new HashMap<String, String>();

    private final String prefix = String.format("%s[%sBountyHunter%s] ", TextColors.DARK_GRAY, TextColors.BLUE, TextColors.DARK_GRAY);

    public void sendHelp(Player p){
        p.sendMessage(Texts.of(TextColors.RED + "Incorrect Usage: Use - /bounty help - for help."));
    }

    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException{
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can use Bounty Hunter commands."));
            return CommandResult.success();
        }
        Player p = (Player) src;
        String[] args = arguments.toString().split(" ");
        if(p.hasPermission("bountyhunter.use")){
            p.sendMessage(Texts.of(TextColors.RED + "You do not have permission!"));
            return CommandResult.success();
        }
        if(!args[0].equalsIgnoreCase("help")
                || args[0].equalsIgnoreCase("view")
                || args[0].equalsIgnoreCase("accept")
                || args[0].equalsIgnoreCase("abandon")
                || args[0].equalsIgnoreCase("place")
                || args[0].equalsIgnoreCase("cancel")
                || args.length > 3
                || args.length == 0){
            sendHelp(p);
            return CommandResult.success();
        } else {
            if(args[0].equalsIgnoreCase("help")){
                p.sendMessage(Texts.of(TextColors.GRAY
                        + "--- "
                        + TextColors.AQUA
                        + "Bounty Hunter Help"
                        + TextColors.GRAY
                        + " ---"
                ));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty help " + TextColors.GRAY + "- Displays this menu."));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty view " + TextColors.GRAY + "- View current open bounties."));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty accept <player> " + TextColors.GRAY + "- Accept a bounty on a player."));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty abandon <player> " + TextColors.GRAY + "- Abandoned your current accepted bounty."));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty <place|add> <player> <value> " + TextColors.GRAY + "- Place a bounty on a player."));
                p.sendMessage(Texts.of(TextColors.BLUE + "/bounty <remove|cancel> <player> " + TextColors.GRAY + "- Cancel a bounty you have placed."));
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("view")){
                if(args.length != 1){
                    sendHelp(p);
                    return CommandResult.success();
                }
                p.sendMessage(Texts.of(prefix + TextColors.AQUA + "Available Bounties:"));
                Iterator iterator = BountyUtil.getUtil().availBounty.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry)iterator.next();
                    p.sendMessage(Texts.of(prefix + TextColors.AQUA + pair.getKey() + " for $" + pair.getValue()));
                    iterator.remove(); // avoids a ConcurrentModificationException
                }
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("accept")){
                if(args.length != 2){
                    sendHelp(p);
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                BountyHandler.getPlugin().acceptBounty(target, p.getName());
                EconManager.setBalance(p.getName(), EconManager.getBalance(p.getName()) - 25D);
                p.sendMessage(Texts.of(prefix + "You have accepted the bounty on player: " + target.getName()));
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("abandon")){
                if(acceptedBounties.containsValue(p.getName())){
                    p.sendMessage(Texts.of(TextColors.RED + "You already have a current bounty!"));
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                BountyHandler.getPlugin().abadonBounty(target, p.getName());
                //TODO: Enable configurable deductions.
                EconManager.setBalance(p.getName(), EconManager.getBalance(p.getName()) - 25D);
                p.sendMessage(Texts.of(prefix + "You have abandoned the bounty on player: " + target.getName() + ". $25 has been removed from your account."));
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("place") || args[0].equalsIgnoreCase("add")){
                if(args.length != 3){
                    sendHelp(p);
                    return CommandResult.success();
                }
                if(!game.getServer().getPlayer(args[1]).isPresent()){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "Player is not online!"));
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                if(BountyUtil.getUtil().hasBounty(args[1])){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "Player already has a bounty!"));
                    return CommandResult.success();
                }
                if(target.hasPermission("bountyhunter.exempt")){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "You cannot set a bounty on that player!"));
                    return CommandResult.success();
                }
                if(BountyHandler.getPlugin().placedBounties.get(p.getName()).equals(1)){
                    p.sendMessage(Texts.of(prefix + "You have a current bounty set you cannot set more then one."));
                    return CommandResult.success();
                }
                BountyUtil.getUtil().setBounty(args[1], Double.parseDouble(args[2]), p.getName());
                p.sendMessage(Texts.of(prefix + TextColors.GREEN + "You have successfully placed a bounty on player: " + args[1] + " for " + args[2]));
                MessageSink broadcastMessage = MessageSinks.toAll();
                broadcastMessage.sendMessage(Texts.of(prefix + TextColors.GREEN + p.getName() + " has placed a bounty on player:" + args[1] + " for: $" + args[2] + ". Type /bounty accept <player> to accept."));
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("cancel")){
                if(args.length != 3){
                    sendHelp(p);
                    return CommandResult.success();
                }
                if(!game.getServer().getPlayer(args[1]).isPresent()){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "Player is no longer online!"));
                    return CommandResult.success();
                }
                if(BountyUtil.getUtil().hasBounty(args[1])){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "Player does not have an existing bounty!"));
                    return CommandResult.success();
                }
                if(BountyUtil.getUtil().bounty.containsKey(p.getName()) && BountyUtil.getUtil().bounties.get(args[1]).equals(p.getName())){
                    p.sendMessage(Texts.of(prefix + TextColors.RED + "You did not set this bounty"));
                    return CommandResult.success();
                }
                BountyUtil.getUtil().removeBounty(args[1], p.getName(), Double.parseDouble(args[2]));
                p.sendMessage(Texts.of(prefix + TextColors.GREEN + "You have successfully removed their bounty."));
                return CommandResult.success();
            }
            return CommandResult.success();
        }
    }

}
