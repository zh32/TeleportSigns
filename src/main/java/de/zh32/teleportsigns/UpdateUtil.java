package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.ServerInfo;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author zh32
 */
public class UpdateUtil {
    
    private long lastUpdate = System.currentTimeMillis();
    private int ticksAhead = 0;
    private final TeleportSignsPlugin plugin;
    private final int signsPerTick;

    protected UpdateUtil(TeleportSignsPlugin plugin) {
        this.plugin = plugin;
        this.signsPerTick = plugin.getData().getSignsPerTick();
    }
    
    public void updateAllSigns(List<TeleportSign> tempList) {
        int size = tempList.size();
        int offset = 0;
        while (size > signsPerTick) {
            plugin.getUpdateUtil().updateSignChunk(tempList.subList(offset, offset + signsPerTick));
            size -= signsPerTick;
            offset += signsPerTick;
        }         
        updateSignChunk(tempList.subList(offset, offset + size));
    }
    
    public void updateSignChunk(final List<TeleportSign> list) {      
        long now = System.currentTimeMillis();
        if (now - lastUpdate < 50) {
            ticksAhead++;
        }
        else {
            ticksAhead = 1;
        }
        lastUpdate = now;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                Iterator<TeleportSign> tempIterator = list.iterator();
                while (tempIterator.hasNext()) {
                    TeleportSign sign = tempIterator.next();
                    SignLayout layout = plugin.getData().getLayout(sign.getLayout());
                    ServerInfo server = plugin.getData().getServer(sign.getServer());                   
                    updateSign(sign, layout, server);
                }
            }
        }, ticksAhead);  
    }
    
    private void updateSign(TeleportSign sign, SignLayout layout, ServerInfo server) {
        Location location = sign.getLocation();
        if (location.getWorld().getChunkAt(location).isLoaded()) {
            Block b = location.getBlock();
            if (b.getState() instanceof Sign) {
                Sign s = (Sign) b.getState();
                List<String> lines = layout.parseLayout(server);
                for (int i = 0; i < lines.size(); i++) {
                    s.setLine(i, lines.get(i));
                }
                s.update();
            }
        }
    }
}
