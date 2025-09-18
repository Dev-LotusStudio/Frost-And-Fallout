package dev.lotus.studio.item.view;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class OraxenViewItem implements ViewItem {
    private final String viewType;
    private final String displayName;
    private final List<String> lore;
    private final String id;

    private ItemStack template;

    public OraxenViewItem(@NotNull String id, String displayName, @NotNull List<String> lore, @NotNull String viewType) {
        this.id = Objects.requireNonNull(id, "oraxenId");
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
        this.viewType = Objects.requireNonNull(viewType, "viewType");
    }

    @Override
    public @NotNull String getViewType() {
        return viewType;
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemBuilder itemBuilder = OraxenItems.getItemById(id);
            if (itemBuilder == null) { throw new IllegalArgumentException("Oraxen предмет с ID '" + id + "' не найден"); }
            ItemStack itemStack = itemBuilder.build();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.setDisplayName(displayName);
                if (!lore.isEmpty())     meta.setLore(lore);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
