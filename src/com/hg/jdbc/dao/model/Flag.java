package com.hg.jdbc.dao.model;

public class Flag {

    Integer id;

    String name;
    String value;
    Region region;


    // mob-spawning,invincible,blocked-cmds,allowed-cmds,ice-melt,snow-melt,entry

    public Flag() {

    }


    public Flag(String name, String value, Region region) {
        super();
        this.name = name;
        this.value = value;
        this.region = region;
    }


    public Flag(Integer id) {
        super();
        this.id = id;
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
        this.name = name;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public Region getRegion() {
        return region;
    }


    public void setRegion(Region region) {
        this.region = region;
    }


    @Override
    public String toString() {
        return "Flag [id=" + id + ", name=" + name + ", value=" + value + ", \nregion=" + region + "]";
    }

}
