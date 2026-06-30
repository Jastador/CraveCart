package com.example.cravecart;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartItemEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract CartDao cartDao();
}

