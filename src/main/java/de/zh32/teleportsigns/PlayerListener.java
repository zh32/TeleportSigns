package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.ServerInfo;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    private final TeleportSignsPlugin plugin;
    private final Map<String, Long> cooldowns;
    
    protected PlayerListener(TeleportSignsPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[tsigns]") && e.getPlayer().hasPermission("teleportsigns.create")) {
            ServerInfo info = plugin.getData().getServer(e.getLine(1));
            String layout = e.getLine(2);
            if (layout.equalsIgnoreCase("")) {
                layout = "default";
            }
            if (plugin.getData().getLayout(layout) != null) {
                
                if (info != null) {
                    plugin.getDatabase().save(new TeleportSign(e.getLine(1), e.getBlock().getLocation(), layout));
                    plugin.getData().loadSigns();
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Sign created.");
                }
                else {
                    e.getPlayer().sendMessage(ChatColor.RED + "Can't find this server!");
                }
            }
            else {
                e.getPlayer().sendMessage(ChatColor.RED + "Can't find this layout!");
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getBlock().getState();
            TeleportSign ts = plugin.getDatabase().find(TeleportSign.class).where().
                    eq("x", e.getBlock().getLocation().getX()).
                    eq("y", e.getBlock().getLocation().getY()).
                    eq("z", e.getBlock().getLocation().getZ()).findUnique();
            if (ts != null && e.getPlayer().hasPermission("teleportsigns.destroy")) {
                plugin.getDatabase().delete(ts);
                plugin.getData().loadSigns();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Sign destroyed.");
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getState() instanceof Sign && 
                e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().hasPermission("teleportsigns.use")) {
            if (!hasCooldown(e.getPlayer().getName())) {
                for (TeleportSign ts : plugin.getSigns()) {
                    if (ts != null) {
                        if (ts.getLocation().equals(e.getClickedBlock().getLocation())) {
                            ServerInfo info = plugin.getData().getServer(ts.getServer());
                            if (info != null) {
                                if (plugin.getData().getLayout(ts.getLayout()).isTeleport()) {
                                    if (info.isOnline()) {
                                        ProxyTeleportEvent proxyTeleportEvent = new ProxyTeleportEvent(e.getPlayer(), info);
                                        plugin.getServer().getPluginManager().callEvent(proxyTeleportEvent);
                                        if (!proxyTeleportEvent.isCancelled()) {
                                            ByteArrayOutputStream b = new ByteArrayOutputStream();
                                            DataOutputStream out = new DataOutputStream(b);
                                            try {
                                                out.writeUTF("Connect");
                                                out.writeUTF(proxyTeleportEvent.getServerInfo().getName());
                                            } catch (IOException eee) {
                                                Bukkit.getLogger().info("You'll never see me!");
                                            }
                                            e.getPlayer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                                        }                                    
                                    }
                                    else {
                                        if (plugin.getData().isShowOfflineMsg()) {
                                            e.getPlayer().sendMessage(
                                                    ChatColor.translateAlternateColorCodes('&', plugin.getData().getOfflineMessage()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }      
    }
    
    private boolean hasCooldown(String name) {
        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(name)) {
            if (now - cooldowns.get(name) < plugin.getData().getCooldown()) {
                return true;
            }
        }
        cooldowns.put(name, now);
        return false;
    }
}
