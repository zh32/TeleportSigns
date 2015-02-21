package de.zh32.teleportsigns;

import lombok.Getter;

/**
 *
 * @author zh32
 */
public class SignDestroyer {
	
	@Getter
	private final TeleportSigns teleportSigns;

	public SignDestroyer(TeleportSigns teleportSigns) {
		this.teleportSigns = teleportSigns;
	}
	
	public void destroySign(TeleportSign sign) {
		teleportSigns.getStorage().delete(sign);
		teleportSigns.getTeleportSigns().remove(sign);
	}
}
