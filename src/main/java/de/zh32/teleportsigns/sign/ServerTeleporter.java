package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.event.EventAdapter;
import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.server.GameServer;
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

	public void teleportPlayer(String player, TeleportSign.TeleportSignLocation location) {
		if (cooldown.hasCooldown(player)) {
			return;
		}
		cooldown.setDefaultCooldown(player);
    if(dataFinder.signAtLocation(location) != null){
      GameServer server = dataFinder.signAtLocation(location).getServer();
      if (server == null) {
        return;
      }
      if (server.isOnline()) {
        ProxyTeleportEvent proxyTeleportEvent = eventFactory.callTeleportEvent(player, server);
        if (!proxyTeleportEvent.isCancelled()) {
//				teleportToServer(proxyTeleportEvent.getPlayerName(), proxyTeleportEvent.getServer().getName());
          teleportToServer(proxyTeleportEvent.getPlayerName(), server.getName());
        }
      } else {
        //event.getPlayer().sendMessage(MessageHelper.getMessage("server.offline", server.getName()));
        //throw Exception
      }
    }
  }

	public abstract void teleportToServer(String player, String server);
}
