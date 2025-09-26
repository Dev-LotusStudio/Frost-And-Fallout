package dev.lotus.studio.command.subcommand.item.factory;

import dev.lotus.studio.command.subcommand.item.subcommand.ArmorCommand;
import dev.lotus.studio.command.subcommand.item.subcommand.EatCommand;
import dev.lotus.studio.command.subcommand.item.subcommand.ViewCommand;
import dev.lotus.studio.item.CustomItemManager;



public enum ItemSubCommandType {
    ARMOR(ArmorCommand.class),
    VIEW(ViewCommand.class),
    EAT(EatCommand.class);

    private final Class<? extends AbstractItemSubCommand> clazz;

    ItemSubCommandType(Class<? extends AbstractItemSubCommand> clazz) {
        this.clazz = clazz;
    }

    public AbstractItemSubCommand create(CustomItemManager itemManager) {
        try {
            var constructor = clazz.getDeclaredConstructor(CustomItemManager.class);
            constructor.setAccessible(true); // робимо доступним навіть якщо protected/package-private
            return constructor.newInstance(itemManager);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать команду: " + clazz.getSimpleName(), e);
        }
    }
}


