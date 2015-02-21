package de.zh32.teleportsigns.utility;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zh32
 */
public class MessageHelperTest {

	@Test
	public void can_format_message() {
		final HashMap<String, String> messages = new HashMap<>();
		messages.put("message.test", "This is a test message with \"%s\"parameter");
		MessageHelper.setMessages(messages);
		assertEquals(MessageHelper.getMessage("message.test", "this"), "This is a test message with \"this\"parameter");
	}
	
}
