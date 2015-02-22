package de.zh32.teleportsigns.bukkit;

import de.zh32.teleportsigns.utility.MessageHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		loadMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("tsreload")) {
			if (!sender.hasPermission("teleportsigns.reload")) {
				sender.sendMessage(MessageHelper.getMessage("reload.nopermission"));
			}
			reloadConfig();
			teleportSigns.stopUpdates();
			teleportSigns.initialize();
			teleportSigns.startUpdates();
			sender.sendMessage(MessageHelper.getMessage("reload.success"));
			return true;
		};
		return false;
	}

	private void loadMessages() {
		try {
			InputStream is;
			File file = new File(getDataFolder(), "messages.properties");
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClassLoader().getResourceAsStream("messages.properties");
			}
			Properties properties = new Properties();
			properties.load(is);
			Map<String, String> messages = new HashMap<>();
			Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
			Iterator<Map.Entry<Object, Object>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<Object, Object> entry = it.next();
				messages.put((String) entry.getKey(), ChatColor.translateAlternateColorCodes('&', (String) entry.getValue()));
			}
			MessageHelper.setMessages(messages);
		} catch (IOException ex) {
			Bukkit.getLogger().warning("[TeleportSigns] Can't load messages!");
		}
	}

}
