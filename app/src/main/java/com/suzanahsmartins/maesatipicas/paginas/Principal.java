package com.suzanahsmartins.maesatipicas.paginas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.suzanahsmartins.maesatipicas.Login;
import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.R;
import com.suzanahsmartins.maesatipicas.RecadoDiario;
import com.suzanahsmartins.maesatipicas.databinding.FragmentPrincipalBinding;

public class Principal extends Fragment {

    private FragmentPrincipalBinding binding;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Faz o binding
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();

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
        binding.botaoDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Diario);
            }
        });
        binding.botaoForum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(auth.getCurrentUser() == null){
                    Intent i = new Intent(getActivity(), Login.class);
                    startActivity(i);
                }else {
                    MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Forum);
                }
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
