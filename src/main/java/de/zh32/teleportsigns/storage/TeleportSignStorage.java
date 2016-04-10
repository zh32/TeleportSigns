package de.zh32.teleportsigns.storage;

import de.zh32.teleportsigns.sign.TeleportSign;

import java.util.List;

/**
 * @author zh32
 */
public interface TeleportSignStorage {
	List<TeleportSign> loadAll();

	void save(TeleportSign teleportSign);

	void delete(TeleportSign teleportSign);

	public void initialize();
}
