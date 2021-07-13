package de.False.BuildersWand.Updater;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.ConfigurationFiles.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotification implements Listener {

    private Config config;
    private Main plugin;
    private Update update;
    public UpdateNotification(Main plugin, Config config, Update update){
        this.plugin = plugin;
        this.config = config;
        this.update = update;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("buildersWand.updateNotification")) return;
        update.sendUpdateMessage(player);
    }
}
