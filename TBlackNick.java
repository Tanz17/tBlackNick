package ru.tanz.tblacknick;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class TBlackNick extends JavaPlugin {
    private static TBlackNick instance;
    public static ArrayList<UUID> uuid = new ArrayList<UUID>();
    public static TBlackNick plugin(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        saveDefaultConfig();
        getCommand("tblacknick").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
