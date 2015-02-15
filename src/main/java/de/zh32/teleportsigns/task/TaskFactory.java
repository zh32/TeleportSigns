package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.server.GameServer;
import java.util.List;

/**
 *
 * @author zh32
 */
public interface TaskFactory {

	SignUpdateTask signUpdateTaskWith(List<TeleportSign> signs);

	ServerUpdateTask serverUpdateTaskWith(List<GameServer> servers);
	
}
