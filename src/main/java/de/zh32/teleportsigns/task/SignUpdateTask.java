package de.zh32.teleportsigns.task;

import de.zh32.teleportsigns.sign.TeleportSign;

import java.util.Iterator;
import java.util.List;

/**
 * @author zh32
 */
public abstract class SignUpdateTask extends Task {

	private final int maxUpdatesPerTick;
	private int updatesThisTick;
	private final Iterator<TeleportSign> it;

	public SignUpdateTask(List<TeleportSign> teleportSigns, int updatesPerTick) {
		this.maxUpdatesPerTick = updatesPerTick;
		this.it = teleportSigns.iterator();
	}

	@Override
	public void execute() {
		updatesThisTick = 0;
		while (it.hasNext() && updatesThisTick < maxUpdatesPerTick) {
			updateSign(it.next());
			updatesThisTick++;
		}
		if (it.hasNext()) {
			runTaskLater();
		}
		finish(null);
	}

	public abstract void runTaskLater();

	public abstract void updateSign(TeleportSign next);

}
