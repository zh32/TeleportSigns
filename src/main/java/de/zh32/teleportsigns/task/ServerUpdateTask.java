package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.configuration.BukkitConfiguration;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.server.status.ServerListPing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zh32
 */
public abstract class ServerUpdateTask extends Task<List<GameServer>> {

	private final List<GameServer> servers;
	private final ServerListPing ping;
  private final BukkitConfiguration configuration;

	public ServerUpdateTask(List<GameServer> servers, BukkitConfiguration configuration) {
		this.servers = servers;
		this.ping = new ServerListPing();
    this.configuration = configuration;
	}

	@Override
	public void execute() {
		List<GameServer> updatedServers = new ArrayList<>();
		for (GameServer server : servers) {
			if (ping.updateStatus(server)) {
				updatedServers.add(server);
			}
		}
		finish(updatedServers);
    if(configuration.getDebugMode()) {
      System.out.println("updated servers: " + updatedServers.size());
    }
	}
}
