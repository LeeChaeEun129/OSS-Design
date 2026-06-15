package com.example.subwise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.content.pm.PackageManager;   // ✅ 추가
import android.os.Build;                    // ✅ 추가

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    RecyclerView recyclerCalendar;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 🔒 Android 13 이상 알림 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        recyclerCalendar = findViewById(R.id.recyclerCalendar);
        recyclerCalendar.setLayoutManager(new GridLayoutManager(this, 7)); // 7열 (요일)

        dbHelper = new DatabaseHelper(this);

        List<DayItem> dayList = new ArrayList<>();

        // ✅ 예시: 5월 1~31일
        for (int d = 1; d <= 31; d++) {
            String date = "2026-05-" + String.format("%02d", d);
            Subscription sub = dbHelper.getSubscriptionByDate(date);

            if (sub != null) {
                int color = getCategoryColor(sub.getCategory());
                dayList.add(new DayItem(d, sub.getServiceName(), color));

                // 🔔 알림 예약 (결제 3일 전)
                SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
                boolean alarmEnabled = prefs.getBoolean("alarm_enabled", true);

                if (alarmEnabled) {
                    LocalDate billing = LocalDate.parse(sub.getBillingDate());
                    LocalDate notifyDate = billing.minusDays(3);

                    Calendar cal = Calendar.getInstance();
                    cal.set(notifyDate.getYear(), notifyDate.getMonthValue() - 1,
                            notifyDate.getDayOfMonth(), 9, 0);

                    Intent intent = new Intent(this, ReminderReceiver.class);
                    intent.putExtra("serviceName", sub.getServiceName());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this, sub.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }

            } else {
                dayList.add(new DayItem(d, "", Color.TRANSPARENT));
            }
        }

        CalendarAdapter adapter = new CalendarAdapter(dayList);
        recyclerCalendar.setAdapter(adapter);
    }


    // ✅ 카테고리별 색상 매핑
    private int getCategoryColor(String category) {
        switch (category) {
            case "OTT": return Color.parseColor("#6C63FF"); // 파랑
            case "생활": return Color.parseColor("#6FCF97"); // 초록
            case "도서": return Color.parseColor("#F2C94C"); // 주황
            case "게임": return Color.parseColor("#BB6BD9"); // 보라
            default: return Color.GRAY;
        }
    }
}
