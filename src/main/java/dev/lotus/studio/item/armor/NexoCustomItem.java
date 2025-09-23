package dev.lotus.studio.item.armor;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class NexoCustomItem implements CustomItem {
    private static final NamespacedKey KEY = new NamespacedKey("frostandfallout", "id");

    private final String id;
    private final double temperatureResistance;
    private final double radiationResistance;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public NexoCustomItem(@NotNull String nexoId,
                          double temperatureResistance,
                          double radiationResistance,
                          String displayName,
                          @NotNull List<String> lore) {
        this.id = Objects.requireNonNull(nexoId, "nexoId");
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
            ItemBuilder builder = NexoItems.itemFromId(id);
            if (builder == null) {
                throw new IllegalArgumentException("Nexo item '" + id + "' не найден");
            }
            ItemStack itemStack = builder.build().clone();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream()
                            .map(Component::text)
                            .toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, id);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
