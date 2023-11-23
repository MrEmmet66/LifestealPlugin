package org.mbf.lifestealplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class HeartFragmentCreator {
    public ItemStack create(int amount) {
        ItemStack heartFragment = new ItemStack(Material.SPIDER_EYE, amount);
        ItemMeta meta = heartFragment.getItemMeta();
        meta.getPersistentDataContainer().set(ItemKeys.HEART_FRAGMENT_KEY, PersistentDataType.BOOLEAN, true);
        meta.setDisplayName(ChatColor.RED + "Heart Fragment");
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "With 4 fragments you can craft a full heart"
        ));
        heartFragment.setItemMeta(meta);
        return heartFragment;
    }
}
