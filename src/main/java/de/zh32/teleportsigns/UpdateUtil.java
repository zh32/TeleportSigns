package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.ServerInfo;
import de.zh32.teleportsigns.ping.ServerListPing;
import de.zh32.teleportsigns.ping.StatusResponse;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author zh32
 */
public class UpdateUtil implements Runnable {
    
    private final Queue<TeleportSign> queue;
    private final TeleportSigns plugin;
    private final ServerListPing mcping;
    private boolean firstRun;

    public UpdateUtil(TeleportSigns plugin) {
        this.plugin = plugin;
        queue = new LinkedTransferQueue<>();
        mcping = new ServerListPing(plugin);
        firstRun = true;
    }
    
    public void update() {
        int signsPerTick = plugin.getData().getSignsPerTick();
        int updates = 0;
        TeleportSign sign;
        while (updates < signsPerTick && (sign = queue.poll()) != null) {
            SignLayout layout = plugin.getData().getLayout(sign.getLayout());
            ServerInfo server = plugin.getData().getServer(sign.getServer());                   
            updateSign(sign, layout, server);
            updates++;
        }
        if (plugin.getData().isDebugmode()) Bukkit.getLogger().log(Level.INFO, "[TeleportSigns] Updated {0} signs this tick", updates);
        if (queue.size() > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                
                @Override
                public void run() {                
                    update(); 
                }
            }, 1L);
        }
        else {
            if (plugin.getData().isDebugmode()) Bukkit.getLogger().log(Level.INFO, "[TeleportSigns] Finished update. Scheduling next update in {0} ticks", plugin.getData().getInterval() * 20);
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getData().getInterval() * 20);
        }
    }
    
    void updateSign(TeleportSign sign, SignLayout layout, ServerInfo server) {
        Location location = sign.getLocation();
        if (location.getWorld().getChunkAt(location).isLoaded()) {
            Block b = location.getBlock();
            if (b.getState() instanceof Sign) {
                Sign s = (Sign) b.getState();
                String[] lines = layout.parseLayout(server);
                for (int i = 0; i < lines.length; i++) {
                    s.setLine(i, lines[i]);
                }
                s.update();
            }
        }
    }

    @Override
    public void run() {
        if (plugin.getData().isDebugmode()) Bukkit.getLogger()
                .log(Level.INFO, "[TeleportSigns] Starting update. {0} total", plugin.getData().getSigns().size());
        for (ServerInfo info : plugin.getData().getServers()) { 
            mcping.setHost(info.getAddress());
            mcping.setTimeout(plugin.getData().getTimeout());
            StatusResponse data = null;
            try {
                data = mcping.fetchData();
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
            ServerInfo oldState = (ServerInfo) info.clone();
            if (data != null) {
                info.setOnline(true);
                info.setMotd(data.getDescription());
                info.setPlayersOnline(data.getPlayers().getOnline());
                info.setMaxPlayers(data.getPlayers().getMax());
            }
            else {
                info.setOnline(false);
                info.setMotd(null);
            }
            if (!firstRun && oldState.equals(info)) continue;
            for (TeleportSign ts : plugin.getData().getSigns()) {
                if (ts.getServer().equals(info.getName())) {
                    queue.add(ts);
                }
            }   
        }
        Bukkit.getScheduler().runTask(plugin, new Runnable() {

            @Override
            public void run() {
                update();
                firstRun = false;
            }
        });        
    }
}
