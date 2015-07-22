package com.huntersharpe.BountyHunter.Economy;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Map;

/**
 * Created by Hunter Sharpe on 7/20/15.
 */
public class BalCommand implements CommandExecutor{

    private static Text prefix = Texts.of(TextColors.DARK_GRAY, "[", TextColors.BLUE + "Balance", TextColors.DARK_GRAY, "] ");

    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can use Bounty Hunter."));
            return CommandResult.success();
        }
        Player player = (Player)((Player) src).getPlayer();
        String[] args = arguments.toString().split(" ");
        if(args.length == 0){
            if(!Account.getInstance().hasAccount(player.getName())){
                player.sendMessage(Texts.of(prefix, TextColors.RED, "Could not find your account!"));
                return CommandResult.success();
            }else{
                player.sendMessage(Texts.of(prefix, TextColors.GREEN, Account.getInstance().getAccount(player.getName())));
                return CommandResult.success();
            }
        } else if(args.length == 1){
            if(!args[0].equalsIgnoreCase("top")){
                player.sendMessage(Texts.of(prefix, TextColors.RED, "Incorrect Usage."));
                return CommandResult.success();
            }else{
                Map.Entry<String, Double> maxEntry = null;
                for(Map.Entry<String, Double> entry : Util.accountMap.entrySet()){
                    if(maxEntry == null || entry.getValue() > maxEntry.getValue()){
                        maxEntry = entry;
                    }
                    String maxName = maxEntry.getKey();
                    double maxValue = maxEntry.getValue();
                    player.sendMessage(Texts.of(TextColors.DARK_GRAY, "[", TextColors.BLUE, "Top Balance", TextColors.DARK_GRAY, "] ", TextColors.BLUE, maxName, TextColors.GREEN, "$", maxValue));
                }
                return CommandResult.success();
            }
        }
        return CommandResult.success();
    }

}
