package de.zh32.teleportsigns.storage;

import de.zh32.teleportsigns.TestLayout;
import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.GameServer;
import de.zh32.teleportsigns.TeleportSignsPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author zh32
 */
public class TeleportSignSQLiteStorageTest {

	private TeleportSignsPlugin plugin;
	private TeleportSignSQLiteStorage testee;

	@Before
	public void setup() throws IOException {
		plugin = mock(TeleportSignsPlugin.class);
		when(plugin.layoutByName("default")).thenReturn(new TestLayout());
		when(plugin.serverByName("testserver")).thenReturn(new GameServer().setName("testserver"));
		deleteTestDB();
		testee = new TeleportSignSQLiteStorage(plugin);
	}

	@Test
	public void can_save_teleportsign() {
		testee.createTable();
		testee.save(testSign());
		assertThat(testee.loadAll().size(), is(equalTo(1)));
	}

	@Test
	public void can_save_and_load_teleportsign() {
		testee.createTable();
		testee.save(testSign());
		assertThat(testee.loadAll(), hasItem(testSign()));
	}

	@After
	public void cleanup() throws IOException {
		deleteTestDB();
	}

	private void deleteTestDB() throws IOException {
		Files.deleteIfExists(new File(TeleportSignSQLiteStorage.DATABASE_NAME).toPath());
	}
	
	private TeleportSign testSign() {
		TeleportSign teleportSign = TeleportSign.builder()
				.layout(new TestLayout())
				.server(new GameServer().setName("testserver"))
				.location(new TeleportSign.TeleportSignLocation(1, 1, 1, "world"))
				.build();
		return teleportSign;
	}

	
}
