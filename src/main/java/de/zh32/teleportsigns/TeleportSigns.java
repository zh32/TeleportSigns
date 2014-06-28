package de.zh32.teleportsigns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 *
 * @author zh32
 */
@Getter
public class TeleportSigns extends JavaPlugin {
    
    @Getter
    private static TeleportSigns instance;     
    private PluginData data;
    private UpdateUtil updateUtil;
    
    @Override
    public void onEnable() {
        instance = this;
        setupDB();
        data = new PluginData(this);
        data.loadData();
        updateUtil = new UpdateUtil(this);
        
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, updateUtil, 20);
        
        startMetrics();
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(TeleportSign.class);
        return list;
    }
    
    private void setupDB() {
        File ebean = new File(this.getDataFolder(), "ebean.properties");
        if (!ebean.exists()) {
            this.saveResource("ebean.properties", false);
        }
        try {
            getDatabase().find(TeleportSign.class).findRowCount();
        } catch (PersistenceException ex) {
            Bukkit.getLogger().log(Level.INFO, "Installing database for {0} due to first time usage", getDescription().getName());
            installDDL();
        }
    }
    
    private void startMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            Metrics.Graph signCount = metrics.createGraph("Sign count");
            signCount.addPlotter(new Metrics.Plotter() {

                @Override
                public int getValue() {
                    return data.getSigns().size();
                }
            });
            
            Metrics.Graph serverCount = metrics.createGraph("Server count");
            serverCount.addPlotter(new Metrics.Plotter() {

                @Override
                public int getValue() {
                    return data.getServers().size();
                }
            });
            
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tsreload")) {
            if (sender.hasPermission("teleportsigns.reload")) {
                sender.sendMessage(ChatColor.GREEN + "Reloading configuration from disk");
                getData().reloadData();
            }
            return true;
        }
        return false;
    }
    
    public void addSignLayout(SignLayout layout) {
        data.getSignLayouts().put(layout.getName(), layout);
    }
    
    public void removeSignLayout(SignLayout layout) {
        data.getSignLayouts().remove(layout.getName());
    }
}