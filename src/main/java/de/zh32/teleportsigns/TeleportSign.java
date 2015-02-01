package de.zh32.teleportsigns;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import de.zh32.teleportsigns.ping.GameServer;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Builder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author zh32
 */
@Data
@EqualsAndHashCode
@Builder
public class TeleportSign {

	@Id
	private int id;

	@NotEmpty
	private GameServer server;

	@NotEmpty
	private SignLayout layout;

	@NotEmpty
	private String worldName;

	@NotNull
	private double x;

	@NotNull
	private double y;

	@NotNull
	private double z;
	private Location location;

	private void setLocation(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
	}

	public Location getLocation() {
		World welt = Bukkit.getServer().getWorld(worldName);
		return new Location(welt, x, y, z);
	}

	public void update() {
		if (location.getWorld().getChunkAt(location).isLoaded()) {
			Block b = location.getBlock();
			if (b.getState() instanceof Sign) {
				Sign signBlock = (Sign) b.getState();
				String[] lines = layout.parseLayout(server);
				for (int i = 0; i < lines.length; i++) {
					signBlock.setLine(i, lines[i]);
				}
				signBlock.update();
			}
		}
	}
}
