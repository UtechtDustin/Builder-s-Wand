package de.False.BuildersWand.Updater;

import de.False.BuildersWand.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

class AutoDownload {

    private Main plugin;

    AutoDownload(Main plugin)
    {
        this.plugin = plugin;
    }

    public void autoDownload(int id) {
        String downloadURL = "https://api.spiget.org/v2/resources/" + id + "/download";
        File dir = new File(plugin.getDataFolder().getAbsolutePath());
        String pluginFolder = dir.getParentFile().getAbsolutePath() + "/Builders-Wand.jar";

        try {
            FileUtils.copyURLToFile(new URL(downloadURL), new File(pluginFolder), 10000, 10000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


