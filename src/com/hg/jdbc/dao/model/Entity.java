package com.hg.jdbc.dao.model;

public class Entity {

	Integer id;
	String world;
	Integer x;
	Integer y;
	Integer z;
	String type;
	Boolean tamed;
	String variant;
	String style;
	String customName;
	String color;
	Double health;
	Double maxHealth;
	Integer age;
	Boolean saddled;
	String armor;


	public Entity() {
		super();

		tamed = false;
		health = 0.0;
		maxHealth = 0.0;
		age = 0;
		saddled = false;

	}


	public Entity(Integer id) {
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


	public Boolean getTamed() {
		return tamed;
	}


	public void setTamed(Boolean tamed) {
		this.tamed = tamed;
	}


	public String getVariant() {
		return variant;
	}


	public void setVariant(String variant) {
		this.variant = variant;
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public Double getHealth() {
		return health;
	}


	public void setHealth(Double health) {
		this.health = health;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Boolean getSaddled() {
		return saddled;
	}


	public void setSaddled(Boolean saddled) {
		this.saddled = saddled;
	}


	public String getArmor() {
		return armor;
	}


	public void setArmor(String armor) {
		this.armor = armor;
	}


	public String getCustomName() {
		return customName;
	}


	public void setCustomName(String customName) {
		this.customName = customName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Double getMaxHealth() {
		return maxHealth;
	}


	public void setMaxHealth(Double maxHealth) {
		this.maxHealth = maxHealth;
	}


	@Override
	public String toString() {
		return "Entity [id=" + id + ", world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", type=" + type + ", tamed=" + tamed + ", variant=" + variant + ", style=" + style + ", customName="
				+ customName + ", color=" + color + ", health=" + health + ", maxHealth=" + maxHealth + ", age=" + age + ", saddled=" + saddled + ", armor=" + armor + "]";
	}

}
