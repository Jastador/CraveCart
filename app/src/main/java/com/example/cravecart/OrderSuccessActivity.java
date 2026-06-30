package com.example.cravecart;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cravecart.R;

public class OrderSuccessActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_order_success);
        String id = getIntent().getStringExtra("orderId");
        int total = getIntent().getIntExtra("total", 0);
        ((android.widget.TextView)findViewById(R.id.tvOrder))
                .setText("Order ID: " + id + " • Amount: ₹" + total);
        findViewById(R.id.btnDone).setOnClickListener(v -> finish());
    }
}

