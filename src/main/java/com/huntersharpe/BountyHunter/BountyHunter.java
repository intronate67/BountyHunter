package com.huntersharpe.BountyHunter;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;

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
    Game game;

    @Inject
    Logger logger;

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

    public ConfigurationNode getConfigNode(){
        return config;
    }

    public Logger getLogger(){
        return logger;
    }

    @Subscribe
    public void onServerStart(ServerStartingEvent e){

    }

}
