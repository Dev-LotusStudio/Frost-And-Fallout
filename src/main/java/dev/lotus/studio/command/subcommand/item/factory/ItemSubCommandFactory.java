package dev.lotus.studio.command.subcommand.item.factory;

import dev.lotus.studio.command.subcommand.SubCommandFactory;
import dev.lotus.studio.item.CustomItemManager;

import java.util.Collection;

public class ItemSubCommandFactory {
    public static Collection<AbstractItemSubCommand> createAll(CustomItemManager itemManager) {
        return SubCommandFactory.createAll(ItemSubCommandType.class,
                type -> type.create(itemManager));
    }
}


