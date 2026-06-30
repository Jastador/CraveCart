package com.example.cravecart;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.*;

public class OrderHistoryActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView rv;
    private OrderAdapter adapter;
    private FirebaseFirestore db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rv = findViewById(R.id.rvOrders);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter();
        rv.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
        if (uid == null) { android.widget.Toast.makeText(this,"Not signed in", android.widget.Toast.LENGTH_SHORT).show(); return; }

        db.collection("orders")
                .whereEqualTo("userId", uid)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    java.util.List<OrderBrief> list = new java.util.ArrayList<>();
                    for (com.google.firebase.firestore.DocumentSnapshot d : snap.getDocuments()) {
                        OrderBrief ob = d.toObject(OrderBrief.class);
                        if (ob == null) ob = new OrderBrief();
                        ob.id = d.getId();
                        list.add(ob);
                    }
                    adapter.setItems(list);  // ensure your adapter calls notifyDataSetChanged()
                    if (list.isEmpty()) android.widget.Toast.makeText(this,"No orders yet", android.widget.Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        android.widget.Toast.makeText(this,"Load failed: "+e.getMessage(), android.widget.Toast.LENGTH_LONG).show());
    }
}

