package de.zh32.teleportsigns.task.bukkit;

import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.task.ServerUpdateTask;
import de.zh32.teleportsigns.task.SignUpdateTask;
import de.zh32.teleportsigns.task.TaskFactory;
import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author zh32
 */
public class BukkitTaskFactory implements TaskFactory {
	
	private final Plugin plugin;

	public BukkitTaskFactory(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public SignUpdateTask signUpdateTaskWith(List<TeleportSign> signs) {
		return new BukkitSignUpdateTask(signs, 10, plugin);
	}

	@Override
	public ServerUpdateTask serverUpdateTaskWith(List<GameServer> servers) {
		return new BukkitServerUpdateTask(servers, plugin);
	}

}
