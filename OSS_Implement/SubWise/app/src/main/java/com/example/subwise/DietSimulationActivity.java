package com.example.subwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class DietSimulationActivity extends AppCompatActivity {
    EditText editExchangeValue;   // 단가 입력창
    EditText editItemName;        // 아이템 이름 입력창
    Button btnStartSimulation;
    Button btnChicken, btnCoffee, btnGame;
    Button btnIpad, btnConcertTicket, btnCpu, btnMeal, btnWage, btnTriangleKimbap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_simulation);

        editExchangeValue = findViewById(R.id.editExchangeValue);
        editItemName = findViewById(R.id.editItemName);
        btnStartSimulation = findViewById(R.id.btnStartSimulation);

        // 버튼 초기화
        btnChicken = findViewById(R.id.btnChicken);
        btnCoffee = findViewById(R.id.btnCoffee);
        btnGame = findViewById(R.id.btnGame);
        btnIpad = findViewById(R.id.btnIpad);
        btnConcertTicket = findViewById(R.id.btnConcertTicket);
        btnCpu = findViewById(R.id.btnCpu);
        btnMeal = findViewById(R.id.btnMeal);
        btnWage = findViewById(R.id.btnWage);
        btnTriangleKimbap = findViewById(R.id.btnTriangleKimbap);

        // 버튼 클릭 시 이름만 자동 입력 (단가는 유저가 직접 입력)
        btnChicken.setOnClickListener(v -> {
            editItemName.setText("치킨");
            editExchangeValue.setText("15000");
        });

        btnCoffee.setOnClickListener(v -> {
            editItemName.setText("아메리카노");
            editExchangeValue.setText("4500");
        });

        btnGame.setOnClickListener(v -> {
            editItemName.setText("게임기");
            editExchangeValue.setText("400000");
        });

        btnIpad.setOnClickListener(v -> {
            editItemName.setText("아이패드");
            editExchangeValue.setText("800000");
        });

        btnConcertTicket.setOnClickListener(v -> {
            editItemName.setText("콘서트 티켓");
            editExchangeValue.setText("200000");
        });

        btnCpu.setOnClickListener(v -> {
            editItemName.setText("CPU");
            editExchangeValue.setText("350000");
        });

        btnMeal.setOnClickListener(v -> {
            editItemName.setText("한끼 식사");
            editExchangeValue.setText("8000");
        });

        btnWage.setOnClickListener(v -> {
            editItemName.setText("최저 시급");
            editExchangeValue.setText("10320");
        });

        btnTriangleKimbap.setOnClickListener(v -> {
            editItemName.setText("삼각 김밥");
            editExchangeValue.setText("1500");
        });

        // 시뮬레이션 시작 버튼
        btnStartSimulation.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            List<Subscription> subscriptions = dbHelper.getAllSubscriptions();

            int totalSpending = 0;
            for (Subscription sub : subscriptions) {
                if (!sub.isEssential()) {
                    int monthlyFee = sub.getPrice();
                    if (sub.getPartyCount() > 0) {
                        monthlyFee = monthlyFee / sub.getPartyCount();
                    }
                    totalSpending += monthlyFee * 12;
                }
            }

            String item = editItemName.getText().toString().trim();
            String value = editExchangeValue.getText().toString().trim();

            if (item.isEmpty() || value.isEmpty()) {
                Toast.makeText(this, "아이템과 기준가를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("savingAmount", totalSpending);
                intent.putExtra("selectedItem", item);
                intent.putExtra("itemPrice", Integer.parseInt(value));
                startActivity(intent);
            }
        });

    }
}
