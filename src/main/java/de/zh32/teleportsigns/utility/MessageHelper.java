package de.zh32.teleportsigns.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zh32
 */
public class MessageHelper {

	@Getter
	@Setter
	private static Map<String, String> messages;

	public static String getMessage(String messageCode, Object... args) {
		String template;
		if (messages == null || (template = messages.get(messageCode)) == null) {
			return messageCode;
		}
		return String.format(template, args);
	}

}
