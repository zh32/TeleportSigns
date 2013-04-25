package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

/**
 *
 * @author zh32
 */
public class TeleportSigns extends JavaPlugin {
    
    public List<TeleportSign> signs = new ArrayList<>();
    
    public String offline_message;
    
    private Map<String, SignLayout> signLayouts = new HashMap<>();
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.offline_message = getConfig().getString("offline-message");
        File ebean = new File(this.getDataFolder(), "ebean.properties");
        if (!ebean.exists()) {
            this.saveResource("ebean.properties", false);
        }
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Update(this), 60L, 100L);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Ping.getInstance().loadConfig();
        Ping.getInstance().startPing();
        setupDB();
        loadLayouts();
        loadSigns();
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(TeleportSign.class);
        return list;
    }

    public void loadSigns() {
        signs = this.getDatabase().find(TeleportSign.class).findList();
    }

    private void setupDB() {
        try {
            getDatabase().find(TeleportSign.class).findRowCount();
        } catch (PersistenceException ex) {
            Bukkit.getLogger().log(Level.INFO, "Installing database for {0} due to first time usage", getDescription().getName());
            installDDL();
        }
    }

    public SignLayout getLayout(String layout) {
        return signLayouts.get(layout);
    }
    
    public void loadLayouts() {
        FileConfiguration config = this.getConfig();
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
}