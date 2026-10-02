import dao.ProductDAO;
import model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class providing a console-based User Interface (CLI)
 * for the Inventory Management System.
 *
 * Demonstrates:
 * 1. Layered Architecture: UI delegates all data operations to ProductDAO.
 * 2. Input Validation: Robust Scanner reading avoiding buffer and type errors.
 * 3. Java Collections & Sorting: Demonstrates Comparator and in-memory sorting.
 */
public class Main {

    private static final ProductDAO productDAO = new ProductDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Welcome to the Inventory Management System ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice (1-9): ", 1, 9);

            switch (choice) {
                case 1 -> handleAddProduct();
                case 2 -> handleViewAllProducts();
                case 3 -> handleSearchProduct();
                case 4 -> handleUpdateProduct();
                case 5 -> handleDeleteProduct();
                case 6 -> handleUpdateStock();
                case 7 -> handleFilterByCategory();
                case 8 -> handleSortProducts();
                case 9 -> {
                    System.out.println("\nThank you for using Inventory Management System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }

        scanner.close();
    }

    // =========================================================================
    // Menu & UI Display
    // =========================================================================

    private static void printMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("                    MAIN MENU                     ");
        System.out.println("--------------------------------------------------");
        System.out.println(" 1. Add New Product");
        System.out.println(" 2. View All Products");
        System.out.println(" 3. Search Product (by ID or Name)");
        System.out.println(" 4. Update Product Details");
        System.out.println(" 5. Delete Product");
        System.out.println(" 6. Update Stock Quantity");
        System.out.println(" 7. Filter Products by Category");
        System.out.println(" 8. Sort Products (Price, Quantity, Name)");
        System.out.println(" 9. Exit");
        System.out.println("--------------------------------------------------");
    }

    // =========================================================================
    // Menu Handlers
    // =========================================================================

    /** Option 1: Add a new product */
    private static void handleAddProduct() {
        System.out.println("\n--- [ Add New Product ] ---");
        String name = readNonEmptyString("Enter Product Name: ");
        String category = readNonEmptyString("Enter Category: ");
        double price = readDouble("Enter Price ($): ", 0.0);
        int quantity = readInt("Enter Initial Quantity: ", 0, Integer.MAX_VALUE);

        Product product = new Product(name, category, price, quantity);
        boolean success = productDAO.addProduct(product);

        if (success) {
            System.out.println(">> SUCCESS: Product added successfully!");
        } else {
            System.err.println(">> ERROR: Failed to add product.");
        }
    }

    /** Option 2: View all products in tabular format */
    private static void handleViewAllProducts() {
        System.out.println("\n--- [ All Inventory Products ] ---");
        List<Product> products = productDAO.getAllProducts();
        displayProductTable(products);
    }

    /** Option 3: Search products by ID or Name keyword */
    private static void handleSearchProduct() {
        System.out.println("\n--- [ Search Product ] ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name Keyword");
        int subChoice = readInt("Choose search type (1-2): ", 1, 2);

        if (subChoice == 1) {
            int id = readInt("Enter Product ID: ", 1, Integer.MAX_VALUE);
            Product product = productDAO.getProductById(id);
            if (product != null) {
                displayProductTable(List.of(product));
            } else {
                System.out.println(">> No product found with ID: " + id);
            }
        } else {
            String keyword = readNonEmptyString("Enter name keyword to search: ");
            List<Product> matches = productDAO.searchByName(keyword);
            displayProductTable(matches);
        }
    }

    /** Option 4: Update product details */
    private static void handleUpdateProduct() {
        System.out.println("\n--- [ Update Product Details ] ---");
        int id = readInt("Enter ID of product to update: ", 1, Integer.MAX_VALUE);

        Product existing = productDAO.getProductById(id);
        if (existing == null) {
            System.out.println(">> ERROR: Product with ID " + id + " does not exist.");
            return;
        }

        System.out.println("Current Details: " + existing);
        System.out.println("Leave blank to keep existing value, or enter new value:");

        System.out.print("New Name (" + existing.getName() + "): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            existing.setName(name);
        }

        System.out.print("New Category (" + existing.getCategory() + "): ");
        String category = scanner.nextLine().trim();
        if (!category.isEmpty()) {
            existing.setCategory(category);
        }

        System.out.print("New Price ($" + String.format("%.2f", existing.getPrice()) + "): ");
        String priceInput = scanner.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                double price = Double.parseDouble(priceInput);
                if (price >= 0) existing.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format. Keeping current price.");
            }
        }

