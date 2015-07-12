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
public class Balance implements CommandExecutor{

    public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Texts.of("Only players can use bounty hunter commands."));
            CommandResult.success();
        }
        Player p = (Player) src;
        if(!p.hasPermission("bountyhunter.eco.bal")){
            p.sendMessage(Texts.of(TextColors.RED + "You do not have permission!"));
            return CommandResult.success();
        }
        String[] args = arguments.toString().split(" ");
        if(args.length != 0 ){
            p.sendMessage(Texts.of(String.format(
                    "%s[%sEconomy%s] BountyCommand Usage: /bal or /balance",
                    TextColors.DARK_GRAY,
                    TextColors.BLUE,
                    TextColors.DARK_GRAY,
                    TextColors.DARK_GRAY
            )));
            return CommandResult.success();
        }
        p.sendMessage(Texts.of(String.format(
                "%s[%sEconomy%s] %sYour balance is: "
                + EconManager.getBalance(p.getName()),
                TextColors.DARK_GRAY,
                TextColors.BLUE,
                TextColors.DARK_GRAY,
                TextColors.DARK_GRAY
        )));
        return CommandResult.success();
    }

}
