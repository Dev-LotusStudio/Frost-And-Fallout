package dev.lotus.studio.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.item.eat.EatItem;
import dev.lotus.studio.playerdata.PlayerData;
import dev.lotus.studio.playerdata.PlayerManager;

public class EatEvent implements Listener {
    private final CustomItemManager customItemManager;

    public EatEvent(CustomItemManager customItemManager) {
        this.customItemManager = customItemManager;
    }

        @EventHandler
        public void onPlayerEat(PlayerItemConsumeEvent event) {
            ItemStack consumedItem = event.getItem();


            EatItem eatItem = customItemManager.getEatItemByItemStack(consumedItem);
            if (eatItem == null) return;


            PlayerData playerData = PlayerManager.getInstance().getPlayerData(event.getPlayer());


            double newRadiation =  playerData.getRadiationValue() - eatItem.getRadiationValue();
            double newTemperature = playerData.getTemperatureValue() + eatItem.getTemperatureValue();

            playerData.setRadiationValue(newRadiation);
            playerData.setTemperatureValue(newTemperature);


//            event.getPlayer().sendMessage("§aВы съели: " + consumedItem.getItemMeta().getDisplayName());
//            event.getPlayer().sendMessage("§eРадиация: " + newRadiation);
//            event.getPlayer().sendMessage("§bТемпература: " + newTemperature);
        }




}
