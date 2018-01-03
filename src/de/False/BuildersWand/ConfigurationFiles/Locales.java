package de.False.BuildersWand.ConfigurationFiles;

import de.False.BuildersWand.Main;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Locales
{
    private Main plugin;
    public static HashMap<String, HashMap<String, String>> messages = new HashMap<>();

    public Locales(Main plugin)
    {
        this.plugin = plugin;
    }

    public void load()
    {
        File[] files = new File(plugin.getDataFolder() + "/locales").listFiles();
        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            if (!file.isDirectory() && file.getName().endsWith(".yml"))
            {

                FileConfiguration localeFile = YamlConfiguration.loadConfiguration(file);
                String locale = FilenameUtils.getBaseName(file.getName());
                ConfigurationSection configurationSection = localeFile.getConfigurationSection("");
                if (configurationSection == null)
                {
                    continue;
                }

                messages.put(locale, new HashMap<>());
                Set<String> keys = configurationSection.getKeys(false);
                for (String key : keys)
                {
                    saveKeyToLocale(localeFile, locale, key);
                }
            }
        }
    }

    private void saveKeyToLocale(FileConfiguration file, String locale, String path)
    {
        ConfigurationSection configurationSection = file.getConfigurationSection(path);

        if (configurationSection == null)
        {
            String message = file.getString(path);
            messages.get(locale).put(path, message);
            return;
        }

        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys)
        {
            saveKeyToLocale(file, locale, path + "." + key);
        }
    }

    public void copyDefaultLocales()
    {
        String LOCALES_PATH = "locales/";
        plugin.saveResource(LOCALES_PATH + "en_us.yml", false);
        plugin.saveResource(LOCALES_PATH + "de_de.yml", false);
    }
}