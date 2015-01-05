package de.zh32.teleportsigns;

import com.avaje.ebean.EbeanServer;
import de.zh32.teleportsigns.ping.ServerInfo;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author zh32
 */
@Data
public class PluginData {
    private final TeleportSigns plugin;
    private FileConfiguration config;
    private String offlineMessage;
    private List<ServerInfo> servers;
    private Map<String, SignLayout> signLayouts;
    private int interval;
    private int timeout;
    private int signsPerTick;
    private boolean showOfflineMsg;
    private int cooldown;
    private boolean debugmode;
    private final EbeanServer database;
    private List<TeleportSign> signs;
    
    protected PluginData(TeleportSigns plugin) {
        this.plugin = plugin;
        database = plugin.getDatabase();
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
        
    }
    void loadData() {
        this.config = plugin.getConfig();
        this.offlineMessage = config.getString("offline-message");
        this.showOfflineMsg = config.getBoolean("show-offline-message");
        this.interval = config.getInt("interval");
        this.timeout = config.getInt("timeout");
        this.signsPerTick = config.getInt("sign-updates");
        this.cooldown = config.getInt("cooldown");
        this.debugmode = config.getBoolean("debug");
        signLayouts = loadLayouts();
        servers = loadServers();
        loadSigns();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                checkSigns();
            }
        }, 5L);
    }
    
    void reloadData() {
        plugin.reloadConfig();
        loadData();
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
            SignLayout signLayout = new TeleportSignLayout(layout, online, offline, lines.toArray(new String[0]), teleport, offlineInteger);
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
    
    private void loadSigns() {
        signs = database.find(TeleportSign.class).findList();
    }
    
    private void checkSigns() {
        for (TeleportSign s : signs) {
            ServerInfo server = getServer(s.getServer());
            SignLayout layout = getLayout(s.getLayout());
            if (server == null || layout == null) {
                Bukkit.getLogger().log(Level.SEVERE, "[TeleportSigns] Deleting TeleportSign at {0}", s.getLocation().toString());
                removeSign(s);
            }  
        }
    }
    
    SignLayout getLayout(String layout) {
        return signLayouts.get(layout);
    }
    
    ServerInfo getServer(String server) {
        for (ServerInfo info : plugin.getData().getServers()) {
            if (info.getName().equals(server)) {
                return info;
            }
        }
        return null;
    }
    
    void addSign(TeleportSign ts) {
        signs.add(ts);
        database.save(ts);
        if (debugmode)
            Bukkit.getLogger().log(Level.INFO, "[TeleportSigns] Saved TeleportSign at {0}", ts.getLocation().toString());
    }
    
    void removeSign(TeleportSign ts) {
        signs.remove(ts);
        database.delete(ts);
        if (debugmode)
            Bukkit.getLogger().log(Level.INFO, "[TeleportSigns] Removed TeleportSign at {0}", ts.getLocation().toString());
    }
    
    TeleportSign getSignForLocation(Location loc) {
        for (TeleportSign ts : plugin.getData().getSigns()) {
            if (ts.getLocation().equals(loc)) return ts;
        }
        return null;
    }
}