/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns.ping;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author zh32
 */
public class PingThread {
    private Map<String,String> pinglist;
    private Map<String,String> results;
    private MCPing ping;
    public PingThread(Map<String, String> results, Map<String, String> pinglist) {
        this.pinglist = pinglist;
        this.results = results;
        ping = new MCPing();
    }

    void ping() {
        for (Entry<String, String> e : pinglist.entrySet()) {
            ping.setAddress(e.getValue().split(":")[0]);
            ping.setPort(Integer.parseInt(e.getValue().split(":")[1]));
            if (ping.fetchData()) {
                String numpl = String.valueOf(ping.getPlayersOnline());
                String maxpl = String.valueOf(ping.getMaxPlayers());
                results.put(e.getKey(), numpl + "#@#" + maxpl + "#@#" + ping.getMotd());
            }
            else {
                results.put(e.getKey(), "off");
            }
        }
        
    }
    
}
