package com.hg.jdbc.dao.model;

public class Enchantment {

    Integer id;
    //	Integer bukkitEnchantmentId;
    String type;
    Integer level;
    Item item;


    public Enchantment() {

    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Integer getLevel() {
        return level;
    }


    public void setLevel(Integer level) {
        this.level = level;
    }


    public Item getItem() {
        return item;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    @Override
    public String toString() {
        return "Enchantment [id=" + id + ", type=" + type + ", level=" + level + ", item=" + item + "]";
    }

}
