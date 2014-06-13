package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.jdbc.dao.model.Flag;

public class FlagDAO implements BaseDAO {

	private final Connection connection;


	public FlagDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = com.hg.jdbc.Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_flags (");
		sql.append(" 	id int unsigned auto_increment,");
		sql.append(" 	name varchar(100) NOT NULL,");
		sql.append(" 	value varchar(100) NOT NULL,");
		sql.append(" 	region_id int unsigned NOT NULL,");
		sql.append(" 	PRIMARY KEY (id),");
		sql.append("	FOREIGN KEY (region_id) REFERENCES hg_regions(id) ");
		sql.append(" );");

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

		sql.append(" CREATE TABLE IF NOT EXISTS hg_flags (");
		sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append(" 	name text NOT NULL,");
		sql.append(" 	value text NOT NULL,");
		sql.append(" 	region_id INTEGER NOT NULL");
		sql.append(" );");

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

		Flag flag = (Flag) object;

		StringBuffer sql = new StringBuffer();
		sql.append("insert into hg_flags (name, value, region_id) values (?,?,?)");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());
		stmt.setString(1, flag.getName());
		stmt.setString(2, flag.getValue());
		stmt.setInt(3, flag.getRegion().getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public List<Flag> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_flags");

		ResultSet rs = stmt.executeQuery();
		List<Flag> flags = new ArrayList<Flag>();

		while (rs.next()) {
			Flag flagDatabase = new Flag();

			flagDatabase.setId(rs.getInt("id"));
			flagDatabase.setName(rs.getString("name"));
			flagDatabase.setValue(rs.getString("value"));

			flags.add(flagDatabase);

		}

		rs.close();
		stmt.close();
		return flags;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from hg_flags where id = " + id;
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
	public Flag findById(Integer id) throws SQLException {

		String sql = "select * from hg_flags where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Flag flag = new Flag();

		while (rs.next()) {
			flag.setId(rs.getInt("id"));
			flag.setName(rs.getString("name"));
			flag.setValue(rs.getString("value"));
		}

		rs.close();
		stmt.close();
		return flag;
	}


	@Override
	public void update(Object object) throws SQLException {

	}


	public void deleteByRegionId(Integer regionId) throws SQLException {

		String sql = "delete from hg_flags where region_id = " + regionId;
		PreparedStatement stmt = connection.prepareStatement(sql);

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
