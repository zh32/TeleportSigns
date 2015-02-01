package de.zh32.teleportsigns;


import de.zh32.teleportsigns.ping.GameServer;

/**
 *
 * @author zh32
 */
public interface SignLayout {
    
    public boolean isTeleport();
    
    public String[] parseLayout(GameServer sinfo);

    public String getName();
}
