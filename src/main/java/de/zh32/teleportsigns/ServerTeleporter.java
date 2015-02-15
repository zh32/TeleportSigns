package de.zh32.teleportsigns;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.utility.Cooldown;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 *
 * @author zh32
 */
@Getter
public abstract class ServerTeleporter {

	private final Cooldown cooldown;
	private final TeleportSignsPlugin plugin;

	public ServerTeleporter(TeleportSignsPlugin plugin) {
		this.cooldown = new Cooldown(2000);
		this.plugin = plugin;
	}
	
	public void teleportPlayer(PlayerTeleport teleport) {
		if (cooldown.hasCooldown(teleport.getPlayer())) {
			return;
		}
		cooldown.setDefaultCooldown(teleport.getPlayer());
		GameServer server = plugin.signAt(teleport.getLocation());
		if (server == null) {
			return;
		}
		if (server.isOnline()) {
			ProxyTeleportEvent proxyTeleportEvent = plugin.fireTeleportEvent(teleport.getPlayer(), server);
			if (!proxyTeleportEvent.isCancelled()) {
				teleportToServer(proxyTeleportEvent.getPlayer(), proxyTeleportEvent.getServerInfo().getName());
			}
		} else {
			//event.getPlayer().sendMessage(MessageHelper.getMessage("server.offline", server.getName()));
			//throw Exception
		}
	}

	abstract void teleportToServer(String player, String server);

	@Data
	@Accessors(chain = true)
	public static class PlayerTeleport {
		private String player;
		private TeleportSign.TeleportSignLocation location;
		
	}
}
