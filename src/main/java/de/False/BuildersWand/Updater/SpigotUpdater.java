package de.False.BuildersWand.Updater;

import de.False.BuildersWand.Main;
import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
    private de.False.BuildersWand.Updater.AutoDownload download;
    private ConsoleCommandSender logger = Bukkit.getServer().getConsoleSender();
    private final int id;
    private boolean autoDownload;
    private boolean enabled = true;
    private Player player;
    private Config config;
    private URL url;

    SpigotUpdater(Main plugin, int resourceID, Player player, Config config) throws IOException {
        this(plugin, resourceID, false, config);
        this.player = player;
    }

    public SpigotUpdater(Main plugin, int resourceID, boolean autoDownload, Config config)
            throws IOException {
        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        if (resourceID == 0) throw new IllegalArgumentException("Resource ID cannot be null (0)");

        this.config = config;
        this.autoDownload = autoDownload;
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
                if (player != null && config.getUpdateNotification()) {
                    String updateLine1 = MessageUtil.getText("updateLine1", player).replace("{newVer}", currentVersion);
                    String updateLine2 = MessageUtil.getText("updateLine2", player).replace("{currentVer}", this.plugin.getDescription().getVersion());
                    String updateLine3 = MessageUtil.getText("updateLine3", player).replace("{url}", "https://www.spigotmc.org/resources/51577/");

                    MessageUtil.sendRawPrefixMessage(player, updateLine1);
                    MessageUtil.sendRawPrefixMessage(player, updateLine2);
                    MessageUtil.sendRawPrefixMessage(player, updateLine3);
                }
                if (config.getAutoDownload() && autoDownload) download.autoDownload(this.id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}