package de.zh32.teleportsigns;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.zh32.teleportsigns.ping.ServerInfo;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author zh32
 */
@Data
@Entity()
@Table(name="lobby_teleportsigns")
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

    public void updateSign() {
        Location location = getLocation();
        if (location.getWorld().getChunkAt(location).isLoaded()) {
            Block b = location.getBlock();
            if (b.getState() instanceof Sign) {
                ServerInfo sinfo = TeleportSigns.getInstance().getConfigData().getServer(this.server);
                SignLayout layout = TeleportSigns.getInstance().getConfigData().getLayout(this.layout);
                if (layout != null) {
                    Sign s = (Sign) b.getState();
                    List<String> jau = layout.parseLayout(sinfo);
                    for (int i = 0; i < layout.getLines().size(); i++) {
                        s.setLine(i, jau.get(i));
                    }
                    s.update();
                }
                else {
                    Bukkit.getLogger().log(Level.WARNING, "[TeleportSigns] can't find layout '" + this.layout + "'");
                }
            }
        }
    }
}
