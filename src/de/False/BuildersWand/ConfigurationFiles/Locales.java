package de.False.BuildersWand.ConfigurationFiles;

import de.False.BuildersWand.Main;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Locales
{
    private Main plugin;
    public static HashMap<String, HashMap<String, String>> messages = new HashMap<>();
    private HashMap<String, HashMap<String, String>> defaults = new HashMap<>();

    public Locales(Main plugin)
    {
        this.plugin = plugin;

        setDefaults();
    }

    public void load()
    {
        for (Map.Entry<String, HashMap<String, String>> entry: defaults.entrySet())
        {
            String language = entry.getKey();
            HashMap<String, String> messages = entry.getValue();
            String filePath = "locales/" + language + ".yml";
            File localeFile = new File(plugin.getDataFolder(), filePath);
            FileConfiguration localeConfig = YamlConfiguration.loadConfiguration(localeFile);
            localeConfig.options().copyDefaults(true);

            for (Map.Entry<String, String> messagesEntry: messages.entrySet())
            {
                String key = messagesEntry.getKey();
                String message = messagesEntry.getValue();

                localeConfig.addDefault(key, message);
            }

            try
            {
                localeConfig.save(localeFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

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

    private void setDefaults()
    {
        HashMap<String, String> en_us = new HashMap<>();
        en_us.put("reload", "The config has been reloaded.");
        en_us.put("playerNotFound", "The player can not be found.");
        en_us.put("noPermissions", "You dont have enough permissions.");
        en_us.put("updateLine1", "&eThere is a new update available: &a{newVer}");
        en_us.put("updateLine2", "&eYour Current version: &a{currentVer}");
        en_us.put("updateLine3", "&e{url}");

        HashMap<String, String> de_de = new HashMap<>();
        de_de.put("reload", "Die Konfig wurde neugeladen.");
        de_de.put("playerNotFound", "Der Spieler konnte nicht gefunden werden.");
        de_de.put("noPermissions", "Du besitzt zu wenige Rechte um dies zu tun.");
        de_de.put("updateLine1", "&eEin neues Update steht bereit: &a{newVer}");
        de_de.put("updateLine2", "&eAktuelle Version: &a{currentVer}");
        de_de.put("updateLine3", "&e{url}");

        HashMap<String, String> es_es = new HashMap<>();
        es_es.put("reload", "La configuracion ha sido recargada.");
        es_es.put("playerNotFound", "No se pudo encontrar al jugador.");
        es_es.put("noPermissions", "No tienes los permisos suficientes.");
        es_es.put("updateLine1", "&eHay una nueva version disponible: &a{newVer}");
        es_es.put("updateLine2", "&eTu version actual: &a{currentVer}");
        es_es.put("updateLine3", "&e{url}");

        defaults.put("en_us", en_us);
        defaults.put("de_de", de_de);
        defaults.put("es_es", es_es);
    }
}