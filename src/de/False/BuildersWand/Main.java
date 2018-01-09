package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.ConfigurationFiles.Locales;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.NMS.v_1_10.v_1_10_R1;
import de.False.BuildersWand.NMS.v_1_11.v_1_11_R1;
import de.False.BuildersWand.NMS.v_1_12.v_1_12_R1;
import de.False.BuildersWand.NMS.v_1_8.v_1_8_R1;
import de.False.BuildersWand.NMS.v_1_8.v_1_8_R2;
import de.False.BuildersWand.NMS.v_1_8.v_1_8_R3;
import de.False.BuildersWand.NMS.v_1_9.v_1_9_R1;
import de.False.BuildersWand.NMS.v_1_9.v_1_9_R2;
import de.False.BuildersWand.Updater.SpigotUpdater;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.utilities.Metrics;
import de.False.BuildersWand.utilities.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Main extends JavaPlugin
{
    private Locales locales = new Locales(this);
    private Config config;
    private ParticleUtil particleUtil;
    private NMS nms;
    private Wand wand;

    @Override
    public void onEnable()
    {
        setupNMS();
        loadConfigFiles();

        particleUtil = new ParticleUtil(nms);

        registerEvents();
        registerCommands();
        loadRecipes();
        loadMetrics();
        checkForUpdate(this);
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
        getCommand("bw").setExecutor(new Commands(config, wand));
    }

    private void registerEvents()
    {
        wand = new Wand(this, config, particleUtil, nms);
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(wand, this);
    }

    private void loadConfigFiles()
    {
        config = new Config(this, nms);
        locales.load();
        config.load();
    }

    private void setupNMS() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            switch (version)
            {
                case "v1_8_R1":
                    nms = new v_1_8_R1();
                    break;
                case "v1_8_R2":
                    nms = new v_1_8_R2();
                    break;
                case "v1_8_R3":
                    nms = new v_1_8_R3();
                    break;
                case "v1_9_R1":
                    nms = new v_1_9_R1();
                    break;
                case "v1_9_R2":
                    nms = new v_1_9_R2();
                    break;
                case "v1_10_R1":
                    nms = new v_1_10_R1();
                    break;
                case "v1_11_R1":
                    nms = new v_1_11_R1();
                    break;
                case "v1_12_R1":
                    nms = new v_1_12_R1(this);
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException exn) {
            exn.printStackTrace();
        }
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

    private void checkForUpdate(Main plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    new SpigotUpdater(plugin, 51577);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 20L, 72000L);
    }
}
