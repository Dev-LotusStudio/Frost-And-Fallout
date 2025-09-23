package dev.lotus.studio.database.savezone;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SafeZoneDataRepository {

    private final Dao<SafeZoneDataBase, Integer> safeZoneDao;

    public SafeZoneDataRepository(ConnectionSource connectionSource) throws SQLException {
        this.safeZoneDao = DaoManager.createDao(connectionSource, SafeZoneDataBase.class);
    }

    public void removeProtectZone(int id) throws SQLException {
        safeZoneDao.deleteById(id);
    }

    public void saveZoneDataBase(SafeZoneDataBase safeZoneDataBase) throws SQLException {
        safeZoneDao.createOrUpdate(safeZoneDataBase);
    }

    public List<SafeZoneDataBase> getAllZoneDataBase() throws SQLException {
        return safeZoneDao.queryForAll();
    }

    /** Новий метод */
    public SafeZoneDataBase getZoneById(int id) throws SQLException {
        return safeZoneDao.queryForId(id);
    }

    /**
     * Отримати всі зони у вигляді пар локацій
     */
    public List<Pair<Location, Location>> getSafeZoneDataZones() throws SQLException {
        List<Pair<Location, Location>> zones = new ArrayList<>();
        List<SafeZoneDataBase> structures = getAllZoneDataBase();

        for (SafeZoneDataBase structure : structures) {
            zones.add(parseZone(structure.getLocationValue()));
        }
        return zones;
    }

    private Pair<Location, Location> parseZone(String raw) {
        String[] coordinates = raw.split("\\|");
        String[] loc1 = coordinates[0].split(",");
        String[] loc2 = coordinates[1].split(",");

        Location location1 = new Location(
                Bukkit.getWorld("world"),
                Double.parseDouble(loc1[0].trim()),
                Double.parseDouble(loc1[1].trim()),
                Double.parseDouble(loc1[2].trim())
        );

        Location location2 = new Location(
                Bukkit.getWorld("world"),
                Double.parseDouble(loc2[0].trim()),
                Double.parseDouble(loc2[1].trim()),
                Double.parseDouble(loc2[2].trim())
        );

        return Pair.of(location1, location2);
    }
}
