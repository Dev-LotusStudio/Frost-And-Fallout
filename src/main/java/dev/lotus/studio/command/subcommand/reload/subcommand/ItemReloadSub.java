package dev.lotus.studio.command.subcommand.reload.subcommand;

import dev.lotus.studio.Main;
import dev.lotus.studio.command.subcommand.reload.factory.AbstractReloadCommand;
import dev.lotus.studio.item.CustomItemManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class ItemReloadSub extends AbstractReloadCommand {

    public ItemReloadSub(CustomItemManager itemManager) {
        super(itemManager);
    }

    @Override
    public String getName() {
        return "item";
    }


    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0){
            try {
                itemManager.reloadItemConfig();
            } catch (Exception e){
                Main.getInstance().getLogger().log(Level.WARNING, "Failed to reload item config:" + e.getMessage());
            }
        } else {
            player.sendMessage(getFullUsage("lotus reload"));
        }
        return false;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
