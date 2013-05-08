package de.zh32.teleportsigns.converter;

import de.zh32.teleportsigns.TeleportSigns;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

/**
 *
 * @author zh32
 */
public class Converter {
    
    public static void convert(TeleportSigns plugin) {
        Logger log = Bukkit.getLogger();
        File file = new File(plugin.getDataFolder().getAbsolutePath()+ "/TeleportSigns.db");
        if (file.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder().getAbsolutePath()+ "/TeleportSigns.db");
                Statement stat = conn.createStatement();
                try {
                    stat.execute("ALTER TABLE lobby_teleportsigns ADD layout CHAR(255)");
                    stat.close();
                    stat = conn.createStatement();
                    stat.executeUpdate("UPDATE lobby_teleportsigns SET layout='default' WHERE layout IS null");
                    stat.close();
                    conn.close();
                } catch (SQLException ex) {
                    //column exists
                }           
            } catch (ClassNotFoundException | SQLException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }
}
