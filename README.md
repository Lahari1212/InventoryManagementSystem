# Inventory Management System

A clean, console-based **Inventory Management System** built with **Java 17**, **Object-Oriented Programming (OOP)**, **Maven**, **JDBC**, and **SQLite**.

The application provides complete inventory tracking, allowing users to add, view, search, update, delete, filter, and sort products through an interactive terminal interface backed by persistent relational storage.

---

## Project Overview

This project is a Java console application that uses **SQLite** and **JDBC** to manage product stock and inventory records. 

It is designed with a clear separation of concerns across layered architecture:
* **Model Layer:** Encapsulates the domain entity (`Product`).
* **Data Access Layer (DAO):** Manages all database interactions using SQL and JDBC (`ProductDAO`).
* **Utility Layer:** Handles connection management and automatic schema creation (`DatabaseConnection`).
* **Presentation Layer:** Provides the CLI menu, formatted tables, and user input validation (`Main`).

---

## Features

* **Add New Products:** Record new items with name, category, price, and initial stock quantity.
* **View All Products:** Display all inventory items in a formatted, aligned ASCII table with stock status indicators (`IN STOCK`, `LOW STOCK`, `OUT OF STOCK`).
* **Search Products:** Search for specific products by exact ID or case-insensitive name keywords.
* **Update Product Details:** Modify an existing product's name, category, price, or quantity, with the option to keep existing values.
* **Delete Products:** Remove products from the database by ID with safety confirmation prompts (`yes/no`).
* **Update Stock Quantity:** Quickly adjust available stock levels for any product.
* **Filter by Category:** Filter and list items belonging to a specific department or category.
* **Sort Products:** Sort items dynamically in memory using Java Collections and Comparators:
  * Price (Low to High / High to Low)
  * Quantity (Low to High / High to Low)
  * Name (Alphabetical A–Z)
* **Input Validation:** Defensive terminal input handling that prevents crashes from invalid numbers, empty text fields, and `Scanner` buffer issues.
* **Exception Handling:** Robust SQL and runtime exception handling using `try-with-resources`.
* **Automatic Database Initialization:** The SQLite database file and required tables are created automatically on application startup.

---

## Technologies Used

* **Java 17** (LTS)
* **Apache Maven** (Build and dependency management)
* **JDBC** (`sqlite-jdbc` driver)
* **SQLite** (Embedded, serverless relational database)
* **Java Collections Framework** (`List`, `ArrayList`, `Comparator`)
* **Object-Oriented Programming (OOP)**
* **PreparedStatement** (Parameterized SQL queries for security and performance)

---

## Project Structure

```text
InventoryManagementSystem/
├── src/
│   └── main/
│       ├── java/
│       │   ├── model/
│       │   │   └── Product.java              # Domain model class representing a product
│       │   ├── dao/
│       │   │   └── ProductDAO.java          # Data Access Object handling CRUD SQL queries
│       │   ├── util/
│       │   │   ├── DatabaseConnection.java  # JDBC connection and auto-initialization utility
│       │   │   └── ConnectionTest.java      # Standalone connection diagnostic tool
│       │   └── Main.java                    # Console CLI application and menu loop
│       └── resources/
│           ├── db.properties                # Local SQLite connection configuration
│           └── db.properties.example        # Configuration template
├── database/
│   └── schema.sql                           # SQLite database schema definition and seed data
├── pom.xml                                  # Maven project dependencies and build configuration
├── .gitignore                               # Git ignore configuration
└── README.md                                # Project documentation
```

---

## Database

* **Database Engine:** **SQLite** is used as the relational database engine.
* **Zero Configuration:** SQLite is an embedded, serverless database. No background Windows services, local servers (like MySQL), or network ports need to be configured.
* **Automatic Initialization:** When the application runs, `DatabaseConnection.initializeDatabase()` automatically generates the local database file (`inventory.db`) and executes the `CREATE TABLE IF NOT EXISTS` statement.
* **JDBC Connectivity:** Java communicates directly with the local database file through the official SQLite JDBC driver using `DriverManager.getConnection("jdbc:sqlite:inventory.db")`.
* **Version Control Protection:** The local database file (`inventory.db` and `*.db`) and the active configuration file (`db.properties`) are added to `.gitignore` to keep binary files and local settings out of GitHub.

### `products` Table Schema

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Auto-generated unique identifier |
| `name` | `TEXT` | `NOT NULL` | Name of the product |
| `category` | `TEXT` | `NOT NULL` | Product category classification |
| `price` | `DECIMAL(10, 2)` | `NOT NULL` | Unit price |
| `quantity` | `INTEGER` | `NOT NULL DEFAULT 0` | Available stock count |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Record creation timestamp |

