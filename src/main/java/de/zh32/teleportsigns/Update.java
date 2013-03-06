/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import de.zh32.teleportsigns.ping.Result;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author zh32
 */
class Update implements Runnable {

    private TeleportSigns plugin;

    public Update(TeleportSigns plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {        
        updateSigns();   
    }
   
    private void updateSigns() {
        if (plugin.signs != null) {
            for (TeleportSign ts : plugin.signs) {
                Location l = ts.getLocation();
                if (l.getWorld().getChunkAt(l).isLoaded()) {
                    Block b = l.getBlock();
                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();
                        Result res = Ping.getInstance().results.get(ts.getServer());
                        if (res != null) {
                            if (res.isOnline()) {
                                String npl = String.valueOf(res.getPlayersOnline());
                                String mpl = String.valueOf(res.getMaxPlayers());
                                String motd = res.getMotd();

                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("first-line")));
                                if (plugin.getConfig().getBoolean("use-motd")) {
                                    s.setLine(1, motd);
                                }
                                else {
                                    s.setLine(1, ChatColor.translateAlternateColorCodes('&', Ping.getInstance().display.get(ts.getServer())));
                                }

                                s.setLine(2, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("playercountcolor")) + npl + "/" + mpl);
                                s.setLine(3, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("online-line")));
                                s.update();
                            }
                            else {
                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("first-line")));
                                s.setLine(1, ChatColor.translateAlternateColorCodes('&', Ping.getInstance().display.get(ts.getServer())));
                                s.setLine(2, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("playercountcolor")) + "-/-");
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
