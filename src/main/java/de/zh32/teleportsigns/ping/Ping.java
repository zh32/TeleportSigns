package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSigns;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping {
    
    private final MCPing mcping = new MCPing();
    private TeleportSigns plugin;
    private boolean run;

    public Ping(TeleportSigns plugin) {
        this.plugin = plugin;
    }
    
    public void startPing() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable(){
            

            @Override
            public void run() {
                if (run) {
                    return;
                }
                run = true;
                for (ServerInfo info : plugin.getConfigData().getServers()) {
                    mcping.setAddress(info.getAddress());
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
                run = false;
            }
        }, 100L, plugin.getConfigData().getPingDelay() * 20L);
    }
}
