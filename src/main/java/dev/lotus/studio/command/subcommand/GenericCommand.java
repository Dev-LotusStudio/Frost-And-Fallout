package dev.lotus.studio.command.subcommand;


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
                .reduce((a, b) -> a + "|" + b)
                .orElse("");
        return "/" + name + " <" + children + ">";
    }

    public String getFullUsage(String parentChain) {
        String full = (parentChain == null || parentChain.isEmpty())
                ? name
                : parentChain + " " + name;

        return "/" + full + " ..."; // для опису групи
    }


    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(getUsage());
            return true;
        }

        String sub = args[0].toLowerCase();
        for (SubCommand cmd : subCommands) {
            if (cmd.getName().equalsIgnoreCase(sub)) {
                return cmd.execute(player, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        player.sendMessage("§cНеизвестная подкоманда. " + getUsage());
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

