package dev.lotus.studio.command.subcommand;


import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {
    String getName();
    String getUsage();
    boolean execute(Player player, String[] args);
    List<String> tabComplete(Player player, String[] args);

    default String getFullUsage(String parentChain) {
        if (parentChain == null || parentChain.isEmpty()) {
            return "/" + getName() + " " + getUsage();
        }
        return "/" + parentChain + " " + getName() +
                (getUsage().isEmpty() ? "" : " " + getUsage());
    }
}

