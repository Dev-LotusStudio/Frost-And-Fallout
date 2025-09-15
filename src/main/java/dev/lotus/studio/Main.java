package dev.lotus.studio;

import com.j256.ormlite.support.ConnectionSource;
import dev.lotus.studio.database.DatabaseInitializer;
import dev.lotus.studio.database.playerdata.PlayerDataService;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lotus.studio.database.playerdata.PlayerDataServiceImpl;
import dev.lotus.studio.database.savezone.SaveZoneDataService;
import dev.lotus.studio.database.savezone.SaveZoneDataServiceImpl;
import dev.lotus.studio.event.EatEvent;
import dev.lotus.studio.event.JoinLeaveEvent;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.command.MainCommand;
import dev.lotus.studio.event.ArmorEvent;
import dev.lotus.studio.playerdata.PlayerBar;
import dev.lotus.studio.playerdata.PlayerManager;
import dev.lotus.studio.safezone.SafeZoneManager;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private static Main instance;
    private CustomItemManager itemManager;




    private PlayerDataService playerDataBase;
    private SaveZoneDataService saveZoneDataService;
    private DatabaseInitializer databaseInitializer;



    @Override
    public void onEnable() {

        instance = this;
        PlayerManager.getInstance().startGlobalTask();
        //cfg
        itemManager = new CustomItemManager();
        databaseInitializer = new DatabaseInitializer(this);
        playerDataBase = databaseInitializer.getPlayerDataBase();
        saveZoneDataService = databaseInitializer.getSaveZoneDataService();



        itemManager.loadItems();
        getServer().getPluginManager().registerEvents(new ArmorEvent(itemManager),this);
        getServer().getPluginManager().registerEvents(new EatEvent(itemManager),this);
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(playerDataBase),this);
        getLogger().info("Предметы загружены из items.yml.");

        new PlayerBar(this,itemManager);


        new MainCommand("lotus", itemManager,saveZoneDataService);

        SafeZoneManager.getInstance().initializeZones(saveZoneDataService);

    }

    @Override
    public void onDisable() {
       PlayerManager.getInstance().getGlobalTask().cancel();
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
