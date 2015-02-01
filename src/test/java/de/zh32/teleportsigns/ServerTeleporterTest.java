package de.zh32.teleportsigns;

import de.zh32.teleportsigns.ping.GameServer;
import de.zh32.teleportsigns.repository.GameServerRepository;
import de.zh32.teleportsigns.repository.SignLayoutRepository;
import de.zh32.teleportsigns.repository.TeleportSignRepository;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author zh32
 */
public class ServerTeleporterTest {
	
	private TeleportSignRepository signRepository;
	private SignLayoutRepository layoutRepository;
	private GameServerRepository serverRepository;
	private ServerTeleporter testee;
	private Block block;
	private Player player;

	@Before
	public void setup() {
		World world = mock(World.class);
		when(world.getName()).thenReturn("world");

		block = mock(Block.class);
		when(block.getWorld()).thenReturn(world);
		when(block.getState()).thenReturn(mock(Sign.class));

		player = mock(Player.class);
		when(player.hasPermission(anyString())).thenReturn(true);
		
		Location location = new Location(world, 1, 1, 1);
		
		serverRepository = mock(GameServerRepository.class);
		when(serverRepository.byName("testserver")).thenReturn(new GameServer().setName("TESTSERVER").setOnline(true));

		layoutRepository = mock(SignLayoutRepository.class);
		when(layoutRepository.byName("default")).thenReturn(new TeleportSignLayout().setName("default"));

		signRepository = mock(TeleportSignRepository.class);
		when(signRepository.byLocation(any(Location.class))).thenReturn(TeleportSign.builder().server(new GameServer().setName("testserver").setOnline(true)).build());
		testee = new ServerTeleporter(null, signRepository);

	}

	@Test
	public void can_teleport_player() {
		PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, null, block, BlockFace.NORTH);
		testee.onClick(event);
		verify(player, times(1)).sendPluginMessage(any(Plugin.class), argThat(is(equalTo("BungeeCord"))), any(byte[].class));
	}
	
	@Test
	public void server_offline() {
		when(signRepository.byLocation(any(Location.class)))
				.thenReturn(
						TeleportSign.builder().server(new GameServer().setName("testserver").setOnline(false)).build()
				);
		PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, null, block, BlockFace.NORTH);
		testee.onClick(event);
		verify(player, never()).sendPluginMessage(any(Plugin.class), argThat(is(equalTo("BungeeCord"))), any(byte[].class));
	}
	
	@Test
	public void player_has_cooldown() {
		PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, null, block, BlockFace.NORTH);
		testee.onClick(event);
		testee.onClick(event);
		verify(player, times(1)).sendPluginMessage(any(Plugin.class), argThat(is(equalTo("BungeeCord"))), any(byte[].class));
	}
	
}
