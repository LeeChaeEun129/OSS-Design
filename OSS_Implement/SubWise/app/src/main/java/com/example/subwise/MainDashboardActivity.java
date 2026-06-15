package com.example.subwise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainDashboardActivity extends AppCompatActivity {

    RecyclerView recyclerCalendar;
    Button btnAddService, btnStartSimulation;
    DatabaseHelper dbHelper;

    private int currentYear = 2026;
    private int currentMonth = 6; // 시작 월
    private TextView txtMonthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerCalendar = findViewById(R.id.recyclerCalendar);
        recyclerCalendar.setLayoutManager(new GridLayoutManager(this, 7));

        dbHelper = new DatabaseHelper(this);
        txtMonthYear = findViewById(R.id.txtMonthYear);

        // 로그인한 이메일 받아오기 (로그인 흐름 유지)
        String loggedInEmail = getIntent().getStringExtra("email");

        // 초기 달력 로드
        loadCalendar(currentYear, currentMonth);

        // 이전/다음 달 버튼
        Button btnPrevMonth = findViewById(R.id.btnPrevMonth);
        Button btnNextMonth = findViewById(R.id.btnNextMonth);

        btnPrevMonth.setOnClickListener(v -> {
            currentMonth--;
            if (currentMonth < 1) {
                currentMonth = 12;
                currentYear--;
            }
            loadCalendar(currentYear, currentMonth);
        });

        btnNextMonth.setOnClickListener(v -> {
            currentMonth++;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
            loadCalendar(currentYear, currentMonth);
        });

        // 버튼들
        btnAddService = findViewById(R.id.btnAddService);
        Button btnEditService = findViewById(R.id.btnEditService);
        btnStartSimulation = findViewById(R.id.btnStartSimulation);

        btnAddService.setOnClickListener(v -> {
            startActivity(new Intent(this, SubscriptionAddActivity.class));
        });

        btnEditService.setOnClickListener(v -> {
            startActivity(new Intent(this, SubscriptionListActivity.class));
        });

        btnStartSimulation.setOnClickListener(v -> {
            startActivity(new Intent(this, DietSimulationActivity.class));
        });

        // 프로필 버튼 → 이메일 전달
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("email", loggedInEmail); // 로그인 정보 전달 유지
            startActivity(intent);
        });

        // 설정 버튼 → 이메일 전달
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashboardActivity.this, SettingsActivity.class);
            intent.putExtra("email", loggedInEmail); // 필요하다면 설정에도 전달
            startActivity(intent);
        });
    }

    // 달력 로드 메서드
    private void loadCalendar(int year, int month) {
        txtMonthYear.setText(year + "년 " + month + "월");

        List<DayItem> dayList = new ArrayList<>();

        LocalDate firstDay = LocalDate.of(year, month, 1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < startDayOfWeek; i++) {
            dayList.add(new DayItem(-1, "", Color.TRANSPARENT));
        }

        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        for (int d = 1; d <= daysInMonth; d++) {
            String date = String.format(Locale.KOREA, "%04d-%02d-%02d", year, month, d);
            Subscription sub = dbHelper.getSubscriptionByDate(date);

            if (sub == null) {
                for (Subscription s : dbHelper.getAllSubscriptions()) {
                    LocalDate billing = LocalDate.parse(s.getBillingDate());
                    LocalDate current = LocalDate.of(year, month, d);
                    if (billing.getDayOfMonth() == d &&
                            !current.isBefore(billing) &&
                            !current.isAfter(billing.plusYears(1))) {
                        sub = s;
                        break;
                    }
                }
            }

            if (sub != null) {
                int color = getCategoryColor(sub.getCategory());
                dayList.add(new DayItem(d, sub.getServiceName(), color));

                // 🔔 알림 예약 (결제 3일 전)
                SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
                boolean alarmEnabled = prefs.getBoolean("alarm_enabled", true);

                if (alarmEnabled) {
                    // 테스트용: 오늘 날짜 + 몇 분 뒤
                    String serviceName = "카카오톡 톡서랍 (테스트)";
                    LocalDate notifyDate = LocalDate.now(); // 오늘 날짜

                    Calendar cal = Calendar.getInstance();
// 예: 지금 시각에서 1분 뒤로 예약
                    cal.add(Calendar.MINUTE, 1);

                    Intent intent = new Intent(this, ReminderReceiver.class);
                    intent.putExtra("serviceName", serviceName);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this, 9999, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
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

    private int getCategoryColor(String category) {
        switch (category) {
            case "OTT": return Color.parseColor("#6C63FF");
            case "생활": return Color.parseColor("#6FCF97");
            case "도서": return Color.parseColor("#F2C94C");
            case "게임": return Color.parseColor("#BB6BD9");
            default: return Color.GRAY;
        }
    }
}
