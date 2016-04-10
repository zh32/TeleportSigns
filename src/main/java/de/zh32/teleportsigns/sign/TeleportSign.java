package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.server.GameServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

/**
 * @author zh32
 */
@Data
@Builder
@AllArgsConstructor
public class TeleportSign {

	private GameServer server;

	private SignLayout layout;

	private TeleportSignLocation location;

	@Data
	@Accessors(chain = true)
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class TeleportSignLocation {
		private int x;
		private int y;
		private int z;
		private String worldName;
	}

	public boolean equals(Object object) {
		if (!(object instanceof TeleportSign)) {
			return false;


		}
		TeleportSign teleportSign = (TeleportSign) object;
		if (teleportSign.getLocation().equals(this.location)) {
			return true;
		}
		return false;
	}
}
