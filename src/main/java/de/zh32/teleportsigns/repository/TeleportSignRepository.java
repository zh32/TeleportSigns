package de.zh32.teleportsigns.repository;

import de.zh32.teleportsigns.TeleportSign;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author zh32
 */
public interface TeleportSignRepository {

	void save(TeleportSign teleportSign);

	TeleportSign byLocation(Location location);
	
	List<TeleportSign> all();

}
