package com.suzanahsmartins.maesatipicas.diario;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.databinding.FragmentDiarioNovoBinding;
import com.suzanahsmartins.maesatipicas.room.DataBase;

import java.util.Calendar;

public class Diario_Novo extends Fragment {

    private FragmentDiarioNovoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicializa o binding
        binding = FragmentDiarioNovoBinding.inflate(inflater, container, false);


        binding.buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(getActivity()).getNavegacao().retornar();
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextTexto.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Digite um texto", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(() -> {
                    DataBase db = DataBase.getInstance(getContext(), "diario.db");
                    db.getDiario().inserir(new DiarioPagina(Calendar.getInstance().getTimeInMillis(), binding.editTextTexto.getText().toString()));
                    getActivity().runOnUiThread(() -> MainActivity.getInstance(getActivity()).getNavegacao().retornar());
                }).start();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // evita memory leaks
    }
}