---

## OOP and Java Concepts Demonstrated

* **Classes and Objects:** Real-world modeling of product entities with state and behavior.
* **Encapsulation:** All fields in `Product.java` are `private`, with access controlled through public getters and setters.
* **Constructors:** Constructor overloading to handle new products (pre-database ID) and existing rows (with ID assigned by SQLite).
* **Getters and Setters:** Controlled access and updates to object attributes.
* **DAO Pattern:** Clear separation of concerns by encapsulating all database queries inside `ProductDAO.java`, keeping SQL out of the UI layer.
* **PreparedStatement:** Parameterized queries with `?` placeholders preventing SQL injection vulnerabilities.
* **Try-With-Resources:** Clean resource management implementing `AutoCloseable` to automatically close connections, statements, and result sets, preventing connection leaks.
* **Exception Handling:** Structured `try-catch` blocks catching `SQLException` and `NumberFormatException` with user-friendly console feedback.
* **Java Collections:** Using `List<Product>` and `ArrayList` to pass records between layers.
* **Comparator & Lambdas:** In-memory sorting using `Comparator.comparingDouble()`, `Comparator.comparingInt()`, and `String.CASE_INSENSITIVE_ORDER`.

---

## CRUD Operations

The Data Access Object (`ProductDAO.java`) implements the four core database operations:

* **Create (`addProduct`):** Inserts a new product record into the `products` table using parameterized SQL `INSERT`.
* **Read (`getAllProducts`, `getProductById`, `searchByName`, `getProductsByCategory`):** Retrieves all records or filters rows using `SELECT` statements and maps `ResultSet` rows into `Product` objects.
* **Update (`updateProduct`, `updateStock`):** Modifies existing database records by matching their unique `id` via `UPDATE` queries.
* **Delete (`deleteProduct`):** Removes an entry from the database by ID via `DELETE` queries after explicit user confirmation.

---

## How to Run

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher
* **Apache Maven:** Version 3.8 or higher

---

### Running via Terminal / Command Prompt (Windows)

1. Open your terminal (PowerShell or Command Prompt) and navigate to the project directory:
   ```bash
   cd path\to\InventoryManagementSystem
   ```

2. Compile the project:
   ```bash
   mvn clean compile
   ```
   *(If Maven is not in your system PATH, use the full path to `mvn.cmd`, e.g., `& "$HOME\AppData\Local\Programs\apache-maven-3.9.16\bin\mvn.cmd" clean compile`).*

3. Launch the application:
   ```bash
   mvn exec:java "-Dexec.mainClass=Main"
   ```

*(Optional)* You can also test the database connection directly using the diagnostic tool:
```bash
mvn exec:java "-Dexec.mainClass=util.ConnectionTest"
```

---

### Running via an IDE (IntelliJ IDEA, VS Code, or Eclipse)

1. Open the `InventoryManagementSystem` folder in your IDE as a Maven project.
2. Allow Maven to download dependencies automatically.
3. Navigate to `src/main/java/Main.java`.
4. Right-click and choose **Run 'Main.main()'**.

---

## Sample Menu

When launched, the application displays an interactive terminal menu:

```text
==================================================
 Welcome to the Inventory Management System 
==================================================

--------------------------------------------------
                    MAIN MENU                     
--------------------------------------------------
 1. Add New Product
 2. View All Products
 3. Search Product (by ID or Name)
 4. Update Product Details
 5. Delete Product
 6. Update Stock Quantity
 7. Filter Products by Category
 8. Sort Products (Price, Quantity, Name)
 9. Exit
--------------------------------------------------
Enter your choice (1-9):
```

---

## Learning Outcomes

This project demonstrates practical software engineering fundamentals:
* **End-to-End Java Application Development:** Building a working console application from architecture design to data persistence.
* **Database Connectivity & Persistence:** Connecting Java applications to relational databases via JDBC.
* **CRUD Implementation:** Writing and executing reliable SQL queries with parameters.
* **Layered Architecture:** Keeping UI, business logic, domain models, and database access strictly decoupled.
* **Defensive Coding:** Anticipating invalid user input and edge cases to maintain application stability.

---

## Future Improvements

* **Graphical User Interface (GUI):** Add a desktop interface using JavaFX or Swing, or build a web UI using Spring Boot.
* **User Authentication:** Introduce role-based access control (Admin vs. Staff) with login credentials.
* **Reports & Analytics:** Export inventory summaries and sales logs to CSV or PDF formats.
* **Low-Stock Alerts:** Automated email notifications or visual warnings when stock falls below a custom threshold.

---

*Built as a Java application to practice OOP, JDBC, SQL, CRUD operations, and database-driven application development.*
