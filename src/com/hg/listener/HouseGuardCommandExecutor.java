package com.hg.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import com.hg.HgConfig;
import com.hg.HgPermissions;
import com.hg.HouseGuard;
import com.hg.jdbc.dao.BackupDAO;
import com.hg.jdbc.dao.BlockDAO;
import com.hg.jdbc.dao.ChestDAO;
import com.hg.jdbc.dao.EnchantmentDAO;
import com.hg.jdbc.dao.EntityDAO;
import com.hg.jdbc.dao.FlagDAO;
import com.hg.jdbc.dao.ItemDAO;
import com.hg.jdbc.dao.PlayerDAO;
import com.hg.jdbc.dao.RegionDAO;
import com.hg.jdbc.dao.SignDAO;
import com.hg.jdbc.dao.model.Backup;
import com.hg.jdbc.dao.model.Flag;
import com.hg.jdbc.dao.model.Item;
import com.hg.jdbc.dao.model.Region;
import com.hg.util.Messages;
import com.hg.util.Util;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class HouseGuardCommandExecutor implements CommandExecutor {

	public ConsoleCommandSender console = Bukkit.getConsoleSender();

	public final String PrefixYellow = ChatColor.YELLOW.toString();
	public final String PrefixBlue = ChatColor.DARK_AQUA.toString();
	public final String PrefixRed = ChatColor.DARK_RED.toString();

	public final String PrefixYellowConsole = ChatColor.GOLD + "[HouseGuard] " + ChatColor.YELLOW;
	public final String PrefixBlueConsole = ChatColor.BLUE + "[HouseGuard] " + ChatColor.DARK_AQUA;
	public final String PrefixRedConsole = ChatColor.RED + "[HouseGuard] " + ChatColor.DARK_RED;

	private String PrefixCommand;
	private WorldGuardPlugin worldGuard;
	public HgConfig hgConfig;

	private HouseGuard plugin;
	private Economy econ = null;

	private final String STATUS_SALE = "sale";
	private final String STATUS_RENT = "rent";
	private final Integer maxLengthRegionName = 16;

	private final Integer TOTAL_BLOCKS_PER_ROW = 10000;


	public HouseGuardCommandExecutor(HgConfig hgConfig, HouseGuard plugin, Economy econ, WorldGuardPlugin worldGuard) {
		super();
		this.hgConfig = hgConfig;
		this.plugin = plugin;
		PrefixCommand = ChatColor.AQUA + hgConfig.getCommandName() + " " + ChatColor.GREEN;
		this.econ = econ;
		this.worldGuard = worldGuard;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(PrefixRed + Messages.getString("hg.player"));
			return true;
		}

		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("houseguard")) {

			if (args.length == 0) {
				player.sendMessage(PrefixYellow + Messages.getString("hg.help_title", 1));
//				player.sendMessage("    ");

				if (player.hasPermission(HgPermissions.build) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.build_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.build_menu"));
				}

				if (player.hasPermission(HgPermissions.buy) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.buy_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.buy_menu"));
				}

				if (player.hasPermission(HgPermissions.sell) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.sell_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.sell_menu"));
				}

				if (player.hasPermission(HgPermissions.expand) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.expand_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.expand_menu"));
				}

				if (player.hasPermission(HgPermissions.del) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.del_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.del_menu"));
				}

				if (player.hasPermission(HgPermissions.addfriend) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.add_friend_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.add_friend_menu"));
				}

				if (player.hasPermission(HgPermissions.delfriend) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.del_friend_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.del_friend_menu"));
				}

				if (player.hasPermission(HgPermissions.rename) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.rename_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.rename_menu"));
				}

				player.sendMessage(PrefixYellow + Messages.getString("hg.optional"));

				return true;
			} else if (args[0].equalsIgnoreCase("2")) {
				player.sendMessage(PrefixYellow + Messages.getString("hg.help_title", 2));
//				player.sendMessage("    ");

				if (player.hasPermission(HgPermissions.pvp) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.pvp_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.pvp_menu"));
				}

				if (player.hasPermission(HgPermissions.msg) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.msg_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.msg_menu"));
				}

				if (player.hasPermission(HgPermissions.list) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.list_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.list_menu"));
				}

				if (player.hasPermission(HgPermissions.info) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.info_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.info_menu"));
				}

				if (player.hasPermission(HgPermissions.prices) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.prices_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.prices_menu"));
				}

				if (player.hasPermission(HgPermissions.tp) || player.isOp() || player.hasPermission(HgPermissions.all)) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.tp_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.tp_menu"));
				}

				if (player.hasPermission(HgPermissions.admin) || player.isOp()) {
					player.sendMessage(PrefixCommand + Messages.getString("hg.admin_text"));
					player.sendMessage(ChatColor.WHITE + Messages.getString("hg.admin_menu"));
				}

				player.sendMessage(PrefixYellow + Messages.getString("hg.optional"));

				return true;

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.admin"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.admin)) {
					return admin(player);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.build"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.build)) {
					return build(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.buy"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.buy)) {
					return buy(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.sell"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.sell)) {
					return sell(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.expand"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.expand)) {
					return expand(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.pvp"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.pvp)) {
					return pvp(sender, player, args);
				} else {
					player.sendMessage(PrefixRed + ChatColor.RED + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.msg"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.msg)) {
					return msg(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.del"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.del)) {
					return del(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.addfriend"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.addfriend)) {
					return addFriend(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.delfriend"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.delfriend)) {
					return delFriend(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.rename"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.rename)) {
					return rename(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.info"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.info)) {
					return info(player);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.list"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.list)) {
					return list(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.prices"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.prices)) {
					return prices(player);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.tp"))) {

				if (player.isOp() || player.hasPermission(HgPermissions.all) || player.hasPermission(HgPermissions.tp)) {
					return tp(player, args);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.sync"))) {

				if (player.isOp()) {
					return sync(player);
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

			} else if (args[0].equalsIgnoreCase(Messages.getString("hg.command.block"))) {

				if (player.isOp()) {
//					return block();
				} else {
					player.sendMessage(PrefixRed + Messages.getString("hg.permission_denied"));
				}

//			} else if (args[0].equalsIgnoreCase("backup")) {
//
//				if ((player.isOp() && player.getName().equalsIgnoreCase("yarkhs")) || (player.isOp() && player.getName().equalsIgnoreCase("atom"))) {
//					return backup(player, args);
//				} else {
//					player.sendMessage(ChatColor.RED + Messages.getString("hg.permission_denied"));
//				}
//
//			} else if (args[0].equalsIgnoreCase("restore")) {
//
//				if ((player.isOp() && player.getName().equalsIgnoreCase("yarkhs")) || (player.isOp() && player.getName().equalsIgnoreCase("atom"))) {
//					return restore(player, args);
//				} else {
//					player.sendMessage(ChatColor.RED + Messages.getString("hg.permission_denied"));
//				}

			} else {
				player.sendMessage(PrefixRed + Messages.getString("hg.command_not_exist"));
			}

		}

		return false;
	}


	private Boolean admin(Player player) {
		player.sendMessage(PrefixCommand + Messages.getString("hg.admin.list_text"));
		player.sendMessage(ChatColor.WHITE + Messages.getString("hg.admin.list_menu"));

		player.sendMessage(PrefixCommand + Messages.getString("hg.admin.tp_text"));
		player.sendMessage(ChatColor.WHITE + Messages.getString("hg.admin.tp_menu"));

		player.sendMessage(PrefixCommand + Messages.getString("hg.sync_text"));
		player.sendMessage(ChatColor.WHITE + Messages.getString("hg.sync_menu"));

//		player.sendMessage(PrefixCommand + Messages.getString("hg.block_text"));
//		player.sendMessage(ChatColor.WHITE + Messages.getString("hg.block_menu"));

		return true;

	}


	private Boolean build(Player player, String[] args) {

		String regionName;
		Integer size;

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			com.hg.jdbc.dao.model.Player playerBd = playerDAO.findByName(player.getName());

			if (Util.empty(playerBd.getId())) {
				playerBd.setName(player.getName());
				playerDAO.insert(playerBd);
			} else {

				// Validations
				if (!player.isOp() && !player.hasPermission(HgPermissions.admin)) {
					Integer totalRegions = regionDAO.countRegionByPlayer(playerBd);
					Boolean possuiPermissaoExtra = false;

					// TODO verificar permissao de uma maneira melhor
					for (int i = 1; i < 50; i++) {
						if (player.hasPermission(HgPermissions.x + i)) {
							possuiPermissaoExtra = true;
							if (totalRegions >= i) {
								player.sendMessage(PrefixRed + Messages.getString("hg.max_regions", i));
								return true;
							} else {
								break;
							}
						}
					}

					if (!possuiPermissaoExtra && totalRegions >= hgConfig.getMaxAreas()) {
						player.sendMessage(PrefixRed + Messages.getString("hg.max_regions", hgConfig.getMaxAreas()));
						return true;
					}
				}

			}

		} catch (SQLException e1) {
			// alguma coisa deu errado ao tentar comprar a regiao
			e1.printStackTrace();
		}

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.build_text"));
			return true;
		}

		regionName = args[1];

		if (regionName.length() > maxLengthRegionName) {
			player.sendMessage(PrefixRed + Messages.getString("hg.max_digits", maxLengthRegionName));
			return true;
		}

		try {
			size = Integer.parseInt(args[2]);

			if ((size < hgConfig.getMinSize() || size > hgConfig.getMaxSize()) && !player.isOp()) {
				player.sendMessage(PrefixRed + Messages.getString("hg.min_and_max", hgConfig.getMinSize(), hgConfig.getMaxSize()));
				return true;
			}
		} catch (NumberFormatException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.invalid_size"));
			return true;
		}

		Boolean noFence = false;

		try {
			if ("nofence".equalsIgnoreCase(args[3]) || "semcerca".equalsIgnoreCase(args[3])) {
				noFence = true;
			}
		} catch (Exception e) {
		}

		construir(player, size, size, regionName, noFence);
		return true;

	}


	private Boolean buy(Player player, String[] args) {

		String regionName;
		String oldOwnerName;
		String newOwnerName = player.getName().toLowerCase();

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.buy_text"));
			return true;
		}

		try {
			regionName = args[1];
			oldOwnerName = args[2].toLowerCase();
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.buy_text"));
			return true;
		}

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			com.hg.jdbc.dao.model.Player oldOwner = playerDAO.findByName(oldOwnerName);
			com.hg.jdbc.dao.model.Player newOwner = playerDAO.findByName(newOwnerName);

			if (Util.empty(oldOwner.getName())) {
				player.sendMessage(PrefixRed + Messages.getString("hg.player_never_build", oldOwnerName));
			} else {
				if (Util.empty(newOwner.getName())) {
					newOwner.setName(newOwnerName);
					playerDAO.insert(newOwner);
					newOwner = playerDAO.findByName(newOwnerName);
				}

				Region region = regionDAO.findByOwnerAndName(oldOwner, regionName);

				if (Util.empty(region) || Util.empty(region.getStatus())) {
					player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
				} else {

					if (oldOwnerName.equalsIgnoreCase(newOwnerName)) {
						player.sendMessage(PrefixRed + Messages.getString("hg.region_same_owner"));
						return true;
					}

					// Validations
					if (!player.isOp()) {
						Integer totalRegions = regionDAO.countRegionByPlayer(newOwner);
						Boolean possuiPermissaoExtra = false;

						// TODO verificar permissao de uma maneira melhor
						for (int i = 1; i < 50; i++) {
							if (player.hasPermission(HgPermissions.x + i)) {
								possuiPermissaoExtra = true;
								if (totalRegions >= i) {
									player.sendMessage(PrefixRed + Messages.getString("hg.max_regions", i));
									return true;
								} else {
									break;
								}
							}
						}

						if (!possuiPermissaoExtra && totalRegions >= hgConfig.getMaxAreas()) {
							player.sendMessage(PrefixRed + Messages.getString("hg.max_regions", hgConfig.getMaxAreas()));
							return true;
						}
					}

					if (econ.getBalance(newOwner.getName()) < region.getSellingPrice()) {
						player.sendMessage(PrefixRed + Messages.getString("hg.enough_money", hgConfig.getCoinName() + region.getSellingPrice()));
						return true;
					}

					RegionManager regionManager = null;
					for (World world : worldGuard.getServer().getWorlds()) {
						if (world.getName().equalsIgnoreCase(region.getWorld())) {
							regionManager = worldGuard.getGlobalRegionManager().get(world);
							break;
						}

					}

					ProtectedRegion protectedRegion = regionManager.getRegion(region.getFullName());
					for (String playerName : protectedRegion.getOwners().getPlayers()) {
						protectedRegion.getOwners().removePlayer(playerName);
					}
					protectedRegion.getOwners().addPlayer(player.getName());

					regionManager.removeRegion(region.getFullName());
					regionManager.addRegion(protectedRegion);
					regionManager.save();

					String oldRegionFullName = oldOwnerName + "_" + region.getName();
					String newRegionFullName = newOwnerName + "_" + region.getName();

					renameRegion(regionManager, region, oldRegionFullName, newRegionFullName);

					region.setName(region.getName() + oldOwnerName);
					region.setFullName(newRegionFullName);

					econ.withdrawPlayer(newOwner.getName(), region.getSellingPrice());
					econ.depositPlayer(oldOwner.getName(), region.getSellingPrice());

					Integer price = region.getSellingPrice();

					region.setOwner(newOwner);
					region.setStatus("");
					region.setSellingPrice(0);
					regionDAO.update(region);

					player.sendMessage(PrefixYellow + Messages.getString("hg.buy_are_sucess", hgConfig.getCoinName() + price));

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ProtectionDatabaseException e) {
			// TODO fazer mensagem melhor
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}


	private Boolean sell(Player player, String[] args) {

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.sell_text"));
			return true;
		}

		String regionName;
		Double price = 0.0;
		Boolean cancel = false;

		try {
			regionName = args[1];

			if ("cancel".equalsIgnoreCase(args[2])) {
				cancel = true;
			} else {
				price = Double.parseDouble(args[2]);
			}
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.sell_text"));
			return true;
		}

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			com.hg.jdbc.dao.model.Player owner = playerDAO.findByName(player.getName().toLowerCase());
			Region region = regionDAO.findByOwnerAndName(owner, regionName);

			if (cancel) {
				if (Util.empty(region.getStatus())) {
					player.sendMessage(PrefixRed + Messages.getString("hg.region_not_for_sale", region.getName()));
				} else {
					region.setStatus("");
					region.setSellingPrice(price.intValue());

					regionDAO.update(region);
					player.sendMessage(PrefixRed + Messages.getString("hg.cancel_sell", region.getName()));
				}

			} else {
				if (STATUS_SALE.equalsIgnoreCase(region.getStatus())) {
					player.sendMessage(PrefixRed + Messages.getString("hg.already_on_sale"));
					return true;
				}

				if (STATUS_RENT.equalsIgnoreCase(region.getStatus())) {
					player.sendMessage(PrefixRed + Messages.getString("hg.cant_sell_tenancy"));
					return true;
				}

				region.setStatus(STATUS_SALE);
				region.setSellingPrice(price.intValue());

				regionDAO.update(region);
				player.sendMessage(PrefixYellow + Messages.getString("hg.area_sale_success"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}


	private Boolean expand(Player player, String[] args) {
		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());

		String regionName;
		String regionFullName;
		Integer expansion;

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.expand_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.expand_text"));
			return true;
		}

		try {
			expansion = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.invalid_size"));
			return true;
		}

		if (regionManager.getRegion(regionFullName) == null) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			return true;
		}

		if (econ.getBalance(player.getName()) < expansion * hgConfig.getBlockPurchaseExpand()) {
			player.sendMessage(PrefixRed + Messages.getString("hg.enough_money", hgConfig.getCoinName() + expansion * hgConfig.getBlockPurchaseExpand()));
			return true;
		}

		ProtectedRegion protectedRegion = regionManager.getRegion(regionFullName);
		int xmin = protectedRegion.getMinimumPoint().getBlockX();
		int zmin = protectedRegion.getMinimumPoint().getBlockZ();
		int xmax = protectedRegion.getMaximumPoint().getBlockX();
		int zmax = protectedRegion.getMaximumPoint().getBlockZ();

		Integer areaNova = (expansion * 2) + xmax - xmin;
		if (areaNova > hgConfig.getMaxSize()) {
			String tamanhoMax = Integer.toString(hgConfig.getMaxSize());
			String tamanhaArea = Integer.toString(xmax - xmin);
			String tamanhoAreaNovo = Integer.toString(areaNova);

			player.sendMessage(PrefixRed + Messages.getString("hg.max_expand_exceeded", tamanhoMax, tamanhaArea, tamanhoAreaNovo));
			return true;
		}

		regionManager.removeRegion(regionFullName);

		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
		}

		expandir(player, xmin, xmax, zmin, zmax, regionName, expansion);
		return true;
	}


	private Boolean msg(Player player, String[] args) {
		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());

		String regionName;
		String regionFullName;
		StringBuffer message = new StringBuffer();

		if (args.length < 2) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.msg_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;

			message.append(args[2]);
			for (int i = 3; i < args.length; i++) {
				message.append(" ");
				message.append(args[i]);
			}
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.msg_text"));
			return true;
		}

		if (regionManager.getRegion(regionFullName) == null) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			return true;
		}

		ProtectedRegion region = regionManager.getRegion(regionFullName);
		try {
			region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(worldGuard, player, ChatColor.AQUA + "" + message));
		} catch (InvalidFlagFormat e1) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_flags"));
		}

		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
		}

		return true;
	}


	private Boolean del(Player player, String[] args) {

		String regionName;
		String regionFullName;

		if (args.length != 2) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.del_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.del_text"));
			return true;
		}

		try {
//			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			FlagDAO flagDAO = new FlagDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

//			com.hg.jdbc.dao.model.Player owner = playerDAO.findByName(player.getName().toLowerCase());

			RegionManager regionManager = null;
			for (World world : worldGuard.getServer().getWorlds()) {

				regionManager = worldGuard.getGlobalRegionManager().get(world);

				if (regionManager.getRegion(regionFullName) != null) {

					ProtectedRegion protectedRegion = regionManager.getRegion(regionFullName);
					delParede(player, protectedRegion.getMinimumPoint().getBlockX(), protectedRegion.getMaximumPoint().getBlockX(), protectedRegion.getMinimumPoint().getBlockZ(), protectedRegion
							.getMaximumPoint().getBlockZ());
					regionManager.removeRegion(regionFullName);

					try {
						regionManager.save();
					} catch (ProtectionDatabaseException e) {
						player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
					}

				}

			}

			if (regionDAO.hasRegion(regionFullName)) {
				for (Region region : regionDAO.listByFullName(regionFullName)) {
					flagDAO.deleteByRegionId(region.getId());
				}
				regionDAO.deleteByFullName(regionFullName);
				player.sendMessage(PrefixYellow + Messages.getString("hg.area_removed", ChatColor.YELLOW + regionName + ChatColor.AQUA));
			} else {
				player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}


	private Boolean rename(Player player, String[] args) {

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.rename_text"));
			return true;
		}

		String oldRegionName = args[1];
		String newRegionName = args[2];
		String oldRegionFullName = player.getName().toLowerCase() + "_" + oldRegionName;
		String newRegionFullName = player.getName().toLowerCase() + "_" + newRegionName;

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			com.hg.jdbc.dao.model.Player owner = playerDAO.findByName(player.getName());
			Region region = regionDAO.findByOwnerAndName(owner, oldRegionName);
			region.setName(newRegionName);
			region.setFullName(newRegionFullName);

			if (regionDAO.hasRegion(newRegionFullName)) {
				player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.rename_text"));
				return true;
			} else {
				RegionManager regionManager = null;
				for (World world : worldGuard.getServer().getWorlds()) {
					if (world.getName().equalsIgnoreCase(region.getWorld())) {
						regionManager = worldGuard.getGlobalRegionManager().get(world);
						break;
					}

				}

				renameRegion(regionManager, region, oldRegionFullName, newRegionFullName);
				regionDAO.update(region);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}


	private void renameRegion(RegionManager regionManager, Region region, String oldRegionFullName, String newRegionFullName) throws ProtectionDatabaseException {
		ProtectedRegion protectedRegion = regionManager.getRegion(oldRegionFullName);

		BlockVector blockVectorInitial = new BlockVector(region.getInitialPositionX(), region.getInitialPositionY(), region.getInitialPositionZ());
		BlockVector blockVectorFinal = new BlockVector(region.getFinalPositionX(), region.getFinalPositionY(), region.getFinalPositionZ());
		ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(newRegionFullName, blockVectorInitial, blockVectorFinal);

		protectedCuboidRegion.setPriority(100);
		protectedCuboidRegion.setOwners(protectedRegion.getOwners());
		protectedCuboidRegion.setMembers(protectedRegion.getMembers());
		protectedCuboidRegion.setFlags(protectedRegion.getFlags());

		regionManager.addRegion(protectedCuboidRegion);
		regionManager.removeRegion(oldRegionFullName);
		regionManager.save();
	}


	private Boolean pvp(CommandSender sender, Player player, String[] args) {
		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());

		String regionName;
		String regionFullName;
		String pvp;

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.pvp_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;
			pvp = args[2];
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.pvp_text"));
			return true;
		}

		if (regionManager.getRegion(regionFullName) == null) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			return true;
		}

		if (econ.getBalance(player.getName()) < hgConfig.getPVPPrice()) {
			player.sendMessage(PrefixRed + Messages.getString("hg.pvp", hgConfig.getCoinName() + hgConfig.getPVPPrice()));
			return true;
		}

		ProtectedRegion protectedRegion = regionManager.getRegion(regionFullName);
		if (pvp.equalsIgnoreCase("on")) {

			try {
				protectedRegion.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(worldGuard, sender, "allow"));
				player.sendMessage(PrefixYellow + Messages.getString("hg.pvp_enabled", regionName));
				econ.withdrawPlayer(player.getName(), hgConfig.getPVPPrice());
			} catch (InvalidFlagFormat e) {
				player.sendMessage(PrefixRed + Messages.getString("hg.enable_pvp_erro"));
			}
		} else if (pvp.equalsIgnoreCase("off")) {

			try {
				protectedRegion.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(worldGuard, sender, "deny"));
				player.sendMessage(PrefixYellow + Messages.getString("hg.pvp_disabled", regionName));
				econ.withdrawPlayer(player.getName(), hgConfig.getPVPPrice());
			} catch (InvalidFlagFormat e) {
				player.sendMessage(PrefixRed + Messages.getString("hg.disable_pvp_erro"));
			}

		} else {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.pvp_text"));
			return true;
		}

		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
		}

		return true;
	}


	private Boolean addFriend(Player player, String[] args) {

		String regionName;
		String regionFullName;
		String member;

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.add_friend_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;
			member = args[2];
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.add_friend_text"));
			return true;
		}

		Player gPlayer = (Bukkit.getServer().getPlayer(member));
		if (gPlayer == null) {
			player.sendMessage(PrefixRed + Messages.getString("hg.player_offline", member));
			return true;
		}

		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
		ProtectedRegion protectedRegion = regionManager.getRegion(regionFullName);

		if (Util.empty(protectedRegion)) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			return true;
		}

		protectedRegion.getMembers().addPlayer(gPlayer.getName());
		player.sendMessage(PrefixYellow + Messages.getString("hg.add_player_to_area", gPlayer.getName()));
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));

		}

		return true;
	}


	private Boolean delFriend(Player player, String[] args) {

		String regionName;
		String regionFullName;
		String member;

		if (args.length != 3) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.del_friend_text"));
			return true;
		}

		try {
			regionName = args[1];
			regionFullName = player.getName() + "_" + regionName;
			member = args[2];
		} catch (Exception e) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.del_friend_text"));
			return true;
		}

		RegionManager mgr = worldGuard.getRegionManager(player.getWorld());
		if (mgr.getRegion(regionFullName) == null) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
			return true;
		}
		ProtectedRegion region = mgr.getRegion(regionFullName);
		region.getMembers().removePlayer(member);
		player.sendMessage(PrefixYellow + Messages.getString("hg.remove_player_to_area", member));

		try {
			mgr.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));

		}

		return true;
	}


	private Boolean info(Player player) {

		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());

		console.sendMessage("set: " + set);
		console.sendMessage("set.size(): " + set.size());
		console.sendMessage("player.getLocation(): " + player.getLocation());

		if (set.size() == 0) {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_not_exist"));
			return true;
		}

		set.toString().toLowerCase();
		String id = set.iterator().next().getId();
		Double tamanho = regionManager.getRegion(id).getMaximumPoint().getX() - regionManager.getRegion(id).getMinimumPoint().getX();

		if (player.getName().equalsIgnoreCase(regionManager.getRegion(id).getOwners().toUserFriendlyString()) || player.isOp()) {
			if (!player.isOp()) {
				player.sendMessage(PrefixYellow + Messages.getString("hg.area_belongs_you"));
			} else {
				player.sendMessage(PrefixYellow + Messages.getString("hg.area_player", ChatColor.RED + regionManager.getRegion(id).getOwners().toUserFriendlyString()));
			}

			player.sendMessage(PrefixYellow + Messages.getString("hg.area_valor", ChatColor.RED + id.split("_")[id.split("_").length - 1]));
			player.sendMessage(PrefixYellow + Messages.getString("hg.size_valor", ChatColor.RED + "" + tamanho.intValue() + " x " + tamanho.intValue()));

			if (regionManager.getRegion(id).getMembers().size() != 0) {
				player.sendMessage(PrefixYellow + Messages.getString("hg.members_valor", ChatColor.YELLOW + regionManager.getRegion(id).getMembers().toUserFriendlyString()));
			} else {
				player.sendMessage(PrefixYellow + Messages.getString("hg.members_valor_none", ChatColor.RED));
			}
		} else {
			player.sendMessage(PrefixRed + Messages.getString("hg.area_player", ChatColor.RED + regionManager.getRegion(id).getOwners().toUserFriendlyString()));
			player.sendMessage(PrefixRed + Messages.getString("hg.area_valor", ChatColor.RED + id.split("_")[id.split("_").length - 1]));
			player.sendMessage(PrefixRed + Messages.getString("hg.size_valor", ChatColor.RED + "" + tamanho.intValue() + " x " + tamanho.intValue()));
		}

		return true;

	}


	private Boolean list(Player player, String[] args) {

		String parametro = "";

		try {
			parametro = args[1];
		} catch (Exception e) {
		}

		if (Util.empty(parametro)) {

			try {
				PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

				com.hg.jdbc.dao.model.Player hgPlayer = playerDAO.findByName(player.getName());

				List<Region> regions = regionDAO.listByOwner(hgPlayer);

				player.sendMessage(PrefixYellow + Messages.getString("hg.your_areas"));

				for (Region region : regions) {
					player.sendMessage(ChatColor.YELLOW + region.getWorld() + " - " + region.getName() + " (" + region.getFinalPositionX() + ", " + region.getFinalPositionY() + ", "
							+ region.getFinalPositionZ() + ") ");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (parametro.equalsIgnoreCase("sale") || parametro.equalsIgnoreCase("venda")) {

			try {
				RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				List<Region> regions = regionDAO.listByStatus(STATUS_SALE);
				player.sendMessage(PrefixYellow + Messages.getString("hg.regions_sale"));

				for (Region region : regions) {
					player.sendMessage(PrefixYellow + region.getWorld() + " - " + region.getOwner().getName() + " - " + hgConfig.getCoinName() + region.getSellingPrice() + " - " + region.getName()
							+ " (" + region.getFinalPositionX() + ", " + region.getFinalPositionY() + ", " + region.getFinalPositionZ() + ") ");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (parametro.equalsIgnoreCase("tenancy") || parametro.equalsIgnoreCase("aluguel")) {

			try {
				RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				List<Region> regions = regionDAO.listByStatus(STATUS_RENT);
				player.sendMessage(PrefixYellow + Messages.getString("hg.regions_tenancy"));

				for (Region region : regions) {
					player.sendMessage(PrefixYellow + region.getWorld() + " - " + region.getOwner().getName() + " - " + hgConfig.getCoinName() + region.getTenancyTax() + " - " + region.getName()
							+ " (" + region.getFinalPositionX() + ", " + region.getFinalPositionY() + ", " + region.getFinalPositionZ() + ") ");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (parametro.equalsIgnoreCase("all") || parametro.equalsIgnoreCase("todos")) {

			try {
				RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				List<Region> regions = regionDAO.listAll();
				player.sendMessage(PrefixYellow + Messages.getString("hg.regions_all"));

				for (Region region : regions) {
					player.sendMessage(PrefixYellow + region.getWorld() + " - " + region.getOwner().getName() + " - " + hgConfig.getCoinName() + region.getTenancyTax() + " - " + region.getName()
							+ " (" + region.getFinalPositionX() + ", " + region.getFinalPositionY() + ", " + region.getFinalPositionZ() + ") ");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return true;
	}


	private Boolean prices(Player player) {

		Double precoCompra = hgConfig.getBlockPurchasePrice().doubleValue();
		Double precoExpansao = hgConfig.getBlockPurchaseExpand().doubleValue();
		Double precoPVP = hgConfig.getPVPPrice().doubleValue();

		player.sendMessage(PrefixYellow + Messages.getString("hg.price_buy", ChatColor.RED + "" + precoCompra.intValue()));
		player.sendMessage(PrefixYellow + Messages.getString("hg.price_expansion", ChatColor.RED + "" + precoExpansao.intValue()));
		player.sendMessage(PrefixYellow + Messages.getString("hg.price_pvp", ChatColor.RED + "" + precoPVP.intValue()));

		return true;
	}


	private Boolean tp(Player player, String[] args) {

		if (args.length != 2) {
			player.sendMessage(PrefixYellow + PrefixCommand + Messages.getString("hg.tp_text"));
			return true;
		}

		String regionName = args[1];

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			com.hg.jdbc.dao.model.Player hgPlayer = playerDAO.findByName(player.getName());

			Region region = null;

			if (player.isOp()) {
				region = regionDAO.findByFullName(regionName);
			} else {
				region = regionDAO.findByOwnerAndName(hgPlayer, regionName);
			}

			if (!regionDAO.hasRegion(region.getFullName())) {
				player.sendMessage(PrefixRed + Messages.getString("hg.area_not_found", regionName));
				return true;
			}

			for (World world : plugin.getServer().getWorlds()) {
				if (world.getName().equals(region.getWorld())) {
					Double x = region.getInitialPositionX().doubleValue();
					Double y = new Double(world.getHighestBlockAt(region.getInitialPositionX(), region.getInitialPositionZ()).getY());
					Double z = region.getInitialPositionZ().doubleValue();

					if (!player.isOp()) {
						player.sendMessage(PrefixYellow + Messages.getString("hg.teleport", hgConfig.getTpDelay()));
						try {
							Thread.sleep(hgConfig.getTpDelay() * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					player.teleport(new Location(world, x, y, z));
					break;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}


	private Boolean sync(Player player) {
		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			FlagDAO flagDAO = new FlagDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			for (World world : plugin.getServer().getWorlds()) {
				RegionManager regionManager = worldGuard.getGlobalRegionManager().get(world);
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

			if (!Util.empty(player)) {
				player.sendMessage(PrefixYellow + "Sucessfull");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}


	/**
	 * Funcao removida do worldguard para entender como funcionam as flags
	 * 
	 */
	public String appendFlagsList(ProtectedRegion protectedRegion) {
		StringBuffer str = new StringBuffer();

		for (com.sk89q.worldguard.protection.flags.Flag<?> flag : DefaultFlag.getFlags()) {
			Object val = protectedRegion.getFlag(flag);

			str.append(", ");
			str.append(flag.getName()).append(": ").append(String.valueOf(val));
		}

		str.append("(none)");

		return str.toString();
	}


	// TODO precisa fazer
	@SuppressWarnings("unused")
	private Boolean block() {
		return true;
	}


	// TODO
	/**
	 * 
	 * Coisas que precisam ser feitas:
	 * Ovelhas nao vem coloridas;
	 * Cavalos nao spawnam;
	 * Placas e outros blocos estam vindo virados para mesma direcao;
	 * escadas nao colam na parede;
	 * blocos estao vindo do mesmo tipo;
	 * algumas vezes da nullpointer nas placas ao restaurar;
	 * portas e camas nao estam vindo direito;
	 */
	@SuppressWarnings("unused")
	private Boolean backup(Player player, String[] args) {

		World world = null;

		if (args.length >= 2) {
			try {
				world = plugin.getServer().getWorld(args[1]);
			} catch (Exception e) {
				player.sendMessage("World dont exist");
				return true;
			}
		} else {
			world = player.getWorld();
		}

		if (Util.empty(world)) {
			return true;
		}

		try {
			BackupDAO backupDAO = new BackupDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			EntityDAO entityDAO = new EntityDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			ChestDAO chestDAO = new ChestDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			BlockDAO blockDAO = new BlockDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			SignDAO signDAO = new SignDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			ItemDAO itemDAO = new ItemDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			EnchantmentDAO enchantmentDAO = new EnchantmentDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			if (args.length == 3 && "del".equalsIgnoreCase(args[2])) {
				enchantmentDAO.dropTable();
				itemDAO.dropTable();
				signDAO.dropTable();
				chestDAO.dropTable();
				blockDAO.dropTable();
				entityDAO.dropTable();
				backupDAO.dropTable();

				player.sendMessage("Backup removed");
				return true;
			}

			if (hgConfig.getIsMySQL()) {
				backupDAO.createTableMySql();
				entityDAO.createTableMySql();
				blockDAO.createTableMySql();
				signDAO.createTableMySql();
				chestDAO.createTableMySql();
				itemDAO.createTableMySql();
				enchantmentDAO.createTableMySql();
			} else {
				backupDAO.createTableSqlite();
				entityDAO.createTableSqlite();
				blockDAO.createTableSqlite();
				signDAO.createTableSqlite();
				chestDAO.createTableSqlite();
				itemDAO.createTableSqlite();
				enchantmentDAO.createTableSqlite();
			}

			player.sendMessage("Backup started");
			console.sendMessage(PrefixBlueConsole + "Backup started");

			RegionManager regionManager = worldGuard.getGlobalRegionManager().get(world);
			Map<String, ProtectedRegion> regions = regionManager.getRegions();
			StringBuffer blocksAndLocations = new StringBuffer();
			StringBuffer specialBlocksAndLocations = new StringBuffer();

			Integer contador = 0;
			Integer totalBlocksPerRegion = 0;
			Integer totalBlocks = 0;
			Integer totalRegions = 0;
			Integer blocksPerRow = 0;

			Backup backup = new Backup();
			backup.setWorld(world.getName());
			backup.setDate(new Date());

			backupDAO.insert(backup);
			backup = backupDAO.findBy(backup);

			for (String regionfullName : regions.keySet()) {
				ProtectedRegion protectedRegion = regions.get(regionfullName);

				if (!"__global__".equalsIgnoreCase(regionfullName)) {

					totalRegions++;
					totalBlocksPerRegion = 0;

					Integer initialX = protectedRegion.getMinimumPoint().getBlockX();
					Integer initialY = protectedRegion.getMinimumPoint().getBlockY();
					Integer initialZ = protectedRegion.getMinimumPoint().getBlockZ();

					Integer finalX = protectedRegion.getMaximumPoint().getBlockX();
					Integer finalY = protectedRegion.getMaximumPoint().getBlockY();
					Integer finalZ = protectedRegion.getMaximumPoint().getBlockZ();

					for (int x = initialX; x <= finalX; x++) {
						for (int y = initialY; y <= finalY; y++) {
							for (int z = initialZ; z <= finalZ; z++) {
								Block block = world.getBlockAt(x, y, z);

//							BlockFace blockFace = block.getFace(world.getBlockAt(x, y + 1, z));

								contador++;
								totalBlocks++;
								totalBlocksPerRegion++;

								String[] specialBlocks = {"WOODEN_DOOR", "IRON_DOOR_BLOCK", "CROPS", "LADDER", "STONE_BUTTON", "BROWN_MUSHROOM", "RED_MUSHROOM", "FIRE", "REDSTONE_WIRE",
										"SUGAR_CANE_BLOCK", "WOOD_DOOR", "VINE", "IRON_DOOR", "NETHER_WARTS", "TORCH", "REDSTONE_TORCH_OFF", "REDSTONE_TORCH_ON", "LEVER", "STONE_PLATE", "WOOD_PLATE"};

								if (ArrayUtils.contains(specialBlocks, block.getType().toString())) {

									specialBlocksAndLocations.append(block.getType().toString() + ";" + x + ";" + y + ";" + z + ":");

								} else if ("SIGN_POST".equals(block.getType().toString()) || "WALL_SIGN".equals(block.getType().toString()) || "SIGN".equals(block.getType().toString())) {
									Sign sign = (Sign) block.getState();

									com.hg.jdbc.dao.model.Sign hgSign = new com.hg.jdbc.dao.model.Sign();

									StringBuilder lines = new StringBuilder();

									for (String line : sign.getLines()) {
										lines.append(line + "_");
									}

									hgSign.setLine(lines.toString());
									hgSign.setX(x);
									hgSign.setY(y);
									hgSign.setZ(z);
									hgSign.setPitch(sign.getLocation().getPitch());
									hgSign.setYaw(sign.getLocation().getYaw());
									hgSign.setType(sign.getType().toString());
									hgSign.setBackup(backup);

									signDAO.insert(hgSign);

								} else if (!"CHEST".equals(block.getType().toString())) {

									if (blocksPerRow >= TOTAL_BLOCKS_PER_ROW) {
//									sendMessage(PrefixRedConsole + "totalBlocks: " + contador);
										com.hg.jdbc.dao.model.Block hgBlock = new com.hg.jdbc.dao.model.Block();
										hgBlock.setBlocksAndLocations(blocksAndLocations.toString());
										blockDAO.insert(hgBlock);

										blocksPerRow = 0;
										blocksAndLocations = new StringBuffer();
									}
									blocksAndLocations.append(block.getType().toString() + ";" + x + ";" + y + ";" + z + ":");
									blocksPerRow++;

								} else {

									Chest chest = (Chest) block.getState();
									com.hg.jdbc.dao.model.Chest hgChest = new com.hg.jdbc.dao.model.Chest();

									hgChest.setType(block.getType().toString());
									hgChest.setX(x);
									hgChest.setY(y);
									hgChest.setZ(z);
									hgChest.setPitch(block.getLocation().getPitch());
									hgChest.setYaw(block.getLocation().getYaw());
									hgChest.setBackup(backup);

									chestDAO.insert(hgChest);
									hgChest = chestDAO.findBy(hgChest);

									for (ItemStack itemStack : chest.getBlockInventory()) {

										if (!Util.empty(itemStack)) {
											Item item = new Item();
											item.setType(itemStack.getType().toString());
											item.setAmount(itemStack.getAmount());
											item.setDurability(itemStack.getDurability());
											item.setChest(hgChest);

											itemDAO.insert(item);
											item = itemDAO.findBy(item);

											List<com.hg.jdbc.dao.model.Enchantment> enchantments = new ArrayList<com.hg.jdbc.dao.model.Enchantment>();
											// Gets a map containing all enchantments and their levels on this item.
											for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
												Enchantment key = entry.getKey(); // enchantment
												Integer value = entry.getValue(); // level

												com.hg.jdbc.dao.model.Enchantment enchantment = new com.hg.jdbc.dao.model.Enchantment();
												enchantment.setType(key.getName());
												enchantment.setLevel(value);
												enchantment.setItem(item);

												enchantmentDAO.insert(enchantment);
												enchantments.add(enchantment);
											}
											item.setEnchantments(enchantments);
											if (item.getEnchantments().size() > 0) {
												item.setHasEnchantment(true);
												itemDAO.update(item);
											}
										}
									}

								}
							}
						}
					}

					blockDAO.insert(new com.hg.jdbc.dao.model.Block(blocksAndLocations.toString()));
					blockDAO.insert(new com.hg.jdbc.dao.model.Block(specialBlocksAndLocations.toString()));

					blocksAndLocations = new StringBuffer();
					specialBlocksAndLocations = new StringBuffer();

					player.sendMessage("Region " + regionfullName + " saved.");
					console.sendMessage(PrefixBlueConsole + "Region " + regionfullName + " saved. Total blocks: " + totalBlocksPerRegion);
				}
			}

			console.sendMessage(PrefixBlueConsole + "Total blocks:" + totalBlocks);
			console.sendMessage(PrefixBlueConsole + "Total regions:" + totalRegions);

			contador = 0;
			// salvando entidades
			for (Entity entity : world.getEntities()) {

				String[] entityType = {"CHICKEN", "PAINTING", "COW", "SHEEP", "PIG", "OCELOT", "WOLF", "HORSE"};

				if (ArrayUtils.contains(entityType, entity.getType().toString())) {
					contador++;

					Location location = entity.getLocation();

					com.hg.jdbc.dao.model.Entity hgEntity = new com.hg.jdbc.dao.model.Entity();
					hgEntity.setType(entity.getType().toString());
					hgEntity.setWorld(world.toString());

					hgEntity.setX(new Double(location.getX()).intValue());
					hgEntity.setY(new Double(location.getY()).intValue());
					hgEntity.setZ(new Double(location.getZ()).intValue());

					if (entity instanceof Sheep) {
						Sheep sheep = (Sheep) entity;
						hgEntity.setColor(sheep.getColor().toString());
					} else if (entity instanceof Horse) {
						Horse horse = (Horse) entity;
						hgEntity.setVariant(horse.getVariant().toString());
						hgEntity.setColor(horse.getColor().toString());
						hgEntity.setStyle(horse.getStyle().toString());
						hgEntity.setCustomName(horse.getCustomName());

						hgEntity.setHealth(horse.getHealth());
						hgEntity.setMaxHealth(horse.getMaxHealth());
						hgEntity.setAge(horse.getAge());
						hgEntity.setTamed(Boolean.valueOf(horse.isTamed()));

						if (!Util.empty(horse.getInventory().getSaddle())) {
							hgEntity.setSaddled(true);
						} else {
							hgEntity.setSaddled(false);
						}

						if (!Util.empty(horse.getInventory().getArmor())) {
							console.sendMessage(PrefixRedConsole + horse.getInventory().getArmor().getType().toString());
							hgEntity.setArmor(horse.getInventory().getArmor().getType().toString());
						}

					}

					entityDAO.insert(hgEntity);

				}
			}

			console.sendMessage(PrefixRedConsole + "totalEntitys: " + contador);

			player.sendMessage("Backup complete");
			console.sendMessage(PrefixBlueConsole + "Backup complete");

		} catch (Exception e) {
			try {
				BackupDAO backupDAO = new BackupDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				EntityDAO animalDAO = new EntityDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				ChestDAO chestDAO = new ChestDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				BlockDAO blockDAO = new BlockDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				ItemDAO itemDAO = new ItemDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
				EnchantmentDAO enchantmentDAO = new EnchantmentDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

				enchantmentDAO.dropTable();
				itemDAO.dropTable();
				chestDAO.dropTable();
				blockDAO.dropTable();
				animalDAO.dropTable();
				backupDAO.dropTable();
			} catch (Exception e2) {
			} finally {
				e.printStackTrace();
			}
		}

		return true;
	}


	// TODO ainda nao esta pronto. Problema com os blocos
	@SuppressWarnings("unused")
	private Boolean restore(Player player, String[] args) {

		World world = null;

		if (args.length >= 2) {
			try {
				world = plugin.getServer().getWorld(args[1]);
			} catch (Exception e) {
				player.sendMessage("World dont exist");
				return true;
			}
		} else {
			world = player.getWorld();
		}

		if (Util.empty(world)) {
			return true;
		}

		try {
			// recuperando do banco
			BackupDAO backupDAO = new BackupDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			EntityDAO entityDAO = new EntityDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			BlockDAO blockDAO = new BlockDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			SignDAO signDAO = new SignDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			ChestDAO chestDAO = new ChestDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			ItemDAO itemDAO = new ItemDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			EnchantmentDAO enchantmentDAO = new EnchantmentDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			player.sendMessage("Restore started");
			console.sendMessage(PrefixBlueConsole + "Restore started");

			Backup backup = backupDAO.findByWorld(world.getName());

			if (Util.empty(backup.getId())) {
				player.sendMessage("You need to make a backup first");
				return true;
			}

			// Primeiro vai recuperar os blocos que nao sao baus, logo nao tem itens.
			for (com.hg.jdbc.dao.model.Block hgBlock : blockDAO.listAll()) {
				String[] blocks = hgBlock.getBlocksAndLocations().split(":");

				for (String strBlock : blocks) {
					String[] str = strBlock.split(";");

					String type = str[0];
					Integer x = Integer.parseInt(str[1]);
					Integer y = Integer.parseInt(str[2]);
					Integer z = Integer.parseInt(str[3]);
//					Float pitch = Float.parseFloat(str[4]);
//					Float yaw = Float.parseFloat(str[5]);
//					String faceDirection = str[6];

					Block block = world.getBlockAt(new Location(world, x, y, z));
					block.setType(Material.getMaterial(type));

//					BlockFace blockFace = block.getFace(world.getBlockAt(x, y + 1, z));
//					blockFace.
				}

			}

			// Pegando baus com seus itens.
			for (com.hg.jdbc.dao.model.Chest hgChest : chestDAO.listAll()) {
				Block block = world.getBlockAt(new Location(world, hgChest.getX(), hgChest.getY(), hgChest.getZ(), hgChest.getYaw(), hgChest.getPitch()));
				block.setType(Material.getMaterial(hgChest.getType()));
				Chest chest = (Chest) block.getState();

				for (Item item : itemDAO.listByChestId(hgChest.getId())) {
					ItemStack itemstack = new ItemStack(Material.getMaterial(item.getType()), item.getAmount(), item.getDurability());

					if (item.getHasEnchantment()) {
						item.setEnchantments(enchantmentDAO.findByItemId(item.getId()));

						for (com.hg.jdbc.dao.model.Enchantment enchantment : enchantmentDAO.findByItemId(item.getId())) {
							itemstack.addEnchantment(Enchantment.getByName(enchantment.getType()), enchantment.getLevel());
						}

					}
					chest.getBlockInventory().addItem(itemstack);
				}

			}

			// placas
//			for (com.hg.jdbc.dao.model.Sign hgSign : signDAO.listAll()) {
//				System.out.println(hgSign);
//				sendMessage(PrefixBlueConsole + hgSign);
//				Block block = world.getBlockAt(new Location(world, hgSign.getX(), hgSign.getY(), hgSign.getZ(), hgSign.getYaw(), hgSign.getPitch()));
//				block.setType(Material.getMaterial(hgSign.getType()));
//				block.setType(Material.SIGN_POST);
//				Sign sign = (Sign) block.getState();
//
//				Integer index = 0;
//				for (String line : hgSign.getLines().split("_")) {
//					sign.setLine(index, line);
//					index++;
//				}
//
//				sign.update();
//
//			}

			// animais
			for (com.hg.jdbc.dao.model.Entity hgEntity : entityDAO.listAll()) {
				Location location = new Location(world, hgEntity.getX(), hgEntity.getY(), hgEntity.getZ());
				Entity entity = null;

				if (hgEntity.getType().equalsIgnoreCase("horse")) {
					entity = world.spawnEntity(location, EntityType.HORSE);
					Horse horse = (Horse) entity;

					horse.setVariant(Horse.Variant.valueOf(hgEntity.getVariant()));
					horse.setColor(Horse.Color.valueOf(hgEntity.getColor()));
					horse.setStyle(Horse.Style.valueOf(hgEntity.getStyle()));
					horse.setCustomName(hgEntity.getCustomName());
					horse.setHealth(hgEntity.getHealth());
					horse.setMaxHealth(hgEntity.getMaxHealth());
					horse.setAge(hgEntity.getAge());

					if (hgEntity.getTamed()) {
						horse.setTamed(hgEntity.getTamed());
					}

					if (hgEntity.getSaddled()) {
						horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
					}

					if (!Util.empty(hgEntity.getArmor())) {
						horse.getInventory().setArmor(new ItemStack(Material.getMaterial(hgEntity.getArmor())));
					}
				} else {

					if (hgEntity.getType().equalsIgnoreCase("CHICKEN")) {
						entity = world.spawnEntity(location, EntityType.CHICKEN);
					} else if (hgEntity.getType().equalsIgnoreCase("COW")) {
						entity = world.spawnEntity(location, EntityType.COW);
					} else if (hgEntity.getType().equalsIgnoreCase("SHEEP")) {
						entity = world.spawnEntity(location, EntityType.SHEEP);
						Sheep sheep = (Sheep) entity;
						sheep.setColor(DyeColor.valueOf(hgEntity.getColor()));
					} else if (hgEntity.getType().equalsIgnoreCase("PIG")) {
						entity = world.spawnEntity(location, EntityType.PIG);
					} else if (hgEntity.getType().equalsIgnoreCase("OCELOT")) {
						entity = world.spawnEntity(location, EntityType.OCELOT);
					} else if (hgEntity.getType().equalsIgnoreCase("WOLF")) {
						entity = world.spawnEntity(location, EntityType.WOLF);
//					} else if (hgAnimal.getType().equalsIgnoreCase("PAINTING")) {
//						entity = world.spawnEntity(location, EntityType.PAINTING);
					}

//					Entity animal = entity;
//
//					animal.setCustomName(hgAnimal.getCustomName());
//					animal.setHealth(hgAnimal.getHealth());
//					animal.setAge(hgAnimal.getAge());
				}

//				if (this.config.getString("animals." + animalUUID + ".chestcontent") != null) {
//					spawnedAnimal.setCarryingChest(true);
//					spawnedAnimal.getInventory().setContents((ItemStack[]) this.config.get("animals." + animalUUID + ".chestcontent"));
//				}

			}

			player.sendMessage("Restore complete");
			console.sendMessage(PrefixBlueConsole + "Restore complete");

//			enchantmentDAO.dropTable();
//			itemDAO.dropTable();
//			chestDAO.dropTable();
//			blockDAO.dropTable();
//			entityDAO.dropTable();
//			backupDAO.dropTable();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}


	public void construir(Player player, int sizeX, int sizeZ, String regionName, Boolean noFence) {
		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());

		try {
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			if (regionDAO.hasRegion(player.getName().toLowerCase() + "_" + regionName.toLowerCase())) {
				player.sendMessage(PrefixYellow + Messages.getString("hg.name_area") + " " + regionName.toLowerCase());
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Integer initialPositionX = player.getLocation().getBlockX() - sizeX / 2;
		Integer initialPositionY = hgConfig.getMinHeight();
		Integer initialPositionZ = player.getLocation().getBlockZ() - sizeZ / 2;
		Integer finalPositionX = player.getLocation().getBlockX() + sizeX / 2;
		Integer finalPositionY = hgConfig.getMaxHeight();
		Integer finalPositionZ = player.getLocation().getBlockZ() + sizeZ / 2;

		BlockVector blockVectorInitial = new BlockVector(initialPositionX, hgConfig.getMaxHeight(), initialPositionZ);
		BlockVector blockVectorFinal = new BlockVector(finalPositionX, hgConfig.getMinHeight(), finalPositionZ);
		ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(player.getName().toLowerCase() + "_" + regionName, blockVectorInitial, blockVectorFinal);
		DefaultDomain defaultDomain = new DefaultDomain();
		regionManager.addRegion(protectedCuboidRegion);
		protectedCuboidRegion.setPriority(100);
		defaultDomain.addPlayer(player.getName());
		protectedCuboidRegion.setOwners(defaultDomain);

		try {
			protectedCuboidRegion.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(worldGuard, player, "allow"));
			protectedCuboidRegion.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(worldGuard, player, "deny"));
			protectedCuboidRegion.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(worldGuard, player, "deny"));
			protectedCuboidRegion.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(worldGuard, player, "deny"));
			protectedCuboidRegion.setFlag(DefaultFlag.GHAST_FIREBALL, DefaultFlag.GHAST_FIREBALL.parseInput(worldGuard, player, "deny"));
			protectedCuboidRegion.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(worldGuard, player, ChatColor.AQUA + player.getName() + "'s region"));
		} catch (InvalidFlagFormat e1) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_flags"));
		}

		ApplicableRegionSet regions = regionManager.getApplicableRegions(protectedCuboidRegion);
		LocalPlayer localPlayer = worldGuard.wrapPlayer(player);
		if (econ.getBalance(player.getName()) < sizeX * hgConfig.getBlockPurchasePrice()) {
			player.sendMessage(PrefixRed + Messages.getString("hg.enough_money", hgConfig.getCoinName() + sizeX * hgConfig.getBlockPurchasePrice()));
			regionManager.removeRegion(player.getName().toLowerCase() + "_" + regionName);
			return;
		}

		if (!regions.isOwnerOfAll(localPlayer) && !player.isOp() && !player.hasPermission(HgPermissions.admin)) {
			player.sendMessage(PrefixYellow + Messages.getString("hg.move_away"));
			regionManager.removeRegion(player.getName().toLowerCase() + "_" + regionName);
			return;
		}

		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
		}
		player.sendMessage(PrefixYellow + Messages.getString("hg.buy_are_sucess", ChatColor.RED + hgConfig.getCoinName() + sizeX * hgConfig.getBlockPurchasePrice()));
		player.sendMessage(PrefixYellow
				+ Messages.getString("hg.region_and_size", ChatColor.RED + regionName + ChatColor.AQUA, ChatColor.RED + "" + sizeX + ChatColor.AQUA + " x " + ChatColor.RED + sizeZ + ChatColor.AQUA));

		econ.withdrawPlayer(player.getName(), sizeX * hgConfig.getBlockPurchasePrice());

		try {
			PlayerDAO playerDAO = new PlayerDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			RegionDAO regionDAO = new RegionDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
			FlagDAO flagDAO = new FlagDAO(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());

			Region region = new Region();
			region.setOwner(playerDAO.findByName(player.getName()));
			region.setName(regionName);
			region.setFullName(player.getName().toLowerCase() + "_" + regionName.toLowerCase());
			region.setWorld(player.getWorld().getName());
			region.setInitialPositionX(initialPositionX);
			region.setInitialPositionY(initialPositionY);
			region.setInitialPositionZ(initialPositionZ);
			region.setFinalPositionX(finalPositionX);
			region.setFinalPositionY(finalPositionY);
			region.setFinalPositionZ(finalPositionZ);

			regionDAO.insert(region);
			region.setId(regionDAO.findIdByRegion(region));

			flagDAO.insert(new Flag(DefaultFlag.PVP.getName(), "alow", region));
			flagDAO.insert(new Flag(DefaultFlag.USE.getName(), "deny", region));
			flagDAO.insert(new Flag(DefaultFlag.ENDER_BUILD.getName(), "deny", region));
			flagDAO.insert(new Flag(DefaultFlag.CREEPER_EXPLOSION.getName(), "deny", region));
			flagDAO.insert(new Flag(DefaultFlag.GHAST_FIREBALL.getName(), "deny", region));
			flagDAO.insert(new Flag(DefaultFlag.GREET_MESSAGE.getName(), player.getName() + "'s region", region));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Levantando a cerca
		if (!noFence) {
			if (hgConfig.getRemoveTree() == true) {
				for (int y = 20; y < 90; y++) {
					for (int x = initialPositionX; x < finalPositionX; x++) {
						for (int z = initialPositionZ; z < finalPositionZ; z++) {
							Block block = new Location(player.getWorld(), x, y, z).getBlock();
							if (block.getType() == Material.LEAVES || block.getType() == Material.LOG) {
								block.setType(Material.AIR);
							}
						}
					}
				}
			}

			if (hgConfig.getAlign() == false) {
				for (int x = initialPositionX; x < finalPositionX; x++) {
					Block block1 = player.getWorld().getHighestBlockAt(x, initialPositionZ);
					Block block2 = player.getWorld().getHighestBlockAt(x, finalPositionZ);

//					block1.setTypeId(hgConfig.getBlockId());
//					block2.setTypeId(hgConfig.getBlockId());
					block1.setType(Material.getMaterial(hgConfig.getBlockName()));
					block2.setType(Material.getMaterial(hgConfig.getBlockName()));

				}

				for (int z = initialPositionZ; z < finalPositionZ; z++) {
					Block block1 = player.getWorld().getHighestBlockAt(initialPositionX, z);
					Block block2 = player.getWorld().getHighestBlockAt(finalPositionX, z);

//					block1.setTypeId(hgConfig.getBlockId());
//					block2.setTypeId(hgConfig.getBlockId());
					block1.setType(Material.getMaterial(hgConfig.getBlockName()));
					block2.setType(Material.getMaterial(hgConfig.getBlockName()));
				}

			} else {
				Integer y = player.getLocation().getBlockY();
				for (int x = initialPositionX; x < finalPositionX; x++) {
					Block block1 = player.getWorld().getBlockAt(x, y, initialPositionZ);
					Block block2 = player.getWorld().getBlockAt(x, y, finalPositionZ);

//					block1.setTypeId(hgConfig.getBlockId());
//					block2.setTypeId(hgConfig.getBlockId());
					block1.setType(Material.getMaterial(hgConfig.getBlockName()));
					block2.setType(Material.getMaterial(hgConfig.getBlockName()));
				}

				for (int z = initialPositionZ; z < finalPositionZ; z++) {
					Block block1 = player.getWorld().getBlockAt(initialPositionX, y, z);
					Block block2 = player.getWorld().getBlockAt(finalPositionX, y, z);

//					block1.setTypeId(hgConfig.getBlockId());
//					block2.setTypeId(hgConfig.getBlockId());
					block1.setType(Material.getMaterial(hgConfig.getBlockName()));
					block2.setType(Material.getMaterial(hgConfig.getBlockName()));
				}
			}

		}

	}


	// TODO revisar
	public void delParede(Player player, int xmin, int xmax, int zmin, int zmax) {
		Location loc = player.getLocation();
		World world = loc.getWorld();

		for (int y = hgConfig.getMinHeight(); y < hgConfig.getMaxHeight(); y++) {
			for (int x = xmin; x < xmax; x++) {
				Block xb = world.getBlockAt(x, y, zmin);
//				if (xb.getTypeId() == hgConfig.getBlockId()) {
//					xb.setTypeId(0);
//				}
				if (hgConfig.getBlockName().equals(xb.getType().toString())) {
					xb.setType(Material.AIR);
				}
			}
			for (int x2 = xmin; x2 <= xmax; x2++) {
				Block xb = world.getBlockAt(x2, y, zmax);
//				if (xb.getTypeId() == hgConfig.getBlockId()) {
//					xb.setTypeId(0);
//				}
				if (hgConfig.getBlockName().equals(xb.getType().toString())) {
					xb.setType(Material.AIR);
				}
			}
			for (int z1 = zmin; z1 < zmax; z1++) {
				Block zb = world.getBlockAt(xmin, y, z1);
//				if (zb.getTypeId() == hgConfig.getBlockId()) {
//					zb.setTypeId(0);
//				}
				if (hgConfig.getBlockName().equals(zb.getType().toString())) {
					zb.setType(Material.AIR);
				}
			}
			for (int z2 = zmin; z2 <= zmax; z2++) {
				Block zb = world.getBlockAt(xmax, y, z2);
//				if (zb.getTypeId() == hgConfig.getBlockId()) {
//					zb.setTypeId(0);
//				}
				if (hgConfig.getBlockName().equals(zb.getType().toString())) {
					zb.setType(Material.AIR);
				}
			}
		}
	}


	// TODO revisar
	public int ifParede(Player player, int xmin, int xmax, int zmin, int zmax) {
		Location loc = player.getLocation();
		World w = loc.getWorld();
		for (int y = hgConfig.getMinHeight(); y < hgConfig.getMaxHeight(); y++) {
			for (int x = xmin; x < xmax; x++) {
				Block xb = w.getBlockAt(x, y, zmin);
//				if (xb.getTypeId() == hgConfig.getBlockId()) {
//					return xb.getY();
//				}
				if (hgConfig.getBlockName().equals(xb.getType().toString())) {
					return xb.getY();
				}
			}
			for (int x2 = xmin; x2 <= xmax; x2++) {
				Block xb = w.getBlockAt(x2, y, zmax);
//				if (xb.getTypeId() == hgConfig.getBlockId()) {
//					return xb.getY();
//				}
				if (hgConfig.getBlockName().equals(xb.getType().toString())) {
					return xb.getY();
				}
			}
			for (int z1 = zmin; z1 < zmax; z1++) {
				Block zb = w.getBlockAt(xmin, y, z1);
//				if (zb.getTypeId() == hgConfig.getBlockId()) {
//					return zb.getY();
//				}
				if (hgConfig.getBlockName().equals(zb.getType().toString())) {
					return zb.getY();
				}
			}
			for (int z2 = zmin; z2 <= zmax; z2++) {
				Block zb = w.getBlockAt(xmax, y, z2);
//				if (zb.getTypeId() == hgConfig.getBlockId()) {
//					return zb.getY();
//				}
				if (hgConfig.getBlockName().equals(zb.getType().toString())) {
					return zb.getY();
				}

			}
		}
		return 0;
	}


	// TODO revisar
	public void expandir(Player player, int xmin, int xmax, int zmin, int zmax, String area, Integer tamanho) {
		RegionManager mgr = worldGuard.getRegionManager(player.getWorld());
		BlockVector bv1 = new BlockVector(xmin - tamanho, hgConfig.getMinHeight(), zmin - tamanho);
		BlockVector bv2 = new BlockVector(xmax + tamanho, hgConfig.getMaxHeight(), zmax + tamanho);
		ProtectedCuboidRegion pr = new ProtectedCuboidRegion(player.getName().toLowerCase() + "_" + area, bv1, bv2);
		DefaultDomain dd = new DefaultDomain();
		mgr.addRegion(pr);
		pr.setPriority(100);
		dd.addPlayer(player.getName());
		pr.setOwners(dd);

		try {
			pr.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(worldGuard, player, "allow"));
			pr.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(worldGuard, player, "deny"));
			pr.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(worldGuard, player, "deny"));
			pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(worldGuard, player, "deny"));
		} catch (InvalidFlagFormat e1) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_flags"));
		}
		ApplicableRegionSet regions = mgr.getApplicableRegions(pr);
		LocalPlayer localPlayer = worldGuard.wrapPlayer(player);

		if (!regions.isOwnerOfAll(localPlayer) && (!player.isOp())) {
			player.sendMessage(PrefixRed + Messages.getString("hg.move_away"));
			mgr.removeRegion(player.getName().toLowerCase() + "_" + area);
			BlockVector bv1n = new BlockVector(xmin, hgConfig.getMinHeight(), zmin);
			BlockVector bv2n = new BlockVector(xmax, hgConfig.getMaxHeight(), zmax);
			pr.setMaximumPoint(bv2n);
			pr.setMinimumPoint(bv1n);
			mgr.addRegion(pr);
			try {
				pr.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(worldGuard, player, "allow"));
				pr.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(worldGuard, player, "deny"));
				pr.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(worldGuard, player, "deny"));
				pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(worldGuard, player, "deny"));
			} catch (InvalidFlagFormat ex) {
				player.sendMessage(PrefixRed + Messages.getString("hg.erro_select_flags"));
			}
			try {
				mgr.save();
			} catch (ProtectionDatabaseException e) {
				player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
			}
			return;
		}

		try {
			mgr.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(PrefixRed + Messages.getString("hg.erro_save"));
		}
		delParede(player, xmin, xmax, zmin, zmax);
		paredeEx(player, xmin - tamanho, xmax + tamanho, zmin - tamanho, zmax + tamanho, ifParede(player, xmin, xmax, zmin, zmax));

		econ.withdrawPlayer(player.getName(), tamanho * hgConfig.getBlockPurchaseExpand());

		String tamanhoArea = ChatColor.RED + area + ChatColor.AQUA;
		String tamanhoAreaNovo = ChatColor.RED + Integer.toString((tamanho * 2) + xmax - xmin) + ChatColor.AQUA;
		String preco = ChatColor.RED + "[" + hgConfig.getCoinName() + tamanho * hgConfig.getBlockPurchaseExpand() + "]";

		player.sendMessage(ChatColor.AQUA + Messages.getString("hg.expand_success", tamanhoArea, tamanhoAreaNovo, preco));
	}


	// TODO revisar
	public void paredeEx(Player player, int xmin, int xmax, int zmin, int zmax, int y) {
		Location loc = player.getLocation();
		World w = loc.getWorld();
		if (hgConfig.getAlign() == false) {
			for (int x = xmin; x < xmax; x++) {
				Block xb = w.getHighestBlockAt(x, zmin);
//				xb.setTypeId(hgConfig.getBlockId());
				xb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
			for (int x2 = xmin; x2 <= xmax; x2++) {
				Block xb = w.getHighestBlockAt(x2, zmax);
//				xb.setTypeId(hgConfig.getBlockId());
				xb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
			for (int z1 = zmin; z1 < zmax; z1++) {
				Block zb = w.getHighestBlockAt(xmin, z1);
//				zb.setTypeId(hgConfig.getBlockId());
				zb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
			for (int z2 = zmin; z2 <= zmax; z2++) {
				Block zb = w.getHighestBlockAt(xmax, z2);
//				zb.setTypeId(hgConfig.getBlockId());
				zb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
		} else {
			for (int x = xmin; x < xmax; x++) {
				Block xb = w.getBlockAt(x, y, zmin);
				xb.setType(Material.FENCE);
			}
			for (int x2 = xmin; x2 <= xmax; x2++) {
				Block xb = w.getBlockAt(x2, y, zmax);
//				xb.setTypeId(hgConfig.getBlockId());
				xb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
			for (int z1 = zmin; z1 < zmax; z1++) {
				Block zb = w.getBlockAt(xmin, y, z1);
//				zb.setTypeId(hgConfig.getBlockId());
				zb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
			for (int z2 = zmin; z2 <= zmax; z2++) {
				Block zb = w.getBlockAt(xmax, y, z2);
//				zb.setTypeId(hgConfig.getBlockId());
				zb.setType(Material.getMaterial(hgConfig.getBlockName()));
			}
		}
	}

}
