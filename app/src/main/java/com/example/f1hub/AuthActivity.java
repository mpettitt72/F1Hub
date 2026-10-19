package com.example.f1hub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private boolean isLogin = true;
    private FirebaseAuth auth;

    private TextInputEditText emailField, passwordField;
    private MaterialButton submitButton;
    private TextView titleText, subtitleText, tabLogin, tabRegister, forgotText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.et_email);
        passwordField = findViewById(R.id.et_password);
        submitButton = findViewById(R.id.btn_submit);
        titleText = findViewById(R.id.tv_title);
        subtitleText = findViewById(R.id.tv_subtitle);
        tabLogin = findViewById(R.id.tab_login);
        tabRegister = findViewById(R.id.tab_register);
        forgotText = findViewById(R.id.tv_forgot);

        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
        tabLogin.setOnClickListener(v -> setMode(true));
        tabRegister.setOnClickListener(v -> setMode(false));
        submitButton.setOnClickListener(v -> handleSubmit());
        forgotText.setOnClickListener(v -> handleForgotPassword());
    }

    private void setMode(boolean login) {
        isLogin = login;
        if (login) {
            tabLogin.setBackgroundResource(R.drawable.bg_tab_selected);
            tabLogin.setTextColor(Color.WHITE);
            tabRegister.setBackgroundColor(Color.TRANSPARENT);
            tabRegister.setTextColor(getColor(R.color.f1_muted));
            titleText.setText(R.string.welcome_back);
            subtitleText.setText(R.string.login_subtitle);
            submitButton.setText(R.string.sign_in);
            forgotText.setVisibility(View.VISIBLE);
        } else {
            tabRegister.setBackgroundResource(R.drawable.bg_tab_selected);
            tabRegister.setTextColor(Color.WHITE);
            tabLogin.setBackgroundColor(Color.TRANSPARENT);
            tabLogin.setTextColor(getColor(R.color.f1_muted));
            titleText.setText(R.string.create_your_account);
            subtitleText.setText(R.string.register_subtitle);
            submitButton.setText(R.string.sign_up);
            forgotText.setVisibility(View.GONE);
        }
    }

    private void handleSubmit() {
        String email = emailField.getText() == null ? "" : emailField.getText().toString().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        submitButton.setEnabled(false);

        if (isLogin) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            goHome();
                        } else {
                            submitButton.setEnabled(true);
                            Toast.makeText(this, task.getException() != null ? task.getException().getMessage() : "Login failed", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            goHome();
                        } else {
                            submitButton.setEnabled(true);
                            Toast.makeText(this, task.getException() != null ? task.getException().getMessage() : "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void handleForgotPassword() {
        String email = emailField.getText() == null ? "" : emailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, task.getException() != null ? task.getException().getMessage() : "Failed to send reset email", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
