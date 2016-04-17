package de.zh32.teleportsigns.server.status;

import lombok.Data;

import java.util.List;

/**
 * @author zh32 <zh32 at zh32.de>
 */
@Data
public class StatusResponse {

	private Description description;
	private Players players;
	private Version version;
	private String favicon;
	private int time;

	@Data
	public class Description {
		private String text;
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