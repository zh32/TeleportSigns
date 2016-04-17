package de.zh32.teleportsigns;

import de.zh32.teleportsigns.configuration.ConfigurationAdapter;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.sign.SignLayout;
import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.storage.TeleportSignSQLiteStorage;
import de.zh32.teleportsigns.storage.TeleportSignStorage;
import lombok.Getter;

import java.util.List;

/**
 * @author zh32
 */
@Getter
public class DataContainer {

	private TeleportSignStorage storage;

	private final ConfigurationAdapter configuration;

	private List<TeleportSign> teleportSigns;

	private List<SignLayout> layouts;

	private List<GameServer> servers;

	public DataContainer(ConfigurationAdapter configuration) {
		this.configuration = configuration;
	}

	public void initialize() {
		layouts = configuration.loadLayouts();
		servers = configuration.loadServers();
		storage = new TeleportSignSQLiteStorage(configuration.getDatabasePath(), this);
		storage.initialize();
		teleportSigns = storage.loadAll();

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
