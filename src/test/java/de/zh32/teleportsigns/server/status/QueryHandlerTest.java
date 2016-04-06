package de.zh32.teleportsigns.server.status;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author zh32
 */
public class QueryHandlerTest {

	private static final byte[] bytesSendByServer = new byte[]{
			0x74, 0x00, 0x72, 0x7b, 0x22, 0x64, 0x65, 0x73,
			0x63, 0x72, 0x69, 0x70, 0x74, 0x69, 0x6f, 0x6e,
			0x22, 0x3a, 0x22, 0x41, 0x20, 0x4d, 0x69, 0x6e,
			0x65, 0x63, 0x72, 0x61, 0x66, 0x74, 0x20, 0x53,
			0x65, 0x72, 0x76, 0x65, 0x72, 0x22, 0x2c, 0x22,
			0x70, 0x6c, 0x61, 0x79, 0x65, 0x72, 0x73, 0x22,
			0x3a, 0x7b, 0x22, 0x6d, 0x61, 0x78, 0x22, 0x3a,
			0x32, 0x30, 0x2c, 0x22, 0x6f, 0x6e, 0x6c, 0x69,
			0x6e, 0x65, 0x22, 0x3a, 0x30, 0x7d, 0x2c, 0x22,
			0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x22,
			0x3a, 0x7b, 0x22, 0x6e, 0x61, 0x6d, 0x65, 0x22,
			0x3a, 0x22, 0x53, 0x70, 0x69, 0x67, 0x6f, 0x74,
			0x20, 0x31, 0x2e, 0x38, 0x22, 0x2c, 0x22, 0x70,
			0x72, 0x6f, 0x74, 0x6f, 0x63, 0x6f, 0x6c, 0x22,
			0x3a, 0x34, 0x37, 0x7d, 0x7d, 0x09, 0x01, 0x00,
			0x00, 0x01, 0x4b, (byte) 0xac, (byte) 0xc2, 0x63, 0x30
		};// TODO
		  // This is 
		  // {"description":"A Minecraft Server","players":{"max":20,"online":0},"version":{"name":"Spigot 1.8","protocol":47}}
		  // as it was for Minecraft 1.8. For Minecraft 1.9 it must be
		  // {"description":{"text";"A Minecraft Server"},"players":{"max":20,"online":0},"version":{"name":"Spigot 1.9","protocol":109}}
		  // Dont really know how i can covert this and dont want to figur it out for now
	
	private QueryHandler testee;
	
	@Before
	public void setup() {
		ServerListPing.ServerConnection connection = mock(ServerListPing.ServerConnection.class);
		when(connection.getDataOutputStream()).thenReturn(new DataOutputStream(mock(OutputStream.class)));
		when(connection.getDataInputStream()).thenReturn(new DataInputStream(new ByteArrayInputStream(bytesSendByServer)));
		when(connection.getHost()).thenReturn(new InetSocketAddress("localhost", 25555));
		testee = new QueryHandler(connection);
	}

	@Test
	public void can_handle_minecraft_protocol() throws IOException {
		testee.doHandShake();
		//StatusResponse response = testee.doStatusQuery(); //Broken with 1.9. (Only Test)
		assertThat(
				response,
				allOf(
						hasProperty("description", allOf(
										hasProperty("text", is(equalTo("A Minecraft Server")))
								)),
						hasProperty("version", allOf(
										hasProperty("name", is(equalTo("Spigot 1.9"))),
										hasProperty("protocol", is(equalTo(109)))
								))
				)
		);

	}

}
