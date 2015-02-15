package de.zh32.teleportsigns;

import de.zh32.teleportsigns.task.TaskFactory;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.storage.TeleportSignStorage;
import de.zh32.teleportsigns.server.status.ServerListPing;
import de.zh32.teleportsigns.task.Callback;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author zh32
 */
@Getter
public abstract class TeleportSignsPlugin {

	private final TeleportSignStorage storage;
	
	private final TaskFactory taskFactory;
	
	private final ConfigurationAdapter configuration;
	
	private List<TeleportSign> teleportSigns;

	private List<SignLayout> layouts;

	private List<GameServer> servers;

	public TeleportSignsPlugin(TeleportSignStorage storage, TaskFactory taskFactory, ConfigurationAdapter configuration) {
		this.storage = storage;
		this.taskFactory = taskFactory;
		this.configuration = configuration;
	}

	public abstract ProxyTeleportEvent fireTeleportEvent(String player, GameServer server);

	protected void initialize() {
		layouts = configuration.loadLayouts();
		servers = configuration.loadServers();
		teleportSigns = storage.loadAll();
		taskFactory.serverUpdateTaskWith(servers).onFinish(new Callback<List<GameServer>>() {

			@Override
			public void finish(List<GameServer> result) {
				ArrayList<TeleportSign> list = new ArrayList<>();
				for (TeleportSign sign : teleportSigns) {
					if (result.contains(sign.getServer())) {
						list.add(sign);
					}
				}
				taskFactory.signUpdateTaskWith(list).execute();
			}

		}).execute();

	}

	public TeleportSign signAtLocation(TeleportSign.TeleportSignLocation location) {
		for (TeleportSign sign : teleportSigns) {
			if (sign.getLocation().equals(location)) {
				return sign;
			}
		}
		return null;
	}

	public SignLayout layoutByName(String content) {
		for (SignLayout layout : layouts) {
			if (layout.getName().equals(content)) {
				return layout;
			}
		}
		return null;
	}

	public GameServer serverByName(String content) {
		for (GameServer server : servers) {
			if (server.getName().equals(content)) {
				return server;
			}
		}
		return null;
	}

}
