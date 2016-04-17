package de.zh32.teleportsigns.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;

/**
 * @author zh32
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class GameServer implements Cloneable {
	private String name;
	private int playersOnline = 0;
	private int maxPlayers = 0;
	private String motd;
	private boolean online = false;
	private InetSocketAddress address;
	private String displayname;

	@Override
	public GameServer clone() {
		try {
			return (GameServer) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
}
