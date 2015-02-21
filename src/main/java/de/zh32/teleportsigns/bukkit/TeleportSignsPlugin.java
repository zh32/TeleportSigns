package de.zh32.teleportsigns.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author zh32
 */
public class TeleportSignsPlugin extends JavaPlugin {
	private BukkitTeleportSigns teleportSigns;

	@Override
	public void onEnable() {
		teleportSigns = new BukkitTeleportSigns(this);
		teleportSigns.initialize();
		teleportSigns.startUpdates();
	}

	
}
