package de.False.BuildersWand.Updater;

import de.False.BuildersWand.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpigotUpdater extends Thread {
    private final Plugin plugin;
    private AutoDownload download;
    private ConsoleCommandSender logger = Bukkit.getServer().getConsoleSender();
    private final int id;
    private boolean enabled = true;
    private URL url;


    public SpigotUpdater(Main plugin, int resourceID)
            throws IOException {
        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        if (resourceID == 0) throw new IllegalArgumentException("Resource ID cannot be null (0)");

        this.plugin = plugin;
        this.id = resourceID;
        this.url = new URL("https://api.inventivetalent.org/spigot/resource-simple/" + resourceID);

        download = new AutoDownload(plugin);
        super.start();
    }

    public synchronized void start() {
    }

    public void run() {
        if (!this.plugin.isEnabled()) return;
        if (!this.enabled) return;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) this.url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String content = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                content = content + line;
            }
            in.close();

            JSONObject json = null;
            try {
                json = (JSONObject) new JSONParser().parse(content);
            } catch (ParseException e) {
            }
            String currentVersion = null;

            if ((json != null) && (json.containsKey("version"))) {
                String version = (String) json.get("version");
                if ((version != null) && (!version.isEmpty())) {
                    currentVersion = version;
                }
            }
            if (currentVersion == null) {
                return;
            }
            String newVersion = currentVersion.replaceAll("([^\\d]+)", "");
            String oldVersion = this.plugin.getDescription().getVersion().replaceAll("([^\\d]+)", "");
            int spigotVersion = Integer.parseInt(newVersion.replace(".", ""));
            int actualVersion = Integer.parseInt(oldVersion.replace(".", ""));
            if (actualVersion < spigotVersion) {
                download.autoDownload(this.id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}