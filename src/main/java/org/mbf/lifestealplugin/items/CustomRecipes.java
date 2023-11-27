package org.mbf.lifestealplugin.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mbf.lifestealplugin.LifestealPlugin;

import java.util.Arrays;

public class CustomRecipes {
    public static void registerRecipes() {
        ItemStack fragment = new HeartFragmentCreator().create(1);
        ShapedRecipe fragmentRecipe = new ShapedRecipe(new NamespacedKey(LifestealPlugin.getPlugin(), "HeartFragmentRecipe"), fragment);
        fragmentRecipe.shape("GDG","DTD", "GDG");
        fragmentRecipe.setIngredient('G', Material.GOLD_BLOCK);
        fragmentRecipe.setIngredient('D', Material.DIAMOND);
        fragmentRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        Bukkit.addRecipe(fragmentRecipe);

        ShapedRecipe heartRecipe = new ShapedRecipe(new NamespacedKey(LifestealPlugin.getPlugin(), "HeartRecipe"), new HeartCreator().createHeart(1));
        heartRecipe.shape("IFI", "FEF", "IFI");
        heartRecipe.setIngredient('I', Material.IRON_BLOCK);
        heartRecipe.setIngredient('F', new RecipeChoice.ExactChoice(fragment));
        heartRecipe.setIngredient('E', Material.ELYTRA);
        Bukkit.addRecipe(heartRecipe);

        ShapedRecipe reviveBeaconRecipe = new ShapedRecipe(new NamespacedKey(LifestealPlugin.getPlugin(), "ReviveBeaconRecipe"), new ReviveBeaconCreator().create(1));
        reviveBeaconRecipe.shape("HHH", "HTH", "HHH");
        reviveBeaconRecipe.setIngredient('H', new RecipeChoice.ExactChoice(new HeartCreator().createHeart(1)));
        reviveBeaconRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        Bukkit.addRecipe(reviveBeaconRecipe);

    }
}
