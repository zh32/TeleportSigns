package de.zh32.teleportsigns.bukkit;

import de.zh32.teleportsigns.SignCreator;
import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSign.TeleportSignLocation;
import de.zh32.teleportsigns.TeleportSigns;
import de.zh32.teleportsigns.utility.MessageHelper;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author zh32
 */
public class BukkitSignCreator extends SignCreator implements Listener {
	private static final String CREATE_PERMISSION = "teleportsigns.create";
	private final Plugin bukkitPlugin;

	public BukkitSignCreator(TeleportSigns tsigns, Plugin plugin) {
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
					getPlugin().getTaskFactory().signUpdateTaskWith(Arrays.asList(sign)).execute();
				}
			}, 1L);
			event.getPlayer().sendMessage(MessageHelper.getMessage("create.success"));
		} catch (TeleportSignCreationException ex) {
			event.getPlayer().sendMessage(ex.getMessage());
			event.getBlock().breakNaturally();
		}
	}

}
