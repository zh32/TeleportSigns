package de.zh32.teleportsigns;

import de.zh32.teleportsigns.converter.Converter;
import de.zh32.teleportsigns.ping.Ping;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

/**
 *
 * @author zh32
 */
public class TeleportSigns extends JavaPlugin {
  
    @Getter
    private List<TeleportSign> signs;   
    @Getter
    private Ping ping;  
    @Getter
    private ConfigurationData configData;
    @Getter
    private static TeleportSigns instance;
    private long lastUpdate = System.currentTimeMillis();
    private int ticksAhead = 0;
    

    public TeleportSigns() {
        instance = this;
    }

    @Override
    public void onLoad() {
        Converter.convert(this);
    }
    
    @Override
    public void onEnable() {
        configData = new ConfigurationData(this);
        configData.loadConfig();
        setupDB();
        loadSigns();
        File ebean = new File(this.getDataFolder(), "ebean.properties");
        if (!ebean.exists()) {
            this.saveResource("ebean.properties", false);
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ping = new Ping(this);
        
        Bukkit.getScheduler().runTaskAsynchronously(this, ping);
        
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
    
    public void updateSigns(final List<TeleportSign> list) {      
        long now = System.currentTimeMillis();
        if (now - lastUpdate < 50) {
            ticksAhead++;
        }
        else {
            ticksAhead = 1;
        }
        lastUpdate = now;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run() {
                Iterator<TeleportSign> tempIterator = list.iterator();
                while (tempIterator.hasNext()) {
                    tempIterator.next().updateSign();
                }
            }
        }, ticksAhead);  
    }
    
    private void setupDB() {
        try {
            getDatabase().find(TeleportSign.class).findRowCount();
        } catch (PersistenceException ex) {
            Bukkit.getLogger().log(Level.INFO, "Installing database for {0} due to first time usage", getDescription().getName());
            installDDL();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tsreload")) {
            if (sender.hasPermission("teleportsigns.reload")) {
                sender.sendMessage(ChatColor.GREEN + "Reloading configuration from disk");
                getConfigData().reloadConfig();
            }
            return true;
        }
        return false;
    }
    
    
    
}