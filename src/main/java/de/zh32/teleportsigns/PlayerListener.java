package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.Ping;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author zh32
 */
class PlayerListener implements Listener {

    private TeleportSigns plugin;
    public PlayerListener(TeleportSigns plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    private void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[tsigns]") && e.getPlayer().hasPermission("teleportsigns.create")) {
            TeleportSign ts = new TeleportSign();
            ts.setServer(e.getLine(1));
            ts.setLocation(e.getBlock().getLocation());
            plugin.getDatabase().save(ts);
            plugin.loadSigns();
            e.getPlayer().sendMessage(ChatColor.GREEN + "Sign created.");
        }
    }
    
    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getBlock().getState();
            TeleportSign ts = plugin.getDatabase().find(TeleportSign.class).where().
                    eq("x", e.getBlock().getLocation().getX()).
                    eq("y", e.getBlock().getLocation().getY()).
                    eq("z", e.getBlock().getLocation().getZ()).findUnique();
            if (ts != null && e.getPlayer().hasPermission("teleportsigns.destroy")) {
                plugin.getDatabase().delete(ts);
                plugin.loadSigns();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Sign destroyed.");
            }
        }
    }
    
    @EventHandler
    private void onClick(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getState() instanceof Sign && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().hasPermission("teleportsigns.use")) {
            for (TeleportSign ts : plugin.signs) {
                if (ts != null) {
                    if (ts.getLocation().equals(e.getClickedBlock().getLocation())) {
                        if (Ping.getInstance().serverinfos.get(ts.getServer()) != null) {
                            if (Ping.getInstance().serverinfos.get(ts.getServer()).isOnline()) {
                                ByteArrayOutputStream b = new ByteArrayOutputStream();
                                DataOutputStream out = new DataOutputStream(b);
                                try {
                                    out.writeUTF("Connect");
                                    out.writeUTF(ts.getServer());
                                } catch (IOException eee) {
                                    Bukkit.getLogger().info("You'll never see me!");
                                }
                                e.getPlayer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                            }
                            else {
                                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.offline_message));
                            }
                        }
                    }
                }
            }
        }      
    }
}
