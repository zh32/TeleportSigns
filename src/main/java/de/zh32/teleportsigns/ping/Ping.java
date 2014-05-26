package de.zh32.teleportsigns.ping;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSignsPlugin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;


/**
 *
 * @author zh32
 */
public class Ping implements Runnable {
    
    private final ServerListPing mcping;
    private final TeleportSignsPlugin plugin;

    public Ping(TeleportSignsPlugin plugin) {
        this.plugin = plugin;
        mcping = new ServerListPing();
    }

    @Override
    public void run() {
        for (ServerInfo info : plugin.getData().getServers()) {
            mcping.setHost(info.getAddress());
            mcping.setTimeout(plugin.getData().getTimeout());
            StatusResponse data = null;
            try {
                data = mcping.fetchData();
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
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
            final ArrayList<TeleportSign> tempList = new ArrayList<>();
            Iterator<TeleportSign> iterator = plugin.getData().getSigns().iterator();
            while (iterator.hasNext()) {
                TeleportSign ts = iterator.next();
                if (ts.getServer().equals(info.getName())) {
                    tempList.add(ts);
                }
            }
            plugin.getUpdateUtil().updateAllSigns(tempList);
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getData().getInterval() * 20);
    }
}
