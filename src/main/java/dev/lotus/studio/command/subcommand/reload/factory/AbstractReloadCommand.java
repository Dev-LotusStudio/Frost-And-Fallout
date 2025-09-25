package dev.lotus.studio.command.subcommand.reload.factory;

import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.item.CustomItemManager;

public abstract class AbstractReloadCommand implements SubCommand {
    protected final CustomItemManager itemManager;

    protected AbstractReloadCommand(CustomItemManager itemManager) {
        this.itemManager = itemManager;
    }
}