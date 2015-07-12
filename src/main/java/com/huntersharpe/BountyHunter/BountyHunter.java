package com.huntersharpe.BountyHunter;

import com.google.inject.Inject;
import com.huntersharpe.BountyHunter.EconAPI.EconAPI;
import com.huntersharpe.BountyHunter.EconAPI.command.Balance;
import com.huntersharpe.BountyHunter.EconAPI.command.Econ;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Hunter Sharpe on 7/9/15.
 */
@Plugin(id = "bountyhunter", name = "BountyHunter", version = "1.0")
public class BountyHunter {

    @Inject
    public Game game;

    @Inject
    public Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public File defaultConfig;

    //On first run with return to file that doesn't exist.
    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;

    public static BountyHunter plugin;

    public static BountyHunter getInstance(){
        return plugin;
    }

    public File getConfig(){
        return defaultConfig;
    }

    public ConfigurationLoader getConfigLoader(){
        return configManager;
    }

    public ConfigurationNode config = null;

    public void loadConfig() {

        try{
            if(!defaultConfig.exists()){
                defaultConfig.createNewFile();
                config = configManager.load();

                config.getNode("version").setValue(1);
                config.getNode("allow-admin-bounty").setValue(false);
                configManager.save(config);
            }
        } catch (IOException exception){
            logger.log(Level.SEVERE, "The default configuration could not be loaded or created!");
        }

    }

    //Bounty Command

    CommandSpec helpSpec = CommandSpec.builder()
            .permission("bountyhunter.use")
            .description(Texts.of("View command help."))
            .executor(new BountyCommand())
            .build();
    CommandSpec viewSpec = CommandSpec.builder()
            .permission("bountyhunter.use")
            .description(Texts.of("View all current bounties."))
            .executor(new BountyCommand())
            .build();
    CommandSpec acceptSpec = CommandSpec.builder()
            .permission("bountyhunter.use.accept")
            .description(Texts.of("Accept someones bounty."))
            .executor(new BountyCommand())
            .build();
    CommandSpec abandonSpec = CommandSpec.builder()
            .permission("bountyhunter.use.abandon")
            .description(Texts.of("Abandon your current accepted bounty."))
            .executor(new BountyCommand())
            .build();
    CommandSpec placeSpec = CommandSpec.builder()
            .permission("bountyhunter.use.place")
            .description(Texts.of("Place a bounty on someones head."))
            .executor(new BountyCommand())
            .build();
    CommandSpec removeSpec = CommandSpec.builder()
            .permission("bountyhunter.use.remove")
            .description(Texts.of("Remove a bounty you had set on someone."))
            .executor(new BountyCommand())
            .build();

    CommandSpec bountyCommandSpec = CommandSpec.builder()
            .permission("bountyhunter.use")
            .description(Texts.of("Basic Bounty Hunter command"))
            .executor(new BountyCommand())
            .child(helpSpec, "help")
            .child(viewSpec, "view")
            .child(acceptSpec, "accept")
            .child(abandonSpec, "abandon")
            .child(placeSpec, "place", "add")
            .child(removeSpec, "remove", "cancel")
            .build();

    //Economy Command
    CommandSpec setBalCmd = CommandSpec.builder()
            .permission("bountyhunter.eco.admin")
            .description(Texts.of("Set a players balance"))
            .executor(new Econ())
            .build();
    CommandSpec addBalCmd = CommandSpec.builder()
            .permission("bountyhunter.eco.admin")
            .description(Texts.of("Add a certain amount of cash to a players balance."))
            .executor(new Econ())
            .build();
    CommandSpec removeBalCmd = CommandSpec.builder()
            .permission("bountyhunter.eco.admin")
            .description(Texts.of("Remove a certain amount of cash from a players balance."))
            .executor(new Econ())
            .build();
    CommandSpec getBalCmd = CommandSpec.builder()
            .permission("bountyhunter.eco.admin")
            .description(Texts.of("Get a players balance."))
            .executor(new Econ())
            .build();
    CommandSpec topBalCmd = CommandSpec.builder()
            .permission("bountyhunter.eco")
            .description(Texts.of("Get the balance of the richest person."))
            .executor(new Econ())
            .build();
    CommandSpec ecoCommandSpec = CommandSpec.builder()
            .description(Texts.of("BountyHunter Economy BountyCommand"))
            .permission("bountyhunter.eco")
            .executor(new Econ())
            .child(setBalCmd, "set")
            .child(addBalCmd, "add")
            .child(removeBalCmd, "remove")
            .child(getBalCmd, "balance")
            .child(topBalCmd, "top")
            .build();

    //Balance command

    CommandSpec balCommandSpec = CommandSpec.builder()
            .permission("bountyhunter.eco")
            .description(Texts.of("Get your balance"))
            .executor(new Balance())
            .build();

    public ConfigurationNode getConfigNode(){
        return config;
    }

    public Logger getLogger(){
        return logger;
    }

    @Subscribe
    public void onServerStart(ServerStartingEvent e){
        loadConfig();
        game.getCommandDispatcher().register(this, ecoCommandSpec, "eco", "economy");
        game.getCommandDispatcher().register(this, balCommandSpec, "bal", "balance");
        game.getCommandDispatcher().register(this, bountyCommandSpec, "bounty");
    }

    @Subscribe
    public void onServerStopping(ServerStoppingEvent e){
        EconAPI.saveBalances();
    }

}
