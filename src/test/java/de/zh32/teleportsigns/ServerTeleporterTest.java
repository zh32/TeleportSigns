package de.zh32.teleportsigns;

import org.junit.Test;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author zh32
 */
public class ServerTeleporterTest {

	private ServerTeleporter testee;
	private TeleportSignsPlugin plugin;

	@Before
	public void setup() {
		plugin = mock(TeleportSignsPlugin.class);
		testee = spy(new ServerTeleporter(plugin) {

			@Override
			void teleportToServer(String player, String server) {

			}
		});
		
	}

	@Test
	public void can_teleport_player() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		when(plugin.signAt(teleportSignLocation)).thenReturn(gameServer);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		ServerTeleporter.PlayerTeleport teleportReq = new ServerTeleporter.PlayerTeleport().setPlayer("TESTER").setLocation(teleportSignLocation);
		testee.teleportPlayer(teleportReq);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

	@Test
	public void server_offline() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(false);
		when(plugin.signAt(teleportSignLocation)).thenReturn(gameServer);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		ServerTeleporter.PlayerTeleport teleportReq = new ServerTeleporter.PlayerTeleport().setPlayer("TESTER").setLocation(teleportSignLocation);
		testee.teleportPlayer(teleportReq);
		verify(testee, never()).teleportToServer("player", "SERVER");
	}

	@Test
	public void player_has_cooldown() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		when(plugin.signAt(teleportSignLocation)).thenReturn(gameServer);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		ServerTeleporter.PlayerTeleport teleportReq = new ServerTeleporter.PlayerTeleport().setPlayer("TESTER").setLocation(teleportSignLocation);
		testee.teleportPlayer(teleportReq);
		testee.teleportPlayer(teleportReq);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

}
