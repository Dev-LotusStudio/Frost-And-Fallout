package dev.lotus.studio.command.subcommand.safezone.subcommand;

import dev.lotus.studio.command.subcommand.safezone.factory.AbstractSafeZoneSubCommand;
import dev.lotus.studio.database.savezone.SafeZoneDataBase;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.entity.Player;

import java.util.List;

public class SafeZoneListCommand extends AbstractSafeZoneSubCommand {

    SafeZoneListCommand(SafeZoneManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Список усіх збережених зон";
    }

    @Override
    public String getUsage() {
        return "/main safezone list";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        List<SafeZoneDataBase> safeZones = manager.getDataZones();

        if (safeZones.isEmpty()) {
            player.sendMessage("§cНемає збережених зон.");
            return true;
        }

        player.sendMessage("§aСписок зон:");
        for (SafeZoneDataBase zone : safeZones) {
            player.sendMessage(" - Назва: " + zone.getSafeZoneName() + ", ID: " + zone.getSafeZoneId());
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        // Для списку зон автозаповнення не потрібне
        return List.of();
    }
}
