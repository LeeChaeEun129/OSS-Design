package com.example.subwise;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 전달받은 데이터
        int savingAmount = getIntent().getIntExtra("savingAmount", 0);
        String selectedItem = getIntent().getStringExtra("selectedItem");
        int itemPrice = getIntent().getIntExtra("itemPrice", 1); // 유저 입력 단가

        // 환산 계산
        int quantity = savingAmount / itemPrice;

        // 결과 텍스트 표시
        TextView resultText = findViewById(R.id.resultText);
        resultText.setText("필수 구독 제외 1년 총액 " + savingAmount +
                "원으로 " + selectedItem + "을(를) " + quantity + "개 살 수 있습니다!");

        // 이미지 표시 (GridLayout)
        GridLayout imageContainer = findViewById(R.id.imageContainer);

        // 아이템 이름 → 이미지 리소스 매핑
        Map<String, Integer> itemImages = new HashMap<>();
        itemImages.put("치킨", R.drawable.ic_chicken);
        itemImages.put("아메리카노", R.drawable.ic_coffee);
        itemImages.put("게임기", R.drawable.ic_game);
        itemImages.put("아이패드", R.drawable.ic_ipad);
        itemImages.put("콘서트 티켓", R.drawable.ic_ticket);
        itemImages.put("CPU", R.drawable.ic_cpu);
        itemImages.put("한끼 식사", R.drawable.ic_meal);
        itemImages.put("최저 시급", R.drawable.ic_pay);
        itemImages.put("삼각 김밥", R.drawable.ic_rice);

        int imageResId = itemImages.getOrDefault(selectedItem, R.drawable.ic_default);

        // 환산된 갯수만큼 이미지 추가
        for (int i = 0; i < quantity; i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(imageResId);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 120;
            params.height = 120;
            params.setMargins(8, 8, 8, 8);

            img.setLayoutParams(params);
            imageContainer.addView(img);
        }
    }
}
