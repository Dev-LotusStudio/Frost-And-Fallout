package dev.lotus.studio.command.subcommand.safezone.subcommand;

import dev.lotus.studio.command.subcommand.safezone.factory.AbstractSafeZoneSubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class SafeZoneRemoveCommand extends AbstractSafeZoneSubCommand {

    SafeZoneRemoveCommand(SafeZoneManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "remove";
    }


    @Override
    public String getUsage() {
        return "<ID>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length < 1) { // перевіряємо, що після remove є аргумент
            player.sendMessage(getFullUsage("faf safezone"));
            return true;
        }

        int zoneId;
        try {
            zoneId = Integer.parseInt(args[0]); // тут беремо args[0], бо виклик передає вже відсічені аргументи
        } catch (NumberFormatException e) {
            player.sendMessage("§cНекоректний ID зони!");
            return true;
        }
        if (manager.getZoneById(zoneId)){
            manager.removeSafeZone(zoneId);
            player.sendMessage(Component.text("SafeZone з ID " + zoneId + " видалено!"));
            return true;
        }
        return true;
    }


    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return manager.getDataZones().stream()
                    .map(zone -> String.valueOf(zone.getSafeZoneId()))
                    .toList();
        }
        return List.of();
    }
}
