package com.hg;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import com.hg.jdbc.dao.FlagDAO;
import com.hg.jdbc.dao.PlayerDAO;
import com.hg.jdbc.dao.RegionDAO;
import com.hg.jdbc.dao.model.Flag;
import com.hg.jdbc.dao.model.Region;
import com.hg.listener.CommandSetHomeListener;
import com.hg.listener.HouseGuardCommandExecutor;
import com.hg.util.Messages;
import com.hg.util.Util;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class HouseGuard extends JavaPlugin implements Listener {

	public ConsoleCommandSender console = Bukkit.getConsoleSender();
	public int NewRomam = Font.ROMAN_BASELINE;

	public final String PrefixYellow = ChatColor.YELLOW.toString();
	public final String PrefixBlue = ChatColor.DARK_AQUA.toString();
	public final String PrefixRed = ChatColor.DARK_RED.toString();

	public final String PrefixYellowConsole = ChatColor.GOLD + "[HouseGuard] " + ChatColor.YELLOW;
	public final String PrefixBlueConsole = ChatColor.BLUE + "[HouseGuard] " + ChatColor.DARK_AQUA;
	public final String PrefixRedConsole = ChatColor.RED + "[HouseGuard] " + ChatColor.DARK_RED;

	public HgConfig hgConfig;
	public static Permission perms = null;
	public static Economy econ = null;
	public File configFile;
	public FileConfiguration config;

	public static HouseGuard plugin;


	public static HouseGuard getHouseGuard() {
		return (HouseGuard) Bukkit.getPluginManager().getPlugin("HouseGuard");
	}


	public void sendMessage(String message) {
		try {
			console.sendMessage(message);
		} catch (Exception e) {
			System.out.println(message);
		}
	}


	@Override
	public void onEnable() {
		plugin = this;

		if (!verificarDependencias()) {
			return;
		}

		configFile = new File(getDataFolder(), "config.yml");
		try {
			if (!configFile.exists()) {
				saveDefaultConfig();
				configFile.getParentFile().mkdirs();
				sendMessage(PrefixBlueConsole + "Config.yml created!");
			}

			// TODO fazer tratamento de cada config com mensagem de erro personalizada

			hgConfig = new HgConfig();
			Messages.enable(hgConfig.getLanguage());
		} catch (Exception e) {
			Messages.enable("en_US");
			System.out.println(PrefixBlueConsole + Messages.getString("hg.loaded"));
			sendMessage(PrefixBlueConsole + Messages.getString("hg.loaded"));
		}
		if (!setupEconomy()) {
			sendMessage(PrefixRedConsole + "Error in plugin, verify your Vault/WorldEdit/WorldGuard!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		setupPermissions();
		sendMessage(PrefixYellowConsole + "Plugin started successfuly!");
		sendMessage(PrefixYellowConsole + "Version: 1.0");
		sendMessage(PrefixYellowConsole + "Authors: Yarkhs, AtomGamers.");
		sendMessage(PrefixYellowConsole + "Good use :)");

		getServer().getPluginManager().registerEvents(new CommandSetHomeListener(), this);
		this.getCommand("basic").setExecutor(new HouseGuardCommandExecutor(hgConfig, plugin, econ, getWorldGuard()));

//		worldGuard = getWorldGuard();

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			sendMessage(PrefixRedConsole + "Failed to submit the stats to mcstats");
			// Failed to submit the stats :-(
		}

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			FlagDAO flagDAO = new FlagDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			if (hgConfig.getIsMySQL()) {
				playerDAO.createTableMySql();
				regionDAO.createTableMySql();
				flagDAO.createTableMySql();
			} else {
				playerDAO.createTableSqlite();
				regionDAO.createTableSqlite();
				flagDAO.createTableSqlite();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		sync();
	}


	@Override
	public void onDisable() {
		sendMessage(PrefixBlueConsole + Messages.getString("hg.plugin_disabled"));
	}


	private Boolean verificarDependencias() {
		if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
			sendMessage(PrefixRedConsole + "Plugin 'WorldEdit' not found.");
			sendMessage(PrefixRedConsole + "Please, Install WorldEdit");
			sendMessage(PrefixRedConsole + "Download in: 'http://dev.bukkit.org/server-mods/worldedit/'.");
			Bukkit.getPluginManager().disablePlugin(this);
			return false;
		}

		if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			sendMessage(PrefixRedConsole + "Plugin 'WorldGuard' not found.");
			sendMessage(PrefixRedConsole + "Please, Install WorldEdit");
			sendMessage(PrefixRedConsole + "Download in: 'http://dev.bukkit.org/server-mods/worldguard/'.");
			Bukkit.getPluginManager().disablePlugin(this);
			return false;
		}

		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			sendMessage(PrefixRedConsole + "Plugin 'Vault' not found.");
			sendMessage(PrefixRedConsole + "Please, Install WorldEdit");
			sendMessage(PrefixRedConsole + "Download in: 'http://dev.bukkit.org/server-mods/vault/'.");
			Bukkit.getPluginManager().disablePlugin(this);
			return false;
		}

		return true;
	}


	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}

		return (econ != null);
	}


	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			perms = permissionProvider.getProvider();
		}
		return (perms != null);
	}


	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}


	public WorldEditPlugin getWorldEdit() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		return (WorldEditPlugin) plugin;
	}


	private Boolean sync() {
		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			FlagDAO flagDAO = new FlagDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			for (World world : plugin.getServer().getWorlds()) {
				RegionManager regionManager = getWorldGuard().getGlobalRegionManager().get(world);
				Map<String, ProtectedRegion> regions = regionManager.getRegions();

				console.sendMessage(PrefixYellowConsole + world.getName());

				for (String regionfullName : regions.keySet()) {
					if (StringUtils.countMatches(regionfullName, "_") == 1) {
						ProtectedRegion protectedRegion = regions.get(regionfullName);
						console.sendMessage(PrefixYellowConsole + regionfullName);
						String[] str = regionfullName.split("_");
						String playerName = str[0];
						String regionName = str[1];

						com.hg.jdbc.dao.model.Player owner = playerDAO.findByName(playerName);

						if (Util.empty(owner.getId())) {
							owner.setName(playerName);
							playerDAO.insert(owner);
							owner = playerDAO.findByName(playerName);
						}
						console.sendMessage(PrefixBlueConsole + owner);

						Region region = new Region();

						region.setOwner(owner);
						region.setName(regionName);
						region.setFullName(regionfullName);
						region.setWorld(world.getName());
						region.setInitialPositionX(protectedRegion.getMinimumPoint().getBlockX());
						region.setInitialPositionY(protectedRegion.getMinimumPoint().getBlockY());
						region.setInitialPositionZ(protectedRegion.getMinimumPoint().getBlockZ());
						region.setFinalPositionX(protectedRegion.getMaximumPoint().getBlockX());
						region.setFinalPositionY(protectedRegion.getMaximumPoint().getBlockY());
						region.setFinalPositionZ(protectedRegion.getMaximumPoint().getBlockZ());

						Integer regionId = regionDAO.findIdByRegion(region);
						if (regionId == 0) {
							regionDAO.insert(region);
							region.setId(regionDAO.findIdByRegion(region));

							// for complicado necessario para conseguir o nome da flag e seu valor. Tirado diretamente do codigo do worldguard
							for (com.sk89q.worldguard.protection.flags.Flag<?> defaultFlags : DefaultFlag.getFlags()) {
								Object flag = protectedRegion.getFlag(defaultFlags);

								if (Util.empty(flag)) {
									continue;
								}

								Flag hgFlag = new Flag();
								hgFlag.setName(defaultFlags.getName());
								hgFlag.setValue(String.valueOf(flag));
								hgFlag.setRegion(region);

								flagDAO.insert(hgFlag);
							}
						}

						console.sendMessage(PrefixBlueConsole + region);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}