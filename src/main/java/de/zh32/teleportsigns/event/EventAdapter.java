package de.zh32.teleportsigns.event;

import de.zh32.teleportsigns.server.GameServer;

/**
 * @author zh32
 */
public interface EventAdapter {
	ProxyTeleportEvent callTeleportEvent(String player, GameServer server);
}
