package org.mbf.lifestealplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mbf.lifestealplugin.LifestealPlugin;

import java.util.Arrays;

public class ReviveBeaconCreator {
    public ItemStack create(int amount) {
        ItemStack reviveBeacon = new ItemStack(Material.NETHER_STAR, amount);
        ItemMeta meta = reviveBeacon.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Revive Beacon");
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Use this to revive yourself with" + LifestealPlugin.getPlugin().getConfig().getInt("health-after-revive") + " hearts after death"));
        meta.getPersistentDataContainer().set(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN, true);
        reviveBeacon.setItemMeta(meta);
        return reviveBeacon;
    }
}
