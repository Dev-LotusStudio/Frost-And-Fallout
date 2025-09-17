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

public class SaveZoneDataRepository {

    private final Dao<SafeZoneDataBase, Integer> safeZoneDao;

    public SaveZoneDataRepository(ConnectionSource connectionSource) throws SQLException {
        this.safeZoneDao = DaoManager.createDao(connectionSource, SafeZoneDataBase.class);
    }

    /**
     * Видалити зону по id
     */
    public void removeProtectZone(int id) throws SQLException {
        safeZoneDao.deleteById(id);
    }

    /**
     * Зберегти/оновити структуру
     */
    public void saveStructureData(SafeZoneDataBase safeZoneDataBase) throws SQLException {
        safeZoneDao.createOrUpdate(safeZoneDataBase);
    }

    /**
     * Отримати всі структури
     */
    public List<SafeZoneDataBase> getAllStructuresData() throws SQLException {
        return safeZoneDao.queryForAll();
    }

    /**
     * Повертає список зон у форматі пар локацій (лок1, лок2)
     */
    public List<Pair<Location, Location>> getSaveZoneDataZones() throws SQLException {
        List<Pair<Location, Location>> zones = new ArrayList<>();
        List<SafeZoneDataBase> structures = getAllStructuresData();

        for (SafeZoneDataBase structure : structures) {
            String[] coordinates = structure.getLocationValue().split("\\|");
            if (coordinates.length == 2) {
                String[] loc1 = coordinates[0].split(",");
                String[] loc2 = coordinates[1].split(",");

                Location location1 = new Location(
                        Bukkit.getWorld("world"), // або передавати динамічно
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

                zones.add(Pair.of(location1, location2));
            }
        }

        return zones;
    }
}
