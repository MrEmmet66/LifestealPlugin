package org.mbf.lifestealplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.mbf.lifestealplugin.handlers.PlayerListener;
import org.mbf.lifestealplugin.items.CustomRecipes;

public final class LifestealPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CustomRecipes.registerRecipes();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LifestealPlugin getPlugin() {
        return getPlugin(LifestealPlugin.class);
    }
}
