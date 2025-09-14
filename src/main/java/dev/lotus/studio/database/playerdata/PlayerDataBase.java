package dev.lotus.studio.database.playerdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "player_data")
public class PlayerDataBase {

    @DatabaseField(id = true, columnName = "player_name")
    private String playerName;

    @DatabaseField(columnName = "freeze_value", defaultValue = "30")
    private double freezeValue;

    @DatabaseField(columnName = "radiation_value", defaultValue = "0")
    private double radiationValue;

    public PlayerDataBase() {
    }

    public PlayerDataBase(String playerName, double freezeValue, double radiationValue) {
        this.playerName = playerName;
        this.freezeValue = freezeValue;
        this.radiationValue = radiationValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getFreezeValue() {
        return freezeValue;
    }

    public void setFreezeValue(double freezeValue) {
        this.freezeValue = freezeValue;
    }

    public double getRadiationValue() {
        return radiationValue;
    }

    public void setRadiationValue(double radiationValue) {
        this.radiationValue = radiationValue;
    }
}
