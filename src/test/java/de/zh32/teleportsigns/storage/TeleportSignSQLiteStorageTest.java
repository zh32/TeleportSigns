package de.zh32.teleportsigns.storage;

import de.zh32.teleportsigns.TeleportSign;
import de.zh32.teleportsigns.TeleportSignLayout;
import de.zh32.teleportsigns.ping.GameServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author zh32
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class TeleportSignSQLiteStorageTest {
	private TeleportSignSQLiteStorage testee;

	@Before
	public void setup() throws IOException {
		deleteTestDB();
	}
	
	@Test
	public void can_save_teleportsign() {
		World world = mock(World.class);
		PowerMockito.mockStatic(Bukkit.class);
		when(Bukkit.getWorld("world")).thenReturn(world);
		
		testee = new TeleportSignSQLiteStorage();
		testee.createTable();
		TeleportSign teleportSign = TeleportSign.builder()
				.layout(new TeleportSignLayout().setName("default"))
				.server(new GameServer().setName("testserver"))
				.location(new Location(world, 1, 1, 1))
				.build();
		testee.save(teleportSign);
		assertThat(testee.loadAll().size(), is(equalTo(1)));
	}
	
	@Test
	public void can_save_and_load_teleportsign() {
		World world = mock(World.class);
		PowerMockito.mockStatic(Bukkit.class);
		when(Bukkit.getWorld("world")).thenReturn(world);
		
		testee = new TeleportSignSQLiteStorage();
		testee.createTable();
		TeleportSign teleportSign = TeleportSign.builder()
				.layout(new TeleportSignLayout().setName("default"))
				.server(new GameServer().setName("testserver"))
				.location(new Location(world, 1, 1, 1))
				.build();
		testee.save(teleportSign);
		assertThat(testee.loadAll(), hasItem(teleportSign));
	}
	
	@After
	public void cleanup() throws IOException {
		deleteTestDB();
	}

	private void deleteTestDB() throws IOException {
		Files.deleteIfExists(new File(TeleportSignSQLiteStorage.DATABASE_NAME).toPath());
	}
	
}
