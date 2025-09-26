package dev.lotus.studio.command.subcommand.reload.factory;


import dev.lotus.studio.command.subcommand.SubCommandFactory;
import dev.lotus.studio.item.CustomItemManager;

import java.util.Collection;

public class ReloadSubCommandFactory {
    public static Collection<AbstractReloadCommand> createAll(
            CustomItemManager itemManager
    ) {
        return SubCommandFactory.createAll(ReloadSubCommandType.class,
                type -> type.create(itemManager));
    }
}


