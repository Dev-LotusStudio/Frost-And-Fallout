package dev.lotus.studio.database.savezone;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.util.List;

public interface SaveZoneDataService {

    void saveProtectZone(String name, String location);

    void removeProtectZone(int id);

    List<SafeZoneDataBase> getAllSaveZones();

    boolean isProtectZone(Location playerLocation);

    List<Pair<Location, Location>> getSaveZones();

}
