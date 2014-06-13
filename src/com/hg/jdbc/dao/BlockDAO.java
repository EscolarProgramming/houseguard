package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.jdbc.Conexao;
import com.hg.jdbc.dao.model.Block;

public class BlockDAO implements BaseDAO {

	private final Connection connection;


	public BlockDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_blocks ( ");
		sql.append("	id INT AUTO_INCREMENT, ");
		sql.append("	blocks_and_locations MEDIUMTEXT not null, ");
		sql.append("	PRIMARY KEY (id) ");
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

		sql.append(" CREATE TABLE IF NOT EXISTS hg_blocks ( ");
		sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("	blocks_and_locations text not null ");
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

		sql.append(" DROP TABLE IF EXISTS hg_blocks ");

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

		Block block = (Block) object;
		StringBuffer sql = new StringBuffer();

		sql.append("insert into hg_blocks (blocks_and_locations) values (?)");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, block.getBlocksAndLocations());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public List<Block> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_blocks");

		ResultSet rs = stmt.executeQuery();
		List<Block> blocks = new ArrayList<Block>();

		while (rs.next()) {
			Block blockDatabase = new Block();
			blockDatabase.setId(rs.getInt("id"));
			blockDatabase.setBlocksAndLocations(rs.getString("blocks_and_locations"));
			blocks.add(blockDatabase);
		}

		rs.close();
		stmt.close();
		return blocks;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from hg_blocks where id = " + id;
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
	public Block findById(Integer id) throws SQLException {

		String sql = "select * from hg_blocks where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Block block = new Block();

		while (rs.next()) {
			block.setId(rs.getInt("id"));
			block.setBlocksAndLocations(rs.getString("blocks_and_locations"));

		}

		rs.close();
		stmt.close();
		return block;
	}


	public Block findBy(Block block) throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append("select id from hg_blocks where blocks_and_locations=? ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());
		stmt.setString(1, block.getBlocksAndLocations());

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			block.setId(rs.getInt("id"));
		}

		rs.close();
		stmt.close();
		return block;
	}


	@Override
	public void update(Object object) throws SQLException {

//		Block block = (Block) object;
//		String sql = "update hg_blocks set blocks_and_locations=? where id=?";
//		PreparedStatement stmt = connection.prepareStatement(sql.toString());
//
//		stmt.setString(1, block.getBlocksAndLocations());
//		stmt.setInt(2, block.getBackup().getId());
//
//		try {
//			stmt.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			stmt.close();
//		}
	}

}
