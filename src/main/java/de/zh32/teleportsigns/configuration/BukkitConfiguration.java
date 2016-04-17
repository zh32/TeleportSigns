package de.zh32.teleportsigns.configuration;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.sign.SignLayout;
import de.zh32.teleportsigns.sign.TeleportSignLayout;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zh32
 */
public class BukkitConfiguration implements ConfigurationAdapter {

	private final Plugin plugin;

	public BukkitConfiguration(Plugin plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();
	}

	@Override
	public List<SignLayout> loadLayouts() {
		List<SignLayout> results = new ArrayList<>();
		ConfigurationSection layouts = plugin.getConfig().getConfigurationSection("layouts");
		for (String layout : layouts.getKeys(false)) {
			results.add(signFromConfigurationSection(layouts.getConfigurationSection(layout), layout));
		}
		Bukkit.getLogger().info(String.format("Loaded %d layouts from configuration.", results.size()));
		return results;
	}

	private SignLayout signFromConfigurationSection(ConfigurationSection cs, String layout) {
		return TeleportSignLayout.builder()
				.name(layout)
				.layout(cs.getStringList("layout").toArray(new String[0]))
				.numberPlaceHolder(cs.getString("offline-int"))
				.online(cs.getString("online"))
				.offline(cs.getString("offline"))
				.teleport(cs.getBoolean("teleport"))
				.build();
	}

	@Override
	public List<GameServer> loadServers() {
		List<GameServer> list = new ArrayList<>();
		ConfigurationSection server = plugin.getConfig().getConfigurationSection("servers");
		for (String servername : server.getKeys(false)) {
			list.add(serverFromConfigurationSection(server.getConfigurationSection(servername), servername));
		}
		Bukkit.getLogger().info(String.format("Loaded %d servers from configuration.", list.size()));
		return list;
	}

	private GameServer serverFromConfigurationSection(ConfigurationSection cs, String servername) throws NumberFormatException {
		String displayname = cs.getString("displayname");
		String[] addre = cs.getString("address").split(":");
		InetSocketAddress address = new InetSocketAddress(addre[0], Integer.parseInt(addre[1]));
		return new GameServer().setName(servername).setAddress(address).setDisplayname(displayname);
	}

	@Override
	public long getUpdateInterval() {
		return plugin.getConfig().getLong("update-interval", 100L);
	}

	@Override
	public int getTeleportCooldown() {
		return plugin.getConfig().getInt("teleport-cooldown", 2000);
	}

	@Override
	public String getDatabasePath() {
		return plugin.getDataFolder().getPath();
	}

	@Override
	public int getUpdatePerTicks() {
		return plugin.getConfig().getInt("updates-per-tick", 20);
	}

}
