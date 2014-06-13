package com.hg.jdbc.dao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Backup {

	Integer id;
	String world;
	Date date;
	List<Block> blocks;


	public Backup() {
		super();
	}


	public Backup(Integer id) {
		this();
		this.id = id;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getWorld() {
		return world;
	}


	public void setWorld(String world) {
		this.world = world;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public List<Block> getBlocks() {
		return blocks;
	}


	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}


	public void setDate(String dateStr) {
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String getDateString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(this.date);
	}


	@Override
	public String toString() {
		return "Backup [id=" + id + ", world=" + world + ", date=" + date + ", blocks=" + blocks + "]";
	}

}
