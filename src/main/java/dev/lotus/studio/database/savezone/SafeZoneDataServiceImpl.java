package dev.lotus.studio.database.savezone;

import com.j256.ormlite.support.ConnectionSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.sql.SQLException;
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
            safeZoneDataRepository.saveStructureData(safeZoneDataBase);
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

    @Override
    public List<SafeZoneDataBase> getAllSaveZones() {
        try {
            return safeZoneDataRepository.getAllStructuresData();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean isProtectZone(Location playerLocation) {
        try {
            List<Pair<Location, Location>> saveZoneDataZones = safeZoneDataRepository.getSaveZoneDataZones();
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
            return safeZoneDataRepository.getSaveZoneDataZones();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
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
}
