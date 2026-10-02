package dao;

import model.Product;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Product entity.
 * 
 * The DAO pattern encapsulates all database interactions (CRUD operations),
 * separating the database logic from the application/business logic.
 *
 * Key Concepts:
 * 1. PreparedStatement:
 *    - Pre-compiles SQL queries on the database server.
 *    - Uses parameterized placeholders (?) to prevent SQL Injection attacks.
 * 2. try-with-resources:
 *    - Ensures that Connection, PreparedStatement, and ResultSet objects are
 *      automatically closed when the block finishes, preventing resource/memory leaks.
 * 3. ResultSet:
 *    - A cursor pointing to table rows returned by an executeQuery() statement.
 */
public class ProductDAO {

    /**
     * Initializes the DAO and ensures the underlying SQLite table exists.
     */
    public ProductDAO() {
        DatabaseConnection.initializeDatabase();
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The Product object to persist (without an ID)
     * @return true if insertion was successful, false otherwise
     */
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, category, price, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Parameter indices are 1-based in JDBC
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products, or an empty list if none found or error occurs
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, category, price, quantity FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set cursor row by row
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving products: " + e.getMessage());
        }

        return products;
    }

    /**
     * Retrieves a single product by its unique primary key ID.
     *
     * @param id The product ID to search for
     * @return The matching Product object, or null if not found
     */
    public Product getProductById(int id) {
        String sql = "SELECT id, name, category, price, quantity FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding product by ID: " + e.getMessage());
        }

        return null; // Product not found or error occurred
    }

    /**
     * Updates the details of an existing product in the database.
     *
     * @param product The Product object containing updated values and existing ID
     * @return true if the update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, category = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setInt(5, product.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a product from the database using its ID.
     *
     * @param id The ID of the product to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches products whose names contain the given keyword (case-insensitive).
     *
     * @param keyword Partial or full product name
     * @return List of matching products
     */
    public List<Product> searchByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, category, price, quantity FROM products WHERE LOWER(name) LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword.toLowerCase().trim() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error searching products by name: " + e.getMessage());
        }

        return products;
    }

    /**
     * Filters products by category name.
     *
     * @param category The category to filter by
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, category, price, quantity FROM products WHERE LOWER(category) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error filtering products by category: " + e.getMessage());
        }

        return products;
    }

    /**
     * Updates only the stock quantity for a specific product ID.
     *
     * @param id The product ID
     * @param newQuantity The updated quantity (must be >= 0)
     * @return true if updated successfully, false otherwise
     */
    public boolean updateStock(int id, int newQuantity) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating stock quantity: " + e.getMessage());
            return false;
        }
    }
}
