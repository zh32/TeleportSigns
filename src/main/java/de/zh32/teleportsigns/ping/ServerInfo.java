package de.zh32.teleportsigns.ping;

import java.net.InetSocketAddress;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zh32
 */
public class ServerInfo {
    @Getter
    private String name;
    @Getter
    @Setter
    private int playersOnline;
    @Getter
    @Setter
    private int maxPlayers;
    @Getter
    @Setter
    private String motd;
    @Getter
    @Setter
    private boolean online;
    @Getter
    private InetSocketAddress address;
    @Getter
    private String displayname;

    public ServerInfo(String servername, InetSocketAddress address, String displayname) {
        this.name = servername;
        this.address = address;
        this.displayname = displayname;
        this.online = false;
    }
}
