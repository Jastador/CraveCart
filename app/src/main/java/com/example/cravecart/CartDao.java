package com.example.cravecart;

import androidx.room.*;
import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM cart_items") List<CartItemEntity> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE) void insert(CartItemEntity item);
    @Update void update(CartItemEntity item);
    @Delete void delete(CartItemEntity item);
    @Query("DELETE FROM cart_items") void clear();
}