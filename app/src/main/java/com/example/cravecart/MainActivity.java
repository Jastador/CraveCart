package com.example.cravecart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import com.google.firebase.auth.FirebaseAuth;
import android.util.Log;



public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    CheckBox showPasswordLogin;
    EditText emailField, passwordField;
    Button btnLogin;
    TextView newUser, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.btnLogin);
        newUser = findViewById(R.id.newUser);
        forgotPassword = findViewById(R.id.forgotPassword);


        showPasswordLogin = findViewById(R.id.showPasswordLogin);

        showPasswordLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // keep cursor at end after toggling
            passwordField.setSelection(passwordField.getText().length());
        });


        btnLogin.setOnClickListener(v -> {
            com.google.firebase.auth.FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

            // already signed in?
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                finish();
                return;
            }

            // sign in anonymously
            mAuth.signInAnonymously()
                    .addOnSuccessListener(r -> {
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e ->
                            android.widget.Toast.makeText(this, "Login failed: "+e.getMessage(), android.widget.Toast.LENGTH_LONG).show()
                    );
        });



        newUser.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });

        forgotPassword.setOnClickListener(v -> {
            // Later: Navigate to Forgot Password page
        });
    }
}
