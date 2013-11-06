package de.zh32.teleportsigns;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author zh32 <zh32 at zh32.de>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProxyTeleportEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final Player player;
    private String serverName;
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
            return handlers;
    }

    public static HandlerList getHandlerList() {
            return handlers;
    }

    public ProxyTeleportEvent(Player player, String serverName) {
            this.player = player;
            this.serverName = serverName;
    }

}
