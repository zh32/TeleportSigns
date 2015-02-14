package de.zh32.teleportsigns;

import de.zh32.teleportsigns.storage.TeleportSignStorage;
import de.zh32.teleportsigns.storage.TeleportSignSQLiteStorage;
import de.zh32.teleportsigns.task.ServerUpdateTask;
import de.zh32.teleportsigns.task.SignUpdateTask;
import de.zh32.teleportsigns.ping.ServerListPing;
import de.zh32.teleportsigns.ping.StatusResponse;
import de.zh32.teleportsigns.task.Callback;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 *
 * @author zh32
 */
@Getter
public abstract class TeleportSignsPlugin {

	private List<TeleportSign> teleportSigns;

	private List<SignLayout> layouts;

	private List<GameServer> servers;

	private TeleportSignStorage storage;

	public abstract List<SignLayout> loadLayouts();

	public abstract List<GameServer> loadServers();

	public abstract SignUpdateTask signUpdateTaskWith(List<TeleportSign> signs);

	public abstract ServerUpdateTask serverUpdateTaskWith(List<GameServer> servers);

	public abstract ProxyTeleportEvent fireTeleportEvent(String player, GameServer server);

	protected void initialize() {
		layouts = loadLayouts();
		servers = loadServers();
		storage = new TeleportSignSQLiteStorage(this);
		teleportSigns = storage.loadAll();
		final ServerListPing ping = new ServerListPing();
		//start ping
		serverUpdateTaskWith(servers).onFinish(new Callback<List<GameServer>>() {

			@Override
			public void finish(List<GameServer> result) {
				ArrayList<TeleportSign> list = new ArrayList<>();
				for (TeleportSign sign : teleportSigns) {
					if (result.contains(sign.getServer())) {
						list.add(sign);
					}
				}
				signUpdateTaskWith(list).execute();
			}

		}).execute();

	}

	public GameServer signAt(TeleportSign.TeleportSignLocation location) {
		for (TeleportSign sign : teleportSigns) {
			if (sign.getLocation().equals(location)) {
				return sign.getServer();
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
