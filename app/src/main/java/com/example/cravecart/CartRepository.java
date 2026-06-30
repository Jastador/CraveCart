package com.example.cravecart;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.room.Room;
import java.util.List;

public class CartRepository {

    private final AppDatabase db;
    private final CartDao dao;

    public CartRepository(Context ctx) {
        db = Room.databaseBuilder(ctx.getApplicationContext(),
                        AppDatabase.class, "cravecart.db")
                .fallbackToDestructiveMigration()
                .build();
        dao = db.cartDao();
    }

    // Callback to deliver items on the main thread
    public interface ItemsCallback { void onResult(List<CartItemEntity> items); }

    public void getAllAsync(ItemsCallback cb) {
        new Thread(() -> {
            List<CartItemEntity> items = dao.getAll();
            new Handler(Looper.getMainLooper()).post(() -> cb.onResult(items));
        }).start();
    }

    // Blocking getter (call only from a background thread)
    public List<CartItemEntity> getAllSync() {
        return dao.getAll();
    }


    public void addOrInc(String foodId, String name, int unitPrice, String imageUrl, int delta) {
        new Thread(() -> {
            List<CartItemEntity> all = dao.getAll();
            CartItemEntity target = null;
            for (CartItemEntity it : all) {
                if (it.foodId.equals(foodId)) { target = it; break; }
            }
            if (target == null) {
                CartItemEntity x = new CartItemEntity();
                x.foodId = foodId;
                x.name = name;
                x.unitPrice = unitPrice;
                x.imageUrl = imageUrl;
                x.quantity = Math.max(0, delta);
                if (x.quantity > 0) dao.insert(x);
            } else {
                target.quantity = Math.max(0, target.quantity + delta);
                if (target.quantity == 0) dao.delete(target);
                else dao.update(target);
            }
        }).start();
    }

    // ---- Clear all cart items
    public void clear() {
        new Thread(dao::clear).start();
    }
}