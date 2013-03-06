/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zh32.teleportsigns;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author zh32
 */
@Entity()
@Table(name="lobby_teleportsigns")
public class TeleportSign {
    
    @Getter
    @Id
    private int id;
    
    @Getter
    @Setter
    @NotEmpty
    private String server;
    
    @Getter
    @Setter
    @NotEmpty
    private String worldName;
    
    @Getter
    @Setter
    @NotNull
    private double x;
    
    @Getter
    @Setter
    @NotNull
    private double y;
    
    @Getter
    @Setter
    @NotNull
    private double z;
    
    public void setLocation(Location location) {
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
