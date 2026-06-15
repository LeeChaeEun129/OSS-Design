package com.example.subwise;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SubscriptionEditActivity extends AppCompatActivity {
    EditText editServiceName, editPrice, editBillingDate;
    CheckBox chkEssential;
    Button btnComplete, btnParty, btnOtt, btnLife, btnBook, btnGame;
    DatabaseHelper dbHelper;
    int subId;
    String partyName = "";
    int partyCount = 1;
    String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_add); // 같은 레이아웃 재사용

        editServiceName = findViewById(R.id.editServiceName);
        editPrice = findViewById(R.id.editPrice);
        editBillingDate = findViewById(R.id.editBillingDate);
        chkEssential = findViewById(R.id.chkEssential);
        btnComplete = findViewById(R.id.btnComplete);
        btnParty = findViewById(R.id.btnParty);
        btnOtt = findViewById(R.id.btnOtt);
        btnLife = findViewById(R.id.btnLife);
        btnBook = findViewById(R.id.btnBook);
        btnGame = findViewById(R.id.btnGame);

        dbHelper = new DatabaseHelper(this);
        subId = getIntent().getIntExtra("id", -1);

        Subscription sub = dbHelper.getSubscriptionById(subId);
        if (sub != null) {
            editServiceName.setText(sub.getServiceName());
            editPrice.setText(String.valueOf(sub.getPrice()));
            editBillingDate.setText(sub.getBillingDate());
            chkEssential.setChecked(sub.isEssential());
            selectedCategory = sub.getCategory();
            partyName = sub.getPartyName();
            partyCount = sub.getPartyCount();
        } else {
            Toast.makeText(this, "구독 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnComplete.setText("구독 수정 완료");

        // ✅ 날짜 선택 (DatePickerDialog)
        editBillingDate.setFocusable(false);
        editBillingDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            int y = now.get(Calendar.YEAR);
            int m = now.get(Calendar.MONTH);
            int d = now.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selected = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                editBillingDate.setText(selected);
            }, y, m, d);
            dpd.show();
        });

        // ✅ 카테고리 버튼 클릭 처리 + 기존 선택 반영
        Button[] cats = new Button[]{btnOtt, btnLife, btnBook, btnGame};
        for (Button b : cats) {
            if (b.getText().toString().equals(selectedCategory)) {
                b.setSelected(true);
                b.setAlpha(0.7f);
            }
            b.setOnClickListener(v -> {
                for (Button bb : cats) {
                    bb.setSelected(false);
                    bb.setAlpha(1.0f);
                }
                v.setSelected(true);
                v.setAlpha(0.7f);
                selectedCategory = ((Button) v).getText().toString();
            });
        }

        // ✅ 파티 생성 버튼 (ActivityResultLauncher)
        ActivityResultLauncher<Intent> partyLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        partyName = result.getData().getStringExtra("partyName");
                        partyCount = result.getData().getIntExtra("partyCount", 1);
                        Toast.makeText(this, "파티 수정: " + partyName + " (" + partyCount + "명)", Toast.LENGTH_SHORT).show();
                    }
                });

        btnParty.setOnClickListener(v -> {
            Intent intent = new Intent(this, PartyCreateActivity.class);
            partyLauncher.launch(intent);
        });

        // ✅ 구독 수정 완료 버튼
        btnComplete.setOnClickListener(v -> {
            String name = editServiceName.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();
            String billing = editBillingDate.getText().toString().trim();
            boolean essential = chkEssential.isChecked();

            if (name.isEmpty() || priceStr.isEmpty() || billing.isEmpty() || selectedCategory.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "금액 입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateSubscription(subId, name, price, billing,
                    selectedCategory, partyName, partyCount, essential, true);
            Toast.makeText(this, "구독 수정 완료!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
