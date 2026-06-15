package com.example.subwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PartyCreateActivity extends AppCompatActivity {
    EditText editPartyName, editMemberCount;
    Button btnCreateParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_create);

        editPartyName = findViewById(R.id.editPartyName);
        editMemberCount = findViewById(R.id.editMemberCount);
        btnCreateParty = findViewById(R.id.btnCreateParty);

        btnCreateParty.setOnClickListener(v -> {
            String name = editPartyName.getText().toString().trim();
            String members = editMemberCount.getText().toString().trim();

            if (name.isEmpty() || members.isEmpty()) {
                Toast.makeText(this, "파티 이름과 인원 수를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            int count;
            try {
                count = Integer.parseInt(members);
                if (count <= 0) {
                    Toast.makeText(this, "인원 수는 1명 이상이어야 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "인원 수는 숫자로 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ 결과 반환
            Intent resultIntent = new Intent();
            resultIntent.putExtra("partyName", name);
            resultIntent.putExtra("partyCount", count);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "파티 생성 완료: " + name + " (" + count + "명)", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
