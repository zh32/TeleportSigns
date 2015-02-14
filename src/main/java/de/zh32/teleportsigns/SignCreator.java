package de.zh32.teleportsigns;

import de.zh32.teleportsigns.utility.MessageHelper;


/**
 *
 * @author zh32
 */
public class SignCreator {
	
	public static final String IDENTIFIER = "[tsigns]";
	protected static final int IDENTIFIER_LINE = 0;
	protected static final int SERVER_LINE = 1;
	protected static final int LAYOUT_LINE = 2;	
	private final TeleportSignsPlugin plugin;

	public SignCreator(TeleportSignsPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean isTeleportSignCreated(String[] content) {
		return content[IDENTIFIER_LINE].equals(IDENTIFIER);
	}

	public TeleportSign createSign(String[] content, TeleportSign.TeleportSignLocation location) throws TeleportSignCreationException {
		GameServer gameServer = plugin.serverByName(content[SERVER_LINE]);
		if (gameServer == null) {
			throw new TeleportSignCreationException("server.notfound");
		}
		SignLayout layout = plugin.layoutByName(content[LAYOUT_LINE]);
		if (layout == null) {
			throw new TeleportSignCreationException("layout.notfound");
		}
		return TeleportSign.builder().layout(layout).server(gameServer).location(location).build();
	}

	public static class TeleportSignCreationException extends Exception {

		public TeleportSignCreationException(String messageCode) {
			super(MessageHelper.getMessage(messageCode));
		}
	}
}
