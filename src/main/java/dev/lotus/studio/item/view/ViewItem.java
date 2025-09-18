package dev.lotus.studio.item.view;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ViewItem {
    @NotNull String getViewType();
    @NotNull ItemStack getItemStack();
}
