package com.suzanahsmartins.maesatipicas.agenda.despertador;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.R;
import com.suzanahsmartins.maesatipicas.agenda.Compromisso;
import com.suzanahsmartins.maesatipicas.agenda.MeuReceiver;
import com.suzanahsmartins.maesatipicas.room.DataBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            );
        }

        // Opcional: desbloqueia a tela
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager != null) {
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("AlarmActivity");
            keyguardLock.disableKeyguard();
        }


        Log.e("Teste", "10");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar agora = Calendar.getInstance();
                Log.e("Teste", "11");

                // Ex: "Janeiro 2025"
                String mesano = new SimpleDateFormat("MMMM yyyy", new Locale("pt", "BR")).format(agora.getTime());
                mesano = mesano.substring(0, 1).toUpperCase() + mesano.substring(1);
                DataBase db = DataBase.getInstance(AlarmActivity.this, "agenda.db");
                List<Compromisso> l = db.getAgenda().listarPorMes(mesano);

                boolean iniciado = false;

                for (Compromisso i : l) {
                    Log.e("Teste", "12");
                    try {
                        // Pega hora e minuto do compromisso
                        String[] partes = i.hora.split(":");
                        int hora = Integer.parseInt(partes[0]);
                        int minuto = Integer.parseInt(partes[1]);

                        // Pega o dia do compromisso
                        int dia = Integer.parseInt(i.dia);

                        // Cria objeto Calendar com a data e hora do compromisso
                        Calendar dataCompromisso = Calendar.getInstance();
                        dataCompromisso.set(Calendar.DAY_OF_MONTH, dia);
                        dataCompromisso.set(Calendar.HOUR_OF_DAY, hora);
                        dataCompromisso.set(Calendar.MINUTE, minuto);
                        dataCompromisso.set(Calendar.SECOND, 0);
                        dataCompromisso.set(Calendar.MILLISECOND, 0);

                        // Verifica se a hora ainda não passou
                        long diferenca = Math.abs(agora.getTimeInMillis() - dataCompromisso.getTimeInMillis());
                        Log.e("test", "1");
                        if (diferenca < 60 * 1000) { // menos de 1 minuto de diferença
                            iniciado = true;
                            Log.e("test", "2");
                            runOnUiThread(AlarmActivity.this::carregar);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                if (!alarmManager.canScheduleExactAlarms()) {
                                    // Redirecionar o usuário para a tela de permissões para ativar isso
                                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                                    startActivity(intent);
                                }
                            }
                            Intent intent = new Intent(getApplicationContext(), MeuReceiver.class);
                            intent.putExtra("titulo", i.titulo);
                            intent.putExtra("descricao", i.descricao);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i.id, intent, PendingIntent.FLAG_IMMUTABLE);

                            Log.e("Teste", "13");
// Configura o AlarmManager para despertar o app no horário definido
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace(); // Evita crash se houver erro no parse
                    }
                }

                if(!iniciado) {
                    runOnUiThread(AlarmActivity.this::finish);
                }

            }
        }).start();

    }

    private void carregar(){




        // Tela cheia + acordar a tela
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        setContentView(R.layout.activity_alarm);

        // Tocar música em loop
        mediaPlayer = MediaPlayer.create(this, R.raw.alarme); // coloque um mp3 em res/raw/alarme.mp3
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Vibrar continuamente
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createWaveform(
                    new long[]{0, 1000, 1000}, 0); // padrão, repete
            vibrator.vibrate(effect);
        } else {
            vibrator.vibrate(new long[]{0, 1000, 1000}, 0);
        }

        // Botão para desligar
        Button btnDesligar = findViewById(R.id.btnDesligar);
        btnDesligar.setOnClickListener(v -> {
            pararAlarme();

            Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void pararAlarme() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop(); // pode lançar IllegalStateException
            } catch (IllegalStateException e) {
                e.printStackTrace(); // Evita crash
            }

            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace(); // por precaução
            }

            mediaPlayer = null;
        }

        if(vibrator != null){
            vibrator.cancel();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pararAlarme();
    }
}
