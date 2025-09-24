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
    public String getDescription() {
        return "Встановлює першу точку зони";
    }

    @Override
    public String getUsage() {
        return "/main safezone pos1";
    }

    @Override
    public boolean execute(Player player, String[] args) {
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
