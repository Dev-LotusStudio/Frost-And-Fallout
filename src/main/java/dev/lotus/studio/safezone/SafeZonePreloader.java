package dev.lotus.studio.safezone;


import dev.lotus.studio.database.savezone.SafeZoneDataService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SafeZonePreloader {

    private final SafeZoneManager manager;
    private final SafeZoneDataService dataService;
    private final Plugin plugin;

    public SafeZonePreloader(SafeZoneManager manager, SafeZoneDataService dataService, Plugin plugin) {
        this.manager = manager;
        this.dataService = dataService;
        this.plugin = plugin;
        startTask();
    }

    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    preloadZonesForPlayer(player);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 40L); // кожні 2 сек
    }

    private void preloadZonesForPlayer(Player player) {
        Location current = player.getLocation();
        Location velocity = player.getVelocity().toLocation(player.getWorld());

        // Прогноз наступної позиції
        Location predicted = current.clone().add(velocity.multiply(3)); // умовно 3 сек вперед

        // Отримуємо зони поруч з передбаченою позицією
        List<Integer> nearbyZoneIds = dataService.getNearbyZoneIds(predicted);

        for (int zoneId : nearbyZoneIds) {
            manager.getZoneById(zoneId); //  Підвантажуємо в кеш
        }
    }
}

