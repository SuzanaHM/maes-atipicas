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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                MainActivity.getInstance(getActivity()).setCompromissoEdit(null);
                MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda_Novo);
            }
        });

        // Criando dados fictícios de exemplo
        List<AgendaMes> agendaMesList = new ArrayList<>();

        carregarMeses();

        DataBase db = DataBase.getInstance(getContext(), "agenda.db");
        new Thread(() -> {
            Calendar cal = Calendar.getInstance();
            int mesAtual = cal.get(Calendar.MONTH); // de 0 a 11
            int anoAtual = cal.get(Calendar.YEAR);
            int diaAtual = cal.get(Calendar.DAY_OF_MONTH);

            for (String s : meses) {
                List<Compromisso> l = db.getAgenda().listarPorMes(s);
                if (l.isEmpty()) {
                    Compromisso c = new Compromisso();
                    c.titulo = "você não tem compromisso este mês";
                    c.dia = "!";
                    l = new ArrayList<>(Arrays.asList(c));
                }

                ArrayList<Compromisso> lf = new ArrayList<>();

                for (Compromisso i : l) {
                    if (i.dia.equals("!")) {
                        lf.add(i);
                    } else {
                        int diaCompromisso = Integer.parseInt(i.dia);

                        // Supondo que seu string "s" seja tipo "Janeiro 2025"
                        String[] partes = s.split(" ");
                        String nomeMes = partes[0];
                        int anoCompromisso = Integer.parseInt(partes[1]);
                        int mesCompromisso = nomeMesToNumero(nomeMes); // converte Janeiro → 0, Fevereiro → 1, etc.

                        if (mesCompromisso == mesAtual && anoCompromisso == anoAtual) {
                            if (diaCompromisso > diaAtual) {
                                lf.add(i); // dia no futuro
                            } else if (diaCompromisso == diaAtual) {
                                // verificar hora
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                    Date horaCompromisso = sdf.parse(i.hora);

                                    Calendar agora = Calendar.getInstance();
                                    Calendar horaAtual = Calendar.getInstance();
                                    horaAtual.set(Calendar.HOUR_OF_DAY, agora.get(Calendar.HOUR_OF_DAY));
                                    horaAtual.set(Calendar.MINUTE, agora.get(Calendar.MINUTE));
                                    horaAtual.set(Calendar.SECOND, 0);
                                    horaAtual.set(Calendar.MILLISECOND, 0);

                                    Calendar horaDoCompromisso = Calendar.getInstance();
                                    horaDoCompromisso.setTime(horaCompromisso);
                                    horaDoCompromisso.set(Calendar.YEAR, anoCompromisso);
                                    horaDoCompromisso.set(Calendar.MONTH, mesCompromisso);
                                    horaDoCompromisso.set(Calendar.DAY_OF_MONTH, diaCompromisso);

                                    if (horaDoCompromisso.after(horaAtual)) {
                                        lf.add(i); // ainda vai acontecer hoje
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace(); // ou log
                                }
                            }
                        } else {
                            lf.add(i); // mês diferente do atual
                        }

                    }
                }

                agendaMesList.add(new AgendaMes(s, lf));

                requireActivity().runOnUiThread(() -> {
                    agendaMesAdapter = new AgendaMesAdapter(agendaMesList);
                    binding.recyclerAgenda.setAdapter(agendaMesAdapter);
                });
            }
        }).start();


        agendaMesAdapter = new AgendaMesAdapter(agendaMesList);
        binding.recyclerAgenda.setAdapter(agendaMesAdapter);

        return binding.getRoot();
    }
    private int nomeMesToNumero(String nomeMes) {
        switch (nomeMes.toLowerCase()) {
            case "janeiro": return 0;
            case "fevereiro": return 1;
            case "março": return 2;
            case "abril": return 3;
            case "maio": return 4;
            case "junho": return 5;
            case "julho": return 6;
            case "agosto": return 7;
            case "setembro": return 8;
            case "outubro": return 9;
            case "novembro": return 10;
            case "dezembro": return 11;
            default: return -1;
        }
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getInstance(getActivity()).setCompromissoEdit(compromisso);
                    MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda_Novo);
                }
            });
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
