package de.zh32.teleportsigns;

import java.util.List;
import lombok.Data;

/**
 *
 * @author zh32
 */
@Data
class SignLayout {
    private final String name;    
    private final String online;
    private final String offline;
    private final List<String> lines;
    private final boolean teleport;
}
