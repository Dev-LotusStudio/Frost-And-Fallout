package dev.lotus.studio.playerdata;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import dev.lotus.studio.Main;
import dev.lotus.studio.handlers.RadiationHandler;
import dev.lotus.studio.handlers.TemperatureHandler;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.item.view.ViewType;

public class PlayerBar {

    private final Main plugin;
    private final CustomItemManager itemManager;

    public PlayerBar(Main plugin, CustomItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        startActionBarTask();
    }

    private void startActionBarTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateActionBar(player);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Выполнять каждую секунду
    }

    private void updateActionBar(Player player) {
        ViewType customItem;
        customItem = itemManager.getViewItemByItemStack(player.getInventory().getItemInMainHand());
        if (customItem == null) {
            customItem = itemManager.getViewItemByItemStack(player.getInventory().getItemInOffHand());
        }

        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        double radiation = playerData.getRadiationValue();
        double temperature = playerData.getTemperatureValue();

        Component radiationBar = Component.text("");
        Component temperatureBar = Component.text("");

        if (customItem != null) {

            if ("RADIATION".equalsIgnoreCase(customItem.getViewType())) {
                radiationBar = RadiationHandler.getInstance().createProgressBar(radiation,"☢", true);
            }

            else if ("TEMPERATURE".equalsIgnoreCase(customItem.getViewType())) {
                temperatureBar = TemperatureHandler.getInstance().createProgressBar(temperature, "\uD83C\uDF21",true);
            }
        }

        if (radiationBar.equals(Component.empty()) && temperatureBar.equals(Component.empty())) {
            temperatureBar = TemperatureHandler.getInstance().createProgressBar(temperature, "\uD83C\uDF21", false);
        }

        Component actionBarMessage = radiationBar.append(Component.text("  ")).append(temperatureBar);

        player.sendActionBar(actionBarMessage);
    }



}
