package dev.lotus.studio.command.subcommand.safezone.factory;


import dev.lotus.studio.safezone.SafeZoneManager;

import java.util.Arrays;
import java.util.Collection;

public class SafeZoneSubCommandFactory {
    public static Collection<AbstractSafeZoneSubCommand> createAll(SafeZoneManager manager) {
        return Arrays.stream(SafeZoneSubCommandType.values())
                .map(type -> type.create(manager))
                .toList();
    }
}

