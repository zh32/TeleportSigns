package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.sign.TeleportSign;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author zh32
 */
public class SignUpdateTaskTest {
	
	private SignUpdateTask testee;
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
		
		testee = spy(new SignUpdateTask(teleportSigns, signUpdatesPerTick) {

			@Override
			public void runTaskLater() {
				this.execute();
			}

			@Override
			public void updateSign(TeleportSign next) {
				
			}
		});
	}
	
	@Test
	public void can_update_in_multiple_ticks() {
		testee.execute();
		verify(testee, times(expectedRuns)).execute();
		verify(testee, times(expectedRuns - 1)).runTaskLater();
	}

}
