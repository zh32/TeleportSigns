package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.GameServer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;

/**
 *
 * @author zh32
 */
@Data
@Accessors(chain = true)
class TeleportSignLayout implements SignLayout {
    private String name;    
    private String online;
    private String offline;
    private String[] layout;
    private boolean teleport;
    private String offlineInteger;

    @Override
    public String[] parseLayout(GameServer sinfo) {
        String[] laa = new String[layout.length];
        int motdCount = 0;
        String tempMotd = sinfo.getMotd() == null ? "" : sinfo.getMotd();
        String[] splitMotd = tempMotd.split("(?<=\\G.{15})");
        for (int i = 0; i < layout.length; i++) {
            String line = layout[i];
            line = line.replace("%displayname%", sinfo.getDisplayname());
            if (sinfo.isOnline()) {
                line = line.replace("%isonline%", online);
                line = line.replace("%numpl%", String.valueOf(sinfo.getPlayersOnline()));
                line = line.replace("%maxpl%", String.valueOf(sinfo.getMaxPlayers()));
                if (line.contains("%motd%")) {
                    if (motdCount < splitMotd.length) {
                        String motd = splitMotd[motdCount];
                        if (motd != null) {
                            line = line.replace("%motd%", motd);
                        }
                        motdCount++;
                    } else {
                        line = line.replace("%motd%", "");
                    }
                }
            }
            else {
                line = line.replace("%isonline%", offline);
                line = line.replace("%numpl%", offlineInteger);
                line = line.replace("%maxpl%", offlineInteger);
                line = line.replace("%motd%", "");
            }
            laa[i] = ChatColor.translateAlternateColorCodes('&', line);
        }
        return laa;
    }
}
