package de.False.BuildersWand.Updater;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.ConfigurationFiles.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class UpdateNotification implements Listener {

    private Config config;
    private Main plugin;
    public UpdateNotification(Main plugin, Config config){
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("buildersWand.updateNotification")) return;
        try {
            new SpigotUpdater(plugin, 51577, player, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
