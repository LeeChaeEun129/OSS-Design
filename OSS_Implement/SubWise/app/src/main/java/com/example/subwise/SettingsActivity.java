package com.example.subwise;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotification = findViewById(R.id.switchNotification);

        // SharedPreferences 불러오기
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean alarmEnabled = prefs.getBoolean("alarm_enabled", true);

        // 저장된 값으로 스위치 초기화
        switchNotification.setChecked(alarmEnabled);

        // 스위치 상태 변경 시 저장
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("alarm_enabled", isChecked);
            editor.apply();

            if (isChecked) {
                Toast.makeText(this, "알림 켜짐", Toast.LENGTH_SHORT).show();

                // ✅ Android 12 이상에서 정확한 알람 권한 확인
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (!alarmManager.canScheduleExactAlarms()) {
                        // 권한 없으면 설정 화면으로 안내
                        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(intent);
                    }
                }

            } else {
                Toast.makeText(this, "알림 꺼짐", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
