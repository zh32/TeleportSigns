package de.zh32.teleportsigns;

import de.zh32.teleportsigns.server.GameServer;
import de.zh32.teleportsigns.sign.TeleportSign;
import de.zh32.teleportsigns.task.Callback;
import de.zh32.teleportsigns.task.ServerUpdateTask;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zh32
 */
public abstract class UpdateLoop {

	private final List<TeleportSign> teleportSigns;
	@Getter
	private final ServerUpdateTask serverUpdateTask;

	public UpdateLoop(ServerUpdateTask serverUpdateTask, List<TeleportSign> teleportSigns) {
		this.serverUpdateTask = serverUpdateTask;
		this.teleportSigns = teleportSigns;
	}

	public abstract void updateTeleportSigns(List<TeleportSign> list);

	public abstract void startUpdateLoop();

	public abstract void rerunUpdateLoop();

	public abstract void stopUpdateLoop();

	public void initialize() {
		serverUpdateTask.onFinish(new Callback<List<GameServer>>() {

			@Override
			public void finish(List<GameServer> result) {
				List<TeleportSign> list = new ArrayList<>();
				for (TeleportSign sign : teleportSigns) {
					if (result.contains(sign.getServer())) {
						list.add(sign);
					}
				}
				updateTeleportSigns(list);
			}

		});

	}
}
