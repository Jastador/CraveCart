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
import com.google.firebase.auth.FirebaseUser;

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

        forgotPassword.setOnClickListener(v -> sendPasswordResetEmail());
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
                    if (!task.isSuccessful()) {
                        setAuthenticationButtonsEnabled(true);
                        passwordField.setError("Incorrect email or password");
                        passwordField.requestFocus();

                        Toast.makeText(
                                MainActivity.this,
                                "Could not sign in. Check your email and password.",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    verifyEmailAndOpenDashboard();
                });
    }

    private void verifyEmailAndOpenDashboard() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            setAuthenticationButtonsEnabled(true);
            Toast.makeText(this, "Could not confirm your account.", Toast.LENGTH_SHORT).show();
            return;
        }

        user.reload().addOnCompleteListener(reloadTask -> {
            setAuthenticationButtonsEnabled(true);

            FirebaseUser refreshedUser = mAuth.getCurrentUser();

            if (!reloadTask.isSuccessful() || refreshedUser == null) {
                mAuth.signOut();

                Toast.makeText(
                        MainActivity.this,
                        "Could not confirm email verification. Check your connection and try again.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            if (!refreshedUser.isEmailVerified()) {
                mAuth.useAppLanguage();

                refreshedUser.sendEmailVerification();
                mAuth.signOut();

                Toast.makeText(
                        MainActivity.this,
                        "Verify your email before signing in. A fresh link was requested.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            Toast.makeText(
                    MainActivity.this,
                    "Signed in successfully",
                    Toast.LENGTH_SHORT
            ).show();

            openDashboard();
        });
    }

    private void continueAsGuest() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.isAnonymous()) {
            openDashboard();
            return;
        }

        if (currentUser != null) {
            mAuth.signOut();
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

    private void sendPasswordResetEmail() {
        String email = emailField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Enter your email first");
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address");
            emailField.requestFocus();
            return;
        }

        forgotPassword.setEnabled(false);
        mAuth.useAppLanguage();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    forgotPassword.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(
                                MainActivity.this,
                                "Password-reset email sent. Check your inbox.",
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                MainActivity.this,
                                "Could not send reset email. Try again later.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void setAuthenticationButtonsEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnContinueAsGuest.setEnabled(enabled);
        newUser.setEnabled(enabled);
        forgotPassword.setEnabled(enabled);
    }

    private void openDashboard() {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        finish();
    }
}