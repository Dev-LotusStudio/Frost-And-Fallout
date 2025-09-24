package dev.lotus.studio.command.subcommand.safezone.factory;


import dev.lotus.studio.command.subcommand.SubCommand;
import dev.lotus.studio.safezone.SafeZoneManager;

public abstract class AbstractSafeZoneSubCommand implements SubCommand {
    protected final SafeZoneManager manager;

    protected AbstractSafeZoneSubCommand(SafeZoneManager manager) {
        this.manager = manager;
    }
}

