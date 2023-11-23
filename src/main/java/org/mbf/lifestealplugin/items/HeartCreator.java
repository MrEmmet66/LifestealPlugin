package org.mbf.lifestealplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class HeartCreator {
    public ItemStack createHeart(int amount) {
        ItemStack heart = new ItemStack(Material.FERMENTED_SPIDER_EYE, amount);
        ItemMeta meta = heart.getItemMeta();
        meta.getPersistentDataContainer().set(ItemKeys.HEART_KEY, PersistentDataType.BOOLEAN, true);
        meta.setDisplayName(ChatColor.RED + "Heart");
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Use this to gain additional heart"
        ));
        heart.setItemMeta(meta);
        return heart;
    }
}
