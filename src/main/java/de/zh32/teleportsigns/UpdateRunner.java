package de.zh32.teleportsigns;

import java.util.Iterator;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author zh32
 */
public class UpdateRunner extends BukkitRunnable {
	private final int maxUpdatesPerTick;
	private int updatesThisTick;
	private final Iterator<TeleportSign> it;
	private final Plugin plugin;

	public UpdateRunner(List<TeleportSign> teleportSigns, int updatesPerTick, Plugin plugin) {
		this.maxUpdatesPerTick = updatesPerTick;
		this.it = teleportSigns.iterator();
		this.plugin = plugin;
	}

	@Override
	public void run() {
		updatesThisTick = 0;
		while (it.hasNext() && updatesThisTick < maxUpdatesPerTick) {
			it.next().update();
			updatesThisTick++;
		}
		if (it.hasNext()) {
			runTaskLater(plugin, 1L);
		}
	}

}
