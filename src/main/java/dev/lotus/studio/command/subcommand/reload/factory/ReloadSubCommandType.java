package dev.lotus.studio.command.subcommand.reload.factory;


import dev.lotus.studio.command.subcommand.reload.subcommand.AllReloadSub;
import dev.lotus.studio.command.subcommand.reload.subcommand.ItemReloadSub;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.safezone.SafeZoneManager;

public enum ReloadSubCommandType {
    ITEMS(ItemReloadSub.class),
    ALL(AllReloadSub.class);

    private final Class<? extends AbstractReloadCommand> clazz;

    ReloadSubCommandType(Class<? extends AbstractReloadCommand> clazz) {
        this.clazz = clazz;
    }

    public AbstractReloadCommand create(SafeZoneManager manager, CustomItemManager itemManager) {
        try {
            var constructor = clazz.getDeclaredConstructor(CustomItemManager.class, SafeZoneManager.class);
            constructor.setAccessible(true);
            return constructor.newInstance(itemManager, manager);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать команду: " + clazz.getSimpleName(), e);
        }
    }
}
