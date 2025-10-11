package dev.lotus.studio.item.armor;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomItem {
    @NotNull String getCustomItem();
    @NotNull ItemStack getItemStack();
    double getTemperatureResistance();
    double getRadiationResistance();
}
