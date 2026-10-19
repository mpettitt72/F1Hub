package com.example.f1hub;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);

        ImageView bg = findViewById(R.id.bg_track);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0f);
        bg.setColorFilter(new ColorMatrixColorFilter(matrix));

        findViewById(R.id.btn_get_started).setOnClickListener(v ->
                startActivity(new Intent(this, AuthActivity.class))
        );
    }
}
