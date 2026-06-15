package com.example.subwise;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SubscriptionAddActivity extends AppCompatActivity {
    EditText editServiceName, editPrice, editBillingDate;
    TextView txtNextPayment;
    Button btnOtt, btnLife, btnBook, btnGame, btnParty, btnComplete;
    CheckBox chkEssential;
    String selectedCategory = "";
    String partyName = "";
    int partyCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_add);

        editServiceName = findViewById(R.id.editServiceName);
        editPrice = findViewById(R.id.editPrice);
        editBillingDate = findViewById(R.id.editBillingDate);
        txtNextPayment = findViewById(R.id.txtNextPayment);

        chkEssential = findViewById(R.id.chkEssential);
        btnOtt = findViewById(R.id.btnOtt);
        btnLife = findViewById(R.id.btnLife);
        btnBook = findViewById(R.id.btnBook);
        btnGame = findViewById(R.id.btnGame);
        btnParty = findViewById(R.id.btnParty);
        btnComplete = findViewById(R.id.btnComplete);

        // ✅ 결제일 선택 (DatePickerDialog)
        editBillingDate.setFocusable(false);
        editBillingDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            int y = now.get(Calendar.YEAR);
            int m = now.get(Calendar.MONTH);
            int d = now.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selected = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                editBillingDate.setText(selected);

                String next = calculateNextMonthDate(year, month, dayOfMonth);
                txtNextPayment.setText("다음달 결제 예정일: " + next);
            }, y, m, d);
            dpd.show();
        });

        // ✅ 카테고리 선택 (버튼 토글)
        Button[] cats = new Button[]{btnOtt, btnLife, btnBook, btnGame};
        for (Button b : cats) {
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

        // ✅ 필수 구독 체크박스
        chkEssential.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "필수 구독으로 설정됨", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ PartyCreateActivity 호출 (값 받아오기)
        ActivityResultLauncher<Intent> partyLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        partyName = result.getData().getStringExtra("partyName");
                        partyCount = result.getData().getIntExtra("partyCount", 1);
                        Toast.makeText(this, "파티 설정: " + partyName + " (" + partyCount + "명)", Toast.LENGTH_SHORT).show();
                    }
                });

        btnParty.setOnClickListener(v -> {
            Intent intent = new Intent(this, PartyCreateActivity.class);
            partyLauncher.launch(intent);
        });

        // ✅ 구독 추가 완료
        btnComplete.setOnClickListener(v -> {
            String name = editServiceName.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();
            String billing = editBillingDate.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || billing.isEmpty() || selectedCategory.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력/선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "금액 입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isEssential = chkEssential.isChecked();

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.insertSubscription(name, price, billing,
                    selectedCategory, partyName, partyCount, isEssential, true);

            Toast.makeText(this, "구독 추가 완료!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // ✅ 다음달 결제일 계산
    private String calculateNextMonthDate(int year, int monthZeroBased, int day) {
        Calendar next = Calendar.getInstance();
        next.set(Calendar.YEAR, year);
        next.set(Calendar.MONTH, monthZeroBased);
        next.set(Calendar.DAY_OF_MONTH, 1);
        next.add(Calendar.MONTH, 1);

        int maxDayNextMonth = next.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayToSet = Math.min(day, maxDayNextMonth);
        next.set(Calendar.DAY_OF_MONTH, dayToSet);

        int ny = next.get(Calendar.YEAR);
        int nm = next.get(Calendar.MONTH) + 1;
        int nd = next.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", ny, nm, nd);
    }
}
