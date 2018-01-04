package de.False.BuildersWand.ConfigurationFiles;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
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
    private File file;
    private FileConfiguration config;

    private String name;
    private Material material;

    private boolean craftingEnabled;
    private boolean craftingShapeless;
    private List<String> craftingRecipe;
    private HashMap<String, Material> ingredient = new HashMap<String, Material>();

    private boolean particleEnabled;
    private Particle particle;
    private int particleCount;

    private boolean consumeItems;
    private int maxSize;

    public Config(Main plugin)
    {
        this.plugin = plugin;
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
        particle = Particle.valueOf(config.getString("particles.type"));
        particleCount = config.getInt("particles.count");
    }

    private void addDefaults()
    {
        config.addDefault("wand.name", "&3Builders Wand");
        config.addDefault("wand.material", "BLAZE_ROD");
        config.addDefault("wand.maxSize", 8);
        config.addDefault("wand.consumeItems", true);

        List<String> recipeList = new ArrayList<>();
        recipeList.add("xxd");
        recipeList.add("xbx");
        recipeList.add("bxx");
        config.addDefault("crafting.enabled", true);
        config.addDefault("crafting.shapeless", true);
        config.addDefault("crafting.recipe", recipeList);

        config.addDefault("particles.enabled", true);
        config.addDefault("particles.type", "REDSTONE");
        config.addDefault("particles.count", 3);

        save();
    }

    public void copyDefaultLocales()
    {
        plugin.saveResource("config.yml", false);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public void setCraftingEnabled(boolean craftingEnabled)
    {
        this.craftingEnabled = craftingEnabled;
    }

    public boolean isCraftingShapeless()
    {
        return craftingShapeless;
    }

    public void setCraftingShapeless(boolean craftingShapeless)
    {
        this.craftingShapeless = craftingShapeless;
    }

    public List<String> getCraftingRecipe()
    {
        return craftingRecipe;
    }

    public void setCraftingRecipe(List<String> craftingRecipe)
    {
        this.craftingRecipe = craftingRecipe;
    }

    public HashMap<String, Material> getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(HashMap<String, Material> ingredient)
    {
        this.ingredient = ingredient;
    }

    public boolean isParticleEnabled()
    {
        return particleEnabled;
    }

    public void setParticleEnabled(boolean particleEnabled)
    {
        this.particleEnabled = particleEnabled;
    }

    public Particle getParticle()
    {
        return particle;
    }

    public void setParticle(Particle particle)
    {
        this.particle = particle;
    }

    public int getParticleCount()
    {
        return particleCount;
    }

    public void setParticleCount(int particleCount)
    {
        this.particleCount = particleCount;
    }

    public boolean isConsumeItems()
    {
        return consumeItems;
    }

    public void setConsumeItems(boolean consumeItems)
    {
        this.consumeItems = consumeItems;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }
}