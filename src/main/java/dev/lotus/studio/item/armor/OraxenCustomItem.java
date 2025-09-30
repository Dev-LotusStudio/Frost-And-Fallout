package dev.lotus.studio.item.armor;

import dev.lotus.studio.item.ItemKeys;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class OraxenCustomItem implements CustomItem {
    private final String id;
    private final double temperatureResistance;
    private final double radiationResistance;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public OraxenCustomItem(@NotNull String oraxenId,
                            double temperatureResistance,
                            double radiationResistance,
                            String displayName,
                            @NotNull List<String> lore) {
        this.id = Objects.requireNonNull(oraxenId, "oraxenId");
        this.temperatureResistance = temperatureResistance;
        this.radiationResistance = radiationResistance;
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
    }

    @Override public @NotNull String getCustomItem() { return id; }
    @Override public double getTemperatureResistance() { return temperatureResistance; }
    @Override public double getRadiationResistance() { return radiationResistance; }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemBuilder builder = OraxenItems.getItemById(id);
            if (builder == null) throw new IllegalArgumentException("Oraxen предмет с ID '" + id + "' не найден");
            ItemStack itemStack = builder.build();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream()
                            .map(Component::text)
                            .toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(ItemKeys.FF_ID, PersistentDataType.STRING, id);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
