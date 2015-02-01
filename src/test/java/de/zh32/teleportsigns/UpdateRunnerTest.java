package de.zh32.teleportsigns;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author zh32
 */
public class UpdateRunnerTest {

	@Test
	public void can_update_in_multiple_ticks() {
		final TeleportSign sign = mock(TeleportSign.class);
		ArrayList<TeleportSign> teleportSigns = new ArrayList<>();
		for (int i = 0; i < 80; i++) {
			teleportSigns.add(sign);
		}		
		final UpdateRunner testee = spy(new UpdateRunner(teleportSigns, 8, mock(Plugin.class)));
		doAnswer(new Answer<BukkitTask>() {

			@Override
			public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
				testee.run();
				return null;
			}
		}).when(testee).runTaskLater(any(Plugin.class), anyLong());
		testee.run();
		verify(testee, times(10)).run();
		verify(testee, times(9)).runTaskLater(any(Plugin.class), eq(1L));
	}
	
}
