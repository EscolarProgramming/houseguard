package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.dao.model.Player;

public class PlayerDAO implements BaseDAO {

    private final Connection connection;

    public PlayerDAO(HgConfig hgConfig) throws SQLException {
        this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
    }

    public PlayerDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
        this.connection = com.hg.jdbc.Conexao.getConnection(isMySQL, server, database, user, password);
    }


    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }


    @Override
    public void createTableMySql() throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_players (");
        sql.append(" 	id INT unsigned auto_increment,");
        sql.append(" 	name varchar(100) NOT NULL UNIQUE,");
        sql.append(" 	PRIMARY KEY (id)");
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

        sql.append(" CREATE TABLE IF NOT EXISTS hg_players (");
        sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append(" 	name text NOT NULL UNIQUE");
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

        Player player = (Player) object;

        StringBuffer sql = new StringBuffer();
        sql.append("insert into hg_players (name) values (?)");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());
        stmt.setString(1, player.getName());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }


    @Override
    public List<Player> listAll() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_players");

        ResultSet rs = stmt.executeQuery();
        List<Player> players = new ArrayList<Player>();

        while (rs.next()) {
            Player playerDatabase = new Player();

            playerDatabase.setId(rs.getInt("id"));
            playerDatabase.setName(rs.getString("name"));

            players.add(playerDatabase);

        }

        rs.close();
        stmt.close();
        return players;
    }


    @Override
    public void delete(Integer id) throws SQLException {

        String sql = "delete from hg_players where id = " + id;
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
    public Player findById(Integer id) throws SQLException {

        String sql = "select * from hg_players where id=" + id;
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Player player = new Player();

        while (rs.next()) {
            player.setId(rs.getInt("id"));
            player.setName(rs.getString("name"));
        }

        rs.close();
        stmt.close();
        return player;
    }


    public Player findByName(String name) throws SQLException {

        String sql = "select * from hg_players where name='" + name.toLowerCase() + "'";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Player player = new Player();

        while (rs.next()) {
            player.setId(rs.getInt("id"));
            player.setName(rs.getString("name"));
        }

        rs.close();
        stmt.close();
        return player;
    }


    @Override
    public void update(Object object) throws SQLException {

    }

}
