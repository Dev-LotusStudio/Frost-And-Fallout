package dev.lotus.studio.command;

import dev.lotus.studio.command.subcommand.GenericCommand;
import dev.lotus.studio.command.subcommand.HelpCommand;
import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.command.subcommand.item.factory.ItemSubCommandFactory;
import dev.lotus.studio.command.subcommand.reload.factory.ReloadSubCommandFactory;
import dev.lotus.studio.command.subcommand.safezone.factory.SafeZoneSubCommandFactory;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class MainCommand extends AbstractCommand {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public MainCommand(String command, CustomItemManager itemManager, SafeZoneManager safeZoneManager) {
        super(command);

        // тут реєструєш усі "верхньорівневі" команди тобто усі реалізації SubCommand
        register(new GenericCommand<>(
                "item",
                "/main item <armor|view|eat>",
                ItemSubCommandFactory.createAll(itemManager)
        ));

        register(new GenericCommand<>(
                "safezone",
                "/main safezone <pos1|pos2|save|list|remove>",
                SafeZoneSubCommandFactory.createAll(safeZoneManager)
        ));

        register(new  GenericCommand<>(
                "reload",
                "main",
                ReloadSubCommandFactory.createAll(itemManager)));

        register(new HelpCommand());

    }

    private void register(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду можно выполнять только игроком.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§aИспользуйте /" + label + " help");
            return true;
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub == null) {
            player.sendMessage("§cНеизвестная подкоманда. Используйте /" + label + " help");
            return true;
        }

        // делегуємо вниз, напр. в ItemCommand
        return sub.execute(player, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return List.of();

        if (args.length == 1) {
            // показати верхньорівневі команди: item, savezone, reload
            return new ArrayList<>(subCommands.keySet());
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            // делегуємо вниз, напр. ItemCommand → ArmorCommand → give
            return sub.tabComplete(player, Arrays.copyOfRange(args, 1, args.length));
        }

        return List.of();
    }
}
