package com.suzanahsmartins.maesatipicas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suzanahsmartins.maesatipicas.agenda.AlarmActivity;
import com.suzanahsmartins.maesatipicas.agenda.Alarme;
import com.suzanahsmartins.maesatipicas.databinding.ActivityMainBinding;
import com.suzanahsmartins.maesatipicas.paginas.Principal;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Criação do objeto de binding
    private ActivityMainBinding binding;
    private Navegacao navegacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialização do binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Habilitar Edge-to-Edge
        EdgeToEdge.enable(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Aplicar a configuração de padding para o sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new Principal()) // Substitua R.id.fragment_container com o ID do container do seu fragmento
                .commit();



        navegacao = new Navegacao(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1);
            }
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Redirecionar o usuário para a tela de permissões para ativar isso
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }




        Alarme.define(03, 8, "teste", "exemplo",this);


    }

    public static MainActivity getInstance(Activity activity){
        if(activity instanceof MainActivity){
            return (MainActivity) activity;
        }
        return null;
    }

    public Navegacao getNavegacao(){
        return navegacao;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpeza do binding
        binding = null;
    }
}
