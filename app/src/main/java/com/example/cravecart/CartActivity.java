package com.example.cravecart;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cravecart.databinding.ActivityCartBinding;
import java.util.List;
import android.content.Intent;

public class CartActivity extends AppCompatActivity
{
    private ActivityCartBinding b;
    private CartAdapter adapter;
    private CartRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        repo = new CartRepository(this);

        b.btnBack.setOnClickListener(v -> finish());

        adapter = new CartAdapter(new CartAdapter.CartCallbacks()
        {
            // Increase & Decrease item quantity, and remove item from cart
            @Override public void onInc(CartItemEntity item)
            {
                repo.addOrInc(item.foodId, item.name, item.unitPrice, item.imageUrl, +1);
                reload();
            }
            @Override public void onDec(CartItemEntity item)
            {
                repo.addOrInc(item.foodId, item.name, item.unitPrice, item.imageUrl, -1);
                reload();
            }
            @Override public void onRemove(CartItemEntity item)
            {
                repo.addOrInc(item.foodId, item.name, item.unitPrice, item.imageUrl, -9999);
                reload();
            }
        });

        b.rvCart.setLayoutManager(new LinearLayoutManager(this));
        b.rvCart.setAdapter(adapter);

        b.btnCheckout.setOnClickListener(v -> {
            int total = computeTotal(adapter.getItems());
            if (total <= 0) { Toast.makeText(this, "Cart is empty.", Toast.LENGTH_SHORT).show(); return; }
            Intent i = new Intent(this, CheckoutActivity.class);
            i.putExtra("total", total);  // in ₹
            startActivity(i);
        });

        reload();
    }

    private void reload()
    {
        repo.getAllAsync(items -> {
            adapter.setItems(items);
            b.tvTotal.setText("Total: ₹" + computeTotal(items));
        });
    }

    private int computeTotal(List<CartItemEntity> items)
    {
        int sum = 0;
        for (CartItemEntity c : items) sum += c.lineTotal();
        return sum;
    }
}
