package de.zh32.teleportsigns;

import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.sign.SignLayout;
import de.zh32.teleportsigns.configuration.ConfigurationAdapter;
import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.task.TaskFactory;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.storage.TeleportSignSQLiteStorage;
import de.zh32.teleportsigns.storage.TeleportSignStorage;
import de.zh32.teleportsigns.task.Callback;
import de.zh32.teleportsigns.task.Task;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author zh32
 */
@Getter
public abstract class Application {

	private TeleportSignStorage storage;

	private final TaskFactory taskFactory;

	private final ConfigurationAdapter configuration;

	private List<TeleportSign> teleportSigns;

	private List<SignLayout> layouts;

	private List<GameServer> servers;

	private Task serverTask;

	public Application(TaskFactory taskFactory, ConfigurationAdapter configuration) {
		this.taskFactory = taskFactory;
		this.configuration = configuration;
	}

	public abstract ProxyTeleportEvent fireTeleportEvent(String player, GameServer server);


	public abstract void scheduleSignUpdates(List<TeleportSign> list);
	
	public void initialize() {
		layouts = configuration.loadLayouts();
		servers = configuration.loadServers();
		storage = new TeleportSignSQLiteStorage(configuration.getDatabasePath(), this);
		storage.initialize();
		teleportSigns = storage.loadAll();
		serverTask = taskFactory.serverUpdateTaskWith(servers).onFinish(new Callback<List<GameServer>>() {

			@Override
			public void finish(List<GameServer> result) {
				List<TeleportSign> list = new ArrayList<>();
				for (TeleportSign sign : teleportSigns) {
					if (result.contains(sign.getServer())) {
						list.add(sign);
					}
				}
				scheduleSignUpdates(list);
			}

		});

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
