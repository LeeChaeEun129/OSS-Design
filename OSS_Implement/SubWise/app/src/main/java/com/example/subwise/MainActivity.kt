//22412204 이채은
//git repository 주소: https://github.com/LeeChaeEun129/OSS-Design
//4. [Implementation]
package com.example.subwise

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.subwise.R;
class MainActivity : AppCompatActivity() {
    var editEmail: EditText? = null
    var editPassword: EditText? = null
    var btnLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 로그인 화면 XML 연결 (activity_login.xml)
        setContentView(R.layout.activity_login)

        editEmail = findViewById<EditText?>(R.id.editEmail)
        editPassword = findViewById<EditText?>(R.id.editPassword)
        btnLogin = findViewById<Button?>(R.id.btnLogin)

        btnLogin!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val email = editEmail!!.getText().toString()
                val password = editPassword!!.getText().toString()

                // 간단한 로그인 검증
                if (email == "test@test.com" && password == "1234") {
                    Toast.makeText(this@MainActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    // 로그인 성공 → 대시보드로 이동
                    val intent = Intent(this@MainActivity, MainDashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "로그인 실패. 다시 시도하세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}