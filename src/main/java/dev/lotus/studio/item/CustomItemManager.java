package dev.lotus.studio.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import dev.lotus.studio.Main;
import dev.lotus.studio.item.armor.CustomItem;
import dev.lotus.studio.item.armor.OraxenCustomItem;
import dev.lotus.studio.item.armor.StandardArmor;
import dev.lotus.studio.item.eat.EatItem;
import dev.lotus.studio.item.eat.OraxenEatItem;
import dev.lotus.studio.item.eat.StandardEatItem;
import dev.lotus.studio.item.view.ViewType;
import dev.lotus.studio.item.view.OraxenViewView;
import dev.lotus.studio.item.view.StandardViewItem;
import dev.lotus.studio.utils.OraxenUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemManager {
    private final Map<String, CustomItem> items = new HashMap<>();
    private final Map<String, ViewType> viewItems = new HashMap<>();

    private final Map<String, EatItem> eatItems = new HashMap<>();

    private boolean isOraxenEnabled;
    public CustomItemManager() {
        this.isOraxenEnabled = Bukkit.getPluginManager().getPlugin("Oraxen") != null;

        if (!isOraxenEnabled) {
            Main.getInstance().getLogger().warning("Oraxen не найден. Предметы из Oraxen будут пропущены.");
        }
    }

    public void loadItems() {
        File file = new File(Main.getInstance().getDataFolder(), "items.yml");
        if (!file.exists()) {
            Main.getInstance().saveResource("items.yml", false);
        }
        if (!OraxenUtils.isOraxenEnable()){
            Main.getInstance().getLogger().warning("Oraxen не найден. Предметы из Oraxen будут пропущены.");
        }
        this.isOraxenEnabled = Bukkit.getPluginManager().getPlugin("Oraxen") != null;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Загрузка обычных предметов
        loadStandardItems(config);

        // Загрузка view_item
        loadViewItems(config);

        // Загрузка eat_item
        loadEatItems(config);
    }



    private void loadStandardItems(FileConfiguration config) {
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            String path = "items." + key;

            try {
                String type = config.getString(path + ".type");
                if (type == null) {
                    throw new IllegalArgumentException("Не указан тип предмета для ключа '" + key + "'");
                }

                if ("standard".equalsIgnoreCase(type)) {
                    Material material = Material.valueOf(config.getString(path + ".material"));
                    String displayName = config.getString(path + ".displayName");
                    List<String> lore = config.getStringList(path + ".lore");
                    double temperatureResistance = config.getDouble(path + ".temperatureResistance");
                    double radiationResistance = config.getDouble(path + ".radiationResistance");

                    items.put(key, new StandardArmor(material, displayName, lore, temperatureResistance, radiationResistance));
                    Main.getInstance().getLogger().info("Успешно загружен стандартный предмет: " + key);
                } else if ("oraxen".equalsIgnoreCase(type)) {
                    if (!isOraxenEnabled) {
                        Main.getInstance().getLogger().warning("Oraxen-предмет '" + key + "' пропущен, так как Oraxen не активен.");
                        continue;
                    }

                    String oraxenId = config.getString(path + ".oraxenId");
                    if (oraxenId == null || oraxenId.isEmpty()) {
                        throw new IllegalArgumentException("Отсутствует 'oraxenId' для предмета с ключом '" + key + "'");
                    }
                    double temperatureResistance = config.getDouble(path + ".temperatureResistance");
                    double radiationResistance = config.getDouble(path + ".radiationResistance");

                    items.put(key, new OraxenCustomItem(oraxenId, temperatureResistance, radiationResistance));
                    Main.getInstance().getLogger().info("Успешно загружен Oraxen-предмет: " + key);
                } else {
                    throw new IllegalArgumentException("Неизвестный тип предмета '" + type + "' для ключа '" + key + "'");
                }
            } catch (IllegalArgumentException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке предмета с ключом '" + key + "': " + e.getMessage());
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке предмета с ключом '" + key + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadViewItems(FileConfiguration config) {
        for (String key : config.getConfigurationSection("view_item").getKeys(false)) {
            String path = "view_item." + key;

            try {
                String type = config.getString(path + ".type");
                if (type == null) {
                    throw new IllegalArgumentException("Не указан тип предмета для view_item с ключом '" + key + "'");
                }

                if ("standard".equalsIgnoreCase(type)) {
                    Material material = Material.valueOf(config.getString(path + ".material"));
                    String displayName = config.getString(path + ".displayName");
                    List<String> lore = config.getStringList(path + ".lore");
                    String viewType = config.getString(path + ".view_type");

                    viewItems.put(key, new StandardViewItem(material, displayName, lore, viewType));
                    Main.getInstance().getLogger().info("Успешно загружен стандартный view_item: " + key);
                } else if ("oraxen".equalsIgnoreCase(type)) {
                    if (!isOraxenEnabled) {
                        Main.getInstance().getLogger().warning("Oraxen view_item '" + key + "' пропущен, так как Oraxen не активен.");
                        continue;
                    }

                    String oraxenId = config.getString(path + ".oraxenId");
                    if (oraxenId == null || oraxenId.isEmpty()) {
                        throw new IllegalArgumentException("Отсутствует 'oraxenId' для view_item с ключом '" + key + "'");
                    }
                    String viewType = config.getString(path + ".view_type");

                    viewItems.put(key, new OraxenViewView(oraxenId, viewType));
                    Main.getInstance().getLogger().info("Успешно загружен Oraxen view_item: " + key);
                } else {
                    throw new IllegalArgumentException("Неизвестный тип view_item '" + type + "' для ключа '" + key + "'");
                }
            } catch (IllegalArgumentException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке view_item с ключом '" + key + "': " + e.getMessage());
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке view_item с ключом '" + key + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadEatItems(FileConfiguration config) {
        if (!config.contains("eat_item")) return;

        for (String key : config.getConfigurationSection("eat_item").getKeys(false)) {
            String path = "eat_item." + key;

            try {
                String type = config.getString(path + ".type");
                if (type == null) {
                    throw new IllegalArgumentException("Не указан тип предмета для eat_item с ключом '" + key + "'");
                }

                if ("standard".equalsIgnoreCase(type)) {
                    Material material = Material.valueOf(config.getString(path + ".material"));
                    String displayName = config.getString(path + ".displayName");
                    List<String> lore = config.getStringList(path + ".lore");
                    int radiationValue = config.getInt(path + ".foodValue.radiation");
                    int temperatureValue = config.getInt(path + ".foodValue.temperature");

                    eatItems.put(key, new StandardEatItem(material, displayName, lore, radiationValue, temperatureValue));
                    Main.getInstance().getLogger().info("Успешно загружен стандартный eat_item: " + key);
                } else if ("oraxen".equalsIgnoreCase(type)) {
                    String oraxenId = config.getString(path + ".oraxenId");
                    if (oraxenId == null || oraxenId.isEmpty()) {
                        throw new IllegalArgumentException("Отсутствует 'oraxenId' для eat_item с ключом '" + key + "'");
                    }

                    int radiationValue = config.getInt(path + ".foodValue.radiation");
                    int temperatureValue = config.getInt(path + ".foodValue.temperature");

                    eatItems.put(key, new OraxenEatItem(oraxenId, radiationValue, temperatureValue));
                    Main.getInstance().getLogger().info("Успешно загружен Oraxen eat_item: " + key);
                } else {
                    throw new IllegalArgumentException("Неизвестный тип eat_item '" + type + "' для ключа '" + key + "'");
                }
            } catch (IllegalArgumentException e) {
                Main.getInstance().getLogger().warning("Ошибка при загрузке eat_item с ключом '" + key + "': " + e.getMessage());
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("Непредвиденная ошибка при загрузке eat_item с ключом '" + key + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public EatItem getEatItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        for (EatItem eatItem : eatItems.values()) {
            if (eatItem.getItemStack().isSimilar(itemStack)) {
                return eatItem;
            }
        }

        return null;
    }


    public CustomItem getCustomItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }



        NamespacedKey idKey = new NamespacedKey("oraxen", "id");
        String oraxenId = itemStack.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        System.out.println(" ");
        System.out.println("Oraxen id: " + oraxenId);

        if (oraxenId != null) {
            for (CustomItem item : items.values()) {

                String itemOraxenId = item.getItemStack().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);

                if (oraxenId.equals(itemOraxenId)) {
                    System.out.println("FOUNNND WITH ORAXEN ITEM");
                    return item;
                }
            }
        }


        System.out.println("Проверка без praxen");

        if (!itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ARMOR_TRIM)) {
            return null;
        }

        for (CustomItem item : items.values()) {
//            System.out.println(itemStack);
//            System.out.println(item.getItemStack());
//            System.out.println(" ");
//            System.out.println(" ");
//            System.out.println(" ");
            ItemStack customItemStack = item.getItemStack();
            if (customItemStack == null || !customItemStack.hasItemMeta()) {
                continue;
            }

            var customMeta = customItemStack.getItemMeta();
            var itemMeta = itemStack.getItemMeta();

            if (customMeta.displayName() != null && customMeta.displayName().equals(itemMeta.displayName()) &&
                    customMeta.lore() != null && customMeta.lore().equals(itemMeta.lore())) {
                return item;
            }
        }

        return null;
    }


    public ViewType getViewItemByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        for (ViewType viewItem : viewItems.values()) {
            if (viewItem.getItemStack().isSimilar(itemStack)) {
                return viewItem;
            }
        }

        return null;
    }



    public Map<String, CustomItem> getItems() {
        return items;
    }

    public Map<String, ViewType> getViewItems() {
        return viewItems;
    }

    public Map<String, EatItem> getEatItems() {
        return eatItems;
    }

    public EatItem getEatItem(String key) {
        return eatItems.get(key);
    }

    public CustomItem getItem(String key) {
        return items.get(key);
    }

    public ViewType getViewItem(String key) {
        return viewItems.get(key);
    }


    public void reloadItemConfig(){
        try {
            Main.getInstance().getLogger().info("Items.yml reloading!");
            loadItems();
        } catch (Exception e){
            Main.getInstance().getLogger().warning("Items.yml reload failed");
        }
        Main.getInstance().getLogger().info("Items.yml reload");
    }
}
