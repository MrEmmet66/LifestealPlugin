package org.mbf.lifestealplugin.handlers;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.mbf.lifestealplugin.LifestealPlugin;
import org.mbf.lifestealplugin.items.HeartCreator;
import org.mbf.lifestealplugin.items.HeartFragmentCreator;
import org.mbf.lifestealplugin.items.ItemKeys;
import org.mbf.lifestealplugin.items.ReviveBeaconCreator;

import java.net.http.WebSocket;
import java.util.Date;

public class PlayerListener implements Listener {
    private final HeartCreator heartCreator = new HeartCreator();
    private final HeartFragmentCreator heartFragmentCreator = new HeartFragmentCreator();
    private final ReviveBeaconCreator reviveBeaconCreator = new ReviveBeaconCreator();
    private final LifestealPlugin plugin = LifestealPlugin.getPlugin();

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getAction().isLeftClick() || e.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if(!e.getItem().getItemMeta().getPersistentDataContainer().has(ItemKeys.HEART_KEY, PersistentDataType.BOOLEAN)) {
            return;
        }
        if(e.getPlayer().getMaxHealth() >= plugin.getConfig().getInt("max-hearts") * 2) {
            e.getPlayer().sendMessage(ChatColor.RED + "You already have max hearts");
            return;
        }

        Player player = e.getPlayer();
        player.setMaxHealth(player.getMaxHealth() + 2);
        player.getInventory().setItemInMainHand(consumeItem(e.getItem()));
        player.sendMessage(ChatColor.GREEN + "You gained 1 heart");
    }

    @EventHandler
    public void setReviveBeaconCreator(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] items = inventory.getMatrix();
        for(ItemStack item : items) {
            if(item == null)
                return;
        }
        Player player = (Player) event.getView().getPlayer();
        if(player.getPersistentDataContainer().get(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER) >= plugin.getConfig().getInt("max-revive-beacons")) {
            player.sendMessage(ChatColor.RED + "You already have max revive beacons");
            event.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }
        if(isItemHeart(items[0]) && isItemHeart(items[1]) && isItemHeart(items[2]) && isItemHeart(items[3]) && items[4] != null && isItemHeart(items[5]) && isItemHeart(items[6]) && isItemHeart(items[7]) && isItemHeart(items[8])) {
            inventory.setResult(reviveBeaconCreator.create(1));
        }
    }

    @EventHandler
    public void onReviveBeaconCraft(CraftItemEvent event) {
        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN)) {
            Player player = (Player) event.getWhoClicked();
            int reviveBeaconValue = player.getPersistentDataContainer().get(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER);
            player.getPersistentDataContainer().set(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER, reviveBeaconValue + 1);
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onReviveBeaconUse(PlayerInteractEvent event) {
        if(event.getAction().isLeftClick() || event.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN)) {
            return;
        }
        Player player = event.getPlayer();
        int reviveBeaconValue = player.getPersistentDataContainer().get(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER);
        if(player.getPersistentDataContainer().has(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN)) {
            player.sendMessage(ChatColor.RED + "You has already activated revive beacon");
            return;
        }
        player.getPersistentDataContainer().set(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN, true);
        player.getInventory().setItemInMainHand(consumeItem(event.getItem()));
        player.sendMessage(ChatColor.GREEN + "You activated revive beacon!");
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if(player.getKiller() == null)
            return;
        if(player.getKiller().getMaxHealth() >= plugin.getConfig().getInt("health-when-drop-fragments") * 2)
            e.getDrops().add(heartFragmentCreator.create(plugin.getConfig().getInt("drop-fragments")));
        else
            e.getDrops().add(heartCreator.createHeart(plugin.getConfig().getInt("drop-hearts")));
        if(player.getMaxHealth() == 2 && !player.getPersistentDataContainer().has(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN))
            e.getPlayer().ban("You died with 1 heart", (Date) null, null, true);
        else if(player.getMaxHealth() == 2 && player.getPersistentDataContainer().has(ItemKeys.REVIVE_BEACON_KEY, PersistentDataType.BOOLEAN)) {
            player.sendMessage(ChatColor.GREEN + "You died with 1 heart, but you have revive beacon!");
            player.setMaxHealth(plugin.getConfig().getInt("health-after-revive") * 2);
            player.getPersistentDataContainer().remove(ItemKeys.REVIVE_BEACON_KEY);
        }
        else
            e.getPlayer().setMaxHealth(e.getPlayer().getMaxHealth() - plugin.getConfig().getInt("remove-hearts") * 2);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        e.getPlayer().getPersistentDataContainer().set(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();
        if(container.has(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER))
            return;
        player.getPersistentDataContainer().set(ItemKeys.PLAYER_REVIVE_BEACON_KEY, PersistentDataType.INTEGER, 0);
    }

    private ItemStack consumeItem(ItemStack item) {
        ItemStack newItemStack = new ItemStack(item);
        newItemStack.setAmount(item.getAmount() - 1);
        return newItemStack;
    }

    private boolean isItemHeart(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(ItemKeys.HEART_KEY, PersistentDataType.BOOLEAN);
    }
}