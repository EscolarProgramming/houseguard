package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.Conexao;
import com.hg.jdbc.dao.model.Chest;
import com.hg.jdbc.dao.model.Item;
import com.hg.util.Util;

public class ItemDAO implements BaseDAO {

    private final Connection connection;

    public ItemDAO(HgConfig hgConfig) throws SQLException {
        this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
    }

    public ItemDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
        this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
    }


    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }


    @Override
    public void createTableMySql() throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_items ( ");
        sql.append("	id INT AUTO_INCREMENT, ");
        sql.append("	type varchar(50), ");
        sql.append("	amount INT, ");
        sql.append("	durability SMALLINT, ");
        sql.append("	chest_id INT NOT NULL, ");
        sql.append("	has_enchantment BOOLEAN, ");
        sql.append("	PRIMARY KEY (id), ");
        sql.append("	FOREIGN KEY (chest_id) REFERENCES hg_chests(id) ");
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

        sql.append(" CREATE TABLE IF NOT EXISTS hg_items ( ");
        sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("	type text, ");
        sql.append("	amount INTEGER, ");
        sql.append("	durability INTEGER, ");
        sql.append("	chest_id INTEGER, ");
        sql.append("	has_enchantment INTEGER, ");
        sql.append("	FOREIGN KEY (chest_id) REFERENCES hg_chests(id) ");
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

        sql.append(" DROP TABLE IF EXISTS hg_items ");

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

        Item item = (Item) object;

        StringBuffer sql = new StringBuffer();

        sql.append("insert into hg_items (type, amount, durability, has_enchantment, chest_id) ");
        sql.append(" values (?,?,?,?,?)");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, item.getType());
        stmt.setInt(2, item.getAmount());
        stmt.setInt(3, item.getDurability());
        stmt.setBoolean(4, item.getHasEnchantment());
        stmt.setInt(5, item.getChest().getId());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }


    @Override
    public List<Item> listAll() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_items");

        ResultSet rs = stmt.executeQuery();
        List<Item> items = new ArrayList<Item>();

        while (rs.next()) {
            Item itemDatabase = new Item();
            itemDatabase.setId(rs.getInt("id"));
            itemDatabase.setType(rs.getString("type"));
            itemDatabase.setAmount(rs.getInt("amount"));
            itemDatabase.setDurability(rs.getShort("durability"));
            itemDatabase.setHasEnchantment(rs.getBoolean("has_enchantment"));
            itemDatabase.setChest(new Chest(rs.getInt("chest_id")));
            items.add(itemDatabase);
        }

        rs.close();
        stmt.close();
        return items;
    }


    @Override
    public void delete(Integer id) throws SQLException {

        String sql = "delete from hg_items where id = " + id;
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
    public Item findById(Integer id) throws SQLException {

        String sql = "select * from hg_items where id=" + id;
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Item item = new Item();

        while (rs.next()) {
            item.setId(rs.getInt("id"));
            item.setType(rs.getString("type"));
            item.setAmount(rs.getInt("amount"));
            item.setDurability(rs.getShort("durability"));
            item.setHasEnchantment(rs.getBoolean("has_enchantment"));
            item.setChest(new Chest(rs.getInt("chest_id")));

        }

        rs.close();
        stmt.close();
        return item;
    }


    public List<Item> listByChestId(Integer chestId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_items where chest_id = " + chestId);

        ResultSet rs = stmt.executeQuery();
        List<Item> items = new ArrayList<Item>();

        while (rs.next()) {
            Item itemDatabase = new Item();
            itemDatabase.setId(rs.getInt("id"));
            itemDatabase.setType(rs.getString("type"));
            itemDatabase.setAmount(rs.getInt("amount"));
            itemDatabase.setDurability(rs.getShort("durability"));
            itemDatabase.setHasEnchantment(rs.getBoolean("has_enchantment"));
            itemDatabase.setChest(new Chest(rs.getInt("chest_id")));

            items.add(itemDatabase);
        }

        rs.close();
        stmt.close();
        return items;
    }


    public Item findBy(Item item) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append("select id from hg_items where type=? and amount=? and durability=? and has_enchantment=? ");

        if (!Util.empty(item.getChest())) {
            sql.append("and chest_id=?");
        }

        PreparedStatement stmt = connection.prepareStatement(sql.toString());
        stmt.setString(1, item.getType());
        stmt.setInt(2, item.getAmount());
        stmt.setInt(3, item.getDurability());
        stmt.setBoolean(4, item.getHasEnchantment());

        if (!Util.empty(item.getChest())) {
            stmt.setInt(5, item.getChest().getId());
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            item.setId(rs.getInt("id"));
        }

        rs.close();
        stmt.close();
        return item;
    }


    @Override
    public void update(Object object) throws SQLException {

        Item item = (Item) object;
        String sql = "update hg_items set type=?, amount=?, durability=?, has_enchantment=?, chest_id=? where id=?";
        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, item.getType());
        stmt.setInt(2, item.getAmount());
        stmt.setInt(3, item.getDurability());
        stmt.setBoolean(4, item.getHasEnchantment());
        stmt.setInt(5, item.getChest().getId());
        stmt.setInt(6, item.getId());

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }

}
