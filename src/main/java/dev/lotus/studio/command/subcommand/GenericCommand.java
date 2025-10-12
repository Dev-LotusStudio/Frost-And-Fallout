package dev.lotus.studio.command.subcommand;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GenericCommand<T extends SubCommand> implements SubCommand {

    private final String name;
    private final String usage;
    private final Collection<T> subCommands;

    public GenericCommand(String name, String usage, Collection<T> subCommands) {
        this.name = name;
        this.usage = usage;
        this.subCommands = subCommands;
    }

    @Override
    public String getName() { return name; }


    @Override
    public String getUsage() {
        String children = subCommands.stream()
                .map(SubCommand::getName)
                .reduce((a, b) -> a + " | " + b)
                .orElse("");
        return "/" + name + " [ " + children + " ]";
    }

    @Override
    public Component getFullUsage(String parentChain) {
        // Формуємо повний ланцюжок команд
        String full = (parentChain == null || parentChain.isEmpty())
                ? "/" + name
                : "/" + parentChain + " " + name;

        // Формуємо список підкоманд
        String children = subCommands.stream()
                .map(SubCommand::getName)
                .reduce((a, b) -> a + " | " + b)
                .orElse("...");

        return Component.text("Invalid command usage.", NamedTextColor.RED)
                .append(Component.newline())
                .append(Component.text("Correct: ", NamedTextColor.GRAY))
                .append(Component.text(full + " ", NamedTextColor.AQUA))
                .append(Component.text("[ " + children + " ]", NamedTextColor.GRAY));
    }





    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(Permission.USE.getNode())){
            player.sendMessage("you don't have permission");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(getFullUsage("faf"));
            return true;
        }

        String sub = args[0].toLowerCase();
        for (SubCommand cmd : subCommands) {
            if (cmd.getName().equalsIgnoreCase(sub)) {
                return cmd.execute(player, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        player.sendMessage(getFullUsage("faf"));
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                    .map(SubCommand::getName)
                    .collect(Collectors.toList());
        }

        String sub = args[0].toLowerCase();
        for (SubCommand cmd : subCommands) {
            if (cmd.getName().equalsIgnoreCase(sub)) {
                return cmd.tabComplete(player, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return List.of();
    }
}

