package dev.lotus.studio.utils;

import org.bukkit.Location;

public class MapperUtils {


    public static String formatLocation(Location location) {
        return location.getBlockX() + "," +
                location.getBlockY() + "," +
                location.getBlockZ();
    }
}
