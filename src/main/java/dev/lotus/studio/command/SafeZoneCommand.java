package dev.lotus.studio.command;

import dev.lotus.studio.database.savezone.SafeZoneDataBase;
import dev.lotus.studio.database.savezone.SafeZoneDataService;
import dev.lotus.studio.safezone.SafeZone;
import dev.lotus.studio.safezone.SafeZoneManager;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static dev.lotus.studio.utils.MapperUtils.formatLocation;

public class SafeZoneCommand {
    private final SafeZoneManager safeZoneManager = SafeZoneManager.getInstance();

    private Location pos1 = null;
    private Location pos2 = null;


    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("only players can execute this command");
            return true;
        }
        if (!player.hasPermission("lotusOffSeason.savezone")) {
            player.sendMessage("dont have permission");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage("no valid command: /lotus savezone pos1|pos2|save <name>");
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "pos1":
                pos1 = player.getLocation();
                player.sendMessage("first point: " + formatLocation(pos1));
                break;

            case "pos2":
                pos2 = player.getLocation();
                player.sendMessage("second point: " + formatLocation(pos2));
                break;

            case "save":
                if (args.length < 3) {
                    player.sendMessage("set name to savezone: /lotus savezone save <назва>");
                    return true;
                }
                saveZone(player, pos1, pos2, args[2]);
                break;

            case "list":
                listZones(player);
                return true;

            case "remove":
                if (args.length < 3) {
                    player.sendMessage("Укажіть ID зони: /lotus savezone remove <id>");
                    return true;
                }
                removeZone(player, Integer.parseInt(args[2]));
                break;

            default:
                player.sendMessage("Невідома команда. Використовуйте: pos1, pos2, save, list, remove.");
        }
        return true;
    }

    private void saveZone(Player player, Location pos1, Location pos2, String zoneName) {
        if (pos1 != null && pos2 != null) {
            Pair<Location, Location> zoneLoc = new ImmutablePair<>(pos1, pos2);
            SafeZone safeZone = new SafeZone(zoneName, zoneLoc);

            // runtime
            safeZoneManager.addSafeZone(safeZone);

            player.sendMessage("Зона '" + zoneName + "' успішно збережена.");
        } else {
            player.sendMessage("Будь ласка, спочатку встановіть обидві точки (pos1 і pos2).");
        }
    }

    private void listZones(Player player) {
        List<SafeZoneDataBase> safeZones = safeZoneManager.getDataZones();

        if (safeZones.isEmpty()) {
            player.sendMessage("немає зон.");
            return;
        }

        HashMap<String, Integer> saveId = new HashMap<>();
        safeZones.forEach(zone -> saveId.put(zone.getSafeZoneName(), zone.getSafeZoneId()));

        player.sendMessage("Список зон:");
        saveId.forEach((name, id) ->
                player.sendMessage(" - Назва: " + name + ", ID: " + id)
        );
    }

    private void removeZone(Player player, int id) {
        // runtime
        safeZoneManager.removeSafeZone(id);

        player.sendMessage(Component.text("Safe zone with ID " + id + " deleted!"));
    }
}
