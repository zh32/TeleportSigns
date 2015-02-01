package de.zh32.teleportsigns.repository;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.storage.TeleportSignStorage;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author zh32
 */
public class TeleportSignRepository {

	private TeleportSignStorage signStorage;
	private List<TeleportSign> teleportSigns;
	private Plugin plugin;
	
	public void initialize() {
		teleportSigns = signStorage.loadAll();
	}
	
	public void save(final TeleportSign teleportSign) {
		teleportSigns.add(teleportSign);
		new BukkitRunnable() {

			@Override
			public void run() {
				signStorage.save(teleportSign);
			}
		}.runTaskAsynchronously(plugin);
	}

	public TeleportSign byLocation(Location location) {
		for (TeleportSign sign : teleportSigns) {
			if (sign.getLocation().equals(location)) {
				return sign;
			}
		}
		return null;
	}
	
	public List<TeleportSign> all() {
		return teleportSigns;
	}
	
}
