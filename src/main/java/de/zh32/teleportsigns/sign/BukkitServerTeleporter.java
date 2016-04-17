package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.event.BukkitEventAdapter;
import de.zh32.teleportsigns.sign.TeleportSign.TeleportSignLocation;
import de.zh32.teleportsigns.utility.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author zh32
 */
public class BukkitServerTeleporter extends ServerTeleporter implements Listener {
	private static final String TELEPORT_PERMISION = "teleportsigns.use";
	private final Plugin bukkitPlugin;

	public BukkitServerTeleporter(DataContainer dataContainer, Plugin bukkitPlugin) {
		super(dataContainer, new BukkitEventAdapter());
		this.bukkitPlugin = bukkitPlugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!isTeleportSignAction(event)) {
			return;
		}
		if (!event.getPlayer().hasPermission(TELEPORT_PERMISION)) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("teleport.nopermission"));
			return;
		}
		TeleportSign teleportSign = findTeleportSign(event);
		if (teleportSign != null) {
			teleportPlayer(event.getPlayer().getName(), teleportSign);
		}
	}

	private boolean isTeleportSignAction(PlayerInteractEvent event) {
		return event.hasBlock()
				&& event.getClickedBlock().getState() instanceof Sign
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK;
	}

	private TeleportSign findTeleportSign(PlayerInteractEvent event) {
		return getDataFinder().signAtLocation(new TeleportSignLocation(
				event.getClickedBlock().getX(),
				event.getClickedBlock().getY(),
				event.getClickedBlock().getZ(),
				event.getClickedBlock().getWorld().getName()
		));
	}

	@Override
	public void teleportToServer(String player, String serverName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(serverName);
		} catch (IOException eee) {
			Bukkit.getLogger().info("You'll never see me!");
		}
		Bukkit.getPlayer(player).sendPluginMessage(bukkitPlugin, "BungeeCord", b.toByteArray());
	}

}
