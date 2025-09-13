package dev.lotus.studio;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lotus.studio.database.hibernate.playerdata.PlayerDataServiceImpl;
import dev.lotus.studio.database.hibernate.savezone.SaveZoneDataService;
import dev.lotus.studio.database.hibernate.savezone.SaveZoneDataServiceImpl;
import dev.lotus.studio.event.EatEvent;
import dev.lotus.studio.event.JoinLeaveEvent;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.command.MainCommand;
import dev.lotus.studio.database.hibernate.HibernateUtil;
import dev.lotus.studio.event.ArmorEvent;
import dev.lotus.studio.playerdata.PlayerBar;
import dev.lotus.studio.playerdata.PlayerManager;
import dev.lotus.studio.safezone.SafeZoneManager;

public final class Main extends JavaPlugin {

    private static Main instance;
    private CustomItemManager itemManager;




    private PlayerDataServiceImpl playerDataBase;
    private SaveZoneDataService saveZoneDataService;




    @Override
    public void onEnable() {

        instance = this;
        PlayerManager.getInstance().startGlobalTask();
        //cfg
        itemManager = new CustomItemManager();
        this.playerDataBase = new PlayerDataServiceImpl();
        this.saveZoneDataService = new SaveZoneDataServiceImpl();

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
        // Закриття SessionFactory Hibernate при вимкненні плагіна
        if (HibernateUtil.getSessionFactory() != null) {
            HibernateUtil.getSessionFactory().close();
        }
        getLogger().info("LotusOffSeason plugin disabled!");
        HandlerList.unregisterAll(this);
    }
    public PlayerDataServiceImpl getPlayerDataBase() {
        return playerDataBase;
    }



    public static Main getInstance() {
        return instance;
    }


}
