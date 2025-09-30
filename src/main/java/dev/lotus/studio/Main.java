package dev.lotus.studio;

import dev.lotus.studio.database.DatabaseInitializer;
import dev.lotus.studio.database.playerdata.PlayerDataService;
import dev.lotus.studio.safezone.SafeZonePreloaded;
import dev.lotus.studio.utils.metric.Metrics;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lotus.studio.database.savezone.SafeZoneDataService;
import dev.lotus.studio.event.EatEvent;
import dev.lotus.studio.event.JoinLeaveEvent;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.command.MainCommand;
import dev.lotus.studio.event.ArmorEvent;
import dev.lotus.studio.playerdata.PlayerBar;
import dev.lotus.studio.playerdata.PlayerManager;
import dev.lotus.studio.safezone.SafeZoneManager;

import java.util.Optional;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main instance;
    private CustomItemManager itemManager;

    private PlayerDataService playerDataBase;
    private SafeZoneDataService safeZoneDataService;
    private DatabaseInitializer databaseInitializer;

    @Override
    public void onEnable() {
        metric();
        if (initialize()){
            getLogger().log(Level.INFO, "Plugin has been enabled!");
        }
    }


    public boolean initialize() {
        try {
            instance = this;
            PlayerManager.getInstance().startGlobalTask();
            //cfg
            itemManager = new CustomItemManager();
            databaseInitializer = new DatabaseInitializer(this);
            playerDataBase = databaseInitializer.getPlayerDataBase();
            safeZoneDataService = databaseInitializer.getSaveZoneDataService();



            itemManager.loadItems();
            getServer().getPluginManager().registerEvents(new ArmorEvent(itemManager),this);
            getServer().getPluginManager().registerEvents(new EatEvent(itemManager),this);
            getServer().getPluginManager().registerEvents(new JoinLeaveEvent(playerDataBase),this);
            getLogger().info("Предметы загружены из items.yml.");

            new PlayerBar(this,itemManager);


            new MainCommand("lotus", itemManager, SafeZoneManager.getInstance());

            SafeZoneManager.getInstance().initialize(safeZoneDataService);
        } catch (Exception e) {
            getLogger().severe("Exeption Initialize plugin:  " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
       PlayerManager.getInstance().getGlobalTask().cancel();
        Optional.ofNullable(SafeZoneManager.getInstance().getPreloaded())
                .ifPresent(SafeZonePreloaded::stopTask);

        // Закриття DataBase
        if (databaseInitializer != null) {
            databaseInitializer.closeConnection();
        }
        getLogger().info("Frost and Fallout plugin disabled!");
        HandlerList.unregisterAll(this);
    }
    private void metric(){
        int pluginId = 27372;
        Metrics metrics = new Metrics(this, pluginId);
    }


    public PlayerDataService getPlayerDataBase() {
        return playerDataBase;
    }



    public static Main getInstance() {
        return instance;
    }


}
