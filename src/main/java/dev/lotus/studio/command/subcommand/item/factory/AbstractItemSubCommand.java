package dev.lotus.studio.command.subcommand.item.factory;

import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.item.CustomItemManager;

public abstract class AbstractItemSubCommand implements SubCommand {
    protected final CustomItemManager itemManager;

    public AbstractItemSubCommand(CustomItemManager itemManager) {
        this.itemManager = itemManager;
    }

}
