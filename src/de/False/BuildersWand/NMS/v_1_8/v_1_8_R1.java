package de.False.BuildersWand.NMS.v_1_8;

import de.False.BuildersWand.NMS.NMS;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class v_1_8_R1 implements NMS
{
    @Override
    public void spawnParticle(String particle, Location location){
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
        return true;
    }

    @Override
    public String getDefaultParticle()
    {
        return Effect.COLOURED_DUST.toString();
    }

    @Override
    public void addShapelessRecipe(List<String> recipeStrings, HashMap<String, Material> ingredients, ItemStack resultItemStack)
    {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(resultItemStack);
        for (Map.Entry<String, Material> entry: ingredients.entrySet())
        {
            String materialShortcut = entry.getKey();
            Material material = entry.getValue();
            StringBuilder fullString = new StringBuilder();

            for (String string: recipeStrings)
            {
                fullString.append(string);
            }

            int itemCount = StringUtils.countMatches(fullString.toString(),materialShortcut);
            shapelessRecipe.addIngredient(itemCount, material);
        }

        Bukkit.getServer().addRecipe(shapelessRecipe);
    }

    @Override
    public void addShapedRecipe(List<String> recipeStrings, HashMap<String, Material> ingredients, ItemStack resultItemStack)
    {
        ShapedRecipe shapedRecipe = new ShapedRecipe(resultItemStack);
        shapedRecipe.shape(recipeStrings.toArray(new String[recipeStrings.size()]));
        for (Map.Entry<String, Material> entry: ingredients.entrySet())
        {
            String materialShortcut = entry.getKey();
            Material material = entry.getValue();
            shapedRecipe.setIngredient(materialShortcut.charAt(0), material);
        }

        Bukkit.getServer().addRecipe(shapedRecipe);
    }

    @Override
    public ItemStack setTag(ItemStack itemStack, String path, String value)
    {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsStack.setTag(compound);
            compound = nmsStack.getTag();
        }

        compound.setString(path, value);
        nmsStack.setTag(compound);
        itemStack = CraftItemStack.asBukkitCopy(nmsStack);

        return itemStack;
    }

    @Override
    public String getTag(ItemStack itemStack, String path)
    {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsStack.setTag(compound);
            compound = nmsStack.getTag();
        }

        if(!compound.hasKey(path))
        {
            return null;
        }

        return compound.getString(path);
    }
}