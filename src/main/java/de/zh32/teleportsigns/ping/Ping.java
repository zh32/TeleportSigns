package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSigns;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping {
    
    private final MCPing mcping;
    private TeleportSigns plugin;
    private boolean run;

    public Ping(TeleportSigns plugin) {
        this.plugin = plugin;
        mcping = new MCPing();
    }
    
    public void runPing(final Runnable callback) {
        for (ServerInfo info : plugin.getConfigData().getServers()) {
            mcping.setAddress(info.getAddress());
            mcping.setTimeout(plugin.getConfigData().getTimeout());
            if (mcping.fetchData()) {
                info.setOnline(true);
                info.setMotd(mcping.getMotd().split("(?<=\\G.{15})"));
                info.setPlayersOnline(mcping.getPlayersOnline());
                info.setMaxPlayers(mcping.getMaxPlayers());
            }
            else {
                info.setOnline(false);
                info.setMotd(null);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, callback);
    }
}
