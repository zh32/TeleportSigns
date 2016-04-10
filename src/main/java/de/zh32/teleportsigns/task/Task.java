package de.zh32.teleportsigns.task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zh32
 */
public abstract class Task<T> {

	private List<Callback<T>> callbacks = new ArrayList<>();

	public Task onFinish(Callback<T> callback) {
		callbacks.add(callback);
		return this;
	}

	public void finish(T result) {
		for (Callback<T> callback : callbacks) {
			callback.finish(result);
		}
	}

	abstract void execute();
}
