package com.example.cravecart;

public class FoodItem {
    public String id;
    public String category; // "Hot Beverages", etc.
    public String name;
    public String description;
    public int price; // in ₹ (integer)
    public String imageUrl;

    public FoodItem() {} // Firestore needs empty

    public FoodItem(String id, String category, String name, String description, int price, String imageUrl) {
        this.id = id; this.category = category; this.name = name; this.description = description;
        this.price = price; this.imageUrl = imageUrl;
    }
}
