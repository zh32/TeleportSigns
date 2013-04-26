package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.ServerInfo;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author zh32
 */
@Data
public class ConfigurationData {
    private TeleportSigns plugin;
    private FileConfiguration config;
    private String offlineMessage;
    private List<ServerInfo> servers = new ArrayList<>();
    private Map<String, SignLayout> signLayouts = new HashMap<>();
    private int pingDelay;
    
    public ConfigurationData(TeleportSigns plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        this.offlineMessage = config.getString("offline-message");
        this.pingDelay = config.getInt("interval");
        loadLayouts();
        loadServers();
    }
    
    public void reloadConfig() {
        servers.clear();
        signLayouts.clear();
        loadConfig();
    }

    private void loadLayouts() {
        ConfigurationSection layouts = config.getConfigurationSection("layouts");
        for (String layout : layouts.getKeys(false)) {
            ConfigurationSection cs = layouts.getConfigurationSection(layout);
            String online = cs.getString("online");
            String offline = cs.getString("offline");
            List<String> lines = cs.getStringList("layout");
            boolean teleport = cs.getBoolean("teleport");
            SignLayout signLayout = new SignLayout(layout, online, offline, lines, teleport);
            signLayouts.put(layout, signLayout);
        }
    }

    private void loadServers() {
        ConfigurationSection server = config.getConfigurationSection("servers");
        for (String servername : server.getKeys(false)) {
            ConfigurationSection cs = server.getConfigurationSection(servername);
            String displayname = cs.getString("displayname");
            String[] addre = cs.getString("address").split(":");
            InetSocketAddress address = new InetSocketAddress(addre[0], Integer.parseInt(addre[1]));
            ServerInfo si = new ServerInfo(servername, address, displayname);
            servers.add(si);
        }
    }
    
    public SignLayout getLayout(String layout) {
        return signLayouts.get(layout);
    }
    
    public ServerInfo getServer(String server) {
        for (ServerInfo info : plugin.getConfigData().getServers()) {
            if (info.getName().equals(server)) {
                return info;
            }
        }
        return null;
    }
}
