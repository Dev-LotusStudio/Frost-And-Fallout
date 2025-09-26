package dev.lotus.studio.command.subcommand.safezone.subcommand;

import dev.lotus.studio.command.subcommand.safezone.factory.AbstractSafeZoneSubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;
import dev.lotus.studio.safezone.SafeZone;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SafeZoneSaveCommand extends AbstractSafeZoneSubCommand {

    SafeZoneSaveCommand(SafeZoneManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getUsage() {
        return "<name>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        System.out.println(Arrays.toString(args));
        if (args.length < 1) {
            player.sendMessage(getFullUsage("lotus safezone"));
            return true;
        }


        String zoneName = args[0];

        var pos1 = manager.getTempPos1(player.getUniqueId());
        var pos2 = manager.getTempPos2(player.getUniqueId());

        if (pos1 == null || pos2 == null) {
            player.sendMessage("Будь ласка, спочатку встановіть обидві точки (pos1 і pos2).");
            return true;
        }
        SafeZone safeZone = new SafeZone(zoneName, new ImmutablePair<>(pos1, pos2));
        manager.addSafeZone(safeZone);
        player.sendMessage("Зона '" + zoneName + "' успішно збережена.");
        manager.removeTempPositions(player.getUniqueId());
        return true;
    }


    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
