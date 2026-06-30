package com.example.cravecart;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cravecart.CartItemEntity;
import com.example.cravecart.CartRepository;
import com.google.firebase.firestore.*;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.google.android.material.button.MaterialButton;
import org.json.JSONObject;
import java.util.*;
import com.google.firebase.auth.FirebaseAuth;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private int amountInRupees;
    private EditText etName, etPhone, etAddress;
    private RadioGroup rgPayment;
    private MaterialButton btnPayNow;
    private FirebaseFirestore db;
    private CartRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        repo = new CartRepository(this);

        amountInRupees = getIntent().getIntExtra("total", 0);
        ((TextView)findViewById(R.id.tvAmount)).setText("Total: ₹" + amountInRupees);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        rgPayment = findViewById(R.id.rgPayment);
        btnPayNow = findViewById(R.id.btnPayNow);

        // Razorpay pre-load
        Checkout.preload(getApplicationContext());

        btnPayNow.setOnClickListener(v -> {
            if (!validateInputs()) return;

            int sel = rgPayment.getCheckedRadioButtonId();
            if (sel == R.id.rbCOD) {
                // Place order without online payment
                placeOrder("COD", "cod-000", true);
            } else {
                startRazorpay(); // UPI/Card/NetBanking
            }
        });
    }

    private boolean validateInputs(){
        if (etName.getText().toString().trim().isEmpty()) { toast("Enter name"); return false; }
        if (etPhone.getText().toString().trim().isEmpty()) { toast("Enter phone"); return false; }
        if (etAddress.getText().toString().trim().isEmpty()) { toast("Enter address"); return false; }
        if (rgPayment.getCheckedRadioButtonId() == -1) { toast("Select payment method"); return false; }
        return true;
    }

    private void startRazorpay() {
        try {
            Checkout co = new Checkout();
            // Test key; replace with your key_id from Razorpay Dashboard
            co.setKeyID("rzp_test_XXXXXXXXXXXXXX");

            JSONObject options = new JSONObject();
            options.put("name", "CraveCart");
            options.put("description", "Order payment");
            options.put("currency", "INR");
            options.put("amount", amountInRupees * 100); // paise

            JSONObject prefill = new JSONObject();
            prefill.put("contact", etPhone.getText().toString().trim());
            prefill.put("email", "user@example.com"); // optional
            options.put("prefill", prefill);



            co.open(this, options);
        } catch (Exception e) {
            e.printStackTrace();
            toast("Payment init failed: " + e.getMessage());
        }
    }

    // Razorpay callbacks
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        placeOrder("ONLINE", razorpayPaymentID, true);
    }

    @Override
    public void onPaymentError(int code, String response) {
        toast("Payment failed: " + response);
        placeOrder("ONLINE", "failed", false); // optional log
    }

    private void placeOrder(String mode, String txnId, boolean paid){
        new Thread(() -> {
            List<CartItemEntity> items = repo.getAllSync();

            String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
            if (uid == null) { runOnUiThread(() -> toast("Not signed in. Try again.")); return; }


            int total = 0;
            List<Map<String,Object>> lines = new ArrayList<>();
            for (CartItemEntity c : items) {
                total += c.lineTotal();
                Map<String,Object> m = new HashMap<>();
                m.put("foodId", c.foodId);
                m.put("name", c.name);
                m.put("price", c.unitPrice);
                m.put("qty", c.quantity);
                m.put("imageUrl", c.imageUrl);
                lines.add(m);
            }

            Map<String,Object> order = new HashMap<>();
            order.put("userId", uid);
            order.put("createdAt", FieldValue.serverTimestamp());
            order.put("customer", etName.getText().toString().trim());
            order.put("phone", etPhone.getText().toString().trim());
            order.put("address", etAddress.getText().toString().trim());
            order.put("items", lines);
            order.put("total", total);
            order.put("paymentMode", mode);
            order.put("paymentTxnId", txnId);
            order.put("paid", paid);
            order.put("status", paid ? "CONFIRMED" : "PENDING");

            int finalTotal = total;
            db.collection("orders").add(order)
                    .addOnSuccessListener(doc -> {
                        for (CartItemEntity c : items) {
                            db.collection("menu").document(c.foodId)
                                    .update("ordersCount", FieldValue.increment(c.quantity));
                        }
                        repo.clear();
                        runOnUiThread(() -> {
                            toast("Order placed!");
                            startActivity(new android.content.Intent(this, OrderSuccessActivity.class)
                                    .putExtra("orderId", doc.getId())
                                    .putExtra("total", finalTotal));
                            finish();
                        });
                    })
                    .addOnFailureListener(e ->
                            runOnUiThread(() -> toast("Order save failed: " + e.getMessage()))
                    );
        }).start();
    }


    private void toast(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
}
