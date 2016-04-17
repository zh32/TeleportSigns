package de.zh32.teleportsigns.server.status;

import lombok.Data;

import java.util.List;

/**
 * @author zh32 <zh32 at zh32.de>
 */
@Data
public class StatusResponse17 implements StatusResponse {

	private String description;
	private Players players;
	private Version version;
	private String favicon;
	private int time;

	@Override
	public Integer getOnlinePlayers() {
		return players.getOnline();
	}

	@Override
	public Integer getMaxPlayers() {
		return players.getMax();
	}

	@Data
	public class Players {
		private int max;
		private int online;
		private List<Player> sample;
	}

	@Data
	public class Player {
		private String name;
		private String id;

	}

	@Data
	public class Version {
		private String name;
		private int protocol;
	}
}