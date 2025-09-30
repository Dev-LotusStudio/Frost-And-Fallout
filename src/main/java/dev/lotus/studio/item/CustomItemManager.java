package dev.lotus.studio.item;

import dev.lotus.studio.item.armor.CustomItemFactory;
import dev.lotus.studio.item.eat.*;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import dev.lotus.studio.Main;
import dev.lotus.studio.item.armor.CustomItem;
import dev.lotus.studio.item.view.ViewItem;
import dev.lotus.studio.item.view.ViewItemFactory;
import dev.lotus.studio.utils.ResourcePackUtils;
import static org.bukkit.Bukkit.getLogger;

import java.io.File;
import java.util.*;

public class CustomItemManager {
    private final Map<String, CustomItem> items = new HashMap<>();
    private final Map<String, ViewItem> viewItems = new HashMap<>();
    private final Map<String, EatItem> eatItems = new HashMap<>();

    private final boolean isOraxenEnabled;
    private final boolean isNexoEnabled;

    public CustomItemManager() {
        this.isOraxenEnabled = ResourcePackUtils.isOraxenEnable();
        if (!isOraxenEnabled) {
            Main.getInstance().getLogger().warning("Oraxen не найден. Предметы из Oraxen будут пропущены.");
        }
        this.isNexoEnabled = ResourcePackUtils.isNexoEnable();
        if (!isNexoEnabled) {
            Main.getInstance().getLogger().warning("Nexo не найден. Предметы из Nexo будут пропущены.");
        }
    }

    public void loadItems() {
        File file = new File(Main.getInstance().getDataFolder(), "items.yml");
        if (!file.exists()) {
            Main.getInstance().saveResource("items.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Clear existing items before loading new ones
        items.clear();
        viewItems.clear();
        eatItems.clear();

        loadStandardItems(config);
        loadViewItems(config);
        loadEatItems(config);
    }

    private void loadStandardItems(FileConfiguration config) {
        if (!config.contains("items")) return;
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) return;

        CustomItemFactory factory = new CustomItemFactory();

        for (String key : itemsSection.getKeys(false)) {
            String path = "items." + key;
            try {
                ConfigurationSection itemSection = config.getConfigurationSection(path);
                if (itemSection == null) {
                    throw new IllegalArgumentException("Пустая секция для " + path);
                }

                CustomItem item = factory.fromSection(itemSection);
                items.put(key, item);
                Main.getInstance().getLogger().info("Загружен item: " + key + " (" + item.getCustomItem() + ")");

            } catch (IllegalArgumentException | IllegalStateException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке item '" + key + "': " + e.getMessage());
            } catch (Throwable t) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке item '" + key + "': " + t.getMessage());
                t.printStackTrace();
            }
        }
    }

