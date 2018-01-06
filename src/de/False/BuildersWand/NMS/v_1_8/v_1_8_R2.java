package de.False.BuildersWand.NMS.v_1_8;

import de.False.BuildersWand.NMS.NMS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class v_1_8_R2 implements NMS
{
    @Override
    public void spawnParticle(String particle, Location location){
        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();
        location.getWorld().playEffect(location, Effect.valueOf(particle), 0);
    }

    @Override
    public ItemStack getItemInHand(Player player)
    {
        return player.getItemInHand();
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