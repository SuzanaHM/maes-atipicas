package com.suzanahsmartins.maesatipicas.agenda;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.R;

public class MeuReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Ação quando o alarme disparar
        if (intent != null && intent.getExtras() != null){
            if (intent.getExtras().containsKey("titulo") && intent.getExtras().containsKey("descricao")) {
                notificar(context, intent.getStringExtra("titulo"), intent.getStringExtra("descricao"));
            }
        }
    }

    private void notificar(Context context, String titulo, String descricao) {
        // Criação do som do alarme (você pode usar um som de sistema ou um arquivo personalizado)
        Uri alarmeUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone alarme = RingtoneManager.getRingtone(context, alarmeUri);
        alarme.play();

        // Exibindo uma notificação para o alarme
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Criando um canal de notificação (necessário para Android 8.0 ou superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "alarme_channel",
                    "Alarmes",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Criando um PendingIntent para abrir a MainActivity (ou qualquer outra Activity)
        Intent intent = new Intent(context, MainActivity.class);  // Ajuste conforme necessário
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Criando a notificação
        Notification notification = new NotificationCompat.Builder(context, "alarme_channel")
                .setSmallIcon(R.drawable.icon)  // Substitua pelo seu ícone
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)  // Toca o som padrão do alarme
                .setVibrate(new long[]{0, 500, 1000, 500})
                .setContentIntent(pendingIntent)  // Associe o PendingIntent à notificação
                .setAutoCancel(true)  // Fecha a notificação ao clicar nela
                .build();

        // Envia a notificação
        notificationManager.notify(0, notification);
    }

}
