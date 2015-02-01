package de.zh32.teleportsigns;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author zh32
 */
public class UpdateRunnerTest {
	
	private UpdateRunner testee;
	private final int expectedRuns = 10;
	private final int signUpdatesPerTick = 8;
	private final int signsToUpdate = 80;

	@Before
	public void setup() {
		final TeleportSign sign = mock(TeleportSign.class);
		ArrayList<TeleportSign> teleportSigns = new ArrayList<>();
		for (int i = 0; i < signsToUpdate; i++) {
			teleportSigns.add(sign);
		}
		
		testee = spy(new UpdateRunner(teleportSigns, signUpdatesPerTick, mock(Plugin.class)));
		doAnswer(withCallToRun()).when(testee).runTaskLater(any(Plugin.class), anyLong());
	}
	
	@Test
	public void can_update_in_multiple_ticks() {
		testee.run();
		verify(testee, times(expectedRuns)).run();
		verify(testee, times(expectedRuns - 1)).runTaskLater(any(Plugin.class), eq(1L));
	}

	private Answer withCallToRun() {
		return new Answer<BukkitTask>() {

			@Override
			public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
				testee.run();
				return null;
			}
		};
	}

}
