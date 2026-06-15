package com.example.subwise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String serviceName = intent.getStringExtra("serviceName");

        // 🔒 알림 ON/OFF 스위치 확인
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean alarmEnabled = prefs.getBoolean("alarm_enabled", true);
        if (!alarmEnabled) return; // 꺼져 있으면 알림 무시

        // Android 13 이상 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return; // 권한 없으면 알림 무시
            }
        }

        // 알림 채널 생성 (Android 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "subwise_channel",
                    "구독 알림",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // 알림 빌더
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "subwise_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("결제 예정 알림")
                .setContentText(serviceName + " 구독 결제가 3일 후 예정되어 있습니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // 알림 띄우기
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
