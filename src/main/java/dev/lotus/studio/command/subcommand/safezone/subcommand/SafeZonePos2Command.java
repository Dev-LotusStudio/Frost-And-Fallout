package dev.lotus.studio.command.subcommand.safezone.subcommand;

import dev.lotus.studio.command.subcommand.safezone.factory.AbstractSafeZoneSubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.entity.Player;

import java.util.List;

import static dev.lotus.studio.utils.MapperUtils.formatLocation;

public class SafeZonePos2Command extends AbstractSafeZoneSubCommand {

    SafeZonePos2Command(SafeZoneManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "pos2";
    }

    @Override
    public String getDescription() {
        return "Встановлює другу точку для SafeZone";
    }

    @Override
    public String getUsage() {
        return "/main safezone pos2";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        manager.setTempPos2(player.getUniqueId(), player.getLocation());
        player.sendMessage("Друга точка встановлена: " + formatLocation(player.getLocation()));
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
