package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSigns;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping implements Runnable {
    
    private final ServerListPing17 mcping;
    private final TeleportSigns plugin;
    private final int signsPerTick;

    public Ping(TeleportSigns plugin) {
        this.plugin = plugin;
        mcping = new ServerListPing17();
        signsPerTick = plugin.getConfigData().getSignsPerTick();
    }

    @Override
    public void run() {
        for (ServerInfo info : plugin.getConfigData().getServers()) {
            mcping.setHost(info.getAddress());
            mcping.setTimeout(plugin.getConfigData().getTimeout());
            try {
                StatusResponse data = mcping.fetchData();
                info.setOnline(true);
                info.setMotd(data.getDescription().replace("Â", "").split("(?<=\\G.{15})"));
                info.setPlayersOnline(data.getPlayers().getOnline());
                info.setMaxPlayers(data.getPlayers().getMax());
            } catch (IOException ex) {
                info.setOnline(false);
                info.setMotd(null);
                if (!(ex instanceof ConnectException)) {
                    Bukkit.getLogger().log(Level.SEVERE, "[TeleportSigns] Error fetching data from server " + info.getAddress().toString(), ex);
                }
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
