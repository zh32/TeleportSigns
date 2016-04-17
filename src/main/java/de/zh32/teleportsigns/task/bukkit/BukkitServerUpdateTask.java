package de.zh32.teleportsigns.task.bukkit;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.server.status.ServerListPing;
import de.zh32.teleportsigns.task.ServerUpdateTask;

import java.util.List;

/**
 * @author zh32
 */
public class BukkitServerUpdateTask extends ServerUpdateTask implements Runnable {

	public BukkitServerUpdateTask(List<GameServer> servers, ServerListPing slp) {
		super(servers, slp);
	}

	@Override
	public void run() {
		this.execute();
	}

}
