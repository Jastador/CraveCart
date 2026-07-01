package com.example.cravecart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private CheckBox showPasswordLogin;
    private EditText emailField, passwordField;
    private Button btnLogin, btnContinueAsGuest, newUser, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.btnLogin);
        btnContinueAsGuest = findViewById(R.id.btnContinueAsGuest);
        newUser = findViewById(R.id.newUser);
        forgotPassword = findViewById(R.id.forgotPassword);
        showPasswordLogin = findViewById(R.id.showPasswordLogin);

        showPasswordLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance()
                );
            } else {
                passwordField.setTransformationMethod(
                        PasswordTransformationMethod.getInstance()
                );
            }

            passwordField.setSelection(passwordField.getText().length());
        });

        btnLogin.setOnClickListener(v -> signInWithEmailAndPassword());
        btnContinueAsGuest.setOnClickListener(v -> continueAsGuest());

        newUser.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignUpActivity.class))
        );

        forgotPassword.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        "Password reset will be added next.",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void signInWithEmailAndPassword() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Enter your email address");
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address");
            emailField.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Enter your password");
            passwordField.requestFocus();
            return;
        }

        setAuthenticationButtonsEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setAuthenticationButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(
                                MainActivity.this,
                                "Signed in successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        openDashboard();
                    } else {
                        passwordField.setError("Incorrect email or password");
                        passwordField.requestFocus();

                        Toast.makeText(
                                MainActivity.this,
                                "Could not sign in. Check your email and password.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void continueAsGuest() {
        if (mAuth.getCurrentUser() != null) {
            openDashboard();
            return;
        }

        setAuthenticationButtonsEnabled(false);

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    setAuthenticationButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(
                                MainActivity.this,
                                "Continuing as guest",
                                Toast.LENGTH_SHORT
                        ).show();

                        openDashboard();
                    } else {
                        Toast.makeText(
                                MainActivity.this,
                                "Guest login failed. Please try again.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void setAuthenticationButtonsEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnContinueAsGuest.setEnabled(enabled);
    }

    private void openDashboard() {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        finish();
    }
}