package dev.lotus.studio.command.subcommand;

import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand implements SubCommand {
    @Override
    public String getName() { return "help"; }


    @Override
    public String getUsage() { return "/main help"; }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("§aДоступні команди:");
        player.sendMessage(" §e/item <armor|view|eat>");
        player.sendMessage(" §e/savezone <pos1|pos2|save|list|remove>");
        player.sendMessage(" §e/help");
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}

