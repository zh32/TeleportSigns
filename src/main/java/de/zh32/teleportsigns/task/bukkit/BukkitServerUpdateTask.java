package de.zh32.teleportsigns.task.bukkit;

import de.zh32.teleportsigns.BukkitPlugin;
import de.zh32.teleportsigns.configuration.BukkitConfiguration;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.task.ServerUpdateTask;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 *
 * @author zh32
 */
public class BukkitServerUpdateTask extends ServerUpdateTask implements Runnable {
	private final Plugin plugin;

	public BukkitServerUpdateTask(List<GameServer> servers, Plugin plugin) {
		super(servers, (BukkitConfiguration) ((BukkitPlugin)plugin).getConfiguration());
		this.plugin = plugin;
	}

	@Override
	public void run() {
		this.execute();
	}

}
