package com.huntersharpe.BountyHunter;

import com.google.inject.Inject;
import com.huntersharpe.BountyHunter.EconAPI.EconManager;
import com.huntersharpe.BountyHunter.Util.BountyHandler;
import com.huntersharpe.BountyHunter.Util.BountyUtil;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
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

    private TextBuilder builder = Texts.builder();

    private final Text prefix = Texts.builder("[").color(TextColors.DARK_GRAY)
            .append(Texts.builder("Bounty").color(TextColors.BLUE).build())
            .append(Texts.builder("]").color(TextColors.DARK_GRAY).build())
            .build();

    //private final String prefix = String.format("%s[%sBountyHunter%s] ", TextColors.DARK_GRAY, TextColors.BLUE, TextColors.DARK_GRAY);

    public void sendHelp(Player p){
        Text text = Texts.builder("Incorrect Usage: Use - /bounty help - for help.").color(TextColors.RED).build();
        p.sendMessage(text);
    }

    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException{
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can use Bounty Hunter commands."));
            return CommandResult.success();
        }
        Player p = (Player) src;
        String[] args = arguments.toString().split(" ");
        if(p.hasPermission("bountyhunter.use")){
            Text text = Texts.builder("You do not have permission!").color(TextColors.RED).build();
            p.sendMessage(text);
            return CommandResult.success();
        }
        if(!args[0].equalsIgnoreCase("help")
                || !args[0].equalsIgnoreCase("view")
                || !args[0].equalsIgnoreCase("accept")
                || !args[0].equalsIgnoreCase("abandon")
                || !args[0].equalsIgnoreCase("place")
                || !args[0].equalsIgnoreCase("cancel")
                || args.length > 3
                || args.length == 0){
            sendHelp(p);
            return CommandResult.success();
        } else {
            if(args[0].equalsIgnoreCase("help")){
                Text header = Texts.builder("--- ").color(TextColors.GRAY)
                        .append(Texts.builder("Bounty Hnter Help ").color(TextColors.AQUA).build())
                        .append(Texts.builder("---").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(header);
                Text help = Texts.builder("/bounty help ").color(TextColors.BLUE)
                        .append(Texts.builder("- Displays this menu.").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(help);
                Text view = Texts.builder("/bounty view ").color(TextColors.BLUE)
                        .append(Texts.builder("- View current open bounties.").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(view);
                Text accept = Texts.builder("/bounty accept <player> ").color(TextColors.BLUE)
                        .append(Texts.builder("- Accept a bounty on a player.").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(accept);
                Text abandon = Texts.builder("/bounty abandon <player> ").color(TextColors.BLUE)
                        .append(Texts.builder("- Abandon your current accepted bounty").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(abandon);
                Text place = Texts.builder("/bounty <place|add> <player> <value> ").color(TextColors.BLUE)
                        .append(Texts.builder("- Place a bounty on a player.").build())
                        .build();
                p.sendMessage(place);
                Text remove = Texts.builder("/bounty <remove|cancel> <player> ").color(TextColors.GRAY)
                        .append(Texts.builder("- Cancel a bounty you have placed.").color(TextColors.GRAY).build())
                        .build();
                p.sendMessage(remove);
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("view")){
                if(args.length != 1){
                    sendHelp(p);
                    return CommandResult.success();
                }
                Text text = Texts.of(prefix, TextColors.AQUA, "Available Bounties: ");
                p.sendMessage(text);
                Iterator iterator = BountyUtil.getUtil().availBounty.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry)iterator.next();
                    Text entries = Texts.of(
                            prefix,
                            TextColors.AQUA, String.valueOf(pair.getKey()), " for $",
                            TextColors.WHITE, String.valueOf(pair.getValue())
                    );
                    p.sendMessage(entries);
                    iterator.remove(); // avoids a ConcurrentModificationException
                }
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("accept")){
                if(args.length != 2) {
                    sendHelp(p);
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                BountyHandler.getPlugin().acceptBounty(target, p.getName());
                EconManager.setBalance(p.getName(), EconManager.getBalance(p.getName()) - 25D);
                Text text = Texts.of(
                        prefix,
                        TextColors.GREEN,
                        "You have accepted the bounty on player: " + target.getName()
                );
                p.sendMessage(text);
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("abandon")){
                if(acceptedBounties.containsValue(p.getName())){
                    Text text = Texts.of(TextColors.RED, "You already have a current bounty!");
                    p.sendMessage(text);
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                BountyHandler.getPlugin().abadonBounty(target, p.getName());
                double deduction = BountyHunter.getInstance().getConfigNode().getNode((Object[]) "root.economy.deduction-value".split("\\.")).getDouble();
                EconManager.setBalance(p.getName(), EconManager.getBalance(p.getName()) - deduction);
                Text text = Texts.of(
                        prefix,
                        TextColors.GREEN,
                        "You have abandoned the bounty on player: "
                                + target.getName()
                                + ". $"
                                + String.valueOf(deduction)
                                + " has been removed from your account."
                );
                p.sendMessage(text);
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("place") || args[0].equalsIgnoreCase("add")){
                if(args.length != 3){
                    sendHelp(p);
                    return CommandResult.success();
                }
                if(!game.getServer().getPlayer(args[1]).isPresent()){
                    Text text = Texts.of(
                            prefix,
                            TextColors.RED,
                            "Player is not online!"
                    );
                    p.sendMessage(text);
                    return CommandResult.success();
                }
                Player target = (Player) game.getServer().getPlayer(args[1]);
                if(BountyUtil.getUtil().hasBounty(args[1])){
                    Text text = Texts.of(
                            prefix,
                            TextColors.RED,
                            "Player already has a bounty!"
                    );
                    p.sendMessage(text);
                    return CommandResult.success();
                }
                if(target.hasPermission("bountyhunter.exempt")){
                    p.sendMessage(Texts.of(prefix, TextColors.RED, "You cannot set a bounty on that player!"));
                    return CommandResult.success();
                }
                if(BountyHandler.getPlugin().placedBounties.get(p.getName()).equals(1)){
                    p.sendMessage(Texts.of(prefix, TextColors.RED, "You have a current bounty set you cannot set more then one."));
                    return CommandResult.success();
                }
                BountyUtil.getUtil().setBounty(args[1], Double.parseDouble(args[2]), p.getName());
                p.sendMessage(Texts.of(prefix, TextColors.GREEN, "You have successfully placed a bounty on player: " + args[1] + " for " + args[2]));
                MessageSink broadcastMessage = MessageSinks.toAll();
                broadcastMessage.sendMessage(Texts.of(prefix, TextColors.GREEN, p.getName() + " has placed a bounty on player:" + args[1] + " for: $" + args[2] + ". Type /bounty accept <player> to accept."));
                return CommandResult.success();
            }
            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("cancel")){
                if(args.length != 3){
                    sendHelp(p);
                    return CommandResult.success();
                }
                if(!game.getServer().getPlayer(args[1]).isPresent()){
                    p.sendMessage(Texts.of(prefix, TextColors.RED, "Player is no longer online!"));
                    return CommandResult.success();
                }
                if(BountyUtil.getUtil().hasBounty(args[1])){
                    p.sendMessage(Texts.of(prefix, TextColors.RED, "Player does not have an existing bounty!"));
                    return CommandResult.success();
                }
                if(BountyUtil.getUtil().bounty.containsKey(p.getName()) && BountyUtil.getUtil().bounties.get(args[1]).equals(p.getName())){
                    p.sendMessage(Texts.of(prefix, TextColors.RED, "You did not set this bounty"));
                    return CommandResult.success();
                }
                BountyUtil.getUtil().removeBounty(args[1], p.getName(), Double.parseDouble(args[2]));
                p.sendMessage(Texts.of(prefix, TextColors.GREEN, "You have successfully removed their bounty."));
                return CommandResult.success();
            }
            return CommandResult.success();
        }
    }
}