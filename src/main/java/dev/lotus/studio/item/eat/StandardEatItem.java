package dev.lotus.studio.item.eat;

import dev.lotus.studio.item.ItemKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class StandardEatItem implements EatItem {
    private final Material material;
    private final String materialName;
    private final String displayName;
    private final List<String> lore;
    private final int radiationValue;
    private final int temperatureValue;

    private ItemStack template;

    public StandardEatItem(@NotNull Material material,
                           String displayName,
                           @NotNull List<String> lore,
                           int radiationValue,
                           int temperatureValue) {
        this.material = Objects.requireNonNull(material, "material");
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
        this.radiationValue = radiationValue;
        this.temperatureValue = temperatureValue;
        this.materialName = material.name();
    }

    @Override public int getRadiationValue() { return radiationValue; }
    @Override public int getTemperatureValue() { return temperatureValue; }

    @Override public @NotNull String getEatItem() { return materialName; }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream().map(Component::text).toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(ItemKeys.FF_ID, PersistentDataType.STRING, materialName);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
