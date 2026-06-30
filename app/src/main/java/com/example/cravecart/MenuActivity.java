package com.example.cravecart;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.util.Log;
import com.google.firebase.firestore.Query;

import com.example.cravecart.databinding.ActivityMenuBinding;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding b;
    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private CartRepository cartRepo;
    private FirebaseFirestore db;

    private final List<Category> categories = Arrays.asList(
            new Category("Trending"),
            new Category("Snacks & Starters"),
            new Category("Pizza & Pasta"),
            new Category("Main Course"),
            new Category("Desserts"),
            new Category("Hot Beverages"),
            new Category("Cold Beverages")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = FirebaseFirestore.getInstance();
        cartRepo = new CartRepository(this);

        // Back
        b.btnBack.setOnClickListener(v -> finish());

        // Categories (left)
        b.rvCategories.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(categories, this::loadCategory);
        b.rvCategories.setAdapter(categoryAdapter);

        // Items (right)
        b.rvItems.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(cartRepo);
        b.rvItems.setAdapter(foodAdapter);

        // Cart button
        findViewById(R.id.btnCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );

        findViewById(R.id.orderHistoryIcon).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, OrderHistoryActivity.class))
        );

        findViewById(R.id.settingsIcon).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, SettingsActivity.class))
        );




        loadCategory("Trending");
    }


    private void loadCategory(String categoryKey) {
        Log.d("MENU", "Category clicked: " + categoryKey);

        if ("Trending".equals(categoryKey)) {
            db.collection("menu")
                    .orderBy("ordersCount", Query.Direction.DESCENDING)
                    .limit(5)
                    .get()
                    .addOnSuccessListener(snap -> {
                        List<FoodItem> list = new ArrayList<>();
                        for (DocumentSnapshot d : snap.getDocuments()) {
                            FoodItem fi = d.toObject(FoodItem.class);
                            if (fi != null) list.add(fi);
                        }
                        foodAdapter.setItems(list);
                        Log.d("MENU","Trending loaded: "+list.size());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
            return;
        }

        db.collection("menu").whereEqualTo("category", categoryKey)
                .get()
                .addOnSuccessListener(snap -> {
                    List<FoodItem> list = new ArrayList<>();
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        FoodItem fi = d.toObject(FoodItem.class);
                        if (fi != null) list.add(fi);
                    }
                    foodAdapter.setItems(list);
                    Log.d("MENU","Loaded "+categoryKey+": "+list.size());
                    if (list.isEmpty())
                        Toast.makeText(this, "No items in " + categoryKey, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}