package de.zh32.teleportsigns.configuration;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.sign.SignLayout;

import java.util.List;

/**
 * @author zh32
 */
public interface ConfigurationAdapter {

	List<SignLayout> loadLayouts();

	List<GameServer> loadServers();

	public long getUpdateInterval();

	public int getTeleportCooldown();

	public String getDatabasePath();

	public int getUpdatePerTicks();

}
