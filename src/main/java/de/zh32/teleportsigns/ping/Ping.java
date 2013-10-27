package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSigns;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping implements Runnable {
    
    private final MCPing mcping;
    private TeleportSigns plugin;
    private long lastUpdate = System.currentTimeMillis();
    private int ticksAhead = 0;

    public Ping(TeleportSigns plugin) {
        this.plugin = plugin;
        mcping = new MCPing();
    }

    @Override
    public void run() {
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
            final ArrayList<TeleportSign> tempList = new ArrayList<>();
            Iterator<TeleportSign> iterator = plugin.signs.iterator();
            while (iterator.hasNext()) {
                TeleportSign ts = iterator.next();
                if (ts.getServer().equals(info.getName())) {
                    tempList.add(ts);
                }
            }
            int size = tempList.size();
            int offset = 0;
            while (size > 10) {
                updateSigns(tempList.subList(offset, offset + 10));
                size -= 10;
                offset += 10;
            }         
            updateSigns(tempList.subList(offset, offset + size));
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getConfigData().getInterval() * 20);
    }
    
    public void updateSigns(final List<TeleportSign> list) {      
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
                    tempIterator.next().updateSign();
                }
            }
        }, ticksAhead);  
    }
}
