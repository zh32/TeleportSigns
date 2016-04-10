package de.zh32.teleportsigns.task.bukkit;

import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.task.SignUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @author zh32
 */
public class BukkitSignUpdateTask extends SignUpdateTask implements Runnable {

	private final Plugin plugin;

	public BukkitSignUpdateTask(List<TeleportSign> teleportSigns, int updatesPerTick, Plugin plugin) {
		super(teleportSigns, updatesPerTick);
		this.plugin = plugin;
	}

	@Override
	public void runTaskLater() {
		Bukkit.getScheduler().runTaskLater(plugin, this, 1L);
	}

	@Override
	public void updateSign(TeleportSign sign) {
		Location location = new Location(
				Bukkit.getWorld(sign.getLocation().getWorldName()),
				sign.getLocation().getX(),
				sign.getLocation().getY(),
				sign.getLocation().getZ()
		);
		Block block = location.getBlock();
		if (location.getWorld().getChunkAt(location).isLoaded()) {
			if (block.getState() instanceof Sign) {
				Sign signBlock = (Sign) block.getState();
				String[] lines = sign.getLayout().renderLayoutFor(sign.getServer());
				for (int i = 0; i < lines.length; i++) {
					signBlock.setLine(i, lines[i]);
				}
				signBlock.update();
			}
		}
	}

	@Override
	public void run() {
		this.execute();
	}

}
