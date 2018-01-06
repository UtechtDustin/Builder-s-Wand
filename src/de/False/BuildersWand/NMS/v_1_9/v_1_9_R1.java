package de.False.BuildersWand.NMS.v_1_9;

import de.False.BuildersWand.NMS.NMS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class v_1_9_R1 implements NMS
{
    @Override
    public void spawnParticle(String particle, Location location){
        location.getWorld().spawnParticle(Particle.valueOf(particle), location, 1);
    }

    @Override
    public ItemStack getItemInHand(Player player)
    {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public boolean isMainHand(PlayerInteractEvent event)
    {
        return false;
    }

    @Override
    public String getDefaultParticle()
    {
        return Effect.COLOURED_DUST.toString();
    }
}