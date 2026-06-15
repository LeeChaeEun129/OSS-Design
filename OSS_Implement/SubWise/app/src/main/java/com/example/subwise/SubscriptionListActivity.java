package com.example.subwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubscriptionListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    SubscriptionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadSubscriptions();
    }

    private void loadSubscriptions() {
        List<Subscription> subscriptions = dbHelper.getAllSubscriptions();

        adapter = new SubscriptionAdapter(subscriptions,
                sub -> {
                    // ✅ 수정 버튼 클릭 시 id 전달
                    Intent intent = new Intent(this, SubscriptionEditActivity.class);
                    intent.putExtra("id", sub.getId());
                    startActivity(intent);
                },
                sub -> {
                    // 삭제 버튼 클릭 시
                    dbHelper.deleteSubscription(sub.getId());
                    Toast.makeText(this, "삭제 완료!", Toast.LENGTH_SHORT).show();
                    loadSubscriptions(); // 리스트 새로고침
                });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubscriptions(); // 화면 돌아올 때 리스트 갱신
    }
}
