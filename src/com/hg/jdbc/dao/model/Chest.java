package com.hg.jdbc.dao.model;

import java.util.List;

public class Chest {

    Integer id;
    Integer x;
    Integer y;
    Integer z;
    Float pitch;
    Float yaw;
    String type;
    //	Integer minecraftItemId;
    Backup backup;
    List<Item> itens;


    public Chest() {
        super();
    }


    public Chest(Integer id) {
        this();
        this.id = id;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public List<Item> getItens() {
        return itens;
    }


    public void setItens(List<Item> itens) {
        this.itens = itens;
    }


    public Integer getX() {
        return x;
    }


    public void setX(Integer x) {
        this.x = x;
    }


    public Integer getY() {
        return y;
    }


    public void setY(Integer y) {
        this.y = y;
    }


    public Integer getZ() {
        return z;
    }


    public void setZ(Integer z) {
        this.z = z;
    }


    public Backup getBackup() {
        return backup;
    }


    public void setBackup(Backup backup) {
        this.backup = backup;
    }


    public Float getPitch() {
        return pitch;
    }


    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }


    public Float getYaw() {
        return yaw;
    }


    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Chest [id=" + id + ", x=" + x + ", y=" + y + ", z=" + z + ", pitch=" + pitch + ", yaw=" + yaw + ", type=" + type + ", backup=" + backup + ", itens=" + itens + "]";
    }

}
