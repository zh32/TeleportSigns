package de.zh32.teleportsigns;

import de.zh32.teleportsigns.configuration.BukkitConfiguration;
import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.event.BukkitProxyTeleportEvent;
import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.Application;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.task.Callback;
import de.zh32.teleportsigns.task.bukkit.BukkitServerUpdateTask;
import de.zh32.teleportsigns.task.bukkit.BukkitSignUpdateTask;
import de.zh32.teleportsigns.task.bukkit.BukkitTaskFactory;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author zh32
 */
public class BukkitApplication extends Application {
	private final Plugin plugin;
	private BukkitTask serverUpdateBukkitTask;

	public BukkitApplication(Plugin plugin) {
		super(new BukkitTaskFactory(plugin), new BukkitConfiguration(plugin));
		this.plugin = plugin;
	}

	@Override
	public void initialize() {
		super.initialize();
		Bukkit.getLogger().info(String.format("Loaded %d signs from database.", getTeleportSigns().size()));
		Bukkit.getPluginManager().registerEvents(new BukkitSignCreator(this, plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new BukkitServerTeleporter(this, plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new BukkitSignDestroyer(this), plugin);
	}

	public void startUpdates() {
		serverUpdateBukkitTask = Bukkit.getScheduler().runTaskAsynchronously(plugin, (BukkitServerUpdateTask) getServerTask());
	}
	
	public void stopUpdates() {
		serverUpdateBukkitTask.cancel();
	}

	@Override
	public ProxyTeleportEvent fireTeleportEvent(String player, GameServer server) {
		BukkitProxyTeleportEvent event = new BukkitProxyTeleportEvent(player, server);
		Bukkit.getPluginManager().callEvent(event);
		return new ProxyTeleportEvent()
				.setPlayer(event.getPlayer().getName())
				.setServerInfo(serverByName(event.getServer()))
				.setCancelled(event.isCancelled());
	}

	@Override
	public void scheduleSignUpdates(List<TeleportSign> list) {
		BukkitSignUpdateTask task = (BukkitSignUpdateTask) getTaskFactory().signUpdateTaskWith(list);
		task.onFinish(new Callback() {

			@Override
			public void finish(Object result) {
				Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (BukkitServerUpdateTask) getServerTask(), getConfiguration().getUpdateInterval());
			}
		});
		Bukkit.getScheduler().runTask(plugin, task);
	}

}
