package com.suzanahsmartins.maesatipicas.paginas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.Navegacao;
import com.suzanahsmartins.maesatipicas.agenda.Compromisso;
import com.suzanahsmartins.maesatipicas.databinding.FragmentAgendaNovoBinding;
import com.suzanahsmartins.maesatipicas.room.AgendaDB;
import com.suzanahsmartins.maesatipicas.room.DataBase;

import java.util.Calendar;

public class Agenda_Novo extends Fragment {

    private FragmentAgendaNovoBinding binding;
    private AgendaDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgendaNovoBinding.inflate(inflater, container, false);
        DataBase db = DataBase.getInstance(getContext(), "agenda.db");

        if (MainActivity.getInstance(getActivity()).getCompromissoEdit() != null) {
            Compromisso compromisso2 = MainActivity.getInstance(getActivity()).getCompromissoEdit();

            // Título e descrição
            binding.editTitulo.setText(compromisso2.titulo);
            binding.editDescricao.setText(compromisso2.descricao);

            // Data (dia, mês e ano)
            try {
                String[] partes = compromisso2.mes.split(" ");
                String nomeMes = partes[0];
                int ano = Integer.parseInt(partes[1]);

                int mes = 0;
                switch (nomeMes.toLowerCase()) {
                    case "janeiro": mes = 0; break;
                    case "fevereiro": mes = 1; break;
                    case "março": mes = 2; break;
                    case "abril": mes = 3; break;
                    case "maio": mes = 4; break;
                    case "junho": mes = 5; break;
                    case "julho": mes = 6; break;
                    case "agosto": mes = 7; break;
                    case "setembro": mes = 8; break;
                    case "outubro": mes = 9; break;
                    case "novembro": mes = 10; break;
                    case "dezembro": mes = 11; break;
                }

                int dia = Integer.parseInt(compromisso2.dia);

                binding.datePicker.updateDate(ano, mes, dia);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Hora
            try {
                String[] horaMinuto = compromisso2.hora.split(":");
                int hora = Integer.parseInt(horaMinuto[0]);
                int minuto = Integer.parseInt(horaMinuto[1]);

                binding.timePicker.setHour(hora);
                binding.timePicker.setMinute(minuto);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Alerta
            if (compromisso2.alerta) {
                binding.radioSim.setChecked(true);
            } else {
                binding.radioNao.setChecked(true);
            }

            binding.botaoAdicionar.setText("Salvar");

            // Ação do botão Adicionar
            binding.botaoAdicionar.setOnClickListener(v -> {

                new Thread(() -> {

                    Compromisso compromisso = new Compromisso();

                    // Título e descrição
                    compromisso.titulo = binding.editTitulo.getText().toString();
                    compromisso.descricao = binding.editDescricao.getText().toString();

                    // Data
                    int dia = binding.datePicker.getDayOfMonth();
                    int mes = binding.datePicker.getMonth() + 1; // Janeiro = 0
                    int ano = binding.datePicker.getYear();

                    String s = "";switch (mes) {
                        case 1:  s = "Janeiro " + ano;     break;
                        case 2:  s = "Fevereiro " + ano;   break;
                        case 3:  s = "Março " + ano;       break;
                        case 4:  s = "Abril " + ano;       break;
                        case 5:  s = "Maio " + ano;        break;
                        case 6:  s = "Junho " + ano;       break;
                        case 7:  s = "Julho " + ano;       break;
                        case 8:  s = "Agosto " + ano;      break;
                        case 9:  s = "Setembro " + ano;    break;
                        case 10: s = "Outubro " + ano;     break;
                        case 11: s = "Novembro " + ano;    break;
                        case 12: s = "Dezembro " + ano;    break;
                        default: s = "Dezembro " + ano; break;
                    }

                    compromisso.dia = String.valueOf(dia);
                    compromisso.mes = String.valueOf(s);

                    // Hora
                    int hora = binding.timePicker.getHour();
                    int minuto = binding.timePicker.getMinute();
                    compromisso.hora = String.format("%02d:%02d", hora, minuto);

                    // Alertar
                    int idSelecionado = binding.radioGroupAlertar.getCheckedRadioButtonId();
                    compromisso.alerta = (idSelecionado == binding.radioSim.getId());

                    db.getAgenda().deletar(compromisso2);
                    db.getAgenda().inserir(compromisso);

                    requireActivity().runOnUiThread(() -> {
                        MainActivity.getInstance(getActivity()).verificarCompromissos(getActivity());
                        MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda);
                    });

                }).start();
                // Inserir no banco

            });
        }else {


            // Inicializa o banco

            // Ação do botão Adicionar
            binding.botaoAdicionar.setOnClickListener(v -> {

                new Thread(() -> {

                    Compromisso compromisso = new Compromisso();

                    // Título e descrição
                    compromisso.titulo = binding.editTitulo.getText().toString();
                    compromisso.descricao = binding.editDescricao.getText().toString();

                    // Data
                    int dia = binding.datePicker.getDayOfMonth();
                    int mes = binding.datePicker.getMonth() + 1; // Janeiro = 0
                    int ano = binding.datePicker.getYear();

                    String s = "";
                    switch (mes) {
                        case 1:
                            s = "Janeiro " + ano;
                            break;
                        case 2:
                            s = "Fevereiro " + ano;
                            break;
                        case 3:
                            s = "Março " + ano;
                            break;
                        case 4:
                            s = "Abril " + ano;
                            break;
                        case 5:
                            s = "Maio " + ano;
                            break;
                        case 6:
                            s = "Junho " + ano;
                            break;
                        case 7:
                            s = "Julho " + ano;
                            break;
                        case 8:
                            s = "Agosto " + ano;
                            break;
                        case 9:
                            s = "Setembro " + ano;
                            break;
                        case 10:
                            s = "Outubro " + ano;
                            break;
                        case 11:
                            s = "Novembro " + ano;
                            break;
                        case 12:
                            s = "Dezembro " + ano;
                            break;
                        default:
                            s = "Dezembro " + ano;
                            break;
                    }

                    compromisso.dia = String.valueOf(dia);
                    compromisso.mes = String.valueOf(s);

                    // Hora
                    int hora = binding.timePicker.getHour();
                    int minuto = binding.timePicker.getMinute();
                    compromisso.hora = String.format("%02d:%02d", hora, minuto);

                    // Alertar
                    int idSelecionado = binding.radioGroupAlertar.getCheckedRadioButtonId();
                    compromisso.alerta = (idSelecionado == binding.radioSim.getId());

                    db.getAgenda().inserir(compromisso);

                    requireActivity().runOnUiThread(() ->
                            MainActivity.getInstance(getActivity()).getNavegacao().navegarTo(Navegacao.Pagina.Agenda));
                            MainActivity.getInstance(getActivity()).verificarCompromissos(getActivity());

                }).start();
                // Inserir no banco

            });
        }

        binding.botaoCancelar.setOnClickListener(new View.OnClickListener() {
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
        binding = null;
    }
}
