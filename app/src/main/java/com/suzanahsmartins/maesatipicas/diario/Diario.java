package com.suzanahsmartins.maesatipicas.diario;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import androidx.fragment.app.Fragment;

import com.suzanahsmartins.maesatipicas.databinding.FragmentDiarioBinding;
import com.suzanahsmartins.maesatipicas.room.DataBase;

public class Diario extends Fragment {

    private FragmentDiarioBinding binding;

    List<DiarioPagina> lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiarioBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.recyclerDiario.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Simulando alguns dados
        lista = new ArrayList<>();

        DiarioAdapter adapter = new DiarioAdapter(lista);
        binding.recyclerDiario.setAdapter(adapter);

        new Thread(() -> {
            DataBase db = DataBase.getInstance(getContext(), "diario.db");
            lista.addAll(db.getDiario().getAll());
            getActivity().runOnUiThread(() -> binding.recyclerDiario.getAdapter().notifyDataSetChanged());
        }).start();

        binding.fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Diario_Novo);
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public class DiarioAdapter extends RecyclerView.Adapter<DiarioAdapter.DiarioViewHolder> {

        private final List<DiarioPagina> listaPaginas;

        public DiarioAdapter(List<DiarioPagina> listaPaginas) {
            this.listaPaginas = listaPaginas;
        }

        @NonNull
        @Override
        public DiarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.diario_pagina, parent, false);
            return new DiarioViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DiarioViewHolder holder, int position) {
            DiarioPagina pagina = listaPaginas.get(position);
            holder.data.setText(pagina.getData());
            holder.texto.setText(pagina.getTexto());

            float[] hsv = new float[3];
            hsv[0] = (float) (Math.random() * 360);
            hsv[1] = 0.3f;
            hsv[2] = 0.95f;

            int corSuave = android.graphics.Color.HSVToColor(hsv);
            holder.texto.setBackgroundColor(corSuave);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Confirmação")
                            .setMessage("Deseja realmente deletar esta anotação?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(() -> {
                                        DataBase db = DataBase.getInstance(getContext(), "diario.db");
                                        db.getDiario().deletar(pagina.getDataMillis());
                                        lista.clear();
                                        lista.addAll(db.getDiario().getAll());
                                        getActivity().runOnUiThread(() -> binding.recyclerDiario.getAdapter().notifyDataSetChanged());
                                    }).start();
                                }
                            })
                            .show();
                    return true;
                }
            });




        }

        @Override
        public int getItemCount() {
            return listaPaginas.size();
        }

        class DiarioViewHolder extends RecyclerView.ViewHolder {
            TextView data;
            TextView texto;

            DiarioViewHolder(View itemView){
                super(itemView);
                data = itemView.findViewById(R.id.dataTexto);
                texto = itemView.findViewById(R.id.diarioTexto);
            }
        }
    }
}
