package com.suzanahsmartins.maesatipicas;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suzanahsmartins.maesatipicas.databinding.ActivityMainBinding;
import com.suzanahsmartins.maesatipicas.paginas.Navegacao;
import com.suzanahsmartins.maesatipicas.paginas.Principal;

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
