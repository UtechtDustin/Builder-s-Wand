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
import java.util.Set;

public class Config
{
    private File file;
    private FileConfiguration config;
    private long renderTime;
    private boolean updateNotification;
    private boolean autoDownload;

    public Config(Main plugin)
    {
        this.file = new File(plugin.getDataFolder(), "config.yml");
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

    public void load()
    {
        config = YamlConfiguration.loadConfiguration(file);
        addDefaults();

        renderTime = config.getLong("wand.renderInterval");
        updateNotification = config.getBoolean("update.notification");
        autoDownload = config.getBoolean("update.autoDownload");
    }

    private void addDefaults()
    {
        config.options().copyDefaults(true);
        config.addDefault("update.notification", true);
        config.addDefault("update.autoDownload", true);
        config.addDefault("general.renderInterval", 2);
        save();
    }

    public long getRenderTime()
    {
        return renderTime;
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