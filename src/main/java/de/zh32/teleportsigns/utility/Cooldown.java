package de.zh32.teleportsigns.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zh32
 */
public class Cooldown {
	private final int cooldownTime;
	private Map<String, Long> cooldowns;

	public Cooldown(int cooldownTime) {
		this.cooldownTime = cooldownTime;
		cooldowns = new HashMap<>();
	}

	public boolean hasCooldown(String player) {
		if (!cooldowns.containsKey(player)) return false;
		return (System.currentTimeMillis() - cooldowns.get(player)) < cooldownTime;
	}

	public void setDefaultCooldown(String player) {
		cooldowns.put(player, System.currentTimeMillis());
	}

}
