package com.example.cravecart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.widget.FrameLayout;


public class DashboardActivity extends AppCompatActivity {
    FrameLayout orderNowButton;

    ImageView supportIcon, orderHistoryIcon, settingsIcon, accountIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        orderNowButton = findViewById(R.id.orderNowButton);
        supportIcon = findViewById(R.id.supportIcon);
        orderHistoryIcon = findViewById(R.id.orderHistoryIcon);
        settingsIcon = findViewById(R.id.settingsIcon);
        accountIcon = findViewById(R.id.accountIcon);

        startPulseAnimation(orderNowButton);

        orderNowButton.setOnClickListener(v -> {
            // Scale animation pop effect
            orderNowButton.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction(() -> orderNowButton.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100))
                    .start();


            // TODO: Open your food menu screen
            Toast.makeText(this, "Going to Food Menu...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, MenuActivity.class));
        });

        boolean DEBUG_SEED = false; // run once then make false
        if (DEBUG_SEED) {
            com.example.cravecart.SeedMenu.run(com.google.firebase.firestore.FirebaseFirestore.getInstance());
        }

        // Support Icon
        supportIcon.setOnClickListener(v ->
                Toast.makeText(this, "Opening Customer Support...", Toast.LENGTH_SHORT).show()
        );

        // Order History
        orderHistoryIcon.setOnClickListener(v ->
                Toast.makeText(this, "Opening Order History...", Toast.LENGTH_SHORT).show()
        );

        // Settings
        settingsIcon.setOnClickListener(v ->
                Toast.makeText(this, "Opening App Settings...", Toast.LENGTH_SHORT).show()
        );

        // Account Settings
        accountIcon.setOnClickListener(v ->
                Toast.makeText(this, "Opening Account Settings...", Toast.LENGTH_SHORT).show()
        );


        findViewById(R.id.orderHistoryIcon).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, OrderHistoryActivity.class))
        );

        findViewById(R.id.settingsIcon).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, SettingsActivity.class))
        );

        findViewById(R.id.accountIcon).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, ProfileActivity.class))
        );


    }
    // 🔹 Hover / Pulse Animation
    private void startPulseAnimation(FrameLayout view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f);

        scaleX.setDuration(600);
        scaleY.setDuration(600);

        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);

        scaleX.start();
        scaleY.start();
    }


}
