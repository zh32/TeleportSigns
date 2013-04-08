package de.zh32.teleportsigns.ping;

import java.net.InetSocketAddress;
import lombok.Data;

/**
 *
 * @author zh32
 */
@Data
public class ServerInfo {
    private final String name;
    private int playersOnline;
    private int maxPlayers;
    private String motd;
    private boolean online;
    private final InetSocketAddress address;
    private final String displayname;

    public ServerInfo(String servername, InetSocketAddress address, String displayname) {
        this.name = servername;
        this.address = address;
        this.displayname = displayname;
        this.online = false;
    }
}
