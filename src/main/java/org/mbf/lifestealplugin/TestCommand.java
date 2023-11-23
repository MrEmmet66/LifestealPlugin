package org.mbf.lifestealplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mbf.lifestealplugin.items.HeartCreator;

public class TestCommand implements CommandExecutor {
    private final HeartCreator heartCreator = new HeartCreator();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Test sadasdda");
        Player player = (Player) sender;
        ItemStack heart = heartCreator.createHeart(1);
        player.getInventory().addItem(heart);
        return true;
    }
}
