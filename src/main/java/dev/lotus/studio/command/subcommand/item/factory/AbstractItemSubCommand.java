package dev.lotus.studio.command.subcommand.item.factory;

import dev.lotus.studio.command.subcommand.Permission;
import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.item.CustomItemManager;
import org.bukkit.entity.Player;

public abstract class AbstractItemSubCommand implements SubCommand {
    protected final CustomItemManager itemManager;

    public AbstractItemSubCommand(CustomItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public final boolean execute(Player player, String[] args) {
        if (!player.hasPermission(Permission.ITEM.getNode())) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        return perform(player, args);
    }

    protected abstract boolean perform(Player player, String[] args);

}
