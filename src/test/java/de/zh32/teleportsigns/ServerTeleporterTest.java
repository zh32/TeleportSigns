package de.zh32.teleportsigns;

import de.zh32.teleportsigns.event.ProxyTeleportEvent;
import de.zh32.teleportsigns.server.GameServer;
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
	private TeleportSigns plugin;

	@Before
	public void setup() {
		plugin = mock(TeleportSigns.class);
		when(plugin.getConfiguration()).thenReturn(new TestConfiguration());
		testee = spy(new ServerTeleporter(plugin) {

			@Override
			public void teleportToServer(String player, String server) {

			}
		});
		
	}

	@Test
	public void can_teleport_player() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

	@Test
	public void server_offline() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(false);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, never()).teleportToServer("player", "SERVER");
	}

	@Test
	public void player_has_cooldown() {
		TeleportSign.TeleportSignLocation teleportSignLocation = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
		GameServer gameServer = new GameServer().setName("SERVER").setOnline(true);
		TeleportSign teleportSign = new TeleportSign(gameServer, null, teleportSignLocation);
		when(plugin.signAtLocation(teleportSignLocation)).thenReturn(teleportSign);
		when(plugin.fireTeleportEvent(anyString(), any(GameServer.class))).thenReturn(new ProxyTeleportEvent().setCancelled(false).setServerInfo(gameServer).setPlayer("player"));
		testee.teleportPlayer("TESTER", teleportSignLocation);
		testee.teleportPlayer("TESTER", teleportSignLocation);
		verify(testee, times(1)).teleportToServer("player", "SERVER");
	}

}
