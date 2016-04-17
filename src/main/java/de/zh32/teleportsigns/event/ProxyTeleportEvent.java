package de.zh32.teleportsigns.event;

import de.zh32.teleportsigns.server.GameServer;

/**
 * @author zh32 <zh32 at zh32.de>
 */
public interface ProxyTeleportEvent {

	boolean isCancelled();

	String getPlayerName();

	GameServer getServer();
}
