/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author zh32
 */
class Update implements Runnable {

    private TeleportSigns plugin;
    
    private Map<String, String> results = Ping.getInstance().results;

    public Update(TeleportSigns plugin) {
        this.plugin = plugin;
    }

    public void run() {        
        updateSigns();   
    }
   
    private void updateSigns() {
        if (plugin.locs != null) {

            for (Entry<String, Location> e : plugin.locs.entrySet()) {
                World w = e.getValue().getWorld();
                Location l = e.getValue();

                if (w.getChunkAt(l).isLoaded()) {
                    Block b = w.getBlockAt(l);

                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();
                        if (results.get(plugin.getServerName(e.getKey())) != null) {
                            String res = results.get(plugin.getServerName(e.getKey()));
                            String[] sl = res.split("#@#");
                            if (sl.length == 3) {
                                String npl = sl[0];
                                String mpl = sl[1];
                                String motd = sl[2];

                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("first-line")));
                                if (plugin.getConfig().getBoolean("use-motd")) {
                                    s.setLine(1, motd);
                                }
                                else {
                                    s.setLine(1, plugin.getServerName(e.getKey()));
                                }
                                
                                s.setLine(2, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("playercountcolor")) + npl + "/" + mpl);
                                s.setLine(3, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("online-line")));
                                s.update();
                            }
                            else {
                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("first-line")));
                                s.setLine(1, plugin.getServerName(e.getKey()));
                                s.setLine(2, ChatColor.BOLD + "-/-");
                                s.setLine(3, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("offline-line")));
                                s.update();
                            }
                        }
                    }               
                }
            }
        }
    }
}
