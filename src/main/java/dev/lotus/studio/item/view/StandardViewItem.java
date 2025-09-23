package dev.lotus.studio.item.view;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class StandardViewItem implements ViewItem {
    private final String viewType;
    private final Material material;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public StandardViewItem(@NotNull Material material,
                            String displayName,
                            @NotNull List<String> lore,
                            @NotNull String viewType) {
        this.material = Objects.requireNonNull(material, "material");
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
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.setDisplayName(displayName);
                if (!lore.isEmpty()) meta.setLore(lore);
                NamespacedKey key = new NamespacedKey("frostandfallout", String.valueOf(material).toLowerCase());
                meta.getPersistentDataContainer().set(
                        key,
                        PersistentDataType.STRING,
                        displayName
                );
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
