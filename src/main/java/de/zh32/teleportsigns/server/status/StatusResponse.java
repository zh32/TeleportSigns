package de.zh32.teleportsigns.server.status;

import java.util.List;
import lombok.Data;

/**
 *
 * @author zh32 <zh32 at zh32.de>
 */
@Data
public class StatusResponse {
    private Description description;
    private Players players;
    private Version version;
    
    @Data
    public class Description {
        private String text; 
    }
    @Data
    public class Players {
        private int max;
        private int online;   
    }
    @Data
    public class Version {
        private String name;
        private int protocol;
    }
}
