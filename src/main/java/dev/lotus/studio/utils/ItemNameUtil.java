package dev.lotus.studio.utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemNameUtil {
    public static String getItemName(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return itemStack.getType().name(); // якщо нема кастомного імені
        }
        Component name = meta.displayName();
        return PlainTextComponentSerializer.plainText().serialize(name);
    }
}
