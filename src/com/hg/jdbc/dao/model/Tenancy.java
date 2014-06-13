package com.hg.jdbc.dao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tenancy {

	Integer id;

	Player owner;
	Player tenant; // inquilino
	Integer tax;
	Region region;

	Date tenancyDate;


	public Tenancy() {

	}


	public Tenancy(Integer id) {
		super();
		this.id = id;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public Integer getTax() {
		return tax;
	}


	public void setTax(Integer tax) {
		this.tax = tax;
	}


	public Region getRegion() {
		return region;
	}


	public void setRegion(Region region) {
		this.region = region;
	}


	public Date getTenancyDate() {
		return tenancyDate;
	}


	public void setTenancyDate(Date tenancyDate) {
		this.tenancyDate = tenancyDate;
	}


	public void setTenancyDate(String tenancyDateStr) {
		try {
			this.tenancyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(tenancyDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String getTenancyDateString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

		return sdf.format(this.tenancyDate);
	}

}
