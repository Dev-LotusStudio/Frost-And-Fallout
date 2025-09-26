package dev.lotus.studio.command.subcommand.safezone.factory;

import dev.lotus.studio.command.subcommand.safezone.subcommand.*;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.safezone.SafeZoneManager;

public enum SafeZoneSubCommandType {
    POS1(SafeZonePos1Command.class),
    POS2(SafeZonePos2Command.class),
    SAVE(SafeZoneSaveCommand.class),
    LIST(SafeZoneListCommand.class),
    REMOVE(SafeZoneRemoveCommand.class);

    private final Class<? extends AbstractSafeZoneSubCommand> clazz;

    SafeZoneSubCommandType(Class<? extends AbstractSafeZoneSubCommand> clazz) {
        this.clazz = clazz;
    }

    public AbstractSafeZoneSubCommand create(SafeZoneManager manager) {
        try {
            var constructor = clazz.getDeclaredConstructor(SafeZoneManager.class);
            constructor.setAccessible(true); // робимо доступним навіть якщо protected/package-private
            return constructor.newInstance(manager);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать команду: " + clazz.getSimpleName(), e);
        }
    }
}
