package dev.lotus.studio.command.subcommand.item.factory;

import dev.lotus.studio.item.CustomItemManager;

import java.util.Arrays;
import java.util.Collection;

public class ItemSubCommandFactory {
    public static Collection<AbstractItemSubCommand> createAll(CustomItemManager itemManager) {
        return Arrays.stream(ItemSubCommandType.values())
                .map(type -> type.create(itemManager))
                .toList();
    }
}

