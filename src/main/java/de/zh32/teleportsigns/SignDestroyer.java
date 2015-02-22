package de.zh32.teleportsigns;

import de.zh32.teleportsigns.sign.TeleportSign;
import lombok.Getter;

/**
 *
 * @author zh32
 */
public class SignDestroyer {
	
	@Getter
	private final Application teleportSigns;

	public SignDestroyer(Application teleportSigns) {
		this.teleportSigns = teleportSigns;
	}
	
	public void destroySign(TeleportSign sign) {
		teleportSigns.getStorage().delete(sign);
		teleportSigns.getTeleportSigns().remove(sign);
	}
}
