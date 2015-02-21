package de.zh32.teleportsigns.event;

import de.zh32.teleportsigns.server.GameServer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 *
 * @author zh32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BukkitProxyTeleportEvent extends PlayerEvent implements Cancellable {
	private boolean cancelled;
    private String server;
    private static final HandlerList handlers = new HandlerList();
	
	public BukkitProxyTeleportEvent(String player, GameServer server) {
		super(Bukkit.getPlayer(player));
	}

    @Override
    public HandlerList getHandlers() {
            return handlers;
    }

    public static HandlerList getHandlerList() {
            return handlers;
    }

}
