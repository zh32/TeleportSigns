package de.zh32.teleportsigns.utility;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 *
 * @author zh32
 */
public class Cooldown {
	private final int cooldownTime;
	private Map<String, Long> cooldowns;

	public Cooldown(int cooldownTime) {
		this.cooldownTime = cooldownTime;
		cooldowns = new HashMap<>();
	}

	public boolean hasCooldown(Player player) {
		if (!cooldowns.containsKey(player.getName())) return false;
		return (System.currentTimeMillis() - cooldowns.get(player.getName())) < cooldownTime;
	}

	public void setDefaultCooldown(Player player) {
		cooldowns.put(player.getName(), System.currentTimeMillis());
	}

}
