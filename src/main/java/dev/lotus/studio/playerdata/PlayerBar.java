package dev.lotus.studio.playerdata;

import dev.lotus.studio.Main;
import dev.lotus.studio.handlers.RadiationHandler;
import dev.lotus.studio.handlers.TemperatureHandler;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.item.view.ViewItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

public class PlayerBar {

    private final Main plugin;
    private final CustomItemManager itemManager;
    private BukkitTask task;

    private static final Component SEP = Component.text("  ");
    private static final String ICON_RADIATION = "☢";
    private static final String ICON_TEMPERATURE = "\uD83C\uDF21";

    public PlayerBar(Main plugin, CustomItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        startActionBarTask();
    }

    public void stop() {
        if (task != null) task.cancel();
    }

    private void startActionBarTask() {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                try {
                    updateActionBar(player);
                } catch (Throwable t) {
                    plugin.getLogger().warning("ActionBar error for " + player.getName() + ": " + t.getMessage());
                }
            }
        }, 0L, 20L);
    }

    private void updateActionBar(Player player) {
        var mainHand = player.getInventory().getItemInMainHand();
        var offHand  = player.getInventory().getItemInOffHand();

        ViewItem viewItem = itemManager.getViewItemByItemStack(mainHand);
        if (viewItem == null) viewItem = itemManager.getViewItemByItemStack(offHand);

        var playerData = PlayerManager.getInstance().getPlayerData(player);
        double radiation   = playerData.getRadiationValue();
        double temperature = playerData.getTemperatureValue();

        Component radiationBar = Component.empty();
        Component temperatureBar = Component.empty();
        boolean hasRadiationBar = false;
        boolean hasTemperatureBar = false;

        if (viewItem != null) {
            String vt = viewItem.getViewType();
            if ("RADIATION".equalsIgnoreCase(vt)) {
                radiationBar = RadiationHandler.getInstance().createProgressBar(radiation, ICON_RADIATION, true);
                hasRadiationBar = true;
            } else if ("TEMPERATURE".equalsIgnoreCase(vt)) {
                temperatureBar = TemperatureHandler.getInstance().createProgressBar(temperature, ICON_TEMPERATURE, true);
                hasTemperatureBar = true;
            }
        }

        if (!hasRadiationBar && !hasTemperatureBar) {
            temperatureBar = TemperatureHandler.getInstance().createProgressBar(temperature, ICON_TEMPERATURE, false);
        }

        Component actionBarMessage = radiationBar.append(SEP).append(temperatureBar);
        player.sendActionBar(actionBarMessage);
    }
}
