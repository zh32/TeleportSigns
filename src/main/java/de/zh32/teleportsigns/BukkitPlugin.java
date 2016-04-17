package de.zh32.teleportsigns;

import de.zh32.teleportsigns.configuration.BukkitConfiguration;
import de.zh32.teleportsigns.configuration.ConfigurationAdapter;
import de.zh32.teleportsigns.server.status.ServerListPing;
import de.zh32.teleportsigns.sign.BukkitServerTeleporter;
import de.zh32.teleportsigns.sign.BukkitSignCreator;
import de.zh32.teleportsigns.sign.BukkitSignDestroyer;
import de.zh32.teleportsigns.task.bukkit.BukkitServerUpdateTask;
import de.zh32.teleportsigns.utility.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author zh32
 */
public class BukkitPlugin extends JavaPlugin {

	private DataContainer dataContainer;
	private ConfigurationAdapter configuration;
	private BukkitUpdateLoop updateLoop;

	@Override
	public void onEnable() {
		configuration = new BukkitConfiguration(this);
		dataContainer = new DataContainer(configuration);
		dataContainer.initialize();
		updateLoop = new BukkitUpdateLoop(
				this,
				configuration,
				dataContainer.getTeleportSigns(),
				new BukkitServerUpdateTask(
						dataContainer.getServers(),
						new ServerListPing(getLogger())
				)
		);
		updateLoop.initialize();
		updateLoop.startUpdateLoop();
		loadMessages();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getPluginManager().registerEvents(new BukkitSignCreator(dataContainer, this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitServerTeleporter(dataContainer, this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitSignDestroyer(dataContainer), this);
	}

	@Override
	public void onDisable() {
		updateLoop.stopUpdateLoop();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("tsreload")) {
			if (!sender.hasPermission("teleportsigns.reload")) {
				sender.sendMessage(MessageHelper.getMessage("reload.nopermission"));
			}
			reloadConfig();
			updateLoop.stopUpdateLoop();
			dataContainer.initialize();
			updateLoop.initialize();
			updateLoop.startUpdateLoop();
			sender.sendMessage(MessageHelper.getMessage("reload.success"));
			return true;
		}
		;
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
