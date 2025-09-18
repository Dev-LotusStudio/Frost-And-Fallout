package dev.lotus.studio.utils;

import org.bukkit.Bukkit;

public final class ResourcePackUtils {

    private ResourcePackUtils() {
        // Запрещаем создание экземпляра
    }

    public static boolean isOraxenEnable() {
        return Bukkit.getPluginManager().getPlugin("Oraxen") != null;
    }

    public static boolean isNexoEnable() {
        return Bukkit.getPluginManager().getPlugin("Nexo") != null;
    }
}

