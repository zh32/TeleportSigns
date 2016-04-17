package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.server.status.ServerListPing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zh32
 */
public abstract class ServerUpdateTask extends Task<List<GameServer>> {

	private final List<GameServer> servers;
	private final ServerListPing ping;

	public ServerUpdateTask(List<GameServer> servers, ServerListPing slp) {
		this.servers = servers;
		this.ping = slp;
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
	}

}
