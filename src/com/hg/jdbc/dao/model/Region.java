package com.hg.jdbc.dao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hg.util.Util;

public class Region {

	Integer id;
	String name;
	String fullName;
	Player owner;
	Player tenant; // inquilino
	String world;

	Integer initialPositionX;
	Integer finalPositionX;
	Integer initialPositionY;
	Integer finalPositionY;
	Integer initialPositionZ;
	Integer finalPositionZ;

	String status;
	Integer sellingPrice;
	Boolean blocked;

	List<Flag> flags;
	List<Player> members;

	Region mainRegion;
	List<Region> subRegions;

	Date startDateTenancy;
	Integer tenancyPeriod;
	Integer tenancyTax;


	public Region() {
		blocked = false;
		sellingPrice = 0;
	}


	public Region(Integer id) {
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


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public Player getOwner() {
		return owner;
	}


	public void setOwner(Player owner) {
		this.owner = owner;
	}


	public Player getTenant() {
		return tenant;
	}


	public void setTenant(Player tenant) {
		this.tenant = tenant;
	}


	public String getWorld() {
		return world;
	}


	public void setWorld(String world) {
		this.world = world;
	}


	public Integer getInitialPositionX() {
		return initialPositionX;
	}


	public void setInitialPositionX(Integer initialPositionX) {
		this.initialPositionX = initialPositionX;
	}


	public Integer getFinalPositionX() {
		return finalPositionX;
	}


	public void setFinalPositionX(Integer finalPositionX) {
		this.finalPositionX = finalPositionX;
	}


	public Integer getInitialPositionY() {
		return initialPositionY;
	}


	public void setInitialPositionY(Integer initialPositionY) {
		this.initialPositionY = initialPositionY;
	}


	public Integer getFinalPositionY() {
		return finalPositionY;
	}


	public void setFinalPositionY(Integer finalPositionY) {
		this.finalPositionY = finalPositionY;
	}


	public Integer getInitialPositionZ() {
		return initialPositionZ;
	}


	public void setInitialPositionZ(Integer initialPositionZ) {
		this.initialPositionZ = initialPositionZ;
	}


	public Integer getFinalPositionZ() {
		return finalPositionZ;
	}


	public void setFinalPositionZ(Integer finalPositionZ) {
		this.finalPositionZ = finalPositionZ;
	}


	public Boolean getBlocked() {
		return blocked;
	}


	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}


	public List<Flag> getFlags() {
		return flags;
	}


	public void setFlags(List<Flag> flags) {
		this.flags = flags;
	}


	public List<Player> getMembers() {
		return members;
	}


	public void setMembers(List<Player> members) {
		this.members = members;
	}


	public Region getMainRegion() {
		return mainRegion;
	}


	public void setMainRegion(Region mainRegion) {
		this.mainRegion = mainRegion;
	}


	public List<Region> getSubRegions() {
		return subRegions;
	}


	public void setSubRegions(List<Region> subRegions) {
		this.subRegions = subRegions;
	}


	public Date getStartDateTenancy() {
		return startDateTenancy;
	}


	public void setStartDateTenancy(Date startDateTenancy) {
		this.startDateTenancy = startDateTenancy;
	}


	public Integer getTenancyPeriod() {
		return tenancyPeriod;
	}


	public void setTenancyPeriod(Integer tenancyPeriod) {
		this.tenancyPeriod = tenancyPeriod;
	}


	public Integer getTenancyTax() {
		return tenancyTax;
	}


	public void setTenancyTax(Integer tenancyTax) {
		this.tenancyTax = tenancyTax;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getSellingPrice() {
		return sellingPrice;
	}


	public void setSellingPrice(Integer sellingPrice) {
		this.sellingPrice = sellingPrice;
	}


	public void setStartDateTenancy(String startDateStr) {
		if (!Util.empty(startDateStr)) {
			try {
				this.startDateTenancy = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public String getStartDateTenancyString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

		return sdf.format(this.startDateTenancy);
	}


	@Override
	public String toString() {
		return "Region [id=" + id + ", name=" + name + ", fullName=" + fullName + ", \nowner=" + owner + ", \ntenant=" + tenant + ", world=" + world + ", initialPositionX=" + initialPositionX
				+ ", finalPositionX=" + finalPositionX + ", initialPositionY=" + initialPositionY + ", finalPositionY=" + finalPositionY + ", initialPositionZ=" + initialPositionZ
				+ ", finalPositionZ=" + finalPositionZ + ", status=" + status + ", sellingPrice=" + sellingPrice + ", blocked=" + blocked + ", \nflags=" + flags + ", \nmembers=" + members
				+ ", \nmainRegion=" + mainRegion + ", \nsubRegions=" + subRegions + ", startDateTenancy=" + startDateTenancy + ", tenancyPeriod=" + tenancyPeriod + ", tenancyTax=" + tenancyTax + "]";
	}

}
