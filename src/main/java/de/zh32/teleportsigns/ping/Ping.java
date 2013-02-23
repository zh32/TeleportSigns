/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns.ping;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
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
    
    static Ping _instance = null;
    File configfile;
    FileConfiguration config;
    public Map<String, String> results = new ConcurrentHashMap<String,String>();
    public Map<String, String> pinglist = new ConcurrentHashMap<String,String>();
    public HashMap<String, String> display = new HashMap<String, String>();

    public ConfigurationSection servers;
    public static Ping getInstance() {
        if(Ping._instance == null) {
            Ping._instance = new Ping();
        }

        return Ping._instance;
    }
    
    public void loadConfig() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TeleportSigns");
        configfile = new File(plugin.getDataFolder(), "ping.yml");        
        if (!configfile.exists()) {
            plugin.saveResource("ping.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configfile);
        servers = config.getConfigurationSection("servers");
        Set<String> keys = servers.getKeys(false);
        for (String s : keys) {
            ConfigurationSection cs = servers.getConfigurationSection(s);
            pinglist.put(s, cs.getString("address"));
            display.put(s, cs.getString("displayname"));
        }
        System.out.println(display);
    }
    
    public void startPing() {
        startPing(pinglist);
    }
    
    public void startPing(Map<String, String> toping) {
        final PingThread pt = new PingThread(results, toping);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                pt.ping();
            }
        };
        timer.schedule(task, 2000, 5000);
    } 
}
