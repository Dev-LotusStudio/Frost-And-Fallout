package dev.lotus.studio.command.subcommand.reload.subcommand;

import dev.lotus.studio.Main;
import dev.lotus.studio.command.subcommand.reload.factory.AbstractReloadCommand;
import dev.lotus.studio.item.CustomItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class AllReloadSub extends AbstractReloadCommand {
    public AllReloadSub(CustomItemManager itemManager) {
        super(itemManager);
    }

    @Override
    public String getName() {
        return "all";
    }


    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length == 0){
            try {
                Optional.ofNullable(Main.getInstance()).ifPresent(Main::initialize);
                player.sendMessage(Component.text("Reload successful!").color(TextColor.color(0x980B)));
            } catch (IllegalStateException e) {
                player.sendMessage(Component.text(e.getMessage()).color(TextColor.color(0x981A00)));
                System.out.println(e.getMessage());
            }
            return true;
        } else player.sendMessage(getFullUsage("faf reload"));
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
