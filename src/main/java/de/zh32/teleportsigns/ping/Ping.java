package de.zh32.teleportsigns.ping;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Getter;
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
    
    @Getter
    private List<ServerInfo> servers = new ArrayList<>();
    static Ping _instance = null;
    private final MCPing mcping = new MCPing();
    
    public static Ping getInstance() {
        if(Ping._instance == null) {
            Ping._instance = new Ping();
        }
        return Ping._instance;
    }
    
    public ServerInfo getServer(String server) {
        for (ServerInfo info : servers) {
            if (info.getName().equals(server)) {
                return info;
            }
        }
        return null;
    }
    
    public void loadConfig() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TeleportSigns");
        File configfile = new File(plugin.getDataFolder(), "ping.yml");        
        if (!configfile.exists()) {
            plugin.saveResource("ping.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
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
    
    public void startPing() {       
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (ServerInfo info : servers) {
                    mcping.setAddress(info.getAddress());
                    if (mcping.fetchData()) {
                        info.setOnline(true);
                        info.setMotd(mcping.getMotd());
                        info.setPlayersOnline(mcping.getPlayersOnline());
                        info.setMaxPlayers(mcping.getMaxPlayers());
                    }
                    else {
                        info.setOnline(false);
                        info.setMotd("");
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000, 5000);
    } 
}
