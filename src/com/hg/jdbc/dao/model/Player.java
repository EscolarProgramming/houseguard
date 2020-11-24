package com.hg.jdbc.dao.model;

import java.util.List;

public class Player {

    Integer id;

    String name;
    List<Region> ownerRegions;
    List<Region> tenantRegions;


    public Player() {

    }


    public Player(String name) {
        super();
        this.name = name;
    }


    public Player(Integer id) {
        super();
        this.id = id;
    }


    public Player(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name.toLowerCase();
    }


    public List<Region> getOwnerRegions() {
        return ownerRegions;
    }


    public void setOwnerRegions(List<Region> ownerRegions) {
        this.ownerRegions = ownerRegions;
    }


    public List<Region> getTenantRegions() {
        return tenantRegions;
    }


    public void setTenantRegions(List<Region> tenantRegions) {
        this.tenantRegions = tenantRegions;
    }


    @Override
    public String toString() {
        return "Player [id=" + id + ", name=" + name + ", ownerRegions=" + ownerRegions + ", tenantRegions=" + tenantRegions + "]";
    }

}
