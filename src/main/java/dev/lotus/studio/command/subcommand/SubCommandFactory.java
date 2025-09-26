package dev.lotus.studio.command.subcommand;


import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class SubCommandFactory {
    public static <E extends Enum<E>, C> Collection<C> createAll(
            Class<E> enumClass,
            Function<E, C> creator
    ) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(creator)
                .toList();
    }
}

