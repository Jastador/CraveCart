package com.example.cravecart;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItemEntity {
    @PrimaryKey(autoGenerate = true) public int localId;
    public String foodId;
    public String name;
    public int unitPrice;
    public int quantity;
    public String imageUrl;

    public int lineTotal(){ return unitPrice * quantity; }
}
