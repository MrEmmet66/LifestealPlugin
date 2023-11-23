package org.mbf.lifestealplugin.handlers;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.mbf.lifestealplugin.LifestealPlugin;
import org.mbf.lifestealplugin.items.HeartCreator;
import org.mbf.lifestealplugin.items.HeartFragmentCreator;
import org.mbf.lifestealplugin.items.ItemKeys;

import java.net.http.WebSocket;

public class PlayerListener implements Listener {
    private final HeartCreator heartCreator = new HeartCreator();
    private final HeartFragmentCreator heartFragmentCreator = new HeartFragmentCreator();
    private final LifestealPlugin plugin = LifestealPlugin.getPlugin();

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getAction().isLeftClick() || e.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if(!e.getItem().getItemMeta().getPersistentDataContainer().has(ItemKeys.HEART_KEY, PersistentDataType.BOOLEAN)) {
            return;
        }
        if(e.getPlayer().getMaxHealth() >= plugin.getConfig().getInt("maxHearts") * 2) {
            e.getPlayer().sendMessage("You already have max hearts");
            return;
        }
        Player player = e.getPlayer();
        player.setMaxHealth(player.getMaxHealth() + 2);
        player.sendMessage("You gained 1 heart");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() == null)
            return;
        if(e.getEntity().getKiller().getMaxHealth() >= plugin.getConfig().getInt("health-when-drop-fragments") * 2)
            e.getDrops().add(heartFragmentCreator.create(plugin.getConfig().getInt("drop-fragments")));
        else
            e.getDrops().add(heartCreator.createHeart(plugin.getConfig().getInt("drop-hearts")));
        e.getPlayer().setMaxHealth(e.getPlayer().getMaxHealth() - plugin.getConfig().getInt("remove-hearts") * 2);
    }
}