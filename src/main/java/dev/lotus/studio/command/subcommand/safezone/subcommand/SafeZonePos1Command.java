package dev.lotus.studio.command.subcommand.safezone.subcommand;

import dev.lotus.studio.command.subcommand.safezone.factory.AbstractSafeZoneSubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.List;

import static dev.lotus.studio.utils.MapperUtils.formatLocation;

public class SafeZonePos1Command extends AbstractSafeZoneSubCommand {

    SafeZonePos1Command(SafeZoneManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "pos1";
    }


    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length != 0){
            player.sendMessage(getFullUsage("faf safezone"));
            return true;
        }
        Location pos1 = player.getLocation();
        manager.setTempPos1(player.getUniqueId(), pos1); // зберігаємо тимчасово у менеджері
        player.sendMessage("§aПерша точка зони встановлена: " + formatLocation(pos1));
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