    private void loadViewItems(FileConfiguration config) {
        if (!config.contains("view_item")) return;
        var section = config.getConfigurationSection("view_item");
        if (section == null) return;

        ViewItemFactory factory = new ViewItemFactory();

        for (String key : section.getKeys(false)) {
            String path = "view_item." + key;

            try {
                var itemSection = config.getConfigurationSection(path);
                if (itemSection == null) {
                    throw new IllegalArgumentException("Пустая секция для " + path);
                }

                ViewItem viewType = factory.fromSection(itemSection);
                viewItems.put(key, viewType);
                Main.getInstance().getLogger().info("Загружен view_item: " + key + " (" + viewType.getViewType() + ")");

            } catch (IllegalArgumentException | IllegalStateException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке view_item '" + key + "': " + e.getMessage());
            } catch (Throwable t) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке view_item '" + key + "': " + t.getMessage());
                t.printStackTrace();
            }
        }
    }

    private void loadEatItems(FileConfiguration config) {
        if (!config.contains("eat_item")) return;
        var section = config.getConfigurationSection("eat_item");
        if (section == null) return;

        EatItemFactory factory = new EatItemFactory();

        for (String key : section.getKeys(false)) {
            String path = "eat_item." + key;
            try {
                var itemSection = config.getConfigurationSection(path);
                if (itemSection == null) {
                    throw new IllegalArgumentException("Пустая секция для " + path);
                }

                String providerStr = itemSection.getString("provider", itemSection.getString("type", "standard"));
                String providerDbg = providerStr.toLowerCase(Locale.ROOT);
                if ("oraxen".equals(providerDbg) && !isOraxenEnabled) {
                    Main.getInstance().getLogger().warning("Oraxen eat_item '" + key + "' пропущен: Oraxen не активен");
                    continue;
                }
                if ("nexo".equals(providerDbg) && !isNexoEnabled) {
                    Main.getInstance().getLogger().warning("Nexo eat_item '" + key + "' пропущен: Nexo не активен");
                    continue;
                }

                EatItem eatItem = factory.fromSection(itemSection);
                eatItems.put(key, eatItem);
                Main.getInstance().getLogger().info("Загружен eat_item: " + key + " (src=" + eatItem.getEatItem() + ")");

            } catch (IllegalArgumentException | IllegalStateException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке eat_item '" + key + "': " + e.getMessage());
            } catch (Throwable t) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке eat_item '" + key + "': " + t.getMessage());
                t.printStackTrace();
            }
        }
    }

    public EatItem getEatItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;
        for (EatItem eatItem : eatItems.values()) {
            try {
                if (eatItem.getItemStack().isSimilar(itemStack)) return eatItem;
            } catch (Exception ignored) {}
        }
        return null;
    }

    public CustomItem getCustomItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;

        var meta = itemStack.getItemMeta();

        String ffId = meta.getPersistentDataContainer().get(ItemKeys.FF_ID, PersistentDataType.STRING);
        if (ffId != null) {
            for (CustomItem item : items.values()) {
                if (ffId.equals(item.getCustomItem())) {
                    return item;
                }
            }
            getLogger().warning("ffId есть, но не нашли совпадение в items\n" + itemStack);
        }

        String oraxenId = meta.getPersistentDataContainer().get(ItemKeys.ORAXEN_ID, PersistentDataType.STRING);
        if (oraxenId != null) {
            for (CustomItem item : items.values()) {
                if (oraxenId.equals(item.getCustomItem())) {
                    return item;
                }
                ItemStack itemItemStack = item.getItemStack();
                if (itemItemStack.hasItemMeta()) {
                    String other = itemItemStack.getItemMeta().getPersistentDataContainer().get(ItemKeys.ORAXEN_ID, PersistentDataType.STRING);
                    if (oraxenId.equals(other)) return item;
                }
            }
            getLogger().warning("oraxenId есть, но не нашли совпадение в items\n" + itemStack);
        }

        if (!meta.hasItemFlag(ItemFlag.HIDE_ARMOR_TRIM)) {
            return null;
        }
        for (CustomItem item : items.values()) {
            ItemStack custom = item.getItemStack();
            if (!custom.hasItemMeta()) continue;
            var itemMeta = custom.getItemMeta();
            if (itemMeta.displayName() != null && Objects.equals(itemMeta.displayName(), meta.displayName())
                    && itemMeta.lore() != null && Objects.equals(itemMeta.lore(), meta.lore())) {
                return item;
            }
        }
        return null;
    }

    public ViewItem getViewItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;
        for (ViewItem viewItem : viewItems.values()) {
            try {
                if (viewItem.getItemStack().isSimilar(itemStack)) {
                    return viewItem;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    public Map<String, CustomItem> getItems() { return items; }
    public Map<String, ViewItem> getViewItems() { return viewItems; }
    public Map<String, EatItem> getEatItems() { return eatItems; }

    public EatItem getEatItem(String key) { return eatItems.get(key); }
    public CustomItem getItem(String key) { return items.get(key); }
    public ViewItem getViewItem(String key) { return viewItems.get(key); }

    public void reloadItemConfig(){
        try {
            Main.getInstance().getLogger().info("Items.yml reloading!");
            loadItems();
            Main.getInstance().getLogger().info("Items.yml reloaded");
        } catch (Exception e){
            Main.getInstance().getLogger().warning("Items.yml reload failed: " + e.getMessage());
        }
    }
}
