package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.event.EventAdapter;
import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.utility.Cooldown;
import lombok.Getter;

/**
 * @author zh32
 */
@Getter
public abstract class ServerTeleporter {

	private final Cooldown cooldown;
	private final DataContainer dataFinder;
	private final EventAdapter eventFactory;

	public ServerTeleporter(DataContainer dataFinder, EventAdapter eventFactory) {
		this.cooldown = new Cooldown(dataFinder.getConfiguration().getTeleportCooldown());
		this.dataFinder = dataFinder;
		this.eventFactory = eventFactory;
	}

	public void teleportPlayer(String player, TeleportSign teleportSign) {
		if (cooldown.hasCooldown(player)) {
			return;
		}
		cooldown.setDefaultCooldown(player);
		if (teleportSign.getServer().isOnline()) {
			ProxyTeleportEvent proxyTeleportEvent = eventFactory.callTeleportEvent(player, teleportSign.getServer());
			if (!proxyTeleportEvent.isCancelled()) {
				teleportToServer(proxyTeleportEvent.getPlayerName(), proxyTeleportEvent.getServer().getName());
			}
		}
	}

	public abstract void teleportToServer(String player, String server);
}
