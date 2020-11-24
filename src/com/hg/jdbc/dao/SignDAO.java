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
import com.hg.jdbc.dao.model.Sign;

public class SignDAO implements BaseDAO {

    private final Connection connection;

    public SignDAO(HgConfig hgConfig) throws SQLException {
        this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
    }

    public SignDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
        this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
    }


    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }


    @Override
    public void createTableMySql() throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_signs ( ");
        sql.append("	id INT AUTO_INCREMENT, ");
        sql.append("	line varchar(200), ");
        sql.append("	x INT, ");
        sql.append("	y INT, ");
        sql.append("	z INT, ");
        sql.append("	pitch varchar(20), ");
        sql.append("	yaw varchar(20), ");
        sql.append("	type varchar(50), ");
        sql.append("	face_direction varchar(20), ");
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

        sql.append(" CREATE TABLE IF NOT EXISTS hg_signs ( ");
        sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("	line text, ");
        sql.append("	x INTEGER, ");
        sql.append("	y INTEGER, ");
        sql.append("	z INTEGER, ");
        sql.append("	pitch text, ");
        sql.append("	yaw text, ");
        sql.append("	type text, ");
        sql.append("	face_direction text, ");
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

        sql.append(" DROP TABLE IF EXISTS hg_signs ");

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

        Sign sign = (Sign) object;
        StringBuffer sql = new StringBuffer();

        sql.append("insert into hg_signs (line, x, y, z, pitch, yaw, type, face_direction, backup_id) values (?,?,?,?,?,?,?,?,?)");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, sign.getLine());
        stmt.setInt(2, sign.getX());
        stmt.setInt(3, sign.getY());
        stmt.setInt(4, sign.getZ());
        stmt.setString(5, sign.getPitch().toString());
        stmt.setString(6, sign.getYaw().toString());
        stmt.setString(7, sign.getType());
        stmt.setString(8, sign.getFaceDirection());
        stmt.setInt(9, sign.getBackup().getId());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }


    @Override
    public List<Sign> listAll() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_signs");

        ResultSet rs = stmt.executeQuery();
        List<Sign> signs = new ArrayList<Sign>();

        while (rs.next()) {
            Sign signDatabase = new Sign();
            signDatabase.setId(rs.getInt("id"));
            signDatabase.setLine(rs.getString("line"));
            signDatabase.setX(rs.getInt("x"));
            signDatabase.setY(rs.getInt("y"));
            signDatabase.setZ(rs.getInt("z"));
            signDatabase.setPitch(Float.parseFloat(rs.getString("pitch")));
            signDatabase.setYaw(Float.parseFloat(rs.getString("yaw")));
            signDatabase.setType(rs.getString("type"));
            signDatabase.setFaceDirection(rs.getString("face_direction"));
            signDatabase.setBackup(new Backup(rs.getInt("backup_id")));
            signs.add(signDatabase);
        }

        rs.close();
        stmt.close();
        return signs;
    }


    @Override
    public void delete(Integer id) throws SQLException {

        String sql = "delete from hg_signs where id = " + id;
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
    public Sign findById(Integer id) throws SQLException {

        String sql = "select * from hg_signs where id=" + id;
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Sign sign = new Sign();

        while (rs.next()) {
            sign.setId(rs.getInt("id"));
            sign.setLine(rs.getString("line"));
            sign.setX(rs.getInt("x"));
            sign.setY(rs.getInt("y"));
            sign.setZ(rs.getInt("z"));
            sign.setPitch(Float.parseFloat(rs.getString("pitch")));
            sign.setYaw(Float.parseFloat(rs.getString("yaw")));
            sign.setType(rs.getString("type"));
            sign.setFaceDirection(rs.getString("face_direction"));
            sign.setBackup(new Backup(rs.getInt("backup_id")));

        }

        rs.close();
        stmt.close();
        return sign;
    }


    public List<Sign> listByBackupId(Integer backupId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_signs where backup_id = " + backupId);

        ResultSet rs = stmt.executeQuery();
        List<Sign> signs = new ArrayList<Sign>();

        while (rs.next()) {
            Sign signDatabase = new Sign();
            signDatabase.setId(rs.getInt("id"));
            signDatabase.setLine(rs.getString("line"));
            signDatabase.setX(rs.getInt("x"));
            signDatabase.setY(rs.getInt("y"));
            signDatabase.setZ(rs.getInt("z"));
            signDatabase.setPitch(Float.parseFloat(rs.getString("pitch")));
            signDatabase.setYaw(Float.parseFloat(rs.getString("yaw")));
            signDatabase.setType(rs.getString("type"));
            signDatabase.setFaceDirection(rs.getString("face_direction"));
            signDatabase.setBackup(new Backup(rs.getInt("backup_id")));

            signs.add(signDatabase);
        }

        rs.close();
        stmt.close();
        return signs;
    }


    public Sign findBy(Sign sign) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append("select id from hg_signs where line=? and x=? and y=? and z=? and pitch=? and yaw=? and type=? face_direction=?");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());
        stmt.setString(1, sign.getLine());
        stmt.setInt(2, sign.getX());
        stmt.setInt(3, sign.getY());
        stmt.setInt(4, sign.getZ());
        stmt.setString(5, sign.getPitch().toString());
        stmt.setString(6, sign.getYaw().toString());
        stmt.setString(7, sign.getType());
        stmt.setString(8, sign.getFaceDirection());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            sign.setId(rs.getInt("id"));
        }

        rs.close();
        stmt.close();
        return sign;
    }


    @Override
    public void update(Object object) throws SQLException {

        Sign sign = (Sign) object;
        String sql = "update hg_signs set line=?, x=?, y=?, z=?, pitch=?, yaw=?, type=?, face_direction=?, backup_id=? where id=?";
        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, sign.getLine());
        stmt.setInt(2, sign.getX());
        stmt.setInt(3, sign.getY());
        stmt.setInt(4, sign.getZ());
        stmt.setString(5, sign.getPitch().toString());
        stmt.setString(6, sign.getYaw().toString());
        stmt.setString(7, sign.getType());
        stmt.setString(8, sign.getFaceDirection());
        stmt.setInt(9, sign.getBackup().getId());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }

}
