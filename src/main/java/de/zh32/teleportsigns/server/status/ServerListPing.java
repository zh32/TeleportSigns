package de.zh32.teleportsigns.server.status;

import com.google.gson.Gson;
import de.zh32.teleportsigns.server.GameServer;
import lombok.Getter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zh32
 */
public class ServerListPing {

	public boolean updateStatus(GameServer server) {
		GameServer oldState = server.clone();
		try {
			ServerConnection connection = new ServerConnection(server.getAddress());
			connection.connect();
			QueryHandler queryHandler = new QueryHandler(connection);
			queryHandler.doHandShake();
			StatusResponse response = queryHandler.doStatusQuery();
			connection.disconnect();
			server
					.setMotd(response.getDescription())
					.setMaxPlayers(response.getOnlinePlayers())
					.setPlayersOnline(response.getMaxPlayers())
					.setOnline(true);
		} catch (Exception ex) {
			server.setOnline(false);
		}
		return hasStatusChanged(oldState, server);
	}

	private static boolean hasStatusChanged(GameServer old, GameServer server) {
		return !old.equals(server);
	}

	@Getter
	public static class ServerConnection {

		private final InetSocketAddress host;
		private Socket socket;
		private InputStream inputStream;
		private OutputStream outputStream;
		private int timeout;
		private DataInputStream dataInputStream;
		private DataOutputStream dataOutputStream;

		public ServerConnection(InetSocketAddress host) {
			this.host = host;
		}

		public void connect() throws IOException {
			socket = new Socket();
			socket.setSoTimeout(timeout);
			socket.connect(host, timeout);
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
		}

		private void disconnect() throws IOException {
			dataInputStream.close();
			dataOutputStream.close();
			socket.close();
		}


	}


}
