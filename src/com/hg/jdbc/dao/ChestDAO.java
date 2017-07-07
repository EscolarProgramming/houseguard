package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.Conexao;
import com.hg.jdbc.dao.model.Backup;
import com.hg.jdbc.dao.model.Chest;

public class ChestDAO implements BaseDAO {

	private final Connection connection;

	public ChestDAO(HgConfig hgConfig) throws SQLException {
		this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
	}
	
	public ChestDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_chests ( ");
		sql.append("	id INT AUTO_INCREMENT, ");
//		sql.append("	type INT, ");
		sql.append("	type varchar(50), ");
		sql.append("	x INT, ");
		sql.append("	y INT, ");
		sql.append("	z INT, ");
		sql.append("	pitch varchar(200), ");
		sql.append("	yaw varchar(200), ");
		sql.append("	backup_id INT NOT NULL, ");
		sql.append("	PRIMARY KEY (id), ");
		sql.append("	FOREIGN KEY (backup_id) REFERENCES hg_backups(id) ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	@Override
	public void createTableSqlite() throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_chests ( ");
		sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
//		sql.append("	type INTEGER, ");
		sql.append("	type text, ");
		sql.append("	x INTEGER, ");
		sql.append("	y INTEGER, ");
		sql.append("	z INTEGER, ");
		sql.append("	pitch text, ");
		sql.append("	yaw text, ");
		sql.append("	backup_id INTEGER NOT NULL, ");
		sql.append("	FOREIGN KEY (backup_id) REFERENCES hg_backups(id) ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public void dropTable() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" DROP TABLE IF EXISTS hg_chests ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	@Override
	public void insert(Object object) throws SQLException {

		Chest chest = (Chest) object;
		StringBuffer sql = new StringBuffer();

		sql.append("insert into hg_chests (type, x, y, z, pitch, yaw, backup_id) values (?,?,?,?,?,?,?)");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, chest.getType());
		stmt.setInt(2, chest.getX());
		stmt.setInt(3, chest.getY());
		stmt.setInt(4, chest.getZ());
		stmt.setString(5, chest.getPitch().toString());
		stmt.setString(6, chest.getYaw().toString());
		stmt.setInt(7, chest.getBackup().getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public List<Chest> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_chests");

		ResultSet rs = stmt.executeQuery();
		List<Chest> chests = new ArrayList<Chest>();

		while (rs.next()) {
			Chest chestDatabase = new Chest();
			chestDatabase.setId(rs.getInt("id"));
			chestDatabase.setType(rs.getString("type"));
			chestDatabase.setX(rs.getInt("x"));
			chestDatabase.setY(rs.getInt("y"));
			chestDatabase.setZ(rs.getInt("z"));
			chestDatabase.setPitch(Float.parseFloat(rs.getString("pitch")));
			chestDatabase.setYaw(Float.parseFloat(rs.getString("yaw")));
			chestDatabase.setBackup(new Backup(rs.getInt("backup_id")));
			chests.add(chestDatabase);
		}

		rs.close();
		stmt.close();
		return chests;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from hg_chests where id = " + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public Chest findById(Integer id) throws SQLException {

		String sql = "select * from hg_chests where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Chest chest = new Chest();

		while (rs.next()) {
			chest.setId(rs.getInt("id"));
			chest.setType(rs.getString("type"));
			chest.setX(rs.getInt("x"));
			chest.setY(rs.getInt("y"));
			chest.setZ(rs.getInt("z"));
			chest.setPitch(Float.parseFloat(rs.getString("pitch")));
			chest.setYaw(Float.parseFloat(rs.getString("yaw")));
			chest.setBackup(new Backup(rs.getInt("backup_id")));

		}

		rs.close();
		stmt.close();
		return chest;
	}


	public List<Chest> listByBackupId(Integer backupId) throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_chests where backup_id = " + backupId);

		ResultSet rs = stmt.executeQuery();
		List<Chest> chests = new ArrayList<Chest>();

		while (rs.next()) {
			Chest chestDatabase = new Chest();
			chestDatabase.setId(rs.getInt("id"));
			chestDatabase.setType(rs.getString("type"));
			chestDatabase.setX(rs.getInt("x"));
			chestDatabase.setY(rs.getInt("y"));
			chestDatabase.setZ(rs.getInt("z"));
			chestDatabase.setPitch(Float.parseFloat(rs.getString("pitch")));
			chestDatabase.setYaw(Float.parseFloat(rs.getString("yaw")));
			chestDatabase.setBackup(new Backup(rs.getInt("backup_id")));

			chests.add(chestDatabase);
		}

		rs.close();
		stmt.close();
		return chests;
	}


	public Chest findBy(Chest chest) throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append("select id from hg_chests where type=? and x=? and y=? and z=? and pitch=? and yaw=? and backup_id=? ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());
		stmt.setString(1, chest.getType());
		stmt.setInt(2, chest.getX());
		stmt.setInt(3, chest.getY());
		stmt.setInt(4, chest.getZ());
		stmt.setString(5, chest.getPitch().toString());
		stmt.setString(6, chest.getYaw().toString());
		stmt.setInt(7, chest.getBackup().getId());

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			chest.setId(rs.getInt("id"));
		}

		rs.close();
		stmt.close();
		return chest;
	}


	@Override
	public void update(Object object) throws SQLException {

		Chest chest = (Chest) object;
		String sql = "update hg_chests set type=?, x=?, y=?, z=?, pitch=?, yaw=?, backup_id=? where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, chest.getType());
		stmt.setInt(2, chest.getX());
		stmt.setInt(3, chest.getY());
		stmt.setInt(4, chest.getZ());
		stmt.setString(5, chest.getPitch().toString());
		stmt.setString(6, chest.getYaw().toString());
		stmt.setInt(7, chest.getBackup().getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
