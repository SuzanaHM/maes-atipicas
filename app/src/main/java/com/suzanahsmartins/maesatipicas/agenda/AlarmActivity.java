package com.suzanahsmartins.maesatipicas.agenda;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.R;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
