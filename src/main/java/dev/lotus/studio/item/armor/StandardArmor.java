package dev.lotus.studio.item.armor;

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

public final class StandardArmor implements CustomItem {
    private final String id;
    private final double temperatureResistance;
    private final double radiationResistance;
    private final Material material;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public StandardArmor(@NotNull Material material,
                         String displayName,
                         @NotNull List<String> lore,
                         double temperatureResistance,
                         double radiationResistance) {
        this.material = Objects.requireNonNull(material, "material");
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
        this.temperatureResistance = temperatureResistance;
        this.radiationResistance = radiationResistance;
        this.id = material.name();
    }

    @Override public @NotNull String getCustomItem() { return id; }
    @Override public double getTemperatureResistance() { return temperatureResistance; }
    @Override public double getRadiationResistance() { return radiationResistance; }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemStack is = new ItemStack(material);
            ItemMeta meta = is.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream()
                            .map(Component::text)
                            .toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(ItemKeys.FF_ID, PersistentDataType.STRING, id);
                is.setItemMeta(meta);
            }
            template = is;
        }
        return template.clone();
    }
}
