package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.sign.TeleportSign;
import lombok.Getter;

/**
 *
 * @author zh32
 */
public class SignDestroyer {
	
	@Getter
	private final DataContainer teleportSigns;

	public SignDestroyer(DataContainer teleportSigns) {
		this.teleportSigns = teleportSigns;
	}
	
	public void destroySign(TeleportSign sign) {
		teleportSigns.getStorage().delete(sign);
		teleportSigns.getTeleportSigns().remove(sign);
	}
}
