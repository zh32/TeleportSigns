package de.zh32.teleportsigns.event;

import de.zh32.teleportsigns.server.GameServer;
import org.bukkit.Bukkit;


public class BukkitEventAdapter implements EventAdapter {

	@Override
	public ProxyTeleportEvent callTeleportEvent(String player, GameServer server) {
		BukkitProxyTeleportEvent event = new BukkitProxyTeleportEvent(player, server);
		Bukkit.getPluginManager().callEvent(event);
		return event;
	}

}
