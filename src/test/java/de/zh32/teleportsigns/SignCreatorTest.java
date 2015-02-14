package de.zh32.teleportsigns;

import de.zh32.teleportsigns.TeleportSign.TeleportSignLocation;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

/**
 *
 * @author zh32
 */
public class SignCreatorTest {

	private SignCreator testee;
	private TeleportSignsPlugin plugin;
	private TeleportSignLocation location;

	@Before
	public void setup() {
		plugin = mock(TeleportSignsPlugin.class);
		when(plugin.layoutByName("default")).thenReturn(new TestLayout());
		when(plugin.serverByName("testserver")).thenReturn(new GameServer().setName("testserver"));
		testee = new SignCreator(plugin);
		location = new TeleportSign.TeleportSignLocation(1, 1, 1, "world");
	}

	@Test
	public void can_create_sign() throws SignCreator.TeleportSignCreationException {
		String[] lines = new String[]{SignCreator.IDENTIFIER, "testserver", "default"};
		testee.createSign(lines, location);
		Assert.assertThat(
				testee.createSign(lines, location),
				hasProperty("location", is(equalTo(location)))
		);
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void server_not_found() throws SignCreator.TeleportSignCreationException {
		exception.expect(SignCreator.TeleportSignCreationException.class);
		exception.expectMessage(containsString("server"));
		String[] lines = new String[]{SignCreator.IDENTIFIER, "nothere", "default"};
		testee.createSign(lines, location);
	}

	@Test
	public void layout_not_found() throws SignCreator.TeleportSignCreationException {
		exception.expect(SignCreator.TeleportSignCreationException.class);
		exception.expectMessage(containsString("layout"));
		String[] lines = new String[]{SignCreator.IDENTIFIER, "testserver", "nothere"};
		testee.createSign(lines, location);
	}

}
