package com.suzanahsmartins.maesatipicas.agenda.despertador;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import java.util.Calendar;

public class Alarme {

    public static void define(int hora, int minuto, String titulo, String descricao, Context activity, int id){
        // Configuração do AlarmManager para agendar o alarme
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hora);  // Hora do despertar
        calendar.set(Calendar.MINUTE, minuto);     // Minuto do despertar
        calendar.set(Calendar.SECOND, 0);          // Resetando o segundo

// Verifica se a hora já passou para o dia atual
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // Se o horário já passou, agende para o próximo dia
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Redirecionar o usuário para a tela de permissões para ativar isso
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                activity.startActivity(intent);
            }
        }

        Intent intent2 = new Intent(activity, AlarmReceiver.class);
        intent2.putExtra("titulo", titulo);
        intent2.putExtra("desc", descricao);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(activity, id, intent2, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent2);
    }
}
