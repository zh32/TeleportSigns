package de.zh32.teleportsigns;

import de.zh32.teleportsigns.configuration.ConfigurationAdapter;
import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.task.Callback;
import de.zh32.teleportsigns.task.ServerUpdateTask;
import de.zh32.teleportsigns.task.bukkit.BukkitServerUpdateTask;
import de.zh32.teleportsigns.task.bukkit.BukkitSignUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;


public class BukkitUpdateLoop extends UpdateLoop {

	private final Plugin plugin;
	private final ConfigurationAdapter configuration;
	private int bukkitTaskId;

	public BukkitUpdateLoop(Plugin plugin, ConfigurationAdapter configuration, List<TeleportSign> teleportSigns, ServerUpdateTask updateTask) {
		super(updateTask, teleportSigns);
		this.plugin = plugin;
		this.configuration = configuration;
	}

	@Override
	public void startUpdateLoop() {
		bukkitTaskId = Bukkit.getScheduler().runTaskAsynchronously(plugin, (BukkitServerUpdateTask) getServerUpdateTask()).getTaskId();
	}

	@Override
	public void rerunUpdateLoop() {
		bukkitTaskId = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (BukkitServerUpdateTask) getServerUpdateTask(), configuration.getUpdateInterval()).getTaskId();
	}

	@Override
	public void stopUpdateLoop() {
		Bukkit.getScheduler().cancelTask(bukkitTaskId);
	}

	@Override
	public void updateTeleportSigns(List<TeleportSign> list) {
		BukkitSignUpdateTask task = new BukkitSignUpdateTask(list, configuration.getUpdatePerTicks(), plugin);
		task.onFinish(new Callback() {

			@Override
			public void finish(Object result) {
				rerunUpdateLoop();
			}
		});
		Bukkit.getScheduler().runTask(plugin, task);
	}

}
