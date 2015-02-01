package de.zh32.teleportsigns.repository;

import de.zh32.teleportsigns.ping.GameServer;

/**
 *
 * @author zh32
 */
public interface GameServerRepository {

	GameServer byName(String line);

}
