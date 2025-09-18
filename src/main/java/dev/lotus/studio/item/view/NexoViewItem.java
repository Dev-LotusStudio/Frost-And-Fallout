package dev.lotus.studio.item.view;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import com.nexomc.nexo.items.ItemBuilder;
import com.nexomc.nexo.api.NexoItems;

import java.util.List;
import java.util.Objects;

public final class NexoViewItem implements ViewItem {
    private final String viewType;
    private final String displayName;
    private final List<String> lore;
    private final String id;

    private ItemStack template;

    public NexoViewItem(@NotNull String id, String displayName, @NotNull List<String> lore, @NotNull String viewType) {
        this.id = Objects.requireNonNull(id, "nexoId");
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
            ItemBuilder itemBuilder = NexoItems.itemFromId(id);
            if (itemBuilder == null) throw new IllegalArgumentException("Nexo item '" + id + "' не найден");
            ItemStack itemStack = itemBuilder.build().clone();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.setDisplayName(displayName);
                if (!lore.isEmpty()) meta.setLore(lore);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
         return template.clone();
    }
}
