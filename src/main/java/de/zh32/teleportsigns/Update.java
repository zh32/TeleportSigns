package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import de.zh32.teleportsigns.ping.ServerInfo;
import java.util.logging.Level;
import org.bukkit.Bukkit;
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
                updateSign(ts);
            }
        }
    }
    
    public void updateSign(TeleportSign sign) { 
        Location l = sign.getLocation();
        if (l.getWorld().getChunkAt(l).isLoaded()) {
            Block b = l.getBlock();
            if (b.getState() instanceof Sign) {
                ServerInfo sinfo = Ping.getInstance().getServer(sign.getServer());
                SignLayout layout = plugin.getLayout(sign.getLayout());
                if (layout != null) {
                    Sign s = (Sign) b.getState();
                    for (int i = 0; i < 4; i++) {
                        s.setLine(i, replaceInfo(sinfo, layout, layout.getLines().get(i)));
                    }
                    s.update();
                }
                else {
                    Bukkit.getLogger().log(Level.WARNING, "[TeleportSigns] can't find layout '" + sign.getLayout() + "'");
                }
            }
        }
    }
    
    private String replaceInfo(ServerInfo sinfo, SignLayout layout, String str) {
        String erg = str;
        erg = erg.replace("%numpl%", String.valueOf(sinfo.getPlayersOnline()));
        erg = erg.replace("%maxpl%", String.valueOf(sinfo.getMaxPlayers()));
        erg = erg.replace("%motd%", sinfo.getMotd());
        erg = erg.replace("%displayname%", sinfo.getDisplayname());
        
        if (sinfo.isOnline()) {
            erg = erg.replace("%isonline%", layout.getOnline());
        }
        else {
            erg = erg.replace("%isonline%", layout.getOffline());
        }
        return ChatColor.translateAlternateColorCodes('&', erg);
        
    }
}
