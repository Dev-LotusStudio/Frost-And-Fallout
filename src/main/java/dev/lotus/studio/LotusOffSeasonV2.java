package dev.lotus.studio;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lotus.studio.database.hibernate.playerdata.PlayerDataServiceImpl;
import dev.lotus.studio.database.hibernate.savezone.SaveZoneDataService;
import dev.lotus.studio.database.hibernate.savezone.SaveZoneDataServiceImpl;
import dev.lotus.studio.database.hibernate.structures.StructureDataService;
import dev.lotus.studio.database.hibernate.structures.StructureDataServiceImpl;
import dev.lotus.studio.event.EatEvent;
import dev.lotus.studio.event.JoinLeaveEvent;
import dev.lotus.studio.item.CustomItemManager;
import dev.lotus.studio.command.MainCommand;
import dev.lotus.studio.database.hibernate.HibernateUtil;
import dev.lotus.studio.event.ArmorEvent;
import dev.lotus.studio.playerdata.PlayerBar;
import dev.lotus.studio.playerdata.PlayerManager;
import dev.lotus.studio.safezone.SafeZoneManager;
import dev.lotus.studio.trader.hoarder.HoarderConfig;
import dev.lotus.studio.trader.hoarder.HolderManager;

public final class LotusOffSeasonV2 extends JavaPlugin {

    private static LotusOffSeasonV2 instance;
    private CustomItemManager itemManager;


    private HoarderConfig horderConfig;

    private HolderManager holderManager;

    private PlayerDataServiceImpl playerDataBase;
    private SaveZoneDataService saveZoneDataService;


    private StructureDataService structureDataService;

    @Override
    public void onEnable() {

        instance = this;
        PlayerManager.getInstance().startGlobalTask();
        // config
        itemManager = new CustomItemManager();
        this.playerDataBase = new PlayerDataServiceImpl();
        this.saveZoneDataService = new SaveZoneDataServiceImpl();

        itemManager.loadItems();
        getServer().getPluginManager().registerEvents(new ArmorEvent(itemManager), this);
        getServer().getPluginManager().registerEvents(new EatEvent(itemManager), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(playerDataBase), this);
        getLogger().info("Предметы загружены из items.yml.");

        new PlayerBar(this, itemManager);


        horderConfig = new HoarderConfig();
        holderManager = new HolderManager(horderConfig);
        holderManager.loadHoldersFromFile();

        new MainCommand("lotus", itemManager, holderManager, saveZoneDataService);

        SafeZoneManager.getInstance().initializeZones(saveZoneDataService);

        //test
        structureDataService = new StructureDataServiceImpl();
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

    public HoarderConfig getHorderConfig() {
        return horderConfig;
    }

    public static LotusOffSeasonV2 getInstance() {
        return instance;
    }

    public HolderManager getHolderManager() {
        return holderManager;
    }
}
