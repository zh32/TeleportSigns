package de.zh32.teleportsigns.ping;

import lombok.Getter;

/**
 *
 * @author zh32
 */
public class Result {
    
    @Getter
    private int playersOnline;
    @Getter
    private int maxPlayers;
    @Getter
    private String motd;
    @Getter
    private boolean online;

    public Result(int playersOnline, int maxPlayers, String motd, boolean b) {
        this.playersOnline = playersOnline;
        this.maxPlayers = maxPlayers;
        this.motd = motd;
        this.online = b;
    }

    public Result(boolean b) {
        this.online = b;
    }

}
