package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Diagnostic utility to verify the JDBC connection to SQLite
 * and ensure automatic database initialization works properly.
 */
public class ConnectionTest {

    public static void main(String[] args) {
        System.out.println("Initializing SQLite database...");

        // Automatically creates 'inventory.db' and the 'products' table if not present
        DatabaseConnection.initializeDatabase();

        System.out.println("Attempting to connect to SQLite via JDBC...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("==================================================");
                System.out.println("Connection SUCCESSFUL!");
                System.out.println("Database Product : " + metaData.getDatabaseProductName());
                System.out.println("Database Version : " + metaData.getDatabaseProductVersion());
                System.out.println("Driver Name      : " + metaData.getDriverName());
                System.out.println("Driver Version   : " + metaData.getDriverVersion());
                System.out.println("SQLite Database  : inventory.db created & ready!");
                System.out.println("==================================================");
            }
        } catch (SQLException e) {
            System.err.println("==================================================");
            System.err.println("Connection FAILED!");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("==================================================");
        }
    }
}
