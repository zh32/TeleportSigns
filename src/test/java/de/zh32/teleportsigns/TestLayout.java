package de.zh32.teleportsigns;

import de.zh32.teleportsigns.sign.SignLayout;
import de.zh32.teleportsigns.server.GameServer;

/**
 *
 * @author zh32
 */
public class TestLayout implements SignLayout {

	@Override
	public boolean isTeleport() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String[] renderLayoutFor(GameServer sinfo) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getName() {
		return "default";
	}

}
