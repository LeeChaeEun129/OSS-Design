package com.example.subwise;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail, editPassword;
    Button btnLogin, btnRegister;
    DatabaseHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        editEmail = findViewById(R.id.editEmail);   // ✅ 추가
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString();
            String pw = editPassword.getText().toString();
            if (db.checkUser(email, pw)) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainDashboardActivity.class);
                intent.putExtra("email", email); // 로그인한 이메일 전달
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}