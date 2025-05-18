package com.suzanahsmartins.maesatipicas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suzanahsmartins.maesatipicas.agenda.despertador.Alarme;
import com.suzanahsmartins.maesatipicas.agenda.Compromisso;
import com.suzanahsmartins.maesatipicas.agenda.despertador.AlarmReceiver;
import com.suzanahsmartins.maesatipicas.databinding.ActivityMainBinding;
import com.suzanahsmartins.maesatipicas.paginas.Principal;
import com.suzanahsmartins.maesatipicas.room.DataBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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


        teste();

        new AlarmReceiver();

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        binding.botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavegacao().retornar();
            }
        });


        agendarVerificacaoMeiaNoite(this);
        verificarCompromissos(this);

        new VerificarReceiver();

        criarCanalDeServico();

        SharedPreferences prefs = getSharedPreferences("servico", Context.MODE_PRIVATE);
        boolean estaRodando = prefs.getBoolean("rodando", false);

        if (!estaRodando) {
            Intent serviceIntent = new Intent(this, SegundoPlano.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }

    }

    private void criarCanalDeServico(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "canal_servico",
                    "Serviço em segundo plano",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void agendarVerificacaoMeiaNoite(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, VerificarReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Se já passou de meia-noite hoje, agenda para a próxima meia-noite
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    public class VerificarReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(() -> {
                verificarCompromissos(MainActivity.this);
            }).start();
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                // Cria a intent para iniciar o serviço
                Intent serviceIntent = new Intent(context, SegundoPlano.class);
                ContextCompat.startForegroundService(context, serviceIntent);
            }
        }
    }

    public void verificarCompromissos(Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar agora = Calendar.getInstance();

                // Ex: "Janeiro 2025"
                String mesano = new SimpleDateFormat("MMMM yyyy", new Locale("pt", "BR")).format(agora.getTime());
                mesano = mesano.substring(0, 1).toUpperCase() + mesano.substring(1);
                DataBase db = DataBase.getInstance(activity, "agenda.db");
                List<Compromisso> l = db.getAgenda().listarPorMes(mesano);

                for (Compromisso i : l) {
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
                        if (dataCompromisso.after(agora)) {
                            Alarme.define(hora, minuto, i.titulo, i.descricao, MainActivity.getInstance(activity), i.id);

                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Evita crash se houver erro no parse
                    }
                }

            }
        }).start();
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

    private Compromisso compromisso;
    public void setCompromissoEdit(Compromisso comp){
        compromisso = comp;
    }
    public Compromisso getCompromissoEdit(){
        return compromisso;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpeza do binding
        binding = null;
    }

    public void teste(){








    }
}
