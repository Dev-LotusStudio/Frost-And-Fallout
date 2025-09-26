package dev.lotus.studio.database.savezone;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "safe_zones")
public class SafeZoneDataBase {

    @DatabaseField(generatedId = true, columnName = "safe_zone_id")
    private int safeZoneId;

    @DatabaseField(columnName = "safezone_name", canBeNull = false)
    private String safeZoneName;

    @DatabaseField(columnName = "location_value", canBeNull = false)
    private String locationValue;

    public SafeZoneDataBase() {
    }

    public SafeZoneDataBase(String safeZoneName, String locationValue) {
        this.safeZoneName = safeZoneName;
        this.locationValue = locationValue;
    }

    public int getSafeZoneId() {
        return safeZoneId;
    }

    public String getSafeZoneName() {
        return safeZoneName;
    }

    public void setSafeZoneName(String safeZoneName) {
        this.safeZoneName = safeZoneName;
    }

    public String getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }
}
