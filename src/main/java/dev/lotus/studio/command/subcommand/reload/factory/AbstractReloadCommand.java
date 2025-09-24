package dev.lotus.studio.command.subcommand.reload.factory;

import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.safezone.SafeZoneManager;

public abstract class AbstractReloadCommand implements SubCommand {
    protected final CustomItemManager itemManager;
    protected final SafeZoneManager safeZoneManager;

    protected AbstractReloadCommand(CustomItemManager itemManager , SafeZoneManager safeZoneManager) {
        this.itemManager = itemManager;
        this.safeZoneManager = safeZoneManager;
    }
}