package de.zh32.teleportsigns;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author zh32
 */
@Data
@Entity()
@Table(name="lobby_teleportsigns")
@EqualsAndHashCode
public class TeleportSign {
    
    @Id
    private int id;
    
    @NotEmpty
    private String server;
    
    @NotEmpty
    private String layout;
    
    @NotEmpty
    private String worldName;
    
    @NotNull
    private double x;
    
    @NotNull
    private double y;
    
    @NotNull
    private double z;

    public TeleportSign() {
    }
    
    public TeleportSign(String server, Location loc, String layout) {
        this.server = server;
        this.layout = layout;
        setLocation(loc);
    }
    
    private void setLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location getLocation() {
        World welt = Bukkit.getServer().getWorld(worldName);
        return new Location(welt, x, y, z);
    }
}
