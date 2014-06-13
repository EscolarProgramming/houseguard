package com.hg.jdbc.dao.model;

public class Sign {

	String line;
	Integer id;
	Integer x;
	Integer y;
	Integer z;
	Float pitch;
	Float yaw;
//	Integer typeId;
	String type;
	Backup backup;
	String faceDirection;


	public Sign() {
		super();
	}


	public Sign(Integer id) {
		this();
		this.id = id;
	}


	public String getLine() {
		return line;
	}


	public void setLine(String line) {
		this.line = line;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public Backup getBackup() {
		return backup;
	}


	public void setBackup(Backup backup) {
		this.backup = backup;
	}


	public String getFaceDirection() {
		return faceDirection;
	}


	public void setFaceDirection(String faceDirection) {
		this.faceDirection = faceDirection;
	}


	@Override
	public String toString() {
		return "Sign [line=" + line + ", id=" + id + ", x=" + x + ", y=" + y + ", z=" + z + ", pitch=" + pitch + ", yaw=" + yaw + ", type=" + type + ", backup=" + backup + ", faceDirection="
				+ faceDirection + "]";
	}

}
