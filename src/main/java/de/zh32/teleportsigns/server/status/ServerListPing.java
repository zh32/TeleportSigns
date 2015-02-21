package de.zh32.teleportsigns.server.status;

import com.google.gson.Gson;
import de.zh32.teleportsigns.server.GameServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import lombok.Getter;

/**
 *
 * @author zh32
 */
public class ServerListPing {

	private final static Gson gson = new Gson();

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
					.setMaxPlayers(response.getPlayers().getMax())
					.setPlayersOnline(response.getPlayers().getOnline())
					.setOnline(true);
		} catch (IOException ex) {
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
