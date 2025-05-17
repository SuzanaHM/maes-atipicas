package com.suzanahsmartins.maesatipicas;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class SegundoPlano extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getSharedPreferences("servico", MODE_PRIVATE)
                .edit()
                .putBoolean("rodando", true)
                .apply();

        Notification notification = new NotificationCompat.Builder(this, "canal_servico")
                .setContentTitle("Mães Atípicas")
                .setContentText("O app está rodando em segundo plano")
                .setSmallIcon(R.drawable.icon)
                .build();

        startForeground(9999999, notification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Marcar que parou
        getSharedPreferences("servico", MODE_PRIVATE)
                .edit()
                .putBoolean("rodando", false)
                .apply();
    }
}
