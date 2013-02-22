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
    @Id
    private int id;
    
    @NotEmpty
    private String server;
    
    @NotEmpty
    private String worldName;
    
    @NotNull
    private double x;

    @NotNull
    private double y;

    @NotNull
    private double z;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
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
    
    public String getServer() {
        return server;
    }
    
    public void setServer(String server) {
        this.server = server;
    }
}
