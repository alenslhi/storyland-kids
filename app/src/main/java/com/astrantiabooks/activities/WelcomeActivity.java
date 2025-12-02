package com.astrantiabooks.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.astrantiabooks.R;
import com.astrantiabooks.models.LocalData;
import com.astrantiabooks.models.PrefManager; // Import Baru
import com.astrantiabooks.models.User;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- UPDATE DISINI: CEK SESI OTOMATIS ---
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isLoggedIn()) {
            User savedUser = prefManager.getUser();
            if (savedUser != null) {
                LocalData.currentUser = savedUser; // Load ke memory

                Intent intent;
                if ("admin".equals(savedUser.getRole())) {
                    intent = new Intent(this, AdminMainActivity.class);
                } else {
                    intent = new Intent(this, MainActivity.class);
                }
                startActivity(intent);
                finish();
                return;
            }
        }
        // ----------------------------------------

        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btnLogin).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        findViewById(R.id.btnRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}