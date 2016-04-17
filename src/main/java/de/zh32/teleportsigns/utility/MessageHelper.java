package de.zh32.teleportsigns.utility;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
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
