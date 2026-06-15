package com.example.subwise;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText editEmail, editPassword, editConfirmPassword, editJob;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editJob = findViewById(R.id.editJob);   // ✅ 직업 입력칸 초기화
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String pw = editPassword.getText().toString().trim();
            String confirmPw = editConfirmPassword.getText().toString().trim();
            String job = editJob.getText().toString().trim();

            if (email.isEmpty() || pw.isEmpty() || confirmPw.isEmpty() || job.isEmpty()) {
                Toast.makeText(this, "모든 칸을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (!pw.equals(confirmPw)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = db.insertUser(email, pw, job);   // ✅ job까지 전달
                if (inserted) {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "회원가입 실패 (이미 존재하는 사용자?)", Toast.LENGTH_SHORT).show();
                }
        }
        });
    }
}
