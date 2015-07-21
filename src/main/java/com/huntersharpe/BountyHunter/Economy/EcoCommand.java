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

/**
 * Created by Hunter Sharpe on 7/20/15.
 */
public class EcoCommand implements CommandExecutor {

    private static Text prefix = Texts.of(TextColors.DARK_GRAY, "[", TextColors.BLUE + "BountyHunter", TextColors.DARK_GRAY, "] ");


    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException{
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can use the Economy"));
            return CommandResult.success();
        }
        Player player = (Player) ((Player) src).getPlayer();
        String[] args = arguments.toString().split(" ");
        if(args.length > 2 || args.length == 0){
            player.sendMessage(Texts.of(prefix, TextColors.RED, "Incorrect Usage!"));
            return CommandResult.success();
        }else{
            if(args[0].equalsIgnoreCase("set")){
                String name = args[1];
                double value = Double.parseDouble(args[2]);
                if(!Account.getInstance().hasAccount(name)){
                    player.sendMessage(Texts.of(prefix, TextColors.RED, "Player does not have an account."));
                    return CommandResult.success();
                }else{
                    double oldBalance = Account.getInstance().getAccount(name);
                    new Account(name, value);
                    Util.getInstance().saveAccounts();
                    player.sendMessage(Texts.of(prefix, TextColors.GREEN, "Set user: " + name + "'s balance to " + value));
                }
            }
            if(args[0].equalsIgnoreCase("add")){
                String name = args[1];
                double value = Double.parseDouble(args[2]);
                if(!Account.getInstance().hasAccount(name)){
                    player.sendMessage(Texts.of(prefix, TextColors.RED, "Player does not have an account."));
                    return CommandResult.success();
                }else{
                    double oldBalance = Account.getInstance().getAccount(name);
                    double newBalance = oldBalance + value;
                    new Account(name, newBalance);
                    Util.getInstance().saveAccounts();
                    player.sendMessage(Texts.of(prefix, TextColors.GREEN, "Added: " + newBalance + " to user: " + name + "'s account."));
                }
            }
            if(args[0].equalsIgnoreCase("remove")){
                String name = args[1];
                double value = Double.parseDouble(args[2]);
                if(!Account.getInstance().hasAccount(name)){
                    player.sendMessage(Texts.of(prefix, TextColors.RED, "Player does not have an account."));
                    return CommandResult.success();
                }else{
                    //TODO: Handle negative balances.
                    double oldBalance = Account.getInstance().getAccount(name);
                    double newBalance = oldBalance - value;
                    new Account(name, newBalance);
                    Util.getInstance().saveAccounts();
                    player.sendMessage(Texts.of(prefix, TextColors.GREEN, "Removed: " + newBalance + " from user: " + name + "'s account."));
                    return CommandResult.success();
                }
            }
            if(args[0].equalsIgnoreCase("bal")){
                //TODO: Get player balance
            }
        }
        return CommandResult.success();
    }

}
