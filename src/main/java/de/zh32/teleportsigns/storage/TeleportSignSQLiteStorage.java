package de.zh32.teleportsigns.storage;

import de.zh32.teleportsigns.DataContainer;
import de.zh32.teleportsigns.sign.TeleportSign;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zh32
 */
public class TeleportSignSQLiteStorage implements TeleportSignStorage {

	public static final String TABLE_NAME = "teleportsign";
	public static final String DATABASE_NAME = "teleportsigns.db";

	private final DataContainer plugin;
	private final String databasePath;

	public TeleportSignSQLiteStorage(String databasePath, DataContainer plugin) {
		this.databasePath = databasePath;
		this.plugin = plugin;
	}

	@Override
	public List<TeleportSign> loadAll() {
		Connection c = null;
		Statement stmt = null;
		List<TeleportSign> results = new ArrayList<>();
		try {
			c = getConnection();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s;", TABLE_NAME));

			while (rs.next()) {
				results.add(signFromResultSet(rs));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public void save(TeleportSign teleportSign) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = getConnection();
			stmt = c.prepareStatement(String.format("INSERT INTO %s(server, layout, x, y, z, world) VALUES(?,?,?,?,?,?);", TABLE_NAME));
			stmt.setString(1, teleportSign.getServer().getName());
			stmt.setString(2, teleportSign.getLayout().getName());
			stmt.setInt(3, teleportSign.getLocation().getX());
			stmt.setInt(4, teleportSign.getLocation().getY());
			stmt.setInt(5, teleportSign.getLocation().getZ());
			stmt.setString(6, teleportSign.getLocation().getWorldName());
			stmt.executeUpdate();
			stmt.close();
			c.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(TeleportSign teleportSign) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = getConnection();
			stmt = c.prepareStatement(String.format("DELETE FROM %s WHERE x = ? AND y = ? AND z = ? AND world = ?", TABLE_NAME));
			stmt.setInt(1, teleportSign.getLocation().getX());
			stmt.setInt(2, teleportSign.getLocation().getY());
			stmt.setInt(3, teleportSign.getLocation().getZ());
			stmt.setString(4, teleportSign.getLocation().getWorldName());
			stmt.executeUpdate();
			stmt.close();
			c.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s/%s", databasePath, DATABASE_NAME));
	}

	private TeleportSign signFromResultSet(ResultSet rs) throws SQLException {

		int x = rs.getInt("x");
		int y = rs.getInt("y");
		int z = rs.getInt("z");
		String world = rs.getString("world");
		String layout = rs.getString("layout");
		String server = rs.getString("server");
		//TODO: get server and layout
		return TeleportSign.builder()
				.layout(plugin.layoutByName(layout))
				.server(plugin.serverByName(server))
				.location(new TeleportSign.TeleportSignLocation(x, y, z, world))
				.build();
	}

	public void createTable() {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = getConnection();
			stmt = c.prepareStatement(String.format(
					"CREATE TABLE IF NOT EXISTS %s "
							+ "(ID INT PRIMARY KEY ,"
							+ " x           INT    NOT NULL,"
							+ " y           INT    NOT NULL,"
							+ " z           INT    NOT NULL,"
							+ " server           CHAR(255)    NOT NULL,"
							+ " layout            CHAR(255)     NOT NULL,"
							+ " world        CHAR(255))", TABLE_NAME));
			stmt.executeUpdate();
			stmt.close();
			c.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize() {
		createTable();
	}

}
