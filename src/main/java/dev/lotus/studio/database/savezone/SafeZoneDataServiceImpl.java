package dev.lotus.studio.database.savezone;

import com.j256.ormlite.support.ConnectionSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SafeZoneDataServiceImpl implements SafeZoneDataService {

    private final SafeZoneDataRepository safeZoneDataRepository;

    public SafeZoneDataServiceImpl(ConnectionSource connectionSource) throws SQLException {
        this.safeZoneDataRepository = new SafeZoneDataRepository(connectionSource);
    }

    @Override
    public void saveProtectZone(String name, String location) {
        try {
            SafeZoneDataBase safeZoneDataBase = new SafeZoneDataBase(name, location);
            safeZoneDataRepository.saveZoneDataBase(safeZoneDataBase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeProtectZone(int id) {
        try {
            safeZoneDataRepository.removeProtectZone(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**  Реалізація getZoneById */
    @Override
    public SafeZoneDataBase getZoneById(int id) {
        try {
            return safeZoneDataRepository.getZoneById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SafeZoneDataBase> getAllSaveZones() {
        try {
            return safeZoneDataRepository.getAllZoneDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean isProtectZone(Location playerLocation) {
        try {
            List<Pair<Location, Location>> saveZoneDataZones = safeZoneDataRepository.getSafeZoneDataZones();
            for (Pair<Location, Location> zone : saveZoneDataZones) {
                if (isLocationInZone(playerLocation, zone.getLeft(), zone.getRight())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Pair<Location, Location>> getSaveZones() {
        try {
            return safeZoneDataRepository.getSafeZoneDataZones();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /** ✅ Новий метод */
    public List<Integer> getNearbyZoneIds(Location predicted) {
        List<Integer> result = new ArrayList<>();
        try {
            List<SafeZoneDataBase> zones = safeZoneDataRepository.getAllZoneDataBase();
            for (SafeZoneDataBase zone : zones) {
                Pair<Location, Location> locPair = parseZone(zone.getLocationValue());
                if (isNear(predicted, locPair.getLeft(), locPair.getRight(), 20)) { // радіус 20 блоків
                    result.add(zone.getSafeZoneId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isLocationInZone(Location location, Location loc1, Location loc2) {
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        int px = location.getBlockX();
        int py = location.getBlockY();
        int pz = location.getBlockZ();

        return (px >= x1 && px <= x2) &&
                (py >= y1 && py <= y2) &&
                (pz >= z1 && pz <= z2);
    }

    private boolean isNear(Location player, Location l1, Location l2, int radius) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX()) - radius;
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY()) - radius;
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ()) - radius;

        int x2 = Math.max(l1.getBlockX(), l2.getBlockX()) + radius;
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY()) + radius;
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ()) + radius;

        int px = player.getBlockX();
        int py = player.getBlockY();
        int pz = player.getBlockZ();

        return (px >= x1 && px <= x2) &&
                (py >= y1 && py <= y2) &&
                (pz >= z1 && pz <= z2);
    }

    private Pair<Location, Location> parseZone(String raw) {
        String[] coordinates = raw.split("\\|");
        String[] loc1 = coordinates[0].split(",");
        String[] loc2 = coordinates[1].split(",");

        Location location1 = new Location(null,
                Double.parseDouble(loc1[0].trim()),
                Double.parseDouble(loc1[1].trim()),
                Double.parseDouble(loc1[2].trim()));

        Location location2 = new Location(null,
                Double.parseDouble(loc2[0].trim()),
                Double.parseDouble(loc2[1].trim()),
                Double.parseDouble(loc2[2].trim()));

        return Pair.of(location1, location2);
    }
}
