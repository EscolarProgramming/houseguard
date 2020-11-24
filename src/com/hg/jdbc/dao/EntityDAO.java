package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.Conexao;
import com.hg.jdbc.dao.model.Entity;

public class EntityDAO implements BaseDAO {

    private final Connection connection;

    public EntityDAO(HgConfig hgConfig) throws SQLException {
        this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
    }

    public EntityDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
        this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
    }


    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }


    @Override
    public void createTableMySql() throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_entitys ( ");
        sql.append("	id INT AUTO_INCREMENT, ");
        sql.append("	world varchar(50), ");
        sql.append("	x INT, ");
        sql.append("	y INT, ");
        sql.append("	z INT, ");
        sql.append("	type varchar(50), ");
        sql.append("	tamed BOOLEAN, ");
        sql.append("	variant varchar(50), ");
        sql.append("	style varchar(50), ");
        sql.append("	custom_name varchar(50), ");
        sql.append("	color varchar(50), ");
        sql.append("	health DOUBLE, ");
        sql.append("	max_health DOUBLE, ");
        sql.append("	age INT, ");
        sql.append("	saddled BOOLEAN, ");
        sql.append("	armor varchar(50), ");
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


    // TODO arrumar
    @Override
    public void createTableSqlite() throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_entitys ( ");
        sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("	world text, ");
        sql.append("	x INTEGER, ");
        sql.append("	y INTEGER, ");
        sql.append("	z INTEGER, ");
        sql.append("	type text, ");
        sql.append("	tamed BOOLEAN, ");
        sql.append("	variant text, ");
        sql.append("	style text, ");
        sql.append("	custom_name text, ");
        sql.append("	color text, ");
        sql.append("	health DOUBLE, ");
        sql.append("	max_health DOUBLE, ");
        sql.append("	age INTEGER, ");
        sql.append("	saddled BOOLEAN, ");
        sql.append("	armor text ");
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

        sql.append(" DROP TABLE IF EXISTS hg_entitys ");

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

        Entity entity = (Entity) object;
        StringBuffer sql = new StringBuffer();

        sql.append("insert into hg_entitys (world, x, y, z, type, tamed, variant, style, custom_name, color, health, max_health, age, saddled, armor) ");
        sql.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, entity.getWorld());
        stmt.setInt(2, entity.getX());
        stmt.setInt(3, entity.getY());
        stmt.setInt(4, entity.getZ());
        stmt.setString(5, entity.getType());
        stmt.setBoolean(6, entity.getTamed());
        stmt.setString(7, entity.getVariant());
        stmt.setString(8, entity.getStyle());
        stmt.setString(9, entity.getCustomName());
        stmt.setString(10, entity.getColor());
        stmt.setDouble(11, entity.getHealth());
        stmt.setDouble(12, entity.getMaxHealth());
        stmt.setInt(13, entity.getAge());
        stmt.setBoolean(14, entity.getSaddled());
        stmt.setString(15, entity.getArmor());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }


    @Override
    public List<Entity> listAll() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_entitys");

        ResultSet rs = stmt.executeQuery();
        List<Entity> entitys = new ArrayList<Entity>();

        while (rs.next()) {
            Entity entityDatabase = new Entity();
            entityDatabase.setId(rs.getInt("id"));
            entityDatabase.setWorld(rs.getString("world"));
            entityDatabase.setX(rs.getInt("x"));
            entityDatabase.setY(rs.getInt("y"));
            entityDatabase.setZ(rs.getInt("z"));
            entityDatabase.setType(rs.getString("type"));
            entityDatabase.setTamed(rs.getBoolean("tamed"));
            entityDatabase.setVariant(rs.getString("variant"));
            entityDatabase.setStyle(rs.getString("style"));
            entityDatabase.setCustomName(rs.getString("custom_name"));
            entityDatabase.setColor(rs.getString("color"));
            entityDatabase.setHealth(rs.getDouble("health"));
            entityDatabase.setMaxHealth(rs.getDouble("max_health"));
            entityDatabase.setAge(rs.getInt("age"));
            entityDatabase.setSaddled(rs.getBoolean("saddled"));
            entityDatabase.setArmor(rs.getString("armor"));

            System.out.println(entityDatabase);
            entitys.add(entityDatabase);
        }

        rs.close();
        stmt.close();
        return entitys;
    }


    @Override
    public void delete(Integer id) throws SQLException {

        String sql = "delete from hg_entitys where id = " + id;
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
    public Entity findById(Integer id) throws SQLException {

        String sql = "select * from hg_entitys where id=" + id;
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Entity entity = new Entity();

        while (rs.next()) {
            entity.setId(rs.getInt("id"));
            entity.setWorld(rs.getString("world"));
            entity.setX(rs.getInt("x"));
            entity.setY(rs.getInt("y"));
            entity.setZ(rs.getInt("z"));
            entity.setType(rs.getString("type"));
            entity.setCustomName(rs.getString("custom_name"));
            entity.setColor(rs.getString("color"));
            entity.setHealth(rs.getDouble("health"));
            entity.setMaxHealth(rs.getDouble("max_health"));
            entity.setAge(rs.getInt("age"));
            entity.setSaddled(rs.getBoolean("saddled"));
            entity.setArmor(rs.getString("armor"));

        }

        rs.close();
        stmt.close();
        return entity;
    }


    public Entity findBy(Entity entity) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append("select id from hg_entitys ");
        sql.append("where world=? and x=? and y=? and z=? and type=? and tamed=? and variant=? ");
        sql.append("and style=? and custom_name=? and color=? and health=? and max_health=? and age=? and saddled=? and armor=? ");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());
        stmt.setString(1, entity.getWorld());
        stmt.setInt(2, entity.getX());
        stmt.setInt(3, entity.getY());
        stmt.setInt(4, entity.getZ());
        stmt.setString(5, entity.getType());
        stmt.setBoolean(6, entity.getTamed());
        stmt.setString(7, entity.getVariant());
        stmt.setString(8, entity.getStyle());
        stmt.setString(9, entity.getCustomName());
        stmt.setString(10, entity.getColor());
        stmt.setDouble(11, entity.getHealth());
        stmt.setDouble(12, entity.getMaxHealth());
        stmt.setInt(13, entity.getAge());
        stmt.setBoolean(14, entity.getSaddled());
        stmt.setString(15, entity.getArmor());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            entity.setId(rs.getInt("id"));
        }

        rs.close();
        stmt.close();
        return entity;
    }


    @Override
    public void update(Object object) throws SQLException {

        Entity entity = (Entity) object;
        StringBuffer sql = new StringBuffer();

        sql.append("update hg_entitys set world=?, x=?, y=?, z=?, type=?, tamed=?, variant=?, ");
        sql.append("style=?, custom_name=?, color=?, health=?, max_health=?, age=?, saddled=?, armor=?");
        sql.append("where id=?");
        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, entity.getWorld());
        stmt.setInt(2, entity.getX());
        stmt.setInt(3, entity.getY());
        stmt.setInt(4, entity.getZ());
        stmt.setString(5, entity.getType());
        stmt.setBoolean(6, entity.getTamed());
        stmt.setString(7, entity.getVariant());
        stmt.setString(8, entity.getStyle());
        stmt.setString(9, entity.getCustomName());
        stmt.setString(10, entity.getColor());
        stmt.setDouble(11, entity.getHealth());
        stmt.setDouble(12, entity.getMaxHealth());
        stmt.setInt(13, entity.getAge());
        stmt.setBoolean(14, entity.getSaddled());
        stmt.setString(15, entity.getArmor());
        stmt.setInt(16, entity.getId());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }
}
