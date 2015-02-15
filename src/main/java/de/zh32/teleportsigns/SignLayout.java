package de.zh32.teleportsigns;

import de.zh32.teleportsigns.server.GameServer;

/**
 *
 * @author zh32
 */
public interface SignLayout {
    
    public boolean isTeleport();
    
    public String[] renderLayoutFor(GameServer sinfo);

    public String getName();
}
