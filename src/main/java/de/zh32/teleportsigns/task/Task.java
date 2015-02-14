package de.zh32.teleportsigns.task;

/**
 *
 * @author zh32
 */
public interface Task<T> {

	Task onFinish(Callback<T> callback);
	void execute();
}
