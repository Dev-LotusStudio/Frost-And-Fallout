package dev.lotus.studio.database.playerdata;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;

public class PlayerDataServiceImpl implements PlayerDataService {
    private final PlayerDataRepository playerDataRepository;

    public PlayerDataServiceImpl(ConnectionSource connectionSource) throws SQLException {
        this.playerDataRepository = new PlayerDataRepository(connectionSource);
    }

    @Override
    public void savePlayer(String playerName, double freezeValue, double radiationValue) {
        PlayerDataBase playerDataBase = new PlayerDataBase(playerName, freezeValue, radiationValue);
        try {
            playerDataRepository.savePlayerData(playerDataBase);
        } catch (SQLException e) {
            e.printStackTrace(); // або логування через plugin.getLogger()
        }
    }

    @Override
    public PlayerDataBase getPlayer(String playerName) {
        try {
            return playerDataRepository.getPlayerData(playerName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePlayer(String playerName, double freezeValue, double radiationValue) {
        PlayerDataBase playerDataBase = getPlayer(playerName);
        if (playerDataBase != null) {
            playerDataBase.setFreezeValue(freezeValue);
            playerDataBase.setRadiationValue(radiationValue);
            try {
                playerDataRepository.updatePlayerData(playerDataBase);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deletePlayer(String playerName) {
        try {
            playerDataRepository.deletePlayerData(playerName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, Double> getValues(String playerName) {
        PlayerDataBase playerDataBase = getPlayer(playerName);
        if (playerDataBase != null) {
            HashMap<String, Double> values = new HashMap<>();
            values.put("freezeValue", playerDataBase.getFreezeValue());
            values.put("radiationValue", playerDataBase.getRadiationValue());
            return values;
        }
        return new HashMap<>(); // краще ніж null
    }
}
