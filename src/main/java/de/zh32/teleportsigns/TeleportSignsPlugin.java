package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
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
import org.mcstats.MetricsLite;

/**
 *
 * @author zh32
 */
@Getter
public class TeleportSignsPlugin extends JavaPlugin {  
    
    private Ping ping;  
    private PluginData data;
    private UpdateUtil updateUtil;
    
    @Override
    public void onEnable() {
        data = new PluginData(this);
        data.loadConfig();
        data.loadSigns();
        updateUtil = new UpdateUtil(this);
        setupDB();
        new TeleportSigns(this);
        File ebean = new File(this.getDataFolder(), "ebean.properties");
        if (!ebean.exists()) {
            this.saveResource("ebean.properties", false);
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ping = new Ping(this);
        
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, ping, 20);
        
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
                getData().reloadConfig();
            }
            return true;
        }
        return false;
    }
}