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
    private boolean online = false;
    private final InetSocketAddress address;
    private final String displayname;
}
