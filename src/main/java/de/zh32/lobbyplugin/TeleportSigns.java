package de.zh32.lobbyplugin;

import de.zh32.lobbyplugin.ping.Ping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportSigns extends JavaPlugin {
    
    public Map<String, Location> locs = new HashMap<String, Location>();
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Update(this), 60L, 100L);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Ping.getInstance().loadConfig();
        Ping.getInstance().startPing();
        setupDB();
        loadSigns();
        
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(TeleportSign.class);
        return list;
    }

    public void loadSigns() {
        List<TeleportSign> ts = this.getDatabase().find(TeleportSign.class).findList();
        for (TeleportSign t : ts) {
            locs.put(t.getServer(), t.getLocation());
        }
    }

    private void setupDB() {
        try {
            getDatabase().find(TeleportSign.class).findRowCount();
        } catch (PersistenceException ex) {
            Bukkit.getLogger().log(Level.INFO, "Installing database for {0} due to first time usage", getDescription().getName());
            installDDL();
        }
    }
}