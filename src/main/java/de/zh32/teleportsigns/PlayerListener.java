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
import org.bukkit.entity.Player;
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

    private final TeleportSigns plugin;
    private final Map<String, Long> cooldowns;
    private final PluginData data;
    
    protected PlayerListener(TeleportSigns plugin) {
        this.plugin = plugin;
        this.data = plugin.getData();
        this.cooldowns = new HashMap<>();
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[tsigns]") && e.getPlayer().hasPermission("teleportsigns.create")) {
            final ServerInfo info = data.getServer(e.getLine(1));
            String layoutName = e.getLine(2).equals("") ? "default" : e.getLine(2);
            final SignLayout layout = data.getLayout(layoutName);
            if (layout != null) { 
                if (info != null) {
                    final TeleportSign ts = new TeleportSign(e.getLine(1), e.getBlock().getLocation(), layoutName);
                    data.addSign(ts);     
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

                        @Override
                        public void run() {
                            plugin.getUpdateUtil().updateSign(ts, layout, info);
                        }
                    }, 1L);
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
            TeleportSign ts = data.getSignForLocation(e.getBlock().getLocation());
            if (ts == null) return;
            if (e.getPlayer().hasPermission("teleportsigns.destroy")) {
                data.removeSign(ts);
                e.getPlayer().sendMessage(ChatColor.GREEN + "Sign destroyed");
            }
            else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.GREEN + "You can't destroy this Sign");
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getState() instanceof Sign 
                && e.getAction() == Action.RIGHT_CLICK_BLOCK 
                && e.getPlayer().hasPermission("teleportsigns.use")) {
            
            if (hasCooldown(e.getPlayer().getName())) return;
            TeleportSign ts = data.getSignForLocation(e.getClickedBlock().getLocation());
            if (ts == null) return;
            ServerInfo info = data.getServer(ts.getServer());
            if (data.getLayout(ts.getLayout()).isTeleport()) {
                if (info.isOnline()) {
                    ProxyTeleportEvent proxyTeleportEvent = new ProxyTeleportEvent(e.getPlayer(), info);
                    plugin.getServer().getPluginManager().callEvent(proxyTeleportEvent);
                    if (!proxyTeleportEvent.isCancelled()) {
                        teleportToServer(e.getPlayer(), info.getName());
                    }                                    
                }
                else {
                    if (data.isShowOfflineMsg()) {
                        e.getPlayer().sendMessage(
                                ChatColor.translateAlternateColorCodes('&', data.getOfflineMessage()));
                    }
                }
            }
        }      
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
    
    private boolean hasCooldown(String name) {
        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(name)) {
            if (now - cooldowns.get(name) < data.getCooldown()) {
                return true;
            }
        }
        cooldowns.put(name, now);
        return false;
    }
}
