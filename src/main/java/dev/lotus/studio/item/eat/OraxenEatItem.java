package dev.lotus.studio.item.eat;

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

public final class OraxenEatItem implements EatItem {
    private static final NamespacedKey KEY = new NamespacedKey("frostandfallout", "id");

    private final String oraxenId;
    private final int radiationValue;
    private final int temperatureValue;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public OraxenEatItem(@NotNull String oraxenId,
                         int radiationValue,
                         int temperatureValue,
                         String displayName,
                         @NotNull List<String> lore) {
        this.oraxenId = Objects.requireNonNull(oraxenId, "oraxenId");
        this.radiationValue = radiationValue;
        this.temperatureValue = temperatureValue;
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
    }

    @Override public int getRadiationValue() { return radiationValue; }
    @Override public int getTemperatureValue() { return temperatureValue; }
    @Override public @NotNull String getEatItem() { return oraxenId; }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemBuilder itemBuilder = OraxenItems.getItemById(oraxenId);
            if (itemBuilder == null) {
                throw new IllegalArgumentException("Oraxen предмет с ID '" + oraxenId + "' не найден.");
            }
            ItemStack itemStack = itemBuilder.build();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream().map(Component::text).toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, oraxenId);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
