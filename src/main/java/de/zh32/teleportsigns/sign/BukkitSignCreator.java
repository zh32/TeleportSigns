package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.sign.TeleportSign.TeleportSignLocation;
import de.zh32.teleportsigns.task.bukkit.BukkitSignUpdateTask;
import de.zh32.teleportsigns.utility.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

/**
 * @author zh32
 */
public class BukkitSignCreator extends SignCreator implements Listener {
	private static final String CREATE_PERMISSION = "teleportsigns.create";
	private final Plugin bukkitPlugin;

	public BukkitSignCreator(DataContainer tsigns, Plugin plugin) {
		super(tsigns);
		this.bukkitPlugin = plugin;
	}

	@EventHandler
	public void onSignChanged(SignChangeEvent event) {
		if (!isTeleportSignCreated(event.getLines())) {
			return;
		}
		if (!event.getPlayer().hasPermission(CREATE_PERMISSION)) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("create.nopermission"));
			return;
		}
		try {
			final TeleportSign sign = createSign(
					event.getLines(),
					new TeleportSignLocation(
							event.getBlock().getX(),
							event.getBlock().getY(),
							event.getBlock().getZ(),
							event.getBlock().getWorld().getName()
					)
			);
			Bukkit.getScheduler().runTaskLater(bukkitPlugin, new Runnable() {

				@Override
				public void run() {
					new BukkitSignUpdateTask(Arrays.asList(sign), 1, bukkitPlugin).execute();
				}
			}, 1L);
			event.getPlayer().sendMessage(MessageHelper.getMessage("create.success"));
		} catch (TeleportSignCreationException ex) {
			event.getPlayer().sendMessage(ex.getMessage());
			event.getBlock().breakNaturally();
		}
	}

}
