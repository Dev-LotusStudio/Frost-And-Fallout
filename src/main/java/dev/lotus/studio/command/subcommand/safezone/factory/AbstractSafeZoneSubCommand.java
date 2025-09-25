package dev.lotus.studio.command.subcommand.safezone.factory;


import dev.lotus.studio.command.subcommand.Permission;
import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.entity.Player;


public abstract class AbstractSafeZoneSubCommand implements SubCommand {
    protected final SafeZoneManager manager;

    protected AbstractSafeZoneSubCommand(SafeZoneManager manager) {
        this.manager = manager;
    }

    @Override
    public final boolean execute(Player player, String[] args) {
        if (!player.hasPermission(Permission.SAFE_ZONE.getNode())) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        return perform(player, args);
    }

    protected abstract boolean perform(Player player, String[] args);
}

