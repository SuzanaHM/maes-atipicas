package com.suzanahsmartins.maesatipicas.agenda.despertador;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getExtras() != null) {
            String titu = intent.getStringExtra("titulo");
            String desc = intent.getStringExtra("desc");
            if (Settings.canDrawOverlays(context)) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

                // Layout principal
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundColor(Color.parseColor("#D2B48C")); // marrom claro (ex: Tan)
                layout.setGravity(Gravity.CENTER);
                layout.setPadding(50, 50, 50, 50);

                // Texto grande
                TextView titulo = new TextView(context);
                titulo.setText(titu);
                titulo.setTextSize(28);
                titulo.setTextColor(Color.BLACK);
                titulo.setGravity(Gravity.CENTER);

                // Texto menor
                TextView subtitulo = new TextView(context);
                subtitulo.setText(desc);
                subtitulo.setTextSize(18);
                subtitulo.setTextColor(Color.DKGRAY);
                subtitulo.setGravity(Gravity.CENTER);

                // Botão deslizável (SeekBar como slide)
                SeekBar slider = new SeekBar(context);
                slider.setMax(100);
                slider.setProgress(0);
                slider.setPadding(50, 50, 50, 50);
                slider.setScaleY(3f);

                // Texto de instrução
                TextView instrucao = new TextView(context);
                instrucao.setText("Arraste para a direita para desligar o alarme");
                instrucao.setTextSize(16);
                instrucao.setTextColor(Color.BLACK);
                instrucao.setGravity(Gravity.CENTER);
                instrucao.setPadding(0, 30, 0, 0);


                // Ação de desbloqueio
                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (progress >= 90) {
                            pararAlarme();
                            wm.removeView(layout);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                // Adicionando tudo
                layout.addView(titulo);
                layout.addView(subtitulo);
                layout.addView(slider);
                layout.addView(instrucao);

                // Parâmetros da janela flutuante
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // API 26+
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT
                );

                // Mostrar na tela
                wm.addView(layout, params);

                mediaPlayer = MediaPlayer.create(context, R.raw.alarme); // coloque um mp3 em res/raw/alarme.mp3
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                // Vibrar continuamente
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect effect = VibrationEffect.createWaveform(
                            new long[]{0, 1000, 1000}, 0); // padrão, repete
                    vibrator.vibrate(effect);
                } else {
                    vibrator.vibrate(new long[]{0, 1000, 1000}, 0);
                }
            }
        }

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

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;



}

