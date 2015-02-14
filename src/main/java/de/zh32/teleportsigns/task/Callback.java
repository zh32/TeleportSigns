/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns.task;

/**
 *
 * @author zh32
 */
public interface Callback<T> {

	void finish(T result);
	
}
