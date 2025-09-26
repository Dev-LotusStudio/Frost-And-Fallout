package dev.lotus.studio.command.subcommand.item.subcommand;

import dev.lotus.studio.command.subcommand.item.factory.AbstractItemSubCommand;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.utils.ItemNameUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EatCommand extends AbstractItemSubCommand {

    EatCommand(CustomItemManager itemManager) {
        super(itemManager);
    }

    @Override
    public String getName() {
        return "eat";
    }


    @Override
    public String getUsage() {
        return "<give|list|help>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§7Использование: " + getFullUsage("lotus item"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> {
                if (args.length != 2) {
                    player.sendMessage("§7Использование: /lotus item eat give <itemKey>");
                    return true;
                }
                var eatItem = itemManager.getEatItem(args[1]);
                if (eatItem == null) {
                    player.sendMessage("§cEat item '" + args[1] + "' не найден!");
                    return true;
                }
                ItemStack itemStack = eatItem.getItemStack();
                player.getInventory().addItem(itemStack);
                player.sendMessage("§aВыдан eat item: " +
                        (itemStack.getItemMeta() != null
                                ? ItemNameUtil.getItemName(itemStack)
                                : "Без имени"));
            }
            case "list" -> {
                player.sendMessage("§aСписок съедобных предметов:");
                itemManager.getEatItems().keySet().forEach(key -> player.sendMessage("- " + key));
            }
            case "help" -> {
                player.sendMessage("§aEat команды:");
                player.sendMessage("/main item eat give <itemKey>");
                player.sendMessage("/main item eat list");
                player.sendMessage("/main item eat help");
            }
            default -> player.sendMessage("§cНеизвестная команда. " + getFullUsage("lotus item"));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return List.of("give", "list", "help");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return itemManager.getEatItems().keySet().stream().toList();
        }
        return List.of();
    }
}
