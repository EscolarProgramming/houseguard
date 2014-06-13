package com.hg.jdbc.dao.model;

public class Block {

	Integer id;
	String blocksAndLocations;


	public Block() {
		super();
	}


	public Block(Integer id) {
		this();
		this.id = id;
	}


	public Block(String blocksAndLocations) {
		super();
		this.blocksAndLocations = blocksAndLocations;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getBlocksAndLocations() {
		return blocksAndLocations;
	}


	public void setBlocksAndLocations(String blocksAndLocations) {
		this.blocksAndLocations = blocksAndLocations;
	}

}
