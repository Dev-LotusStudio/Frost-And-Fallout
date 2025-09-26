package dev.lotus.studio.item.eat;

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

public final class NexoEatItem implements EatItem {
    private static final NamespacedKey KEY = new NamespacedKey("frostandfallout", "id");

    private final String nexoId;
    private final int radiationValue;
    private final int temperatureValue;
    private final String displayName;
    private final List<String> lore;

    private ItemStack template;

    public NexoEatItem(@NotNull String nexoId,
                       int radiationValue,
                       int temperatureValue,
                       String displayName,
                       @NotNull List<String> lore) {
        this.nexoId = Objects.requireNonNull(nexoId, "nexoId");
        this.radiationValue = radiationValue;
        this.temperatureValue = temperatureValue;
        this.displayName = displayName;
        this.lore = List.copyOf(lore);
    }

    @Override public int getRadiationValue() { return radiationValue; }
    @Override public int getTemperatureValue() { return temperatureValue; }
    @Override public @NotNull String getEatItem() { return nexoId; }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (template == null) {
            ItemBuilder itemBuilder = NexoItems.itemFromId(nexoId);
            if (itemBuilder == null) throw new IllegalArgumentException("Nexo item '" + nexoId + "' не найден");
            ItemStack itemStack = itemBuilder.build().clone();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null) meta.displayName(Component.text(displayName));
                if (!lore.isEmpty()) {
                    List<TextComponent> components = lore.stream().map(Component::text).toList();
                    meta.lore(components);
                }
                meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, nexoId);
                itemStack.setItemMeta(meta);
            }
            template = itemStack;
        }
        return template.clone();
    }
}
