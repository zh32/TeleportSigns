package de.zh32.teleportsigns.ping;

import java.net.InetSocketAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author zh32
 */
@Data
@EqualsAndHashCode
public class ServerInfo implements Cloneable {
    private final String name;
    private int playersOnline = 0;
    private int maxPlayers = 0;
    private String motd;
    private boolean online = false;
    private final InetSocketAddress address;
    private final String displayname;

    @Override
    public ServerInfo clone() {
        try {
            return (ServerInfo) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
