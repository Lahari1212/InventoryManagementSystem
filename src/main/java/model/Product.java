package model;

import java.util.Objects;

/**
 * Represents a Product entity in the Inventory Management System.
 * 
 * Object-Oriented Programming (OOP) Principles demonstrated:
 * 1. Encapsulation:
 *    - All fields are marked 'private' to prevent direct external modification.
 *    - Public getters and setters provide controlled access to the internal state.
 * 2. Constructor Overloading:
 *    - No-argument constructor for generic instantiation.
 *    - Parameterized constructor without 'id' (for creating new products before MySQL assigns an auto-generated ID).
 *    - Full constructor with 'id' (for mapping rows fetched from the database).
 */
public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    // 1. Default Constructor (No-arg)
    public Product() {
    }

    // 2. Constructor without ID (used when creating a new product before inserting into MySQL)
    public Product(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // 3. Full Constructor (used when reading an existing product from the database)
    public Product(int id, String name, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // --- Getters and Setters (Encapsulation) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // --- Object Methods: toString, equals, hashCode ---

    @Override
    public String toString() {
        return String.format("Product [ID=%d, Name='%s', Category='%s', Price=$%.2f, Quantity=%d]",
                id, name, category, price, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
