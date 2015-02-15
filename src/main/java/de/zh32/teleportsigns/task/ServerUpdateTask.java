package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.server.status.ServerListPing;
import de.zh32.teleportsigns.server.status.StatusResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zh32
 */
public abstract class ServerUpdateTask implements Task<List<GameServer>> {
	private final List<GameServer> servers;
	private final ServerListPing ping;
	private Callback<List<GameServer>> callback;

	private ServerUpdateTask(List<GameServer> servers) {
		this.servers = servers;
		this.ping = new ServerListPing();
	}

	@Override
	public void execute() {
		List<GameServer> updatedServers = new ArrayList<>();
		for (GameServer server : servers) {
			ping.setHost(server.getAddress());
			StatusResponse fetchData = ping.fetchData();
			server.setMotd(fetchData.getDescription());
			updatedServers.add(server);
		}
		callback.finish(updatedServers);
	}

	@Override
	public ServerUpdateTask onFinish(Callback<List<GameServer>> callback) {
		this.callback = callback;
		return this;
	}

}
