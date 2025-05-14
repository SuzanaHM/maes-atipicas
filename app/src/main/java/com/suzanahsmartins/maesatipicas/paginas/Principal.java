package com.suzanahsmartins.maesatipicas.paginas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.R;
import com.suzanahsmartins.maesatipicas.RecadoDiario;
import com.suzanahsmartins.maesatipicas.databinding.FragmentPrincipalBinding;

public class Principal extends Fragment {

    private FragmentPrincipalBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Faz o binding
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);

        String recado = RecadoDiario.getRandom();
        binding.recadoTextView.setText(recado);
        Glide.with(this)
                .load(R.drawable.icon)
                .into(binding.imageView);

        binding.botaoAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // evita vazamento de memória
    }
}
