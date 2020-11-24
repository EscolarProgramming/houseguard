package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.dao.model.Player;
import com.hg.jdbc.dao.model.Region;
import com.hg.util.Util;

public class RegionDAO implements BaseDAO {

    private final Connection connection;


    public RegionDAO(HgConfig hgConfig) throws SQLException {
        this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
    }

    public RegionDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
        this.connection = com.hg.jdbc.Conexao.getConnection(isMySQL, server, database, user, password);
    }


    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }


    @Override
    public void createTableMySql() throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append(" CREATE TABLE IF NOT EXISTS hg_regions ( ");
        sql.append("	id INT unsigned AUTO_INCREMENT, ");
        sql.append("	name VARCHAR(100) NOT NULL, ");
        sql.append("	full_name VARCHAR(100) NOT NULL, ");
        sql.append("	owner_id INT unsigned NOT NULL, ");
        sql.append("	tenant_id INT unsigned , ");
        sql.append("	world VARCHAR(100) NOT NULL, ");
        sql.append("	main_region_id INT unsigned, ");

        sql.append("	initial_position_x INT NOT NULL, ");
        sql.append("	final_position_x INT NOT NULL, ");
        sql.append("	initial_position_y INT NOT NULL, ");
        sql.append("	final_position_y INT NOT NULL, ");
        sql.append("	initial_position_z INT NOT NULL, ");
        sql.append("	final_position_z INT NOT NULL, ");

        sql.append("	status varchar(10), ");
        sql.append("	selling_price INT, ");
        sql.append("	blocked BOOLEAN, ");
        sql.append("	start_date_tenancy TIMESTAMP, ");
        sql.append("	tenancy_period INT, ");
        sql.append("	tenancy_tax INT, ");
        sql.append("	PRIMARY KEY (id), ");
        sql.append("	FOREIGN KEY (main_region_id)  REFERENCES hg_regions(id), ");
        sql.append("	FOREIGN KEY (owner_id)  REFERENCES hg_players(id), ");
        sql.append("	FOREIGN KEY (tenant_id) REFERENCES hg_players(id) ");
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

        sql.append(" CREATE TABLE IF NOT EXISTS hg_regions ( ");
        sql.append("	id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("	name text NOT NULL, ");
        sql.append("	full_name text NOT NULL, ");
        sql.append("	owner_id INTEGER NOT NULL, ");
        sql.append("	tenant_id INTEGER , ");
        sql.append("	world text NOT NULL, ");
        sql.append("	main_region_id INTEGER, ");

        sql.append("	initial_position_x INTEGER NOT NULL, ");
        sql.append("	final_position_x INTEGER NOT NULL, ");
        sql.append("	initial_position_y INTEGER NOT NULL, ");
        sql.append("	final_position_y INTEGER NOT NULL, ");
        sql.append("	initial_position_z INTEGER NOT NULL, ");
        sql.append("	final_position_z INTEGER NOT NULL, ");

        sql.append("	status text, ");
        sql.append("	selling_price INTEGER, ");
        sql.append("	blocked INTEGER, ");
        sql.append("	start_date_tenancy text, ");
        sql.append("	tenancy_period INTEGER, ");
        sql.append("	tenancy_tax INTEGER ");
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
    public void insert(Object object) throws SQLException {

        Region region = (Region) object;

        StringBuffer sql = new StringBuffer();
        sql.append("insert into hg_regions (name, full_name, owner_id, world, ");
        sql.append("initial_position_x, final_position_x, initial_position_y, final_position_y, initial_position_z, final_position_z, ");
        sql.append("status, selling_price, blocked");

        if (!Util.empty(region.getMainRegion()) && !Util.empty(region.getMainRegion().getId())) {
            sql.append(", main_region_id, start_date_tenancy, tenancy_tax, tenancy_period, tenant_id");
        }

        sql.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?");

        if (!Util.empty(region.getMainRegion()) && !Util.empty(region.getMainRegion().getId())) {
            sql.append(",?,?,?,?,?");
        }

        sql.append(")");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, region.getName());
        stmt.setString(2, region.getFullName());
        stmt.setInt(3, region.getOwner().getId());
        stmt.setString(4, region.getWorld());

        stmt.setInt(5, region.getInitialPositionX());
        stmt.setInt(6, region.getFinalPositionX());
        stmt.setInt(7, region.getInitialPositionY());
        stmt.setInt(8, region.getFinalPositionY());
        stmt.setInt(9, region.getInitialPositionZ());
        stmt.setInt(10, region.getFinalPositionZ());

        stmt.setString(11, region.getStatus());
        stmt.setInt(12, region.getSellingPrice());
        stmt.setBoolean(13, region.getBlocked());

        if (!Util.empty(region.getMainRegion()) && !Util.empty(region.getMainRegion().getId())) {
            stmt.setInt(14, region.getMainRegion().getId());
            stmt.setString(15, region.getStartDateTenancyString());
            stmt.setInt(16, region.getTenancyPeriod());
            stmt.setInt(17, region.getTenancyTax());
            stmt.setInt(18, region.getOwner().getId());
        }

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }
    }


    @Override
    public void update(Object object) throws SQLException {

        Region region = (Region) object;

        StringBuffer sql = new StringBuffer();
        sql.append("update hg_regions set name=?, full_name=?, owner_id=?, world=?, ");
        sql.append("initial_position_x=?, final_position_x=?, initial_position_y=?, final_position_y=?, initial_position_z=?, final_position_z=?, ");
        sql.append("status=?, selling_price=?, blocked=? ");

        if (!Util.empty(region.getMainRegion()) && region.getMainRegion().getId() != 0) {
            sql.append(", main_region_id=?, start_date_tenancy=?, tenancy_tax=?, tenancy_period=?, tenant_id=?");
        }

        sql.append(" where id=?");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        stmt.setString(1, region.getName());
        stmt.setString(2, region.getFullName());
        stmt.setInt(3, region.getOwner().getId());
        stmt.setString(4, region.getWorld());

        stmt.setInt(5, region.getInitialPositionX());
        stmt.setInt(6, region.getFinalPositionX());
        stmt.setInt(7, region.getInitialPositionY());
        stmt.setInt(8, region.getFinalPositionY());
        stmt.setInt(9, region.getInitialPositionZ());
        stmt.setInt(10, region.getFinalPositionZ());

        stmt.setString(11, region.getStatus());
        stmt.setInt(12, region.getSellingPrice());
        stmt.setBoolean(13, region.getBlocked());

        if (!Util.empty(region.getMainRegion()) && region.getMainRegion().getId() != 0) {
            stmt.setInt(14, region.getMainRegion().getId());
            stmt.setString(15, region.getStartDateTenancyString());
            stmt.setInt(16, region.getTenancyTax());
            stmt.setInt(17, region.getTenancyPeriod());
            stmt.setInt(18, region.getTenant().getId());
            stmt.setInt(19, region.getId());
        } else {
            stmt.setInt(14, region.getId());
        }

        try {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
        }

    }


    @Override
    public List<Region> listAll() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from hg_regions");

        ResultSet rs = stmt.executeQuery();
        List<Region> regions = new ArrayList<Region>();

        while (rs.next()) {
            Region regionDatabase = new Region();

            regionDatabase.setId(rs.getInt("id"));
            regionDatabase.setName(rs.getString("name"));
            regionDatabase.setFullName(rs.getString("full_name"));

            regionDatabase.setOwner(new Player(rs.getInt("owner_id")));

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                regionDatabase.setOwner(new Player(tenantId));
            }

            regionDatabase.setWorld(rs.getString("world"));
            regionDatabase.setInitialPositionX(rs.getInt("initial_position_x"));
            regionDatabase.setFinalPositionX(rs.getInt("final_position_x"));
            regionDatabase.setInitialPositionY(rs.getInt("initial_position_y"));
            regionDatabase.setFinalPositionY(rs.getInt("final_position_y"));
            regionDatabase.setInitialPositionZ(rs.getInt("initial_position_z"));
            regionDatabase.setFinalPositionZ(rs.getInt("final_position_z"));

            regionDatabase.setStatus(rs.getString("status"));
            regionDatabase.setSellingPrice(rs.getInt("selling_price"));
            regionDatabase.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                regionDatabase.setMainRegion(new Region(mainRegionId));
            }

            regionDatabase.setStartDateTenancy(rs.getString("start_date_tenancy"));
            regionDatabase.setTenancyPeriod(rs.getInt("tenancy_period"));
            regionDatabase.setTenancyTax(rs.getInt("tenancy_tax"));
            regions.add(regionDatabase);

        }

        rs.close();
        stmt.close();
        return regions;
    }


    @Override
    public void delete(Integer id) throws SQLException {

        String sql = "delete from hg_regions where id = " + id;
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
    public Region findById(Integer id) throws SQLException {

        String sql = "select * from hg_regions where id=" + id;
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Region region = new Region();

        while (rs.next()) {
            region.setId(rs.getInt("id"));
            region.setName(rs.getString("name"));
            region.setFullName(rs.getString("full_name"));

            region.setOwner(new Player(rs.getInt("owner_id")));

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                region.setOwner(new Player(tenantId));
            }

            region.setWorld(rs.getString("world"));
            region.setInitialPositionX(rs.getInt("initial_position_x"));
            region.setFinalPositionX(rs.getInt("final_position_x"));
            region.setInitialPositionY(rs.getInt("initial_position_y"));
            region.setFinalPositionY(rs.getInt("final_position_y"));
            region.setInitialPositionZ(rs.getInt("initial_position_z"));
            region.setFinalPositionZ(rs.getInt("final_position_z"));

            region.setStatus(rs.getString("status"));
            region.setSellingPrice(rs.getInt("selling_price"));
            region.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                region.setMainRegion(new Region(mainRegionId));
            }

            region.setStartDateTenancy(rs.getString("start_date_tenancy"));
            region.setTenancyPeriod(rs.getInt("tenancy_period"));
            region.setTenancyTax(rs.getInt("tenancy_tax"));
        }

        rs.close();
        stmt.close();
        return region;
    }


    public List<Region> listByStatus(String status) throws SQLException {

        StringBuffer sql = new StringBuffer();

        sql.append("select world, r.name name, selling_price, tenancy_tax, owner_id, p.name player_name, ");
        sql.append("	initial_position_x, initial_position_y, initial_position_z, final_position_x, final_position_y, final_position_z ");
        sql.append("from hg_regions r ");
        sql.append("join hg_players p on r.owner_id = p.id");
        sql.append(" where status like '" + status + "'");

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        ResultSet rs = stmt.executeQuery();
        List<Region> regions = new ArrayList<Region>();

        while (rs.next()) {
            Region regionDatabase = new Region();

            regionDatabase.setName(rs.getString("name"));
            regionDatabase.setOwner(new Player(rs.getInt("owner_id"), rs.getString("player_name")));
            regionDatabase.setWorld(rs.getString("world"));

            regionDatabase.setInitialPositionX(rs.getInt("initial_position_x"));
            regionDatabase.setFinalPositionX(rs.getInt("final_position_x"));
            regionDatabase.setInitialPositionY(rs.getInt("initial_position_y"));
            regionDatabase.setFinalPositionY(rs.getInt("final_position_y"));
            regionDatabase.setInitialPositionZ(rs.getInt("initial_position_z"));
            regionDatabase.setFinalPositionZ(rs.getInt("final_position_z"));

            regionDatabase.setSellingPrice(rs.getInt("selling_price"));
            regionDatabase.setTenancyTax(rs.getInt("tenancy_tax"));
            regions.add(regionDatabase);

        }

        rs.close();
        stmt.close();
        return regions;
    }


    public List<Region> listByOwner(Player owner) throws SQLException {

        String sql = "select * from hg_regions where owner_id='" + owner.getId() + "' order by world, name";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        List<Region> regions = new ArrayList<Region>();

        while (rs.next()) {
            Region regionDatabase = new Region();

            regionDatabase.setId(rs.getInt("id"));
            regionDatabase.setName(rs.getString("name"));
            regionDatabase.setFullName(rs.getString("full_name"));

            regionDatabase.setOwner(owner);

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                regionDatabase.setOwner(new Player(tenantId));
            }

            regionDatabase.setWorld(rs.getString("world"));
            regionDatabase.setInitialPositionX(rs.getInt("initial_position_x"));
            regionDatabase.setFinalPositionX(rs.getInt("final_position_x"));
            regionDatabase.setInitialPositionY(rs.getInt("initial_position_y"));
            regionDatabase.setFinalPositionY(rs.getInt("final_position_y"));
            regionDatabase.setInitialPositionZ(rs.getInt("initial_position_z"));
            regionDatabase.setFinalPositionZ(rs.getInt("final_position_z"));

            regionDatabase.setStatus(rs.getString("status"));
            regionDatabase.setSellingPrice(rs.getInt("selling_price"));
            regionDatabase.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                regionDatabase.setMainRegion(new Region(mainRegionId));
            }

            regionDatabase.setStartDateTenancy(rs.getString("start_date_tenancy"));
            regionDatabase.setTenancyPeriod(rs.getInt("tenancy_period"));
            regionDatabase.setTenancyTax(rs.getInt("tenancy_tax"));
            regions.add(regionDatabase);

        }

        rs.close();
        stmt.close();
        return regions;
    }


    public List<Region> listByFullName(String regionFullName) throws SQLException {

        String sql = "select * from hg_regions where full_name='" + regionFullName + "'";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        List<Region> regions = new ArrayList<Region>();

        while (rs.next()) {
            Region regionDatabase = new Region();

            regionDatabase.setId(rs.getInt("id"));
            regionDatabase.setName(rs.getString("name"));
            regionDatabase.setFullName(rs.getString("full_name"));

            regionDatabase.setOwner(new Player(rs.getString("owner_id")));

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                regionDatabase.setOwner(new Player(tenantId));
            }

            regionDatabase.setWorld(rs.getString("world"));
            regionDatabase.setInitialPositionX(rs.getInt("initial_position_x"));
            regionDatabase.setFinalPositionX(rs.getInt("final_position_x"));
            regionDatabase.setInitialPositionY(rs.getInt("initial_position_y"));
            regionDatabase.setFinalPositionY(rs.getInt("final_position_y"));
            regionDatabase.setInitialPositionZ(rs.getInt("initial_position_z"));
            regionDatabase.setFinalPositionZ(rs.getInt("final_position_z"));

            regionDatabase.setStatus(rs.getString("status"));
            regionDatabase.setSellingPrice(rs.getInt("selling_price"));
            regionDatabase.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                regionDatabase.setMainRegion(new Region(mainRegionId));
            }

            regionDatabase.setStartDateTenancy(rs.getString("start_date_tenancy"));
            regionDatabase.setTenancyPeriod(rs.getInt("tenancy_period"));
            regionDatabase.setTenancyTax(rs.getInt("tenancy_tax"));
            regions.add(regionDatabase);

        }

        rs.close();
        stmt.close();
        return regions;
    }


    public Region findByOwnerAndName(Player owner, String regionName) throws SQLException {

        String sql = "select * from hg_regions where owner_id='" + owner.getId() + "' and name='" + regionName + "' order by world, name";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Region region = new Region();

        while (rs.next()) {

            region.setId(rs.getInt("id"));
            region.setName(rs.getString("name"));
            region.setFullName(rs.getString("full_name"));

            region.setOwner(owner);

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                region.setTenant(new Player(tenantId));
            }

            region.setWorld(rs.getString("world"));
            region.setInitialPositionX(rs.getInt("initial_position_x"));
            region.setFinalPositionX(rs.getInt("final_position_x"));
            region.setInitialPositionY(rs.getInt("initial_position_y"));
            region.setFinalPositionY(rs.getInt("final_position_y"));
            region.setInitialPositionZ(rs.getInt("initial_position_z"));
            region.setFinalPositionZ(rs.getInt("final_position_z"));

            region.setStatus(rs.getString("status"));
            region.setSellingPrice(rs.getInt("selling_price"));
            region.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                region.setMainRegion(new Region(mainRegionId));
            }

            region.setStartDateTenancy(rs.getString("start_date_tenancy"));
            region.setTenancyPeriod(rs.getInt("tenancy_period"));
            region.setTenancyTax(rs.getInt("tenancy_tax"));

        }

        rs.close();
        stmt.close();
        return region;
    }


    public Region findByFullName(String regionFullName) throws SQLException {

        String sql = "select * from hg_regions where full_name='" + regionFullName + "'";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Region region = new Region();

        while (rs.next()) {

            region.setId(rs.getInt("id"));
            region.setName(rs.getString("name"));
            region.setFullName(rs.getString("full_name"));

            region.setOwner(new Player(rs.getString("owner_id")));

            Integer tenantId = rs.getInt("tenant_id");
            if (!Util.empty(tenantId)) {
                region.setTenant(new Player(tenantId));
            }

            region.setWorld(rs.getString("world"));
            region.setInitialPositionX(rs.getInt("initial_position_x"));
            region.setFinalPositionX(rs.getInt("final_position_x"));
            region.setInitialPositionY(rs.getInt("initial_position_y"));
            region.setFinalPositionY(rs.getInt("final_position_y"));
            region.setInitialPositionZ(rs.getInt("initial_position_z"));
            region.setFinalPositionZ(rs.getInt("final_position_z"));

            region.setStatus(rs.getString("status"));
            region.setSellingPrice(rs.getInt("selling_price"));
            region.setBlocked(rs.getBoolean("blocked"));

            Integer mainRegionId = rs.getInt("main_region_id");
            if (!Util.empty(mainRegionId)) {
                region.setMainRegion(new Region(mainRegionId));
            }

            region.setStartDateTenancy(rs.getString("start_date_tenancy"));
            region.setTenancyPeriod(rs.getInt("tenancy_period"));
            region.setTenancyTax(rs.getInt("tenancy_tax"));

        }

        rs.close();
        stmt.close();
        return region;
    }


    public Integer countRegionByPlayer(Player player) throws SQLException {

        String sql = "select owner_id, COUNT(*) as count from hg_regions where owner_id=" + player.getId() + " group by owner_id";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Integer count = 0;

        while (rs.next()) {
            count = rs.getInt("count");
        }

        rs.close();
        stmt.close();
        return count;
    }


    public Boolean hasRegion(String regionFullName) throws SQLException {

        String sql = "select count(*) as count from hg_regions where full_name='" + regionFullName + "'";
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        Integer count = 0;

        while (rs.next()) {
            count = rs.getInt("count");
        }

        rs.close();
        stmt.close();

        if (count >= 1) {
            return true;
        } else {
            return false;
        }
    }


    public Integer findIdByRegion(Region region) throws SQLException {

        StringBuffer sql = new StringBuffer();
        sql.append("select id from hg_regions ");
        sql.append("where name='" + region.getName() + "' and full_name='" + region.getFullName() + "' and world='" + region.getWorld() + "' and owner_id=" + region.getOwner().getId());

        PreparedStatement stmt = connection.prepareStatement(sql.toString());

        ResultSet rs = stmt.executeQuery();
        Integer regionId = 0;

        while (rs.next()) {
            regionId = rs.getInt("id");
        }

        rs.close();
        stmt.close();
        return regionId;
    }


    public void deleteByFullName(String regionFullName) throws SQLException {
        String sql = "delete from hg_regions where full_name like '" + regionFullName + "'";
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
