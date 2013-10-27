package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSigns;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping implements Runnable {
    
    private final MCPing mcping;
    private TeleportSigns plugin;
    private int signsPerTick;

    public Ping(TeleportSigns plugin) {
        this.plugin = plugin;
        mcping = new MCPing();
        signsPerTick = plugin.getConfigData().getSignsPerTick();
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
            Iterator<TeleportSign> iterator = plugin.getSigns().iterator();
            while (iterator.hasNext()) {
                TeleportSign ts = iterator.next();
                if (ts.getServer().equals(info.getName())) {
                    tempList.add(ts);
                }
            }
            int size = tempList.size();
            int offset = 0;
            while (size > signsPerTick) {
                plugin.updateSigns(tempList.subList(offset, offset + signsPerTick));
                size -= signsPerTick;
                offset += signsPerTick;
            }         
            plugin.updateSigns(tempList.subList(offset, offset + size));
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getConfigData().getInterval() * 20);
    }
}
