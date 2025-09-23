package dev.lotus.studio.safezone;

import dev.lotus.studio.database.savezone.SafeZoneDataService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class SafeZonePreloaded {

    private final SafeZoneManager manager;
    private final SafeZoneDataService dataService;
    private final Plugin plugin;

    private BukkitTask preloadTask; // зберігаємо таску

    public SafeZonePreloaded(SafeZoneManager manager, SafeZoneDataService dataService, Plugin plugin) {
        this.manager = manager;
        this.dataService = dataService;
        this.plugin = plugin;
        startTask();
    }

    private void startTask() {
        // Запускаємо і зберігаємо таску
        this.preloadTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    preloadZonesForPlayer(player);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 40L); // кожні 2 сек
    }

    public void stopTask() {
        if (preloadTask != null && !preloadTask.isCancelled()) {
            preloadTask.cancel();
            plugin.getLogger().info("[SafeZonePreloaded] Preload task stopped.");
        }
    }

    private void preloadZonesForPlayer(Player player) {
        Location current = player.getLocation();
        Location velocity = player.getVelocity().toLocation(player.getWorld());

        Location predicted = current.clone().add(velocity.multiply(3));
        List<Integer> nearbyZoneIds = dataService.getNearbyZoneIds(predicted);
        for (int zoneId : nearbyZoneIds) {
            manager.getZoneById(zoneId);
        }
    }
}
