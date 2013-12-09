package de.zh32.teleportsigns.ping;

import lombok.Data;

import java.net.InetSocketAddress;

/**
 *
 * @author zh32
 */
@Data
public class ServerInfo {
    private final String name;
    private int playersOnline = 0;
    private int maxPlayers = 0;
    private String motd;
    private boolean online = false;
    private final InetSocketAddress address;
    private final String displayname;
}