        System.out.print("New Quantity (" + existing.getQuantity() + "): ");
        String qtyInput = scanner.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyInput);
                if (qty >= 0) existing.setQuantity(qty);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity format. Keeping current quantity.");
            }
        }

        boolean updated = productDAO.updateProduct(existing);
        if (updated) {
            System.out.println(">> SUCCESS: Product updated successfully!");
        } else {
            System.err.println(">> ERROR: Failed to update product.");
        }
    }

    /** Option 5: Delete a product */
    private static void handleDeleteProduct() {
        System.out.println("\n--- [ Delete Product ] ---");
        int id = readInt("Enter ID of product to delete: ", 1, Integer.MAX_VALUE);

        Product existing = productDAO.getProductById(id);
        if (existing == null) {
            System.out.println(">> ERROR: Product with ID " + id + " does not exist.");
            return;
        }

        System.out.println("Product to delete: " + existing);
        System.out.print("Are you sure you want to delete this product? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            boolean deleted = productDAO.deleteProduct(id);
            if (deleted) {
                System.out.println(">> SUCCESS: Product deleted successfully!");
            } else {
                System.err.println(">> ERROR: Failed to delete product.");
            }
        } else {
            System.out.println(">> Deletion cancelled.");
        }
    }

    /** Option 6: Quick stock update */
    private static void handleUpdateStock() {
        System.out.println("\n--- [ Update Stock Quantity ] ---");
        int id = readInt("Enter Product ID: ", 1, Integer.MAX_VALUE);

        Product existing = productDAO.getProductById(id);
        if (existing == null) {
            System.out.println(">> ERROR: Product with ID " + id + " does not exist.");
            return;
        }

        System.out.printf("Product: %s | Current Stock: %d units\n", existing.getName(), existing.getQuantity());
        int newQuantity = readInt("Enter New Stock Quantity: ", 0, Integer.MAX_VALUE);

        boolean updated = productDAO.updateStock(id, newQuantity);
        if (updated) {
            System.out.printf(">> SUCCESS: Stock for '%s' updated to %d units!\n", existing.getName(), newQuantity);
        } else {
            System.err.println(">> ERROR: Failed to update stock.");
        }
    }

    /** Option 7: Filter products by category */
    private static void handleFilterByCategory() {
        System.out.println("\n--- [ Filter by Category ] ---");
        String category = readNonEmptyString("Enter category name: ");
        List<Product> products = productDAO.getProductsByCategory(category);
        displayProductTable(products);
    }

    /** Option 8: Sort products using Java Collections and Comparators */
    private static void handleSortProducts() {
        System.out.println("\n--- [ Sort Products ] ---");
        List<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        System.out.println("1. Sort by Price (Low to High)");
        System.out.println("2. Sort by Price (High to Low)");
        System.out.println("3. Sort by Quantity (Low to High)");
        System.out.println("4. Sort by Quantity (High to Low)");
        System.out.println("5. Sort by Name (A-Z)");
        int sortChoice = readInt("Choose sorting option (1-5): ", 1, 5);

        // Demonstrating Java Collections Framework and Lambda / Method Reference Comparators
        switch (sortChoice) {
            case 1 -> products.sort(Comparator.comparingDouble(Product::getPrice));
            case 2 -> products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
            case 3 -> products.sort(Comparator.comparingInt(Product::getQuantity));
            case 4 -> products.sort(Comparator.comparingInt(Product::getQuantity).reversed());
            case 5 -> products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        }

        displayProductTable(products);
    }

    // =========================================================================
    // Table Output Formatting Helper
    // =========================================================================

    /**
     * Prints a formatted ASCII table of products.
     * Shows a warning flag [LOW STOCK] if quantity < 5.
     */
    private static void displayProductTable(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println(">> No products found.");
            return;
        }

        String border = "+------+--------------------------------+----------------------+------------+----------+------------+";
        System.out.println(border);
        System.out.printf("| %-4s | %-30s | %-20s | %-10s | %-8s | %-10s |\n",
                "ID", "Name", "Category", "Price", "Quantity", "Status");
        System.out.println(border);

        for (Product p : products) {
            String status = p.getQuantity() == 0 ? "OUT OF STOCK" :
                            p.getQuantity() < 5  ? "LOW STOCK" : "IN STOCK";

            System.out.printf("| %-4d | %-30s | %-20s | $%-9.2f | %-8d | %-10s |\n",
                    p.getId(),
                    truncate(p.getName(), 30),
                    truncate(p.getCategory(), 20),
                    p.getPrice(),
                    p.getQuantity(),
                    status);
        }

        System.out.println(border);
        System.out.printf("Total Products: %d\n", products.size());
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    // =========================================================================
    // Safe Scanner Input Validation Helpers
    // =========================================================================

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min) {
                    return value;
                }
                System.out.printf("Value must be at least %.2f.\n", min);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number (e.g., 19.99).");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Field cannot be empty. Please enter a value.");
        }
    }
}
