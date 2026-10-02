package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Utility class to manage JDBC database connections for SQLite.
 *
 * Key Concepts:
 * 1. Embedded SQLite Database:
 *    - Connects directly to a local file ('inventory.db') without needing
 *      a background database server process, ports, or credentials.
 * 2. Externalized Configuration:
 *    - The connection URL is loaded from 'db.properties' to avoid hardcoded values.
 * 3. Automatic Schema Initialization:
 *    - initializeDatabase() creates the 'products' table if it does not already exist,
 *      ensuring zero manual database setup for the user.
 */
public class DatabaseConnection {

    private static final Properties properties = new Properties();

    // Static block runs once when the class is first loaded into JVM memory
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Warning: Unable to find db.properties in resources classpath. Using default SQLite URL.");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading db.properties: " + e.getMessage());
        }
    }

    // Private constructor prevents external instantiation
    private DatabaseConnection() {
    }

    /**
     * Creates and returns a new database connection to SQLite.
     *
     * @return a live Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url", "jdbc:sqlite:inventory.db");
        return DriverManager.getConnection(url);
    }

    /**
     * Initializes the SQLite database by creating the products table if it does not exist.
     * This eliminates any need for manual database setup or configuration.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "name TEXT NOT NULL, " +
                     "category TEXT NOT NULL, " +
                     "price DECIMAL(10, 2) NOT NULL, " +
                     "quantity INTEGER NOT NULL DEFAULT 0, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                     ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        }
    }
}
