package com.example.cravecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpEmail, signUpPassword, signUpConfirmPassword;
    CheckBox showPasswordCheckBox;
    Button btnVerifyEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        Button btnGoToSignIn = findViewById(R.id.btnGoToSignIn);

        btnGoToSignIn.setOnClickListener(v -> {
            finish();
        });


        mAuth = FirebaseAuth.getInstance();

        // Show/Hide Password
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signUpPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                signUpConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                signUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                signUpConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        btnVerifyEmail.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();
        String confirmPassword = signUpConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this,
                                                        "Verification email sent! Check your inbox.",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(SignUpActivity.this,
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Sign Up Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
