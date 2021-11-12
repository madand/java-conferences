package net.madand.conferences.test;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbHelper {
    private static final String DB_PROPERTIES_FILE = "test_db.properties";

    private final Properties properties = new Properties();
    private final Connection connection;

    private static DbHelper instance;

    private DbHelper() throws IOException, SQLException {
        properties.load(getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE));
        connection = DriverManager.getConnection(
                properties.getProperty("connection_url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
        connection.setAutoCommit(true);
    }

    public static DbHelper getInstance() throws IOException, SQLException {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void recreateDbTables() throws IOException, SQLException {
        Reader reader = Resources.getResourceAsReader(properties.getProperty("recreate_tables_script"));
        connection.setAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
        connection.setAutoCommit(true);
    }
}
