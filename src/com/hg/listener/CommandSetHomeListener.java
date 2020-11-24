package com.hg.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.hg.util.Messages;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class CommandSetHomeListener implements Listener {

    public final String PrefixYellow = ChatColor.YELLOW.toString();
    public final String PrefixBlue = ChatColor.DARK_AQUA.toString();
    public final String PrefixRed = ChatColor.DARK_RED.toString();
    public final String PrefixYellowConsole = ChatColor.GOLD + "[HouseGuard] " + ChatColor.YELLOW;
    public final String PrefixBlueConsole = ChatColor.BLUE + "[HouseGuard] " + ChatColor.DARK_AQUA;
    public final String PrefixRedConsole = ChatColor.RED + "[HouseGuard] " + ChatColor.DARK_RED;
    WorldGuardPlugin worldGuard;

    @EventHandler
    public void OnCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));

        ApplicableRegionSet set = rm.getApplicableRegions(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        if (set.size() == 0) {
            return;
        }

        set.toString().toLowerCase();
        String id = set.iterator().next().getId();

        if (!player.getName().toLowerCase().equalsIgnoreCase(rm.getRegion(id).getOwners().toUserFriendlyString().toLowerCase())
                && !rm.getRegion(id).getMembers().contains(player.getName().toLowerCase())) {

            String s = e.getMessage().toLowerCase();
            if (s.startsWith("/sethome") || s.startsWith("/home set") || s.startsWith("/clan home set")) {
                e.getPlayer().sendMessage(PrefixRed + Messages.getString("hg.cannot_use_command"));
//				e.getPlayer().sendMessage(ChatColor.RED + "Voce so pode usar este comando em suas areas ou em areas nao protegidas.");
                e.setCancelled(true);
            }

        }

    }
}
