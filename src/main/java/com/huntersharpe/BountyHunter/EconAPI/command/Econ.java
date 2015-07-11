package com.huntersharpe.BountyHunter.EconAPI.command;

import com.huntersharpe.BountyHunter.EconAPI.EconManager;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 * Created by Hunter Sharpe on 7/9/15.
 */
public class Econ implements CommandExecutor{

    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException{
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can run Bounty Hunter commands!"));
        }
        Player p = (Player) src;
        if(!p.hasPermission("bountyhunter.eco")){
            p.sendMessage(Texts.of(TextColors.RED + "You do not have permission!"));
        }
        String[] args = arguments.toString().split(" ");
        if(args.length >= 4 || args.length == 0){
            p.sendMessage(Texts.of(TextColors.GREEN+"Usage: /eco <add/remove/set/balance> [player] [amount]"));
        }
        if(args[0].equalsIgnoreCase("add")){
            if(!(p.hasPermission("bountyhunter.eco.admin"))){
                p.sendMessage(Texts.of(TextColors.RED + "You do not have permission!"));
            }
            if(args.length != 3){
                p.sendMessage(Texts.of(TextColors.RED + "BountyCommand Usage: /eco add <player> <amount>"));
            }
            if(!EconManager.hasAccount(args[1])){
                p.sendMessage(Texts.of(TextColors.RED + "Error: Player does not have an account!"));
            }
            double amount = 0;
            try{
                amount = Double.parseDouble(args[2]);
            } catch (Exception e){
                p.sendMessage(Texts.of(TextColors.RED + "You have to add a valid number."));
            }
            EconManager.setBalance(args[1], EconManager.getBalance(args[1]) + amount);
            p.sendMessage(Texts.of(TextColors.GREEN + "Added $" + amount + " to player: " + args[1] + "'s balance."));
        }else if (args[0].equalsIgnoreCase("remove")){
            if(!p.hasPermission("bountyhunter.eco.admin")){
                p.sendMessage(Texts.of(TextColors.RED + "You cannot do this!"));
            }
            if(args.length != 3){
                p.sendMessage(Texts.of(TextColors.RED + "BountyCommand Usage: /eco remove <player> <amount>"));
            }
            if(!EconManager.hasAccount(args[1]))
            {
                p.sendMessage(Texts.of(TextColors.RED + "Error: Player does not have an account"));
            }
            double amount = 0;
            try
            {
                amount = Double.parseDouble(args[2]);
            }catch (Exception e)
            {
                p.sendMessage(Texts.of(TextColors.RED + "You have to input a valid number."));
            }
            p.sendMessage(Texts.of(TextColors.RED + "Deducted " + amount + " from player: " + args[1] + "'s balance."));
            EconManager.setBalance(args[1], EconManager.getBalance(args[1]) - amount);
        }else if(args[0].equalsIgnoreCase("set")){
            if(!p.hasPermission("bountyhunter.eco.admin")){
                p.sendMessage(Texts.of(TextColors.RED + "You cannot do this!"));
            }
            if(args.length != 3){
                p.sendMessage(Texts.of(TextColors.RED + "BountyCommand Usage: /eco set <player> <amount>"));
            }
            if(!EconManager.hasAccount(args[1]))
            {
                p.sendMessage(Texts.of(TextColors.RED + "Error: Player does not have an account"));
            }
            double amount = 0;
            try
            {
                amount = Double.parseDouble(args[2]);
            }catch (Exception e)
            {
                p.sendMessage(Texts.of(TextColors.RED + "You have to input a valid number."));
            }
            p.sendMessage(Texts.of(TextColors.GREEN + "Set player: " + args[1] + "'s balance to " + amount));
            EconManager.setBalance(args[1], amount);
        }else if(args[0].equalsIgnoreCase("balance")){
            if(!(p.hasPermission("bountyhunter.eco.admin"))){

            }
            if(args.length >= 3){
                p.sendMessage(Texts.of("BountyCommand Usage: /eco balance <player>"));
            }
            if(args.length == 2){
                double balance = EconManager.getBalance(args[1]);
                p.sendMessage(Texts.of(TextColors.BLUE + args[1] + "'s" + TextColors.DARK_GRAY +"balance is: " + TextColors.GREEN + balance));
            }
            double balance = EconManager.getBalance(p.getName());
            p.sendMessage(Texts.of(TextColors.DARK_GRAY + "[" + TextColors.BLUE + "Balance" + TextColors.DARK_GRAY + "] " + TextColors.DARK_GRAY + "Their current balance is: " + TextColors.GREEN + balance));
        }else if(args[0].equalsIgnoreCase("top")){
            if(!(p.hasPermission("bountyhunter.eco"))){
                p.sendMessage(Texts.of(TextColors.RED + "You cannot do this!"));
            }
            EconManager.getTopBal(p);
        }else {
            p.sendMessage(Texts.of(TextColors.RED + "Incorrect argument"));
        }
        return CommandResult.success();
    }

}
