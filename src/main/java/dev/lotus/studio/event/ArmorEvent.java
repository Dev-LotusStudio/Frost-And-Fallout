package dev.lotus.studio.event;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import dev.lotus.studio.item.armor.CustomItem;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.playerdata.PlayerManager;

public class ArmorEvent implements Listener {
    private final CustomItemManager customItemManager;

    public ArmorEvent(CustomItemManager customItemManager) {
        this.customItemManager = customItemManager;
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        onArmorChanged(player);
    }

    public void onArmorChanged(Player player) {
        double temperatureResistance = 0;
        double radiationResistance = 0;

        ItemStack[] armorContents = player.getInventory().getArmorContents(); // Получаем предметы брони

        for (ItemStack armorPiece : armorContents) {
            if (armorPiece == null) continue;

            // Проверяем, зарегистрирован ли предмет в CustomItemManager
            CustomItem customItem = customItemManager.getCustomItemByItemStack(armorPiece);
            System.out.println("Найдено " + customItem);
            if (customItem != null) {
                temperatureResistance += customItem.getTemperatureResistance();
                radiationResistance += customItem.getRadiationResistance();
            }
        }

        // Устанавливаем сопротивление для игрока
        PlayerManager.getInstance().getPlayerData(player).setTemperatureResistance(temperatureResistance);
        PlayerManager.getInstance().getPlayerData(player).setRadiationResistance(radiationResistance);
        System.out.println("Сопротивление температуре: " + temperatureResistance);
        System.out.println("Сопротивление радиции: " + radiationResistance);
    }
}
