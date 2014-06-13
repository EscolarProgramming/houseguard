package com.hg;

public class HgConfig {

	private Integer blockPurchasePrice = 10;
	private Integer blockPurchaseExpand = 50;
	private Integer PVPPrice = 500;
	private String coinName = "Coins";
	private String commandName = "/area";
	private Boolean align = false;
//	private Integer blockId = 85;
	private String blockName = "FENCE";
	private Integer blockSubId = 0;
	private Integer maxAreas = 3;
	private Integer minSize = 10;
	private Integer maxSize = 50;
	private Integer minHeight = 0;
	private Integer maxHeight = 256;
	private Boolean removeTree = false;
	private String language = "en_US";
	private Integer tpDelay = 3;

	private Boolean isMySQL = false;
	private String server = "Localhost";
	private String database = "hg";
	private String user = "root";
	private String password = "root";


	public HgConfig() {
		super();
	}


	public Integer getBlockPurchasePrice() {
		return blockPurchasePrice;
	}


	public void setBlockPurchasePrice(Integer blockPurchasePrice) {
		this.blockPurchasePrice = blockPurchasePrice;
	}


	public Integer getBlockPurchaseExpand() {
		return blockPurchaseExpand;
	}


	public void setBlockPurchaseExpand(Integer blockPurchaseExpand) {
		this.blockPurchaseExpand = blockPurchaseExpand;
	}


	public Integer getPVPPrice() {
		return PVPPrice;
	}


	public void setPVPPrice(Integer pVPPrice) {
		PVPPrice = pVPPrice;
	}


	public String getCoinName() {
		return coinName;
	}


	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}


	public String getCommandName() {
		return commandName;
	}


	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}


	public Boolean getAlign() {
		return align;
	}


	public void setAlign(Boolean align) {
		this.align = align;
	}


	public String getBlockName() {
		return blockName;
	}


	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}


	public Integer getBlockSubId() {
		return blockSubId;
	}


	public void setBlockSubId(Integer blockSubId) {
		this.blockSubId = blockSubId;
	}


	public Integer getMaxAreas() {
		return maxAreas;
	}


	public void setMaxAreas(Integer maxAreas) {
		this.maxAreas = maxAreas;
	}


	public Integer getMinSize() {
		return minSize;
	}


	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}


	public Integer getMaxSize() {
		return maxSize;
	}


	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}


	public Integer getMinHeight() {
		return minHeight;
	}


	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}


	public Integer getMaxHeight() {
		return maxHeight;
	}


	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}


	public Boolean getRemoveTree() {
		return removeTree;
	}


	public void setRemoveTree(Boolean removeTree) {
		this.removeTree = removeTree;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public Boolean getIsMySQL() {
		return isMySQL;
	}


	public void setIsMySQL(Boolean isMySQL) {
		this.isMySQL = isMySQL;
	}


	public Integer getTpDelay() {
		return tpDelay;
	}


	public void setTpDelay(Integer tpDelay) {
		this.tpDelay = tpDelay;
	}


	public String getServer() {
		return server;
	}


	public void setServer(String server) {
		this.server = server;
	}


	public String getDatabase() {
		return database;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("-----------------SHP_Config-----------------\n");
		str.append("blockPurchasePrice: " + blockPurchasePrice + "\n");
		str.append("blockPurchaseExpand: " + blockPurchaseExpand + "\n");
		str.append("PVPPrice: " + PVPPrice + "\n");
		str.append("coinName: " + coinName + "\n");
		str.append("CommandName: " + commandName + "\n");
		str.append("align: " + align + "\n");
		str.append("blockName: " + blockName + "\n");
		str.append("blockSubId: " + blockSubId + "\n");
		str.append("maxAreas: " + maxAreas + "\n");
		str.append("minSize: " + minSize + "\n");
		str.append("maxSize: " + maxSize + "\n");
		str.append("minHeight: " + minHeight + "\n");
		str.append("maxHeight: " + maxHeight + "\n");
		str.append("removeTree: " + removeTree + "\n");
		str.append("language: " + language + "\n");
		str.append("tpDelay: " + tpDelay + "\n");
		str.append("isMySQL: " + isMySQL + "\n");
		str.append("server: " + server + "\n");
		str.append("database: " + database + "\n");
		str.append("user: " + user + "\n");
		str.append("password: " + password + "\n");
		str.append("--------------------------------------------");

		return str.toString();
	}

}