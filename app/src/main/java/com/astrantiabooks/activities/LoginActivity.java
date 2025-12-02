package com.astrantiabooks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.astrantiabooks.models.PrefManager;
import com.astrantiabooks.R;
import com.astrantiabooks.models.LocalData;
import com.astrantiabooks.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Pastikan URL ini SAMA PERSIS dengan di Firebase Console Anda
        String dbUrl = "https://astrantia-books-28ad6-default-rtdb.asia-southeast1.firebasedatabase.app/";
        mDatabase = FirebaseDatabase.getInstance(dbUrl).getReference();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> prosesLogin());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void prosesLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Loading...");

        // 1. Login ke Authentication
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 2. Ambil data Role dari Database
                        String uid = mAuth.getCurrentUser().getUid();
                        cekRoleDanRedirect(uid);
                    } else {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Masuk");
                        Toast.makeText(LoginActivity.this, "Login Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cekRoleDanRedirect(String uid) {
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Masuk");

                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    // --- PERBAIKAN PENTING DISINI ---
                    if (user != null) {
                        user.setUid(uid); // <--- TAMBAHKAN INI (Pastikan UID terisi)
                    }
                    // --------------------------------

                    // Simpan ke Sesi
                    PrefManager prefManager = new PrefManager(LoginActivity.this);
                    prefManager.saveUser(user);

                    Intent intent;
                    if (user != null && "admin".equals(user.getRole())) {
                        intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        Toast.makeText(LoginActivity.this, "Login Mode Admin", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        Toast.makeText(LoginActivity.this, "Selamat datang, " + (user != null ? user.getUsername() : ""), Toast.LENGTH_SHORT).show();
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Data profil user tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Masuk");
                Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}