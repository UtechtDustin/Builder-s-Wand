package de.False.BuildersWand.version;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class v_1_13_R2 implements de.False.BuildersWand.version.NMS
{
    private JavaPlugin plugin;
    private Random random;

    public v_1_13_R2(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.random = new Random();
    }

    @Override
    public void spawnParticle(String particle, Location location){
        location.getWorld().spawnParticle(Particle.valueOf(particle), location.getX(), location.getY(), location.getZ(), 0, 128, 0, 0, 10);
    }

    @Override
    public void spawnParticle(String particle, Location location, Player player)
    {
        player.spawnParticle(Particle.valueOf(particle), location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0);
    }

    @Override
    public ItemStack getItemInHand(Player player)
    {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public boolean isMainHand(PlayerInteractEvent event)
    {
        return event.getHand() == EquipmentSlot.HAND;
    }

    @Override
    public String getDefaultParticle()
    {
        return Particle.FLAME.toString();
    }

    @Override
    public void addShapelessRecipe(List<String> recipeStrings, HashMap<String, Material> ingredients, ItemStack resultItemStack)
    {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "buildersWand" + random.nextInt());
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(namespacedKey, resultItemStack);
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
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "buildersWand" + random.nextInt());
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, resultItemStack);
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
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
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
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
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

    public Block setBlockData(Block against, Block SelectionBlock) {
        return SelectionBlock;
    }

    @Override
    public List<Material> getAirMaterials() {
        List<Material> airBlocks = new ArrayList<Material>();
        airBlocks.add(Material.AIR);
        airBlocks.add(Material.CAVE_AIR);

        return airBlocks;
    }
}