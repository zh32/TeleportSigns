package de.zh32.teleportsigns;

import de.zh32.teleportsigns.server.GameServer;
import java.util.List;

/**
 *
 * @author zh32
 */
public interface ConfigurationAdapter {

	List<SignLayout> loadLayouts();

	List<GameServer> loadServers();

}
