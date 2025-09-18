package dev.lotus.studio.command;


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
    private SafeZoneManager safeZoneManager = SafeZoneManager.getInstance();


    private Location pos1 = null;
    private Location pos2 = null;

    public SafeZoneCommand() {
    }

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
                player.sendMessage("fist point: " + formatLocation(pos1));
                break;

            case "pos2":
                pos2 = player.getLocation();
                player.sendMessage("second points: " + formatLocation(pos2));
                break;

            case "save":
                if (args.length < 3) {
                    player.sendMessage("set name to savezone: /lotus savezone save <назва>");
                    return true;
                }
                saveZoneToDB(player, pos1, pos2, args[2]);
                break;
            case "list":
                player.sendMessage("Укажіть назву для зони. Наприклад: /<команда> save <назва>");
                listZoneToDB(player);
                return true;
            case "remove":
                if (args.length < 3) {
                    player.sendMessage("Укажіть назву для зони. Наприклад: /<команда> save <назва>");
                    return true;
                }
                removeZoneToDB(player, Integer.parseInt(args[1]));
                break;
            default:
                player.sendMessage("Невідома команда. Використовуйте: pos1, pos2 або save.");
        }
        return true;
    }

    private void removeZoneToDB(Player player, int id) {
        List<SafeZone>  safeZones = safeZoneManager.getAllSafeZones();
        safeZones.forEach(zone -> {
            if (zone.getZoneID() == id) {
                safeZoneManager.removeSafeZone(id);
                player.sendMessage(Component.text("Safe zone deleted!"));
            }
        });


//        service.getAllSaveZones().forEach(safeZoneDataBase -> {
//            if (safeZoneDataBase.getSafeZoneId() == id){
//                player.sendMessage("Сейв зону удаленно с названием: " + safeZoneDataBase.getSafeZoneName() + " ID: " + safeZoneDataBase.getSafeZoneId());
//            }
//        });
//        service.removeProtectZone(id);
    }

    private void listZoneToDB(Player player) {
        // Отримуємо всі збережені зони з бази даних
        List<SafeZone> safeZoneDataBases = safeZoneManager.getAllSafeZones();

        // Якщо зон немає, повідомляємо гравця
        if (safeZoneDataBases.isEmpty()) {
            player.sendMessage("нет зон.");
            return;
        }

        // Формуємо мапу з імен зон і їх ідентифікаторів
        HashMap<String, Integer> saveId = new HashMap<>();
        safeZoneDataBases.forEach(saveZoneData -> saveId.put(saveZoneData.getZoneName(), saveZoneData.getZoneID()));

        // Виводимо гравцю список зон
        player.sendMessage("Список зон:");
        saveId.forEach((name, id) ->
                player.sendMessage(" - Имя: " + name + ", ID: " + id)
        );
    }


    private void saveZoneToDB(Player player, Location pos1, Location pos2, String zoneName) {
        if (pos1 != null && pos2 != null) {
            Pair<Location, Location> zoneLoc = new ImmutablePair<>(pos1, pos2);
            SafeZone safeZone = new SafeZone(zoneName,zoneLoc);
            safeZoneManager.addSafeZone(safeZone);
            player.sendMessage("Зона '" + zoneName + "' успішно збережена.");
        } else {
            player.sendMessage("Будь ласка, спочатку встановіть обидві точки (pos1 і pos2).");
        }
    }


}
