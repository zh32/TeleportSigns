package de.zh32.teleportsigns.ping;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author zh32
 */
public class Ping {
      
    public Map<String, ServerInfo> serverinfos = new ConcurrentHashMap<>();
    static Ping _instance = null;
    
    public static Ping getInstance() {
        if(Ping._instance == null) {
            Ping._instance = new Ping();
        }
        return Ping._instance;
    }
    
    public void loadConfig() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TeleportSigns");
        File configfile = new File(plugin.getDataFolder(), "ping.yml");        
        if (!configfile.exists()) {
            plugin.saveResource("ping.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
        ConfigurationSection servers = config.getConfigurationSection("servers");
        for (String servername : servers.getKeys(false)) {
            ConfigurationSection cs = servers.getConfigurationSection(servername);
            String displayname = cs.getString("displayname");
            String[] addre = cs.getString("address").split(":");
            InetSocketAddress address = new InetSocketAddress(addre[0], Integer.parseInt(addre[1]));
            ServerInfo si = new ServerInfo(servername, address, displayname);
            serverinfos.put(servername, si);
        }
    }
    
    public void startPing() {       
        TimerTask task = new TimerTask() {
            private MCPing mcping = new MCPing();
            @Override
            public void run() {
                for (Entry<String, ServerInfo> e : serverinfos.entrySet()) {
                    ServerInfo info = e.getValue();
                    mcping.setAddress(info.getAddress());
                    if (mcping.fetchData()) {
                        info.setOnline(true);
                        info.setMotd(mcping.getMotd());
                        info.setPlayersOnline(mcping.getPlayersOnline());
                        info.setMaxPlayers(mcping.getMaxPlayers());
                    }                    
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000, 5000);
    } 
}
