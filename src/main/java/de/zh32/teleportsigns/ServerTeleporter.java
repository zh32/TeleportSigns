package de.zh32.teleportsigns;

import de.zh32.teleportsigns.utility.Cooldown;
import de.zh32.teleportsigns.utility.MessageHelper;
import de.zh32.teleportsigns.repository.TeleportSignRepository;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author zh32
 */
public class ServerTeleporter implements Listener {

	private static final String TELEPORT_PERMISSION = "teleportsigns.use";
	private final Plugin plugin;
	private final TeleportSignRepository signRepository;
	private final Cooldown cooldown;

	public ServerTeleporter(Plugin plugin, TeleportSignRepository signRepository) {
		this.plugin = plugin;
		this.signRepository = signRepository;
		this.cooldown = new Cooldown(2000);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onClick(PlayerInteractEvent event) {
		if (!isTeleportSignAction(event)) {
			return;
		}
		if (!event.getPlayer().hasPermission(TELEPORT_PERMISSION)) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("sign.use.nopermission"));
			return;
		}
		if (cooldown.hasCooldown(event.getPlayer())) {
			return;
		}
		cooldown.setDefaultCooldown(event.getPlayer());
		TeleportSign teleportSign = signRepository.byLocation(event.getClickedBlock().getLocation());
		if (teleportSign == null) {
			return;
		}
		if (teleportSign.getServer().isOnline()) {
			ProxyTeleportEvent proxyTeleportEvent = new ProxyTeleportEvent(event.getPlayer(), teleportSign.getServer());
			if (plugin != null) {
				Bukkit.getServer().getPluginManager().callEvent(proxyTeleportEvent);
			}
			if (!proxyTeleportEvent.isCancelled()) {
				teleportToServer(event.getPlayer(), teleportSign.getServer().getName());
			}
		} else {
			event.getPlayer().sendMessage(MessageHelper.getMessage("server.offline", teleportSign.getServer().getName()));
		}
	}

	private boolean isTeleportSignAction(PlayerInteractEvent event) {
		return event.hasBlock()
				&& event.getClickedBlock().getState() instanceof Sign
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK;
	}

	private void teleportToServer(Player player, String serverName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(serverName);
		} catch (IOException eee) {
			Bukkit.getLogger().info("You'll never see me!");
		}
		player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
}
