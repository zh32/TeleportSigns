package de.zh32.teleportsigns;

import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.utility.Cooldown;
import lombok.Getter;

/**
 *
 * @author zh32
 */
@Getter
public abstract class ServerTeleporter {

	private final Cooldown cooldown;
	private final TeleportSigns plugin;

	public ServerTeleporter(TeleportSigns plugin) {
		this.cooldown = new Cooldown(plugin.getConfiguration().getTeleportCooldown());
		this.plugin = plugin;
	}
	
	public void teleportPlayer(String player, TeleportSign.TeleportSignLocation location) {
		if (cooldown.hasCooldown(player)) {
			return;
		}
		cooldown.setDefaultCooldown(player);
		GameServer server = plugin.signAtLocation(location).getServer();
		if (server == null) {
			return;
		}
		if (server.isOnline()) {
			ProxyTeleportEvent proxyTeleportEvent = plugin.fireTeleportEvent(player, server);
			if (!proxyTeleportEvent.isCancelled()) {
				teleportToServer(proxyTeleportEvent.getPlayer(), proxyTeleportEvent.getServerInfo().getName());
			}
		} else {
			//event.getPlayer().sendMessage(MessageHelper.getMessage("server.offline", server.getName()));
			//throw Exception
		}
	}

	public abstract void teleportToServer(String player, String server);
}
