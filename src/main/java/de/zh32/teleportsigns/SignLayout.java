package de.zh32.teleportsigns;


import de.zh32.teleportsigns.ping.ServerInfo;

/**
 *
 * @author zh32
 */
public interface SignLayout {
    
    public boolean isTeleport();
    
    public String[] parseLayout(ServerInfo sinfo);

    public String getName();
}
