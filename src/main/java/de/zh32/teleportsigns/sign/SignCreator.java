package de.zh32.teleportsigns.sign;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.utility.MessageHelper;
import lombok.Getter;


/**
 * @author zh32
 */
public class SignCreator {

	public static final String IDENTIFIER = "[tsigns]";
	protected static final int IDENTIFIER_LINE = 0;
	protected static final int SERVER_LINE = 1;
	protected static final int LAYOUT_LINE = 2;
	@Getter
	private final DataContainer dataContainer;

	public SignCreator(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
	}

	public boolean isTeleportSignCreated(String[] content) {
		return content[IDENTIFIER_LINE].equals(IDENTIFIER);
	}

	public TeleportSign createSign(String[] content, TeleportSign.TeleportSignLocation location) throws TeleportSignCreationException {
		GameServer gameServer = dataContainer.serverByName(content[SERVER_LINE]);
		if (gameServer == null) {
			throw new TeleportSignCreationException(MessageHelper.getMessage("server.notfound", content[SERVER_LINE]));
		}
		SignLayout layout = dataContainer.layoutByName(content[LAYOUT_LINE]);
		if (layout == null) {
			throw new TeleportSignCreationException(MessageHelper.getMessage("layout.notfound", content[LAYOUT_LINE]));
		}
		TeleportSign teleportSign = TeleportSign.builder().layout(layout).server(gameServer).location(location).build();
		dataContainer.getTeleportSigns().add(teleportSign);
		dataContainer.getStorage().save(teleportSign);
		return teleportSign;
	}

	public static class TeleportSignCreationException extends Exception {

		public TeleportSignCreationException(String messageCode) {
			super(MessageHelper.getMessage(messageCode));
		}
	}
}
