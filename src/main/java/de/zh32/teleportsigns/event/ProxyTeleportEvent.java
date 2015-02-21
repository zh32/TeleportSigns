package de.zh32.teleportsigns.event;

import de.zh32.teleportsigns.server.GameServer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author zh32 <zh32 at zh32.de>
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class ProxyTeleportEvent {

    private boolean cancelled;
    private String player;
    private GameServer serverInfo;
}
