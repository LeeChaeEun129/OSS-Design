package com.example.subwise;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView txtEmail, txtPassword, txtJob;
    Button btnLogout;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtJob = findViewById(R.id.txtJob);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        // 현재 로그인한 사용자 정보 가져오기 (예: email 전달받음)
        String currentEmail = getIntent().getStringExtra("email");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{currentEmail});

        if (cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String job = cursor.getString(cursor.getColumnIndexOrThrow("job"));

            txtEmail.setText("Email: " + email);
            txtPassword.setText("Password: " + password);
            txtJob.setText("Job: " + job);
        }
        cursor.close();

        // 로그아웃 버튼 → 로그인 화면으로 이동
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}

