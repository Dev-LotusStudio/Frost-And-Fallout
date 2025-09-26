package dev.lotus.studio.command.subcommand.safezone.factory;


import dev.lotus.studio.command.subcommand.SubCommandFactory;
import dev.lotus.studio.safezone.SafeZoneManager;

import java.util.Collection;

public class SafeZoneSubCommandFactory {
    public static Collection<AbstractSafeZoneSubCommand> createAll(SafeZoneManager manager) {
        return SubCommandFactory.createAll(SafeZoneSubCommandType.class,
                type -> type.create(manager));
    }
}


