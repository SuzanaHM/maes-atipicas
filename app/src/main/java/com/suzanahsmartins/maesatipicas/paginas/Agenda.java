package com.suzanahsmartins.maesatipicas.paginas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suzanahsmartins.maesatipicas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.suzanahsmartins.maesatipicas.databinding.FragmentAgendaBinding;

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

        // Criando dados fictícios de exemplo
        List<AgendaMes> agendaMesList = new ArrayList<>();

        // Adicionando mês de exemplo com compromissos
        List<Compromisso> compromissosMaio = Arrays.asList(
                new Compromisso("01", "Dentista"),
                new Compromisso("05", "Reunião"),
                new Compromisso("12", "Aniversário da mãe")
        );
        agendaMesList.add(new AgendaMes("Maio 2025", compromissosMaio));



        List<Compromisso> compromissosAgosto = Arrays.asList(
                new Compromisso("!", "Você não tem compromissos nesse mês!")
        );
        agendaMesList.add(new AgendaMes("Junho 2025", compromissosAgosto));

        agendaMesAdapter = new AgendaMesAdapter(agendaMesList);
        binding.recyclerAgenda.setAdapter(agendaMesAdapter);
        // Acesso direto às views com binding, exemplo:
        // binding.recyclerAgenda.setAdapter(...);

        return binding.getRoot();
    }


    public class AgendaMes {
        public String nomeMes;
        public List<Compromisso> compromissos;

        public AgendaMes(String nomeMes, List<Compromisso> compromissos) {
            this.nomeMes = nomeMes;
            this.compromissos = compromissos;
        }
    }

    public class Compromisso {
        public String dia;
        public String descricao;

        public Compromisso(String dia, String descricao) {
            this.dia = dia;
            this.descricao = descricao;
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
            holder.txtDescricao.setText(compromisso.descricao);
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
