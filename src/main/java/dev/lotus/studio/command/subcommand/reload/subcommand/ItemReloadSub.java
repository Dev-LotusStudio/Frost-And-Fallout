package dev.lotus.studio.command.subcommand.reload.subcommand;

import dev.lotus.studio.command.subcommand.reload.factory.AbstractReloadCommand;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.safezone.SafeZoneManager;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemReloadSub extends AbstractReloadCommand {
    /**
     * TODO: Прибрати @param safeZoneManager повныстю з архітектури релоаду!
     * @param itemManager
     * @param safeZoneManager
     */
    public ItemReloadSub(CustomItemManager itemManager , SafeZoneManager safeZoneManager) {
        super(itemManager, safeZoneManager);
    }

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public String getDescription() {
        return "items reload";
    }

    @Override
    public String getUsage() {
        return "reload all items";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0){
            try {
                itemManager.reloadItemConfig();
            } catch (Exception ignored){

            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
