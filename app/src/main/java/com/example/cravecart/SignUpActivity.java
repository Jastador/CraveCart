package com.example.cravecart;

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

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmail, signUpPassword, signUpConfirmPassword;
    private CheckBox showPasswordCheckBox;
    private Button btnVerifyEmail, btnGoToSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        btnGoToSignIn = findViewById(R.id.btnGoToSignIn);

        mAuth = FirebaseAuth.getInstance();

        btnGoToSignIn.setOnClickListener(v -> finish());

        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signUpPassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance()
                );
                signUpConfirmPassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance()
                );
            } else {
                signUpPassword.setTransformationMethod(
                        PasswordTransformationMethod.getInstance()
                );
                signUpConfirmPassword.setTransformationMethod(
                        PasswordTransformationMethod.getInstance()
                );
            }

            signUpPassword.setSelection(signUpPassword.getText().length());
            signUpConfirmPassword.setSelection(signUpConfirmPassword.getText().length());
        });

        btnVerifyEmail.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString();
        String confirmPassword = signUpConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            signUpEmail.setError("Enter your email address");
            signUpEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Enter a valid email address");
            signUpEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            signUpPassword.setError("Enter a password");
            signUpPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            signUpPassword.setError("Password must be at least 6 characters");
            signUpPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            signUpConfirmPassword.setError("Passwords do not match");
            signUpConfirmPassword.requestFocus();
            return;
        }

        setSignUpControlsEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        setSignUpControlsEnabled(true);

                        Toast.makeText(
                                SignUpActivity.this,
                                "Could not create account. This email may already be registered.",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user == null) {
                        setSignUpControlsEnabled(true);

                        Toast.makeText(
                                SignUpActivity.this,
                                "Account was created, but verification could not start.",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    mAuth.useAppLanguage();

                    user.sendEmailVerification()
                            .addOnCompleteListener(verificationTask -> {
                                mAuth.signOut();
                                setSignUpControlsEnabled(true);

                                if (verificationTask.isSuccessful()) {
                                    Toast.makeText(
                                            SignUpActivity.this,
                                            "Account created. Verify your email, then sign in.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Toast.makeText(
                                            SignUpActivity.this,
                                            "Account created, but verification email failed. Sign in later to request a new link.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                                finish();
                            });
                });
    }

    private void setSignUpControlsEnabled(boolean enabled) {
        btnVerifyEmail.setEnabled(enabled);
        btnGoToSignIn.setEnabled(enabled);
        showPasswordCheckBox.setEnabled(enabled);
    }
}