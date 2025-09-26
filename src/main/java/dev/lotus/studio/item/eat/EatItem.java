package dev.lotus.studio.item.eat;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface EatItem {
    @NotNull String getEatItem();
    @NotNull ItemStack getItemStack();
    int getRadiationValue();
    int getTemperatureValue();
}
