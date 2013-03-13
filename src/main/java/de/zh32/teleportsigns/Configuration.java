package de.zh32.teleportsigns;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author zh32
 */
public class Configuration {
    private TeleportSigns plugin;
    
    public boolean usemotd;

    public String first_line;

    public String online_line;

    public String offline_line;

    public String playercountcolor;

    public String offline_message;
    
    public Configuration(TeleportSigns plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.first_line = config.getString("first-line");
        this.offline_line = config.getString("offline-line");
        this.offline_message = config.getString("offline-message");
        this.online_line = config.getString("online-line");
        this.playercountcolor = config.getString("playercountcolor");
        this.usemotd = config.getBoolean("use-motd");
    }
}
