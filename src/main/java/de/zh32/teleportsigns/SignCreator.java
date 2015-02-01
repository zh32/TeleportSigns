package de.zh32.teleportsigns;

import de.zh32.teleportsigns.utility.MessageHelper;
import de.zh32.teleportsigns.repository.GameServerRepository;
import de.zh32.teleportsigns.repository.SignLayoutRepository;
import de.zh32.teleportsigns.repository.TeleportSignRepository;
import de.zh32.teleportsigns.ping.GameServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author zh32
 */
public class SignCreator implements Listener {

	public static final String CREATE_PERMISSION = "teleportsigns.create";
	public static final String IDENTIFIER = "[tsigns]";
	private static final int IDENTIFIER_LINE = 0;
	private static final int SERVER_LINE = 1;
	private static final int LAYOUT_LINE = 2;

	private final GameServerRepository serverRepository;
	private final SignLayoutRepository layoutRepository;
	private final TeleportSignRepository signRepository;

	public SignCreator(GameServerRepository serverRepository, SignLayoutRepository layoutRepository, TeleportSignRepository signRepository) {
		this.serverRepository = serverRepository;
		this.layoutRepository = layoutRepository;
		this.signRepository = signRepository;
	}

	@EventHandler
	public void onSignChanged(SignChangeEvent event) {
		if (!isTeleportSignCreated(event)) {
			return;
		}
		if (!event.getPlayer().hasPermission(CREATE_PERMISSION)) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("sign.create.nopermission"));
			return;
		}
		try {
			TeleportSign teleportSign = createSignFromEvent(event);
			signRepository.save(teleportSign);
			//queue
			event.getPlayer().sendMessage(MessageHelper.getMessage("sign.create.success"));
		} catch (TeleportSignCreationException ex) {
			event.getPlayer().sendMessage(MessageHelper.getMessage("sign.create.success", ex.getMessage()));
		}
	}

	private boolean isTeleportSignCreated(SignChangeEvent event) {
		return event.getLine(IDENTIFIER_LINE).equals(IDENTIFIER);
	}

	private TeleportSign createSignFromEvent(SignChangeEvent event) throws TeleportSignCreationException {
		GameServer gameServer = serverRepository.byName(event.getLine(SERVER_LINE));
		if (gameServer == null) {
			throw new TeleportSignCreationException("server.notfound");
		}
		SignLayout layout = layoutRepository.byName(event.getLine(LAYOUT_LINE));
		if (layout == null) {
			throw new TeleportSignCreationException("layout.notfound");
		}
		return TeleportSign.builder()
				.layout(layout)
				.server(gameServer)
				.worldName(event.getBlock().getWorld().getName())
				.build();
	}

	public static class TeleportSignCreationException extends Exception {

		public TeleportSignCreationException(String messageCode) {
			super(MessageHelper.getMessage(messageCode));
		}
	}
}
