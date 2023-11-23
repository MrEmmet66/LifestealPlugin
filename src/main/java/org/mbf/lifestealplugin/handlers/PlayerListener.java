package org.mbf.lifestealplugin.handlers;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.mbf.lifestealplugin.items.HeartCreator;
import org.mbf.lifestealplugin.items.HeartFragmentCreator;
import org.mbf.lifestealplugin.items.ItemKeys;

import java.net.http.WebSocket;

public class PlayerListener implements Listener {
    private final HeartCreator heartCreator = new HeartCreator();
    private final HeartFragmentCreator heartFragmentCreator = new HeartFragmentCreator();
    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getAction().isLeftClick() || e.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if(!e.getItem().getItemMeta().getPersistentDataContainer().has(ItemKeys.HEART_KEY, PersistentDataType.BOOLEAN)) {
            e.getPlayer().sendMessage("You don't have a heart in your hand");
            return;
        }
        if(e.getPlayer().getMaxHealth() >= 50) {
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
        if(e.getEntity().getKiller().getMaxHealth() >= 30)
            e.getDrops().add(heartFragmentCreator.create(1));
        else
            e.getDrops().add(heartCreator.createHeart(1));
        e.getPlayer().setMaxHealth(e.getPlayer().getMaxHealth() -2);
    }
}