package de.zh32.teleportsigns;

import de.zh32.teleportsigns.sign.SignLayout;
import de.zh32.teleportsigns.configuration.ConfigurationAdapter;
import de.zh32.teleportsigns.server.GameServer;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zh32
 */
public class TestConfiguration implements ConfigurationAdapter {

	@Override
	public List<SignLayout> loadLayouts() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<GameServer> loadServers() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public long getUpdateInterval() {
		return 100L;
	}

	@Override
	public int getTeleportCooldown() {
		return 2000;
	}

	@Override
	public String getDatabasePath() {
		return "./";
	}

}
