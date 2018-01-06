package de.False.BuildersWand.NMS.v_1_9;

import de.False.BuildersWand.NMS.NMS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class v_1_9_R1 implements NMS
{
    @Override
    public void spawnParticle(String particle, Location location){
        location.getWorld().spawnParticle(Particle.valueOf(particle), location.getX(), location.getY(), location.getZ(), 0, 128, 0, 0, 10);
    }

    @Override
    public ItemStack getItemInHand(Player player)
    {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public boolean isMainHand(EquipmentSlot equipmentSlot)
    {
        return equipmentSlot == EquipmentSlot.HAND;
    }

    @Override
    public String getDefaultParticle()
    {
        return Particle.REDSTONE.toString();
    }
}