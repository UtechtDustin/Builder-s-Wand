package de.False.BuildersWand.manager;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WandManager
{
    private File file;
    private FileConfiguration config;
    private List<Wand> wandList = new ArrayList<>();
    private NMS nms;

    public WandManager(Main plugin, NMS nms)
    {
        this.file = new File(plugin.getDataFolder(), "wands.yml");
        this.nms = nms;
    }

    private void loadWands()
    {
        wandList.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("wands");

        for (String key : configurationSection.getKeys(false))
        {
            wandList.add(getWand(key));
        }

        registerRecipes();
    }

    private Wand getWand(String key)
    {
        String configPrefix = "wands." + key + ".";
        Wand wand = new Wand(nms);
        wand.setName(MessageUtil.colorize(config.getString(configPrefix + "name")));
        wand.setMaterial(Material.valueOf(config.getString(configPrefix + "material")));
        wand.setMaxSize(config.getInt(configPrefix + "maxSize"));
        wand.setConsumeItems(config.getBoolean(configPrefix + "consumeItems"));
        wand.setDurability(config.getInt(configPrefix + "durability.amount"));
        wand.setDurabilityEnabled(config.getBoolean(configPrefix + "durability.enabled"));
        wand.setDurabilityText(config.getString(configPrefix + "durability.text"));
        wand.setCraftingEnabled(config.getBoolean(configPrefix + "crafting.enabled"));
        wand.setCraftingShapeless(config.getBoolean(configPrefix + "crafting.shapeless"));
        wand.setCraftingRecipe(config.getStringList(configPrefix +"crafting.recipe"));
        wand.setBlacklist(config.getStringList(configPrefix +"blacklist"));
        wand.setWhitelist(config.getStringList(configPrefix +"whitelist"));
        wand.setIngredient(getIngredientList(key));
        wand.setParticleEnabled(config.getBoolean(configPrefix + "particles.enabled"));
        wand.setParticle(config.getString(configPrefix + "particles.type"));
        wand.setParticleCount(config.getInt(configPrefix + "particles.count"));
        wand.setInventoryEnabled(config.getBoolean(configPrefix + "storage.enabled"));
        wand.setInventorySize(config.getInt(configPrefix + "storage.size"));
        wand.setCustomModelData(config.getInt(configPrefix + "customModelData", -1));

        if(config.isSet(configPrefix + "permission"))
        {
            wand.setPermission(config.getString(configPrefix + "permission"));
        }
        return wand;
    }

    public Wand getWand(ItemStack itemStack){
        for (Wand wand : wandList)
        {
            if(itemStack == null)
            {
                return null;
            }

            Material material = itemStack.getType();
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(material == null || itemMeta == null)
            {
                return null;
            }

            String name = itemMeta.getDisplayName();
            if(wand.getName().equals(name) && material == wand.getMaterial())
            {
                return wand;
            }
        }
        return null;
    }

    private void addDefault()
    {
        String configPrefix = "wands.1.";
        config.options().copyDefaults(true);
        config.addDefault(configPrefix + "name", "&3Builders Wand");
        config.addDefault(configPrefix + "material", "BLAZE_ROD");
        config.addDefault(configPrefix + "maxSize", 8);
        config.addDefault(configPrefix + "consumeItems", true);
        config.addDefault(configPrefix + "durability.amount", 130);
        config.addDefault(configPrefix + "durability.enabled", true);
        config.addDefault(configPrefix + "durability.text", "&5Durability: &e{durability}");
        List<String> recipeList = new ArrayList<>();
        recipeList.add("xxd");
        recipeList.add("xbx");
        recipeList.add("bxx");
        ConfigurationSection configurationSection = config.getConfigurationSection(configPrefix + "crafting.ingredient");
        if(configurationSection == null || configurationSection.getKeys(false).size() <= 0)
        {
            config.addDefault(configPrefix + "crafting.ingredient.d", "DIAMOND");
            config.addDefault(configPrefix + "crafting.ingredient.b", "BLAZE_ROD");
        }

        config.addDefault(configPrefix + "crafting.enabled", true);
        config.addDefault(configPrefix + "crafting.shapeless", false);
        config.addDefault(configPrefix + "crafting.recipe", recipeList);
        config.addDefault(configPrefix + "particles.enabled", true);
        config.addDefault(configPrefix + "particles.type", nms.getDefaultParticle());
        config.addDefault(configPrefix + "particles.count", 3);
        config.addDefault(configPrefix + "storage.enabled", true);
        config.addDefault(configPrefix + "storage.size", 27);
        save();
    }

    public void load()
    {
        config = YamlConfiguration.loadConfiguration(file);
        addDefault();
        loadWands();
    }

    private HashMap<String,Material> getIngredientList(String key)
    {
        String configPrefix = "wands." + key + ".";
        HashMap<String,Material> ingredientList = new HashMap<>();

        ConfigurationSection configurationSection = config.getConfigurationSection(configPrefix + "crafting.ingredient");
        for (String ingredientShortcut : configurationSection.getKeys(false))
        {
            String materialString = config.getString(configPrefix + "crafting.ingredient." + ingredientShortcut);
            ingredientList.put(ingredientShortcut, Material.valueOf(materialString));
        }

        return ingredientList;
    }

    private void registerRecipes()
    {
        for (Wand wand: wandList)
        {
            boolean enabled = wand.isCraftingEnabled();

            if(!enabled)
            {
                return;
            }

            boolean shapeless = wand.isCraftingShapeless();
            List<String> recipeStrings = wand.getCraftingRecipe();
            HashMap<String, Material> ingredients = wand.getIngredient();
            ItemStack resultItemStack = wand.getRecipeResult();
            if(shapeless)
            {
                nms.addShapelessRecipe(recipeStrings, ingredients, resultItemStack);
            }
            else
            {
                nms.addShapedRecipe(recipeStrings, ingredients, resultItemStack);
            }
        }
    }

    public Wand getWandTier(int tier)
    {
        if(config.isSet("wands." + tier))
        {
            return getWand(tier + "");
        }

        return null;
    }

    private void save()
    {
        try
        {
            config.save(file);
            config.load(file);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isWand(ItemStack itemStack)
    {
        return getWand(itemStack) != null;
    }
}
