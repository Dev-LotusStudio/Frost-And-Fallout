package dev.lotus.studio.utils;

import org.bukkit.Bukkit;

public class OraxenUtils {

    public static boolean isOraxenEnable(){
        return Bukkit.getPluginManager().getPlugin("Oraxen") != null;
    }
}
