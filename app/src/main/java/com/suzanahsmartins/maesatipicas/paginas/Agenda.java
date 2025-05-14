package com.suzanahsmartins.maesatipicas.paginas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.suzanahsmartins.maesatipicas.agenda.Compromisso;
import com.suzanahsmartins.maesatipicas.databinding.FragmentAgendaBinding;
import com.suzanahsmartins.maesatipicas.room.DataBase;

public class Agenda extends Fragment {

    private FragmentAgendaBinding binding;
    private AgendaMesAdapter agendaMesAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inicializa o binding
        binding = FragmentAgendaBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        binding.recyclerAgenda.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda_Novo);
            }
        });

        // Criando dados fictícios de exemplo
        List<AgendaMes> agendaMesList = new ArrayList<>();

        carregarMeses();

        DataBase db = DataBase.getInstance(getContext(), "agenda.db");

        new Thread(() -> {
            for(String s : meses){
                List<Compromisso> l = db.getAgenda().listarPorMes(s);
                if(l.isEmpty()){
                    Compromisso c = new Compromisso();
                    c.titulo = "voce não tem compromisso este mês";
                    c.dia = "!";
                    l = new ArrayList<>(Arrays.asList(
                            c
                    ));
                }
                agendaMesList.add(new AgendaMes(s, l));

                requireActivity().runOnUiThread(() -> {
                    agendaMesAdapter = new AgendaMesAdapter(agendaMesList);
                    binding.recyclerAgenda.setAdapter(agendaMesAdapter);
                });
            }
        }).start();

        agendaMesAdapter = new AgendaMesAdapter(agendaMesList);
        binding.recyclerAgenda.setAdapter(agendaMesAdapter);
        // Acesso direto às views com binding, exemplo:
        // binding.recyclerAgenda.setAdapter(...);

        return binding.getRoot();
    }
    ArrayList<String> meses = new ArrayList<>();

    private void carregarMeses() {
        Calendar c = Calendar.getInstance();
        int anoAtual = c.get(Calendar.YEAR);
        int mesAtual = c.get(Calendar.MONTH); // 0 = Janeiro, 1 = Fevereiro, ...

        String[] nomesMeses = {
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };

        for (int ano = anoAtual; ano < anoAtual + 10; ano++) {
            int inicioMes = (ano == anoAtual) ? mesAtual : 0;

            for (int mes = inicioMes; mes < 12; mes++) {
                meses.add(nomesMeses[mes] + " " + ano);
            }
        }
    }




    public class AgendaMes {
        public String nomeMes;
        public List<Compromisso> compromissos;

        public AgendaMes(String nomeMes, List<Compromisso> compromissos) {
            this.nomeMes = nomeMes;
            this.compromissos = compromissos;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evita memory leaks
    }

    public class AgendaMesAdapter extends RecyclerView.Adapter<AgendaMesAdapter.ViewHolder> {
        private List<AgendaMes> lista;

        public AgendaMesAdapter(List<AgendaMes> lista) {
            this.lista = lista;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Infla o layout item_agenda_mes.xml
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.agenda_mes, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AgendaMes agendaMes = lista.get(position);
            holder.txtNomeMes.setText(agendaMes.nomeMes);

            // Configura a RecyclerView interna para exibir compromissos
            holder.recyclerCompromissos.setLayoutManager(new LinearLayoutManager(holder.recyclerCompromissos.getContext()));
            holder.recyclerCompromissos.setAdapter(new CompromissoAdapter(agendaMes.compromissos));
        }

        @Override
        public int getItemCount() {
            return lista.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtNomeMes;
            RecyclerView recyclerCompromissos;

            ViewHolder(View itemView) {
                super(itemView);
                txtNomeMes = itemView.findViewById(R.id.txtNomeMes);
                recyclerCompromissos = itemView.findViewById(R.id.recyclerCompromissos);
            }
        }
    }


    public class CompromissoAdapter extends RecyclerView.Adapter<CompromissoAdapter.ViewHolder> {
        private List<Compromisso> compromissos;

        public CompromissoAdapter(List<Compromisso> compromissos) {
            this.compromissos = compromissos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.agenda_compromisso, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Compromisso compromisso = compromissos.get(position);
            holder.txtDia.setText(compromisso.dia);
            if(compromisso.hora != null) {
                holder.txtDescricao.setText(compromisso.hora + " - " + compromisso.titulo);
            }else{
                holder.txtDescricao.setText(compromisso.titulo);
            }
        }

        @Override
        public int getItemCount() {
            return compromissos.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtDia, txtDescricao;

            ViewHolder(View itemView) {
                super(itemView);
                txtDia = itemView.findViewById(R.id.txtDia);
                txtDescricao = itemView.findViewById(R.id.txtHoraCompromisso);
            }
        }
    }

}
