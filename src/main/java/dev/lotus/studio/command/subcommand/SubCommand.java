package dev.lotus.studio.command.subcommand;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {
    String getName();
    String getUsage();
    boolean execute(Player player, String[] args);
    List<String> tabComplete(Player player, String[] args);

    default Component getFullUsage(String parentChain) {
        Component base = Component.text("Invalid command usage.", NamedTextColor.RED)
                .append(Component.newline())
                .append(Component.text("Correct: ", NamedTextColor.GRAY));

        if (parentChain == null || parentChain.isEmpty()) {
            return base.append(Component.text("/")
                    .append(Component.text(getName(), NamedTextColor.AQUA))
                    .append(Component.space())
                    .append(Component.text(getUsage(), NamedTextColor.GRAY))
            );
        }

        return base.append(Component.text("/")
                .append(Component.text(parentChain + " " + getName(), NamedTextColor.AQUA))
                .append(getUsage().isEmpty()
                        ? Component.empty()
                        : Component.space().append(Component.text(getUsage(), NamedTextColor.GRAY))
                )
        );
    }


}

