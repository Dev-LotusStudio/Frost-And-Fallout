package dev.lotus.studio.command.subcommand.item.subcommand;

import dev.lotus.studio.command.subcommand.item.factory.AbstractItemSubCommand;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.utils.ItemNameUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ViewCommand extends AbstractItemSubCommand {

    ViewCommand(CustomItemManager itemManager) {
        super(itemManager);
    }

    @Override
    public String getName() {
        return "view";
    }


    @Override
    public String getUsage() {
        return "[give | list | help]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(getFullUsage("faf item"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> {
                if (args.length != 2) {
                    player.sendMessage("§7Использование: /faf item view give <itemKey>");
                    return true;
                }
                var viewItem = itemManager.getViewItem(args[1]);
                if (viewItem == null) {
                    player.sendMessage("§cView item '" + args[1] + "' не найден!");
                    return true;
                }
                ItemStack itemStack = viewItem.getItemStack();
                player.getInventory().addItem(itemStack);
                player.sendMessage("§aВыдан view item: " +
                        (itemStack.getItemMeta() != null
                                ? ItemNameUtil.getItemName(itemStack)
                                : "Без имени"));
            }
            case "list" -> {
                player.sendMessage("§aСписок view предметов:");
                itemManager.getViewItems().keySet().forEach(key -> player.sendMessage("- " + key));
            }
            case "help" -> {
                player.sendMessage("§aView команды:");
                player.sendMessage("/main item view give <itemKey>");
                player.sendMessage("/main item view list");
                player.sendMessage("/main item view help");
            }
            default -> player.sendMessage(getFullUsage("faf item"));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return List.of("give", "list", "help");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return itemManager.getViewItems().keySet().stream().toList();
        }
        return List.of();
    }
}
