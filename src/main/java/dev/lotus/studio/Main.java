package dev.lotus.studio;

import dev.lotus.studio.database.DatabaseInitializer;
import dev.lotus.studio.database.playerdata.PlayerDataService;
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

public final class Main extends JavaPlugin {

    private static Main instance;
    private CustomItemManager itemManager;




    private PlayerDataService playerDataBase;
    private SafeZoneDataService safeZoneDataService;
    private DatabaseInitializer databaseInitializer;



    @Override
    public void onEnable() {

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


        new MainCommand("lotus", itemManager);

        SafeZoneManager.getInstance().initializeZones(safeZoneDataService);

    }

    @Override
    public void onDisable() {
       PlayerManager.getInstance().getGlobalTask().cancel();
       SafeZoneManager.getInstance().saveAllSafeZoneToDatabase();
        // Закриття DataBase
        if (databaseInitializer != null) {
            databaseInitializer.closeConnection();
        }
        getLogger().info("LotusOffSeason plugin disabled!");
        HandlerList.unregisterAll(this);
    }
    public PlayerDataService getPlayerDataBase() {
        return playerDataBase;
    }



    public static Main getInstance() {
        return instance;
    }


}
