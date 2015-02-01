package de.zh32.teleportsigns;

import de.zh32.teleportsigns.repository.SignLayoutRepository;
import de.zh32.teleportsigns.repository.TeleportSignRepository;
import de.zh32.teleportsigns.repository.GameServerRepository;
import de.zh32.teleportsigns.ping.GameServer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author zh32
 */
public class SignCreatorTest {

	private TeleportSignRepository signRepository;
	private SignLayoutRepository layoutRepository;
	private GameServerRepository serverRepository;
	private SignCreator testee;
	private Block block;
	private Player player;

	@Before
	public void setup() {
		serverRepository = mock(GameServerRepository.class);
		when(serverRepository.byName("testserver")).thenReturn(new GameServer().setName("TESTSERVER"));

		layoutRepository = mock(SignLayoutRepository.class);
		when(layoutRepository.byName("default")).thenReturn(new TeleportSignLayout().setName("default"));

		signRepository = mock(TeleportSignRepository.class);
		testee = new SignCreator(serverRepository, layoutRepository, signRepository);

		World world = mock(World.class);
		when(world.getName()).thenReturn("world");

		block = mock(Block.class);
		when(block.getWorld()).thenReturn(world);

		player = mock(Player.class);
		when(player.hasPermission(anyString())).thenReturn(true);
	}

	@Test
	public void can_create_sign() {
		String[] lines = new String[]{SignCreator.IDENTIFIER, "testserver", "default"};
		testee.onSignChanged(new SignChangeEvent(block, player, lines));
		verify(signRepository, times(1)).save(any(TeleportSign.class));
	}

	@Test
	public void server_not_found() {
		String[] lines = new String[]{SignCreator.IDENTIFIER, "nothere", "default"};
		testee.onSignChanged(new SignChangeEvent(block, player, lines));
		verify(signRepository, never()).save(any(TeleportSign.class));
	}

	@Test
	public void layout_not_found() {
		String[] lines = new String[]{SignCreator.IDENTIFIER, "testserver", "nothere"};
		testee.onSignChanged(new SignChangeEvent(block, player, lines));
		verify(signRepository, never()).save(any(TeleportSign.class));
	}

}
