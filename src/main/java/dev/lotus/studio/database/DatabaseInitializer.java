package dev.lotus.studio.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.lotus.studio.database.playerdata.PlayerDataBase;
import dev.lotus.studio.database.savezone.SafeZoneDataBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseInitializer {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final String databasePath;
    private ConnectionSource connectionSource;

    public DatabaseInitializer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.databasePath = plugin.getDataFolder().getAbsolutePath() + File.separator + "database.db";
        try {
            createDatabaseFile();
            createTables();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Створюємо файл бази даних якщо його ще нема
     */
    public void createDatabaseFile() throws IOException {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        File databaseFile = new File(databasePath);
        if (!databaseFile.exists()) {
            if (databaseFile.createNewFile()) {
                logger.info("Створено новий файл бази даних: " + databasePath);
            }
        }
    }

    /**
     * Повертає ORMLite ConnectionSource
     */
    public ConnectionSource openConnection() throws SQLException {
        if (connectionSource == null) {
            String url = "jdbc:sqlite:" + databasePath;
            connectionSource = new JdbcConnectionSource(url);

            logger.info("З’єднання з базою даних встановлено (ORMLite)");
        }
        return connectionSource;
    }

    /**
     * Створюємо таблиці через ORMLite
     */
    public void createTables() throws SQLException {
        ConnectionSource cs = openConnection();

        TableUtils.createTableIfNotExists(cs, PlayerDataBase.class);
        TableUtils.createTableIfNotExists(cs, SafeZoneDataBase.class);

        logger.info("Таблиці ORMLite створені/перевірені");
    }

    /**
     * Закриття з’єднання
     */
    public void closeConnection() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
                logger.info("З’єднання з базою даних закрито");
            } catch (Exception e) {
                logger.warning("Помилка закриття з’єднання: " + e.getMessage());
            }
        }
    }
}
