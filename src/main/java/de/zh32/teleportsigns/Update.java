package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import de.zh32.teleportsigns.ping.ServerInfo;
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
                        ServerInfo sinfo = Ping.getInstance().getServer(ts.getServer());
                        if (sinfo != null) {
                            if (sinfo.isOnline()) {
                                String npl = String.valueOf(sinfo.getPlayersOnline());
                                String mpl = String.valueOf(sinfo.getMaxPlayers());
                                String motd = sinfo.getMotd();

                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.config.first_line));
                                if (plugin.config.usemotd) {
                                    s.setLine(1, motd);
                                }
                                else {
                                    s.setLine(1, ChatColor.translateAlternateColorCodes('&', sinfo.getDisplayname()));
                                }

                                s.setLine(2, ChatColor.translateAlternateColorCodes('&', plugin.config.playercountcolor) + npl + "/" + mpl);
                                s.setLine(3, ChatColor.translateAlternateColorCodes('&', plugin.config.online_line));
                                s.update();
                            }
                            else {
                                s.setLine(0, ChatColor.translateAlternateColorCodes('&', plugin.config.first_line));
                                s.setLine(1, ChatColor.translateAlternateColorCodes('&', sinfo.getDisplayname()));
                                s.setLine(2, ChatColor.translateAlternateColorCodes('&', plugin.config.playercountcolor) + "-/-");
                                s.setLine(3, ChatColor.translateAlternateColorCodes('&', plugin.config.offline_line));
                                s.update();
                            }
                        }
                    }
                }
            }
        }
    }
}
