package dev.lotus.studio.database.playerdata;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class PlayerDataRepository {

    private final Dao<PlayerDataBase, String> playerDao;

    public PlayerDataRepository(com.j256.ormlite.support.ConnectionSource connectionSource) throws SQLException {
        this.playerDao = DaoManager.createDao(connectionSource, PlayerDataBase.class);
    }

    /**
     * Зберегти нового гравця
     */
    public void savePlayerData(PlayerDataBase playerDataBase) throws SQLException {
        playerDao.createIfNotExists(playerDataBase);
    }

    /**
     * Отримати дані гравця по імені
     */
    public PlayerDataBase getPlayerData(String playerName) throws SQLException {
        return playerDao.queryForId(playerName);
    }

    /**
     * Оновити дані гравця
     */
    public void updatePlayerData(PlayerDataBase playerDataBase) throws SQLException {
        playerDao.update(playerDataBase);
    }

    /**
     * Видалити дані гравця
     */
    public void deletePlayerData(String playerName) throws SQLException {
        playerDao.deleteById(playerName);
    }
}
