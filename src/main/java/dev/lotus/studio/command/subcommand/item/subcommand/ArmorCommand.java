package dev.lotus.studio.command.subcommand.item.subcommand;


import dev.lotus.studio.command.subcommand.item.factory.AbstractItemSubCommand;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.utils.ItemNameUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class ArmorCommand extends AbstractItemSubCommand {

    ArmorCommand(CustomItemManager itemManager) {
        super(itemManager);
    }

    @Override
    public String getName() {
        return "armor";
    }

    @Override
    public String getDescription() {
        return "Управление предметами брони";
    }

    @Override
    public String getUsage() {
        return "/main item armor <give|list|help>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§7Использование: " + getUsage());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> {
                if (args.length < 2) {
                    player.sendMessage("§7Использование: /main item armor give <itemKey>");
                    return true;
                }
                var customItem = itemManager.getItem(args[1]);
                if (customItem == null) {
                    player.sendMessage("§cItem '" + args[1] + "' не найден!");
                    return true;
                }
                player.getInventory().addItem(customItem.getItemStack());
                player.sendMessage("§aВыдан предмет: " + ItemNameUtil.getItemName(customItem.getItemStack()));
            }
            case "list" -> {
                player.sendMessage("§aСписок предметов брони:");
                itemManager.getItems().keySet().forEach(key -> player.sendMessage("- " + key));
            }
            case "help" -> {
                player.sendMessage("§aArmor команды:");
                player.sendMessage("/main item armor give <itemKey>");
                player.sendMessage("/main item armor list");
                player.sendMessage("/main item armor help");
            }
            default -> player.sendMessage("§cНеизвестная команда. " + getUsage());
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return List.of("give", "list", "help");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return itemManager.getItems().keySet().stream().toList();
        }
        return List.of();
    }
}

