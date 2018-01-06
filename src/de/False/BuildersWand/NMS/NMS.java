package de.False.BuildersWand.NMS;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface NMS
{
    void spawnParticle(String particle, Location location);

    ItemStack getItemInHand(Player player);

    boolean isMainHand(PlayerInteractEvent event);

    String getDefaultParticle();
}
