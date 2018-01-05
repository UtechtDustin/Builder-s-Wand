package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.ConfigurationFiles.Locales;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.utilities.Metrics;
import de.False.BuildersWand.utilities.SpigetUpdate;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Main extends JavaPlugin
{
    private Locales locales = new Locales(this);
    private Config config = new Config(this);

    @Override
    public void onEnable()
    {
        loadConfigFiles();
        registerEvents();
        registerCommands();
        loadRecipes();
        loadMetrics();
        checkForUpdate();
    }

    private void checkForUpdate()
    {
        SpigetUpdate updater = new SpigetUpdate(this, 51577);
        updater.setVersionComparator(VersionComparator.SEM_VER);
        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                if (hasDirectDownload) {
                    if (updater.downloadUpdate()) {
                    } else {
                        getLogger().warning("Update download failed, reason is " + updater.getFailReason());
                    }
                }
            }

            @Override
            public void upToDate() {
            }
        });
    }

    private void loadMetrics()
    {
        Metrics metrics = new Metrics(this);

        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("servers", 1);
                valueMap.put("players", Bukkit.getOnlinePlayers().size());
                return valueMap;
            }
        }));
    }

    private void registerCommands()
    {
        getCommand("bw").setExecutor(new Commands(config));
    }

    private void registerEvents()
    {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Wand(this, config), this);
    }

    private void loadConfigFiles()
    {
        locales.load();
        config.load();
    }

    private void loadRecipes()
    {
        boolean enabled = config.isCraftingEnabled();

        if(!enabled)
        {
            return;
        }

        boolean shapeless = config.isCraftingShapeless();
        List<String> recipeStrings = config.getCraftingRecipe();
        HashMap<String, Material> ingredients = config.getIngredient();
        NamespacedKey namespacedKey = new NamespacedKey(this, "buildersWand");
        ItemStack resultItemStack = Wand.getRecipeResult();
        Recipe recipe;
        if(shapeless)
        {
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

            recipe = shapelessRecipe;
        }
        else
        {
            ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, resultItemStack);
            shapedRecipe.shape(recipeStrings.toArray(new String[recipeStrings.size()]));
            for (Map.Entry<String, Material> entry: ingredients.entrySet())
            {
                String materialShortcut = entry.getKey();
                Material material = entry.getValue();
                shapedRecipe.setIngredient(materialShortcut.charAt(0), material);
            }

            recipe = shapedRecipe;
        }

        Bukkit.getServer().addRecipe(recipe);
    }
}
