package dev.lotus.studio.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.lotus.studio.database.hibernate.savezone.SaveZoneDataService;
import dev.lotus.studio.item.CustomItemManager;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends AbstractCommand {
    private final CustomItemManager itemManager;
    private final SafeZoneCommand saveZoneCommand;

    public MainCommand(String command, CustomItemManager itemManager, SaveZoneDataService saveZoneDataService) {
        super(command);
        this.itemManager = itemManager;
        this.saveZoneCommand =new SafeZoneCommand(saveZoneDataService);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду можно выполнять только как игрок.");
            return true;
        }

        if (args.length < 1) {
            sendMainHelp(player, label);
            return true;
        }

        String category = args[0].toLowerCase();

        switch (category) {
            case "item" -> handleItemCommands(player, label, args);
            case "savezone" -> saveZoneCommand.execute(player, label,args);
            case "reload" -> reloadConfig(player, label, args);
            default -> player.sendMessage("Неизвестная категория. Используйте /" + label + " для помощи.");
        }

        return true;
    }

            // --- Command Reload ---
    private void reloadConfig(Player player, String label, String[] args) {
        if (args.length < 2) {
            sendReloadHelp(player, label);
        }
//        if (args[0].equalsIgnoreCase("reload")){
//            itemManager.reloadItemConfig();
//        }
        String reload = args[1].toLowerCase();

        switch (reload) {
            case "items" -> itemReloadCommand(player, args);
            case "all" -> allReloadCommand(player, args);
            case "help" -> sendReloadHelp(player, label);
            default -> player.sendMessage("Неизвестная команда для item. Используйте /" + label + " item help.");
        }

    }

    private void sendReloadHelp(Player player, String label) {
        player.sendMessage("§aКоманды для reload:");
        player.sendMessage("§7/" + label + " items - Reload items configuration");
        player.sendMessage("§7/" + label + " villager - Reload villager configuration");
        player.sendMessage("§7/" + label + " all - Reload all configuration");
    }

    private void itemReloadCommand(Player player, String[] args) {
        player.sendMessage("Reloading config " + args[1]);
        try {
            itemManager.reloadItemConfig();
        } catch (Exception e) {
            player.sendMessage("Config reload failed.");
        }
        player.sendMessage("Reload complete.");
    }



    private void allReloadCommand(Player player, String[] args) {
        player.sendMessage("Reloading config " + args[1]);
        try {
            itemReloadCommand(player, args);
        } catch (Exception e) {
            player.sendMessage("Config reload failed.");
        }
    }


    // --- Main help command ----
    private void sendMainHelp(Player player, String label) {
        player.sendMessage("§aОсновные команды:");
        player.sendMessage("§7/" + label + " armor - Управление предметами (броня).");
        player.sendMessage("§7/" + label + " horder - Управление торговцами (Holder).");
        player.sendMessage("§7/" + label + " reload - Reload Configs.");
        player.sendMessage("§7/" + label + " help - Показать эту справку.");
    }

    //---  item ---
    private void handleItemCommands(Player player, String label, String[] args) {
        if (args.length < 2) {
            sendItemHelp(player, label);
            return;
        }

        String subCommand = args[1].toLowerCase();

        switch (subCommand) {
            case "armor" -> handleArmorCommands(player, label, args);
            case "view" -> handleViewCommands(player, label, args);
            case "eat" -> handleEatCommands(player, label, args);
            default -> player.sendMessage("Неизвестная команда для item. Используйте /" + label + " item help.");
        }
    }

    private void sendItemHelp(Player player, String label) {
        player.sendMessage("§aКоманды для item:");
        player.sendMessage("§7/" + label + " item armor - Управление предметами брони.");
        player.sendMessage("§7/" + label + " item view - Управление view item.");
        player.sendMessage("§7/" + label + " item eat - Управление съедобными предметами.");
        player.sendMessage("§7/" + label + " item help - Показать помощь для item.");
    }



    // --- View Commands ---
    private void handleViewCommands(Player player, String label, String[] args) {
        if (args.length < 3) {
            sendViewHelp(player, label);
            return;
        }

        String action = args[2].toLowerCase();

        switch (action) {
            case "give" -> handleViewGiveCommand(player, label, args);
            case "list" -> handleViewListCommand(player);
            case "help" -> sendViewHelp(player, label);
            default -> player.sendMessage("Неизвестная команда для view. Используйте /" + label + " view help.");
        }
    }

    private void handleViewGiveCommand(Player player, String label, String[] args) {
        if (args.length < 4) {
            player.sendMessage("Использование: /" + label + " view give <itemKey>");
            return;
        }

        String itemKey = args[3];
        var viewItem = itemManager.getViewItem(itemKey);

        if (viewItem == null) {
            player.sendMessage("View item с ключом '" + itemKey + "' не найден.");
            return;
        }

        ItemStack itemStack = viewItem.getItemStack();
        if (itemStack == null) {
            player.sendMessage("View item с ключом '" + itemKey + "' имеет некорректный ItemStack.");
            return;
        }

        player.getInventory().addItem(itemStack);
        Component displayName = itemStack.getItemMeta().displayName();
        player.sendMessage("Вам выдан view item: " + (itemStack.getItemMeta() != null ? displayName : "Без имени"));
    }


    private void handleViewListCommand(Player player) {
        player.sendMessage("Доступные view items:");
        itemManager.getViewItems().keySet().forEach(key -> player.sendMessage("- " + key));
    }

    private void sendViewHelp(Player player, String label) {
        player.sendMessage("Команды для view:");
        player.sendMessage("/" + label + " view give <itemKey> - Выдать view item.");
        player.sendMessage("/" + label + " view list - Показать список доступных view items.");
        player.sendMessage("/" + label + " view help - Показать помощь для view.");
    }



    // --- Armor Commands ---
    private void handleArmorCommands(Player player, String label, String[] args) {
        if (args.length < 4) {
            sendArmorHelp(player, label);
            return;
        }

        String action = args[2].toLowerCase();

        switch (action) {
            case "give" -> handleGiveCommand(player, label, args);
            case "list" -> handleListCommand(player);
            case "help" -> sendArmorHelp(player, label);
            default -> player.sendMessage("Неизвестная команда для armor. Используйте /" + label + " armor help.");
        }
    }

    private void handleGiveCommand(Player player, String label, String[] args) {
        if (args.length < 4) {
            player.sendMessage("Использование: /" + label + " armor give <itemKey>");
            return;
        }

        String itemKey = args[3];
        var customItem = itemManager.getItem(itemKey);

        if (customItem == null) {
            player.sendMessage("Предмет с ключом '" + itemKey + "' не найден.");
            return;
        }

        player.getInventory().addItem(customItem.getItemStack());
        player.sendMessage("Вам выдан предмет: " + customItem.getItemStack().getItemMeta().getDisplayName());
    }

    private void handleListCommand(Player player) {
        player.sendMessage("Доступные предметы:");
        itemManager.getItems().keySet().forEach(key -> player.sendMessage("- " + key));
    }

    private void sendArmorHelp(Player player, String label) {
        player.sendMessage("Команды для armor:");
        player.sendMessage("/" + label + " armor give <itemKey> - Выдать предмет.");
        player.sendMessage("/" + label + " armor list - Показать список доступных предметов.");
        player.sendMessage("/" + label + " armor help - Показать помощь для armor.");
    }

    // --- Eat Commands ---

    private void handleEatCommands(Player player, String label, String[] args) {
        if (args.length < 4) {
            sendEatHelp(player, label);
            return;
        }

        String action = args[2].toLowerCase();

        switch (action) {
            case "give" -> handleEatGiveCommand(player, label, args);
            case "list" -> handleEatListCommand(player);
            case "help" -> sendEatHelp(player, label);
            default -> player.sendMessage("Неизвестная команда для eat. Используйте /" + label + " eat help.");
        }
    }
    private void handleEatGiveCommand(Player player, String label, String[] args) {
        if (args.length < 4) {
            player.sendMessage("Использование: /" + label + " eat give <itemKey>");
            return;
        }

        String itemKey = args[3];
        var eatItem = itemManager.getEatItem(itemKey);

        if (eatItem == null) {
            player.sendMessage("Eat item с ключом '" + itemKey + "' не найден.");
            return;
        }

        ItemStack itemStack = eatItem.getItemStack();
        if (itemStack == null) {
            player.sendMessage("Eat item с ключом '" + itemKey + "' имеет некорректный ItemStack.");
            return;
        }

        player.getInventory().addItem(itemStack);
        player.sendMessage("Вам выдан eat item: " + (itemStack.getItemMeta() != null ? itemStack.getItemMeta().getDisplayName() : "Без имени"));
    }

    private void handleEatListCommand(Player player) {
        player.sendMessage("Доступные eat items:");
        itemManager.getEatItems().keySet().forEach(key -> player.sendMessage("- " + key));
    }

    private void sendEatHelp(Player player, String label) {
        player.sendMessage("Команды для eat:");
        player.sendMessage("/" + label + " eat give <itemKey> - Выдать eat item.");
        player.sendMessage("/" + label + " eat list - Показать список доступных eat items.");
        player.sendMessage("/" + label + " eat help - Показать помощь для eat.");
    }
















    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // Предлагаем категории верхнего уровня
            suggestions.addAll(List.of("item", "savezone"));
        } else if (args.length == 2) {
            String category = args[0].toLowerCase();
            if ("item".equals(category)) {
                suggestions.addAll(List.of("armor", "view", "eat", "help"));
            }
            else if ("savezone".equals(category)) {
                suggestions.addAll(List.of("pos1", "pos2", "save","list"));
            }
        } else if (args.length == 3) {
            String category = args[0].toLowerCase();
            String subCommand = args[1].toLowerCase();

            if ("item".equals(category)) {
                suggestions.add("give"); // Добавить сюда команду "give"
                switch (subCommand) {
                    case "armor", "view", "eat" -> suggestions.addAll(List.of("give", "list", "help"));
                }
            }

        } else if (args.length == 4) {
            String category = args[0].toLowerCase();
            String subCommand = args[1].toLowerCase();
            String action = args[2].toLowerCase();

            if ("item".equals(category)) {
                switch (subCommand) {
                    case "armor" -> {
                        if ("give".equals(action)) {
                            suggestions.addAll(itemManager.getItems().keySet());
                        }
                    }
                    case "view" -> {
                        if ("give".equals(action)) {
                            suggestions.addAll(itemManager.getViewItems().keySet());
                        }
                    }
                    case "eat" -> {
                        if ("give".equals(action)) {
                            suggestions.addAll(itemManager.getEatItems().keySet());
                        }
                    }
                }
            }
        }

        return suggestions;
    }




}
