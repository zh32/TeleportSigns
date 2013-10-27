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
    private List<ServerInfo> servers;
    private Map<String, SignLayout> signLayouts;
    private int interval;
    private int timeout;
    private boolean showOfflineMsg;
    
    public ConfigurationData(TeleportSigns plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    public void loadConfig() {
        this.config = plugin.getConfig();
        this.offlineMessage = config.getString("offline-message");
        this.showOfflineMsg = config.getBoolean("show-offline-message");
        this.interval = config.getInt("interval");
        this.timeout = config.getInt("timeout");
        signLayouts = loadLayouts();
        servers = loadServers();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    private Map<String, SignLayout> loadLayouts() {
        Map<String, SignLayout> layoutMap = new HashMap<>();
        ConfigurationSection layouts = config.getConfigurationSection("layouts");
        for (String layout : layouts.getKeys(false)) {
            ConfigurationSection cs = layouts.getConfigurationSection(layout);
            String online = cs.getString("online");
            String offline = cs.getString("offline");
            List<String> lines = cs.getStringList("layout");
            boolean teleport = cs.getBoolean("teleport");
            String offlineInteger = cs.getString("offline-int");
            SignLayout signLayout = new SignLayout(layout, online, offline, lines, teleport, offlineInteger);
            layoutMap.put(layout, signLayout);
        }
        return layoutMap;
    }

    private List<ServerInfo> loadServers() {
        List<ServerInfo> list = new ArrayList<>();
        ConfigurationSection server = config.getConfigurationSection("servers");
        for (String servername : server.getKeys(false)) {
            ConfigurationSection cs = server.getConfigurationSection(servername);
            String displayname = cs.getString("displayname");
            String[] addre = cs.getString("address").split(":");
            InetSocketAddress address = new InetSocketAddress(addre[0], Integer.parseInt(addre[1]));
            ServerInfo si = new ServerInfo(servername, address, displayname);
            list.add(si);
        }
        return list;
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
