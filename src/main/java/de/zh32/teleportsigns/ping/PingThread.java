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
    private MCPing ping;
    public PingThread(Map<String, String> pinglist) {
        this.pinglist = pinglist;
        ping = new MCPing();
    }

    void ping() {
        for (Entry<String, String> e : pinglist.entrySet()) {
            ping.setAddress(e.getValue().split(":")[0]);
            ping.setPort(Integer.parseInt(e.getValue().split(":")[1]));
            if (ping.fetchData()) {
                Ping.getInstance().results.put(e.getKey(), new Result(ping.getPlayersOnline(), ping.getMaxPlayers(), ping.getMotd(), true));
            }
            else {
                Ping.getInstance().results.put(e.getKey(), new Result(false));
            }
        }
        
    }
    
}
