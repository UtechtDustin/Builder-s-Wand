package de.False.BuildersWand.ConfigurationFiles;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config
{
    private Main plugin;
    private NMS nms;
    private File file;
    private FileConfiguration config;

    private String name;
    private Material material;

    private boolean craftingEnabled;
    private boolean craftingShapeless;
    private List<String> craftingRecipe;
    private HashMap<String, Material> ingredient = new HashMap<String, Material>();

    private boolean particleEnabled;
    private String particle;
    private int particleCount;

    private boolean consumeItems;
    private int maxSize;
    private long renderTime;

    private boolean durabilityEnabled;
    private int durability;
    private String durabilityText;

    private boolean updateNotification;
    private boolean autoDownload;

    public Config(Main plugin, NMS nms)
    {
        this.plugin = plugin;
        this.nms = nms;

        this.file = new File(plugin.getDataFolder(), "config.yml");
    }

    public void save()
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

    public void load()
    {
        config = YamlConfiguration.loadConfiguration(file);
        addDefaults();

        name = MessageUtil.colorize(config.getString("wand.name"));
        material = Material.valueOf(config.getString("wand.material"));
        maxSize = config.getInt("wand.maxSize");
        consumeItems = config.getBoolean("wand.consumeItems");
        renderTime = config.getLong("wand.renderInterval");
        durability = config.getInt("wand.durability.amount");
        durabilityEnabled = config.getBoolean("wand.durability.enabled");
        durabilityText = config.getString("wand.durability.text");

        craftingEnabled = config.getBoolean("crafting.enabled");
        craftingShapeless = config.getBoolean("crafting.shapeless");
        craftingRecipe = config.getStringList("crafting.recipe");
        ConfigurationSection configurationSection = config.getConfigurationSection("crafting.ingredient");
        for (String ingredientShortcut : configurationSection.getKeys(false))
        {
            String materialString = config.getString("crafting.ingredient." + ingredientShortcut);
            ingredient.put(ingredientShortcut, Material.valueOf(materialString));
        }

        particleEnabled = config.getBoolean("particles.enabled");
        particle = config.getString("particles.type");
        particleCount = config.getInt("particles.count");

        updateNotification = config.getBoolean("update.notification");
        autoDownload = config.getBoolean("update.autoDownload");
    }

    private void addDefaults()
    {
        config.options().copyDefaults(true);
        config.addDefault("update.notification", true);
        config.addDefault("update.autoDownload", true);
        config.addDefault("wand.name", "&3Builders Wand");
        config.addDefault("wand.material", "BLAZE_ROD");
        config.addDefault("wand.maxSize", 8);
        config.addDefault("wand.consumeItems", true);
        config.addDefault("wand.renderInterval", 2);
        config.addDefault("wand.durability.amount", 130);
        config.addDefault("wand.durability.enabled", true);
        config.addDefault("wand.durability.text", "&5Durability: &e{durability}");

        List<String> recipeList = new ArrayList<>();
        recipeList.add("xxd");
        recipeList.add("xbx");
        recipeList.add("bxx");

        ConfigurationSection configurationSection = config.getConfigurationSection("crafting.ingredient");

        if(configurationSection == null || configurationSection.getKeys(false).size() <= 0)
        {
            config.addDefault("crafting.ingredient.d", "DIAMOND");
            config.addDefault("crafting.ingredient.b", "BLAZE_ROD");
        }

        config.addDefault("crafting.enabled", true);
        config.addDefault("crafting.shapeless", false);
        config.addDefault("crafting.recipe", recipeList);

        config.addDefault("particles.enabled", true);
        config.addDefault("particles.type", nms.getDefaultParticle());
        config.addDefault("particles.count", 3);

        save();
    }

    public String getName()
    {
        return name;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public boolean isCraftingEnabled()
    {
        return craftingEnabled;
    }

    public boolean isCraftingShapeless()
    {
        return craftingShapeless;
    }

    public List<String> getCraftingRecipe()
    {
        return craftingRecipe;
    }

    public HashMap<String, Material> getIngredient()
    {
        return ingredient;
    }

    public boolean isParticleEnabled()
    {
        return particleEnabled;
    }

    public String getParticle()
    {
        return particle;
    }

    public void setParticle(String particle)
    {
        this.particle = particle;
    }

    public int getParticleCount()
    {
        return particleCount;
    }

    public boolean isConsumeItems()
    {
        return consumeItems;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public long getRenderTime()
    {
        return renderTime;
    }

    public int getDurability()
    {
        return durability;
    }

    public boolean isDurabilityEnabled()
    {
        return durabilityEnabled;
    }

    public String getDurabilityText()
    {
        return durabilityText;
    }

    public boolean getUpdateNotification()
    {
        return updateNotification;
    }

    public boolean getAutoDownload()
    {
        return autoDownload;
    }
}