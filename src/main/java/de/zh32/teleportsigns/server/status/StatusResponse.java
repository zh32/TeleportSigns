package de.zh32.teleportsigns.server.status;

/**
 * Created by zh32 on 16.04.16.
 */
public interface StatusResponse {

	Integer getOnlinePlayers();

	Integer getMaxPlayers();

	String getDescription();
}
