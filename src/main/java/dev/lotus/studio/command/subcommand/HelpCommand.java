package dev.lotus.studio.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand implements SubCommand {
    @Override
    public String getName() { return "help"; }


    @Override
    public String getUsage() { return "/faf help"; }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage(Component.text("=== Frost-And-Fallout Commands ===")
                .color(TextColor.color(0x46D56B)));
        player.sendMessage(" ");

        // Item commands
        player.sendMessage(Component.text()
                .append(Component.text("/item armor", NamedTextColor.WHITE)
                        .hoverEvent(HoverEvent.showText(Component.text("Equip or modify armor pieces", NamedTextColor.AQUA)))
                        .clickEvent(ClickEvent.suggestCommand("/item armor")))
                .append(Component.text(" - Armor management", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/item view", NamedTextColor.WHITE)
                        .hoverEvent(HoverEvent.showText(Component.text("Inspect item details", NamedTextColor.AQUA)))
                        .clickEvent(ClickEvent.suggestCommand("/item view")))
                .append(Component.text(" - View custom items", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/item eat", NamedTextColor.WHITE)
                        .hoverEvent(HoverEvent.showText(Component.text("Consume custom food items", NamedTextColor.AQUA)))
                        .clickEvent(ClickEvent.suggestCommand("/item eat")))
                .append(Component.text(" - Eat special food", NamedTextColor.GRAY))
        );

        // SafeZone commands
        player.sendMessage(Component.text()
                .append(Component.text("/savezone pos1", NamedTextColor.DARK_AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Set the first corner of the SafeZone", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/savezone pos1")))
                .append(Component.text(" - Set first point", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/savezone pos2", NamedTextColor.DARK_AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Set the second corner of the SafeZone", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/savezone pos2")))
                .append(Component.text(" - Set second point", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/savezone save <name>", NamedTextColor.DARK_AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Save a SafeZone with the given name", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/savezone save ")))
                .append(Component.text(" - Save zone", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/savezone list", NamedTextColor.DARK_AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("List all saved SafeZones", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/savezone list")))
                .append(Component.text(" - List zones", NamedTextColor.GRAY))
        );

        player.sendMessage(Component.text()
                .append(Component.text("/savezone remove <ID>", NamedTextColor.DARK_AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Remove a SafeZone by its ID", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/savezone remove ")))
                .append(Component.text(" - Remove zone", NamedTextColor.GRAY))
        );

        // Help command
        player.sendMessage(Component.text()
                .append(Component.text("/help", NamedTextColor.BLUE)
                        .hoverEvent(HoverEvent.showText(Component.text("Show this help message", NamedTextColor.WHITE)))
                        .clickEvent(ClickEvent.suggestCommand("/help")))
                .append(Component.text(" - Show commands", NamedTextColor.GRAY))
        );
        player.sendMessage(" ");
        player.sendMessage(Component.text("===============================").color(TextColor.color(0x46D56B)));
        player.sendMessage(" ");
        player.sendMessage(Component.text("       ⓁⓄⓉⓊⓈ ⓈⓉⓊⒹⒾⓄ").color(TextColor.color(0xFFB340)));

        return true;
    }


    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}

