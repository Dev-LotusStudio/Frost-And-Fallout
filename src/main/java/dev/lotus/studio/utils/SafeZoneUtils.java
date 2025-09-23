package dev.lotus.studio.utils;


import dev.lotus.studio.safezone.SafeZone;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import dev.lotus.studio.database.savezone.SafeZoneDataBase;

public class SafeZoneUtils {

    public static boolean isLocationInZone(Location playerLoc, Location posLoc1, Location posLoc2) {
        int x1 = Math.min(posLoc1.getBlockX(), posLoc2.getBlockX());
        int y1 = Math.min(posLoc1.getBlockY(), posLoc2.getBlockY());
        int z1 = Math.min(posLoc1.getBlockZ(), posLoc2.getBlockZ());

        int x2 = Math.max(posLoc1.getBlockX(), posLoc2.getBlockX());
        int y2 = Math.max(posLoc1.getBlockY(), posLoc2.getBlockY());
        int z2 = Math.max(posLoc1.getBlockZ(), posLoc2.getBlockZ());

        int px = playerLoc.getBlockX();
        int py = playerLoc.getBlockY();
        int pz = playerLoc.getBlockZ();

        return (px >= x1 && px <= x2) &&
                (py >= y1 && py <= y2) &&
                (pz >= z1 && pz <= z2);
    }

    public static String serializeZone(Pair<Location, Location> pair) {
        return formatLoc(pair.getLeft()) + "|" + formatLoc(pair.getRight());
    }

    private static String formatLoc(Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public static SafeZone fromDatabase(SafeZoneDataBase dbData) {
        Pair<Location, Location> coords = parseZone(dbData.getLocationValue());
        return new SafeZone(dbData.getSafeZoneId(), dbData.getSafeZoneName(), coords);
    }

    private static Pair<Location, Location> parseZone(String value) {
        String[] parts = value.split("\\|");
        String[] loc1 = parts[0].split(",");
        String[] loc2 = parts[1].split(",");

        Location l1 = new Location(null,
                Integer.parseInt(loc1[0]),
                Integer.parseInt(loc1[1]),
                Integer.parseInt(loc1[2]));

        Location l2 = new Location(null,
                Integer.parseInt(loc2[0]),
                Integer.parseInt(loc2[1]),
                Integer.parseInt(loc2[2]));

        return Pair.of(l1, l2);
    }
}
