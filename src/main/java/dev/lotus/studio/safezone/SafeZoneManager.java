package dev.lotus.studio.safezone;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lotus.studio.utils.SafeZoneUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev.lotus.studio.Main;
import dev.lotus.studio.database.savezone.SafeZoneDataBase;
import dev.lotus.studio.database.savezone.SafeZoneDataService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SafeZoneManager {

    private static final SafeZoneManager instance = new SafeZoneManager();
    private final Plugin plugin = Main.getInstance();

    private SafeZoneDataService safeZoneDataService;

    // Кеш із TTL (щоб вивантажувати непотрібні зони)
    private final Cache<Integer, SafeZone> zoneCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(500) // захист від переповнення
            .build();

    private SafeZonePreloaded preloader;

    public static SafeZoneManager getInstance() {
        return instance;
    }

    public void initialize(SafeZoneDataService safeZoneDataService) {
        this.safeZoneDataService = safeZoneDataService;
        this.preloader = new SafeZonePreloaded(this, safeZoneDataService, plugin);

        plugin.getLogger().info("SafeZoneManager ініціалізовано з lazy-загрузкою та кешем.");
    }

    // Додавання нової зони
    public void addSafeZone(SafeZone safeZone) {
        zoneCache.put(safeZone.getZoneID(), safeZone);
        safeZoneDataService.saveProtectZone(
                safeZone.getZoneName(),
                SafeZoneUtils.serializeZone(safeZone.getLocationPair())
        );
    }

    // Видалення
    public void removeSafeZone(int zoneID) {
        zoneCache.invalidate(zoneID);
        safeZoneDataService.removeProtectZone(zoneID);
    }

    // Отримання зони з кешу (або БД, якщо немає в кеші)
    public void getZoneById(int zoneID) {
        SafeZone cached = zoneCache.getIfPresent(zoneID);
        if (cached != null) {
            return;
        }

        SafeZoneDataBase dbData = safeZoneDataService.getZoneById(zoneID);
        if (dbData != null) {
            SafeZone zone = SafeZoneUtils.fromDatabase(dbData);
            zoneCache.put(zoneID, zone);

            plugin.getLogger().info("[SafeZoneManager] Loaded zone from DB and cached: "
                    + zone.getZoneName() + " (ID: " + zone.getZoneID() + ")");
        } else {
            plugin.getLogger().warning("[SafeZoneManager] Zone " + zoneID + " not found in DB.");
        }
    }


    // Перевірка чи гравець в якійсь зоні
    public boolean isPlayerInAnyZone(Player player) {
        Location playerLoc = player.getLocation();
        for (SafeZone zone : zoneCache.asMap().values()) {
            if (SafeZoneUtils.isLocationInZone(playerLoc,
                    zone.getLocationPair().getLeft(),
                    zone.getLocationPair().getRight())) {
                return true;
            }
        }
        return false;
    }

    public List<SafeZoneDataBase> getDataZones(){
        return safeZoneDataService.getAllSaveZones();
    }


    public SafeZonePreloaded getPreloaded() {
        return preloader;
    }
}
