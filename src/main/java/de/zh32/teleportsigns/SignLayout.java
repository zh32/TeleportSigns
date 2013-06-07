package de.zh32.teleportsigns;

import java.util.ArrayList;
import java.util.List;

import de.zh32.teleportsigns.ping.ServerInfo;
import lombok.Data;
import org.bukkit.ChatColor;

/**
 *
 * @author zh32
 */
@Data
class SignLayout {
    private final String name;    
    private final String online;
    private final String offline;
    private final List<String> lines;
    private final boolean teleport;
    private final String offlineInteger;

    public List<String> parseLayout(ServerInfo sinfo) {
        List<String> laa = new ArrayList<>();
        int motdCount = 0;
        for (String line : lines) {
            line = line.replace("%displayname%", sinfo.getDisplayname());
            if (sinfo.isOnline()) {
                line = line.replace("%isonline%", online);
                line = line.replace("%numpl%", String.valueOf(sinfo.getPlayersOnline()));
                line = line.replace("%maxpl%", String.valueOf(sinfo.getMaxPlayers()));
                if (line.contains("%motd%")) {
                    String motd = sinfo.getMotd()[motdCount];
                    if (motd != null) {
                        line = line.replace("%motd%", motd);
                    }
                    motdCount++;
                }
            }
            else {
                line = line.replace("%isonline%", offline);
                line = line.replace("%numpl%", offlineInteger);
                line = line.replace("%maxpl%", offlineInteger);
                line = line.replace("%motd%", "");
            }
            laa.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return laa;
    }
}
